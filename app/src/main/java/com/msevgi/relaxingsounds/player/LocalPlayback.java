/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.msevgi.relaxingsounds.player;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.wifi.WifiManager;
import android.os.PowerManager;
import android.support.v4.media.session.PlaybackStateCompat;

import com.msevgi.relaxingsounds.model.Sound;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import static android.media.MediaPlayer.OnCompletionListener;
import static android.media.MediaPlayer.OnErrorListener;
import static android.media.MediaPlayer.OnPreparedListener;
import static android.media.MediaPlayer.OnSeekCompleteListener;

/**
 * A class that implements local media playback using {@link android.media.MediaPlayer}
 */
public class LocalPlayback implements Playback, AudioManager.OnAudioFocusChangeListener,
        OnCompletionListener, OnErrorListener, OnPreparedListener, OnSeekCompleteListener {

    private static final String TAG = LogHelper.makeLogTag(LocalPlayback.class);

    // The volume we set the media player to when we lose audio focus, but are
    // allowed to reduce the volume instead of stopping playback.
    public static final float VOLUME_DUCK = 0.2f;
    // The volume we set the media player when we have audio focus.
    public static final float VOLUME_NORMAL = 1.0f;

    // we don't have audio focus, and can't duck (play at a low volume)
    private static final int AUDIO_NO_FOCUS_NO_DUCK = 0;
    // we don't have focus, but can duck (play at a low volume)
    private static final int AUDIO_NO_FOCUS_CAN_DUCK = 1;
    // we have full audio focus
    private static final int AUDIO_FOCUSED = 2;

    private final Context mContext;
    private int mState;
    private boolean mPlayOnFocusGain;
    private Callback mCallback;
    private volatile boolean mAudioNoisyReceiverRegistered;

    private HashMap<String, MediaPlayer> mPlayers = new HashMap<>();

    // Type of audio focus we have:
    private int mAudioFocus = AUDIO_NO_FOCUS_NO_DUCK;
    private final AudioManager mAudioManager;
    private String mCurrentMediaId;
    private final IntentFilter mAudioNoisyIntentFilter =
            new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);

    private final BroadcastReceiver mAudioNoisyReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (AudioManager.ACTION_AUDIO_BECOMING_NOISY.equals(intent.getAction())) {
                if (isPlaying()) {
                    Intent i = new Intent(context, MusicService.class);
                    i.setAction(MusicService.ACTION_CMD);
                    i.putExtra(MusicService.CMD_NAME, MusicService.CMD_PAUSE);
                    mContext.startService(i);
                }
            }
        }
    };

    public LocalPlayback(Context context) {
        this.mContext = context;
        this.mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        // Create the Wifi lock (this does not acquire the lock, this just creates it)
        this.mState = PlaybackStateCompat.STATE_NONE;
    }

    public Set<String> getPlayeringSoundIds() {
        Set<String> ids = new HashSet<>();
        try {
            Iterator<Map.Entry<String, MediaPlayer>> iterator = mPlayers.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, MediaPlayer> next = iterator.next();
                if (next.getValue().isPlaying()) {
                    ids.add(next.getKey());
                }
            }
        } catch (Exception e) {
        }
        return ids;
    }



    @Override
    public void stop(boolean notifyListeners) {
        mState = PlaybackStateCompat.STATE_STOPPED;
        // Relax all resources
        for (MediaPlayer mediaPlayer : mPlayers.values()) {
            relaxResources(true, mediaPlayer);
        }
        mPlayers.clear();
        if (notifyListeners && mCallback != null) {
            mCallback.onPlaybackStatusChanged(mState);
        }
        // Give up Audio focus
        giveUpAudioFocus();
        unregisterAudioNoisyReceiver();

    }

    @Override
    public int getState() {
        return mState;
    }

    @Override
    public boolean isPlaying() {
        if (mPlayOnFocusGain) {
            return true;
        }
        try {
            for (MediaPlayer mediaPlayer : mPlayers.values()) {
                if (mediaPlayer.isPlaying()) {
                    return true;
                }
            }
        } catch (Exception e) {
            mPlayers.clear();
        }
        return false;
    }

    private MediaPlayer createMediaPlayer(Sound sound) {
        MediaPlayer mediaPlayer = MediaPlayer.create(mContext, sound.getResourceId());

        // Make sure the media player will acquire a wake-lock while
        // playing. If we don't do that, the CPU might go to sleep while the
        // song is playing, causing playback to stop.
        mediaPlayer.setWakeMode(mContext.getApplicationContext(),
                PowerManager.PARTIAL_WAKE_LOCK);

        // we want the media player to notify us when it's ready preparing,
        // and when it's done playing:
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnErrorListener(this);
        mediaPlayer.setOnSeekCompleteListener(this);
        mPlayers.put(sound.getId(), mediaPlayer);
        return mediaPlayer;
    }

    @Override
    public void play(Sound item) {
        mPlayOnFocusGain = true;
        tryToGetAudioFocus();
        registerAudioNoisyReceiver();
        String mediaId = item.getId();
        mCurrentMediaId = mediaId;
        if (mPlayers.containsKey(mediaId)) {
            MediaPlayer player = mPlayers.get(mediaId);
            if (!player.isPlaying()) {
                player.start();
                updateState();
            } else {
                pauseAndReleaseMediaPlayer(mediaId, player);
            }
        } else {
            createMediaPlayer(item);
        }
    }

    private void pauseAndReleaseMediaPlayer(String mediaId, MediaPlayer player) {
        if (player != null && player.isPlaying()) {
            player.pause();
            relaxResources(true, player);
            mPlayers.remove(mediaId);
            updateState();
        }
    }

    private void updateState() {
        mState = PlaybackStateCompat.STATE_PAUSED;
        for (MediaPlayer mediaPlayer : mPlayers.values()) {
            if (mediaPlayer.isPlaying())
                mState = PlaybackStateCompat.STATE_PLAYING;
        }
        if (mCallback != null) {
            mCallback.onPlaybackStatusChanged(mState);
        }
    }

    @Override
    public void play() {
        mPlayOnFocusGain = true;
        tryToGetAudioFocus();
        registerAudioNoisyReceiver();
        for (MediaPlayer mediaPlayer : mPlayers.values()) {
            if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
                mState = PlaybackStateCompat.STATE_PLAYING;
                mediaPlayer.start();
            }
        }
        if (mCallback != null && mState == PlaybackStateCompat.STATE_PLAYING) {
            mCallback.onPlaybackStatusChanged(mState);
            registerAudioNoisyReceiver();
        }

    }

    @Override
    public void pause() {
        if (mState == PlaybackStateCompat.STATE_PLAYING) {
            relaxResources(false, null);
        }
        for (MediaPlayer mediaPlayer : mPlayers.values()) {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
            }
        }
        updateState();
        unregisterAudioNoisyReceiver();
    }


    @Override
    public void setCallback(Callback callback) {
        this.mCallback = callback;
    }

    /**
     * Try to get the system audio focus.
     */
    private void tryToGetAudioFocus() {
        int result = mAudioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN);
        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            mAudioFocus = AUDIO_FOCUSED;
        } else {
            mAudioFocus = AUDIO_NO_FOCUS_NO_DUCK;
        }
    }

    /**
     * Give up the audio focus.
     */
    private void giveUpAudioFocus() {
        if (mAudioManager.abandonAudioFocus(this) == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            mAudioFocus = AUDIO_NO_FOCUS_NO_DUCK;
        }
    }

    /**
     * Reconfigures MediaPlayer according to audio focus settings and
     * starts/restarts it. This method starts/restarts the MediaPlayer
     * respecting the current audio focus state. So if we have focus, it will
     * play normally; if we don't have focus, it will either leave the
     * MediaPlayer paused or set it to a low volume, depending on what is
     * allowed by the current focus settings. This method assumes mPlayer !=
     * null, so if you are calling it, you have to do so from a context where
     * you are sure this is the case.
     */
    private void configMediaPlayerState(boolean all) {
        if (mAudioFocus == AUDIO_NO_FOCUS_NO_DUCK) {
            // If we don't have audio focus and can't duck, we have to pause,
            if (mState == PlaybackStateCompat.STATE_PLAYING) {
                pause();
            }
        } else {  // we have audio focus:
            registerAudioNoisyReceiver();
            if (mAudioFocus == AUDIO_NO_FOCUS_CAN_DUCK) {
                //mMediaPlayer.setVolume(VOLUME_DUCK, VOLUME_DUCK); // we'll be relatively quiet
            } else {
                //if (mMediaPlayer != null) {
                //  mMediaPlayer.setVolume(VOLUME_NORMAL, VOLUME_NORMAL); // we can be loud again
                //} // else do something for remote client.
            }
            // If we were playing when we lost focus, we need to resume playing.
            if (mPlayOnFocusGain) {
                if (all) {
                    for (MediaPlayer mediaPlayer : mPlayers.values()) {
                        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
                            mediaPlayer.start();
                            mediaPlayer.setLooping(true);
                            mState = PlaybackStateCompat.STATE_PLAYING;
                        }
                    }
                } else {
                    MediaPlayer mediaPlayer = mPlayers.get(mCurrentMediaId);
                    if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
                        mediaPlayer.start();
                        mediaPlayer.setLooping(true);
                        mState = PlaybackStateCompat.STATE_PLAYING;
                    }
                }
            }

            mPlayOnFocusGain = false;
        }

        if (mCallback != null)

        {
            mCallback.onPlaybackStatusChanged(mState);
        }

    }

    /**
     * Called by AudioManager on audio focus changes.
     * Implementation of {@link android.media.AudioManager.OnAudioFocusChangeListener}
     */
    @Override
    public void onAudioFocusChange(int focusChange) {
        LogHelper.d(TAG, "onAudioFocusChange", focusChange);
        if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
            // We have gained focus:
            mAudioFocus = AUDIO_FOCUSED;

        } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS ||
                focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ||
                focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
            // We have lost focus. If we can duck (low playback volume), we can keep playing.
            // Otherwise, we need to pause the playback.
            boolean canDuck = focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK;
            mAudioFocus = canDuck ? AUDIO_NO_FOCUS_CAN_DUCK : AUDIO_NO_FOCUS_NO_DUCK;

            // If we are playing, we need to reset media player by calling configMediaPlayerState
            // with mAudioFocus properly set.
            if (mState == PlaybackStateCompat.STATE_PLAYING && !canDuck) {
                // If we don't have audio focus and can't duck, we save the information that
                // we were playing, so that we can resume playback once we get the focus back.
                mPlayOnFocusGain = true;
            }
        } else {
        }
        configMediaPlayerState(true);
    }

    /**
     * Called when MediaPlayer has completed a seek
     *
     * @see OnSeekCompleteListener
     */
    @Override
    public void onSeekComplete(MediaPlayer mp) {

    }

    /**
     * Called when media player is done playing current song.
     *
     * @see OnCompletionListener
     */
    @Override
    public void onCompletion(MediaPlayer player) {
        // The media player finished playing the current song, so we go ahead
        // and start the next.
        //if (mCallback != null) {
        //  mCallback.onCompletion();
        //}
    }

    /**
     * Called when media player is done preparing.
     *
     * @see OnPreparedListener
     */
    @Override
    public void onPrepared(MediaPlayer player) {
        // The media player is done preparing. That means we can start playing if we
        // have audio focus.
        configMediaPlayerState(false);
    }

    /**
     * Called when there's an error playing media. When this happens, the media
     * player goes to the Error state. We warn the user about the error and
     * reset the media player.
     *
     * @see OnErrorListener
     */
    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        if (mCallback != null) {
            mCallback.onError("MediaPlayer error " + what + " (" + extra + ")");
        }
        return true; // true indicates we handled the error
    }

    /**
     * Releases resources used by the service for playback. This includes the
     * "foreground service" status, the wake locks and possibly the MediaPlayer.
     *
     * @param releaseMediaPlayer Indicates whether the Media Player should also
     *                           be released or not
     */
    private void relaxResources(boolean releaseMediaPlayer, MediaPlayer player) {

        // stop and release the Media Player, if it's available
        if (releaseMediaPlayer && player != null) {
            player.reset();
            player.release();
            player = null;
        }

    }

    private void registerAudioNoisyReceiver() {
        if (!mAudioNoisyReceiverRegistered) {
            mContext.registerReceiver(mAudioNoisyReceiver, mAudioNoisyIntentFilter);
            mAudioNoisyReceiverRegistered = true;
        }
    }

    private void unregisterAudioNoisyReceiver() {
        if (mAudioNoisyReceiverRegistered) {
            mContext.unregisterReceiver(mAudioNoisyReceiver);
            mAudioNoisyReceiverRegistered = false;
        }
    }
}
