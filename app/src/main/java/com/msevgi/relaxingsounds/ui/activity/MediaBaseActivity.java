package com.msevgi.relaxingsounds.ui.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.AppCompatActivity;

import com.msevgi.relaxingsounds.player.MusicService;

/**
 * Created by mustafasevgi on 6.01.2018.
 */

public abstract class MediaBaseActivity extends AppCompatActivity {
    public static final String ACTION_SESSION_CONNECTED = "action.session.connected";
    public static final String ACTION_PLAY_STATE_CHANGED = "action.state.changed";
    public static final String EXTRA_STATE = "extra.state";

    private MediaBrowserCompat mMediaBrowser;

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

    protected void onStateChanged(PlaybackStateCompat state) {
    }

    ;

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
        //Uygulama her arkaya atılıp geri gelme durumunda yeni callback register oluyordu
        //bu durum da birden fazla metadata change cağrısı yapıyordu
        //onStart içerisinde bulunan connect buna sebep oluyor
        //onCreate onDestroy denenebilir ancak şu an bişi yapmıyoruz.
        //onun yerine workAround olarak bir önceki mediaControllerdan mevcut callback i
        //kaldırıp yeniden ekliyoruz
        MediaControllerCompat mediaController = MediaControllerCompat.getMediaController(this);
        if (mediaController != null) {
            mediaController.unregisterCallback(mMediaControllerCallback);
        }
        mediaController = new MediaControllerCompat(this, token);
        MediaControllerCompat.setMediaController(this, mediaController);
        mediaController.registerCallback(mMediaControllerCallback);
        sendBroadcastIntentForAction(ACTION_SESSION_CONNECTED, null);
    }

    protected void sendBroadcastIntentForAction(String action, Bundle bundle) {
        Intent intent = new Intent(action);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }
}
