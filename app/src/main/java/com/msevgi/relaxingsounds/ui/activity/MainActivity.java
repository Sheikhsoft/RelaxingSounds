package com.msevgi.relaxingsounds.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.MenuItem;
import android.view.View;

import com.msevgi.relaxingsounds.R;
import com.msevgi.relaxingsounds.databinding.MainBinding;
import com.msevgi.relaxingsounds.player.service.MusicService;
import com.msevgi.relaxingsounds.ui.fragment.CategoryFragment;
import com.msevgi.relaxingsounds.ui.fragment.LikedSoundsFragment;

/**
 * Created by mustafasevgi on 3.01.2018.
 */

public class MainActivity extends MediaBaseActivity implements View.OnClickListener {
    private MainBinding mBinding;
    private boolean isPlaying = false;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    showFragment(new CategoryFragment(), TRANSITION_TYPE_REPLACE);
                    return true;
                case R.id.navigation_favorites:
                    showFragment(new LikedSoundsFragment(), TRANSITION_TYPE_ADD);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mBinding.navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        if (savedInstanceState == null) {
            showFragment(new CategoryFragment(), TRANSITION_TYPE_REPLACE);
            mBinding.navigation.setSelectedItemId(R.id.navigation_home);
        }
        mBinding.ivPlayPause.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getSupportFragmentManager().popBackStackImmediate();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.flContainer);
        if (fragment instanceof CategoryFragment) {
            finish();
        } else
            super.onBackPressed();
    }

    @Override
    public void sessionConnected() {
        updateMiniPlayer();
    }

    @Override
    protected void onStateChanged(PlaybackStateCompat state) {
        updateMiniPlayer();
    }

    private void updateMiniPlayer() {
        MediaControllerCompat supportController = MediaControllerCompat.getMediaController(this);
        MediaMetadataCompat metadataCompat;
        if (supportController != null) {
            metadataCompat = supportController.getMetadata();
            if (metadataCompat != null) {
                String[] ids = metadataCompat.getBundle().getStringArray(MusicService.EXTRA_PLAYING_IDS);
                if (ids != null && ids.length > 0) {
                    isPlaying = true;
                    mBinding.ivPlayPause.setImageResource(R.drawable.ic_pause);
                } else {
                    isPlaying = false;
                    mBinding.ivPlayPause.setImageResource(R.drawable.ic_play);
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivPlayPause:
                MediaControllerCompat supportController = MediaControllerCompat.getMediaController(this);
                if (supportController != null) {
                    MediaControllerCompat.TransportControls transportControls = supportController.getTransportControls();
                    if (transportControls != null) {
                        if (isPlaying) {
                            transportControls.pause();
                        } else {
                            transportControls.play();
                        }
                    }
                }
                break;
        }
    }
}
