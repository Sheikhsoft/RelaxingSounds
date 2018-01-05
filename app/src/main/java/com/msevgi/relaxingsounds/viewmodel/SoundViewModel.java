package com.msevgi.relaxingsounds.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.msevgi.relaxingsounds.api.ApiModule;
import com.msevgi.relaxingsounds.data.BeanFactory;
import com.msevgi.relaxingsounds.data.DataWrapper;
import com.msevgi.relaxingsounds.model.Category;
import com.msevgi.relaxingsounds.model.Sound;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mustafasevgi on 3.01.2018.
 */

public class SoundViewModel extends ViewModel {
    private final MutableLiveData<DataWrapper<List<Sound>>> soundLiveData;

    public SoundViewModel(Category category) {
        soundLiveData = new MutableLiveData<>();
        if (category != null) {
            getCategorySounds(category);
        } else {
            getLikedSounds();
        }
    }

    public void getLikedSounds() {
        soundLiveData.setValue(BeanFactory.fetching(null));

        ApiModule.getInstance().getService().serviceFavoriteSoundList().enqueue(new Callback<ArrayList<Sound>>() {
            @Override
            public void onResponse(Call<ArrayList<Sound>> call, @NonNull Response<ArrayList<Sound>> response) {
                if (response.body() != null && response.body().size() > 0) {
                    soundLiveData.setValue(BeanFactory.success(response.body()));
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Sound>> call, Throwable t) {
                soundLiveData.setValue(BeanFactory.error(null, null));
            }
        });

    }

    public void getCategorySounds(Category category) {
        soundLiveData.setValue(BeanFactory.fetching(null));

        ApiModule.getInstance().getService().serviceFavoriteSoundList().enqueue(new Callback<ArrayList<Sound>>() {
            @Override
            public void onResponse(Call<ArrayList<Sound>> call, @NonNull Response<ArrayList<Sound>> response) {
                if (response.body() != null && response.body().size() > 0) {
                    // TODO: 5.01.2018 filter with category
                    soundLiveData.setValue(BeanFactory.success(response.body()));
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Sound>> call, Throwable t) {
                soundLiveData.setValue(BeanFactory.error(null, null));
            }
        });

    }

    public MutableLiveData<DataWrapper<List<Sound>>> getSoundLiveData() {
        return soundLiveData;
    }
}
