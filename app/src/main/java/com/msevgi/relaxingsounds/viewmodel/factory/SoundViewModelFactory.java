package com.msevgi.relaxingsounds.viewmodel.factory;

import android.arch.lifecycle.ViewModelProvider;

import com.msevgi.relaxingsounds.model.Category;
import com.msevgi.relaxingsounds.viewmodel.SoundViewModel;

/**
 * Created by mustafasevgi on 5.01.2018.
 */

public class SoundViewModelFactory implements ViewModelProvider.Factory {
    private Category mCategory;

    public SoundViewModelFactory(Category category) {
        mCategory = category;
    }

    @Override
    public SoundViewModel create(Class modelClass) {
        return new SoundViewModel(mCategory);
    }
}
