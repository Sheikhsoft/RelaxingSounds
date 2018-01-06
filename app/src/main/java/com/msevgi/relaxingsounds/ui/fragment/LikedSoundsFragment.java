package com.msevgi.relaxingsounds.ui.fragment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.msevgi.relaxingsounds.R;
import com.msevgi.relaxingsounds.data.DataState;
import com.msevgi.relaxingsounds.data.DataWrapper;
import com.msevgi.relaxingsounds.model.Sound;
import com.msevgi.relaxingsounds.utils.ToolbarOptions;
import com.msevgi.relaxingsounds.viewmodel.SoundViewModel;
import com.msevgi.relaxingsounds.viewmodel.factory.SoundViewModelFactory;

import java.util.List;

/**
 * Created by mustafasevgi on 5.01.2018.
 */

public class LikedSoundsFragment extends BaseSoundsFragment {

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SoundViewModelFactory factory = new SoundViewModelFactory(null);
        mSoundViewModel = ViewModelProviders.of(this, factory).get(SoundViewModel.class);
        mBinding.setListener(this);
        mSoundViewModel.getSoundLiveData().observe(this, new Observer<DataWrapper<List<Sound>>>() {
            @Override
            public void onChanged(@Nullable DataWrapper<List<Sound>> listDataWrapper) {
                if (listDataWrapper.getState().ordinal() == DataState.SUCCESS.ordinal()) {
                    mBinding.setDataWrapper(listDataWrapper);
                    updatePlayingRows();
                }

            }
        });
    }

    @Override
    ToolbarOptions getToolbarOptions() {
        return new ToolbarOptions().setToggleType(ToolbarOptions.TOGGLE_NONE).setTitle(getString(R.string.title_favorites));
    }
}
