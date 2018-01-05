package com.msevgi.relaxingsounds.ui.fragment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.msevgi.relaxingsounds.R;
import com.msevgi.relaxingsounds.viewmodel.factory.SoundViewModelFactory;
import com.msevgi.relaxingsounds.utils.ToolbarOptions;
import com.msevgi.relaxingsounds.data.DataState;
import com.msevgi.relaxingsounds.data.DataWrapper;
import com.msevgi.relaxingsounds.model.Category;
import com.msevgi.relaxingsounds.model.Sound;
import com.msevgi.relaxingsounds.viewmodel.SoundViewModel;

import java.util.List;

/**
 * Created by mustafasevgi on 5.01.2018.
 */

public class CategorySoundsFragment extends BaseSoundsFragment {

    private static final String EXTRA_CATEGORY = "extra.category";

    public static CategorySoundsFragment newInstance(Category category) {
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_CATEGORY, category);
        CategorySoundsFragment fragment = new CategorySoundsFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SoundViewModelFactory factory=new SoundViewModelFactory((Category) getArguments().getParcelable(EXTRA_CATEGORY));
        mSoundViewModel = ViewModelProviders.of(this,factory).get(SoundViewModel.class);
        mSoundViewModel.getSoundLiveData().observe(this, new Observer<DataWrapper<List<Sound>>>() {
            @Override
            public void onChanged(@Nullable DataWrapper<List<Sound>> listDataWrapper) {
                if (listDataWrapper.getState().ordinal() == DataState.SUCCESS.ordinal()) {
                    mBinding.setDataWrapper(listDataWrapper);
                    mBinding.setListener(CategorySoundsFragment.this);
                }

            }
        });
    }

    @Override
    ToolbarOptions getToolbarOptions() {
        return new ToolbarOptions().setToggleType(ToolbarOptions.TOGGLE_BACK).setTitle(getString(R.string.title_category_sounds, ((Category) getArguments().getParcelable(EXTRA_CATEGORY)).getName()));
    }

}
