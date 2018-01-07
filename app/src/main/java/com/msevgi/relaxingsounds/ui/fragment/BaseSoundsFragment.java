package com.msevgi.relaxingsounds.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.msevgi.relaxingsounds.R;
import com.msevgi.relaxingsounds.adapter.SoundRecyclerAdapter;
import com.msevgi.relaxingsounds.databinding.SoundListBinding;
import com.msevgi.relaxingsounds.model.Sound;
import com.msevgi.relaxingsounds.player.LogHelper;
import com.msevgi.relaxingsounds.player.PlaybackManager;
import com.msevgi.relaxingsounds.player.service.MusicService;
import com.msevgi.relaxingsounds.ui.activity.MediaBaseActivity;
import com.msevgi.relaxingsounds.viewmodel.SoundViewModel;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by mustafasevgi on 5.01.2018.
 */

public abstract class BaseSoundsFragment extends BaseFragment implements SoundRecyclerAdapter.SoundItemClickListener {
    public final String TAG = LogHelper.makeLogTag(BaseSoundsFragment.class);
    protected SoundListBinding mBinding;
    protected SoundViewModel mSoundViewModel;

    BroadcastReceiver mediaUpdatedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            MediaControllerCompat supportMediaController = null;
            if (getActivity() != null) {
                supportMediaController = MediaControllerCompat.getMediaController(getActivity());
                if (supportMediaController != null && intent != null && intent.getAction() != null) {
                    MediaMetadataCompat metadata = supportMediaController.getMetadata();

                    switch (intent.getAction()) {
                        case MediaBaseActivity.ACTION_SESSION_CONNECTED:
                            break;
                        case MediaBaseActivity.ACTION_PLAY_STATE_CHANGED:

                            onPlaybackStateChanged(intent.getExtras().getInt(MediaBaseActivity.EXTRA_STATE));
                            break;
                    }
                }
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentFilter filter = new IntentFilter(MediaBaseActivity.ACTION_SESSION_CONNECTED);
        filter.addAction(MediaBaseActivity.ACTION_PLAY_STATE_CHANGED);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mediaUpdatedReceiver, filter);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_sound_list, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void playOrPause(Sound sound, int position) {
        playOrPause(sound);
    }

    @Override
    public void likeOrUnlike(Sound sound, int position) {

    }

    @Override
    public void volumeValue(Sound sound, int value) {
        LogHelper.d(TAG, "volume value :", value);
        Bundle bundle = new Bundle();
        bundle.putParcelable(PlaybackManager.EXTRA_SOUND, sound);
        MediaControllerCompat.getMediaController(getActivity()).getTransportControls().sendCustomAction(PlaybackManager.CUSTOM_ACTION_SOUND_VOLUME, bundle);
    }

    public void playOrPause(final Sound sound) {
        if (getActivity() != null) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(PlaybackManager.EXTRA_SOUND, sound);
            MediaControllerCompat.getMediaController(getActivity()).getTransportControls().playFromMediaId(sound.getId(), bundle);
        }
    }

    protected void onPlaybackStateChanged(int state) {
        updatePlayingRows();
    }

    public void updatePlayingRows() {
        MediaControllerCompat supportController = MediaControllerCompat.getMediaController(getActivity());
        if (supportController != null) {
            MediaMetadataCompat metadataCompat = supportController.getMetadata();
            if (metadataCompat != null) {
                String[] ids = metadataCompat.getBundle().getStringArray(MusicService.EXTRA_PLAYING_IDS);
                if (ids != null) {
                    Set<String> set = new HashSet<String>(Arrays.asList(ids));
                    mSoundViewModel.updatePlayingRows(mBinding.getDataWrapper().getData(), set);
                    if (mBinding.recyclerviewProducts.getAdapter() != null) {
                        mBinding.recyclerviewProducts.getAdapter().notifyDataSetChanged();
                    }
                }
            }
        }
    }
}
