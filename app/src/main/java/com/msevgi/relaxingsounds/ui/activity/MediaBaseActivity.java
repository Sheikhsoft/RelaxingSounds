package com.msevgi.relaxingsounds.ui.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.AppCompatActivity;

import com.msevgi.relaxingsounds.R;
import com.msevgi.relaxingsounds.player.service.MusicService;
import com.msevgi.relaxingsounds.ui.fragment.BaseFragment;

/**
 * Created by mustafasevgi on 6.01.2018.
 */

public abstract class MediaBaseActivity extends AppCompatActivity {
    public static final String ACTION_SESSION_CONNECTED = "action.session.connected";
    public static final String ACTION_PLAY_STATE_CHANGED = "action.state.changed";
    public static final String EXTRA_STATE = "extra.state";

    public static final int TRANSITION_TYPE_ADD = 1;
    public static final int TRANSITION_TYPE_REPLACE = 2;

    @IntDef({MediaBaseActivity.TRANSITION_TYPE_ADD, MediaBaseActivity.TRANSITION_TYPE_REPLACE})
    public @interface transitionType {
    }

    private MediaBrowserCompat mMediaBrowser;

    private MediaBrowserCompat.ConnectionCallback mConnectionCallback =
            new MediaBrowserCompat.ConnectionCallback() {
                @Override
                public void onConnected() {
                    try {
                        connectToSession(mMediaBrowser.getSessionToken());
                    } catch (RemoteException e) {
                    }
                }

                @Override
                public void onConnectionSuspended() {
                    MediaControllerCompat supportMediaController = MediaControllerCompat.getMediaController(MediaBaseActivity.this);
                    if (supportMediaController != null) {
                        supportMediaController.unregisterCallback(mMediaControllerCallback);
                    }
                }
            };

    private MediaControllerCompat.Callback mMediaControllerCallback =
            new MediaControllerCompat.Callback() {
                @Override
                public void onPlaybackStateChanged(@NonNull PlaybackStateCompat state) {
                    Bundle bundle = new Bundle();
                    bundle.putInt(EXTRA_STATE, state.getState());
                    sendBroadcastIntentForAction(ACTION_PLAY_STATE_CHANGED, bundle);
                    onStateChanged(state);
                }

                @Override
                public void onMetadataChanged(MediaMetadataCompat metadata) {
                }
            };

    protected abstract void onStateChanged(PlaybackStateCompat state);

    protected abstract void sessionConnected();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMediaBrowser = new MediaBrowserCompat(this,
                new ComponentName(this, MusicService.class), mConnectionCallback, null);
    }

    @Override
    public void onStart() {
        super.onStart();
        mMediaBrowser.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        mMediaBrowser.disconnect();
    }

    @Override
    protected void onDestroy() {
        MediaControllerCompat mediaController = MediaControllerCompat.getMediaController(this);
        if (mediaController != null)
            mediaController.unregisterCallback(mMediaControllerCallback);

        mMediaControllerCallback = null;
        mConnectionCallback = null;

        mMediaBrowser = null;
        super.onDestroy();
    }

    private void connectToSession(MediaSessionCompat.Token token) throws RemoteException {
        MediaControllerCompat mediaController = MediaControllerCompat.getMediaController(this);
        if (mediaController != null) {
            mediaController.unregisterCallback(mMediaControllerCallback);
        }
        mediaController = new MediaControllerCompat(this, token);
        MediaControllerCompat.setMediaController(this, mediaController);
        mediaController.registerCallback(mMediaControllerCallback);
        sendBroadcastIntentForAction(ACTION_SESSION_CONNECTED, null);
        sessionConnected();
    }

    protected void sendBroadcastIntentForAction(String action, Bundle bundle) {
        Intent intent = new Intent(action);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }

    public void showFragment(BaseFragment fragment, @MainActivity.transitionType int transitionType) {
        if (!isDestroyed()) {
            String tag = fragment.getClass().getName();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            switch (transitionType) {
                case TRANSITION_TYPE_ADD:
                    transaction.add(R.id.flContainer, fragment, tag);
                    break;
                case TRANSITION_TYPE_REPLACE:
                    transaction.replace(R.id.flContainer, fragment, tag);
                    break;
            }
            transaction.addToBackStack(tag).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
        }
    }
}
