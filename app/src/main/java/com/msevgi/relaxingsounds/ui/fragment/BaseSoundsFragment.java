package com.msevgi.relaxingsounds.ui.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.msevgi.relaxingsounds.R;
import com.msevgi.relaxingsounds.adapter.SoundRecyclerAdapter;
import com.msevgi.relaxingsounds.databinding.SoundListBinding;
import com.msevgi.relaxingsounds.model.Sound;
import com.msevgi.relaxingsounds.viewmodel.SoundViewModel;

/**
 * Created by mustafasevgi on 5.01.2018.
 */

public abstract class BaseSoundsFragment extends BaseFragment implements SoundRecyclerAdapter.SoundItemClickListener {
    protected SoundListBinding mBinding;
    protected SoundViewModel mSoundViewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_sound_list, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void playOrPause(Sound sound, int position) {

    }

    @Override
    public void likeOrUnlike(Sound sound, int position) {

    }

    @Override
    public void volumeValue(int value, int position) {

    }
}
