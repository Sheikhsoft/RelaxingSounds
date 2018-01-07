package com.msevgi.relaxingsounds.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.msevgi.relaxingsounds.R;
import com.msevgi.relaxingsounds.api.ApiModule;
import com.msevgi.relaxingsounds.api.RetrofitCallback;
import com.msevgi.relaxingsounds.data.BeanFactory;
import com.msevgi.relaxingsounds.data.DataWrapper;
import com.msevgi.relaxingsounds.data.RSError;
import com.msevgi.relaxingsounds.model.Category;
import com.msevgi.relaxingsounds.model.Sound;
import com.msevgi.relaxingsounds.player.LogHelper;
import com.msevgi.relaxingsounds.utils.RealmUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import retrofit2.Call;

/**
 * Created by mustafasevgi on 3.01.2018.
 */

public class SoundViewModel extends ViewModel {
    private final static String TAG = LogHelper.makeLogTag(SoundViewModel.class);
    private final MutableLiveData<DataWrapper<List<Sound>>> soundLiveData;
    private final MutableLiveData<List<Sound>> likedSoundLiveData;

    public SoundViewModel(Category category) {
        soundLiveData = new MutableLiveData<>();

        likedSoundLiveData = new MutableLiveData<>();
        likedSoundLiveData.setValue(RealmUtils.readLikedSoundsFromRealm());

        if (category != null) {
            getCategorySounds(category);
        } else {
            getLikedSounds();
        }
    }

    public void getLikedSounds() {
        soundLiveData.setValue(BeanFactory.fetching(null));

        if (likedSoundLiveData.getValue() != null) {
            if (likedSoundLiveData.getValue().size() == 0) {
                soundLiveData.setValue(BeanFactory.error(null, new RSError(RSError.NO_RESULT, R.string.no_liked_sound)));
            } else {
                soundLiveData.setValue(BeanFactory.success(likedSoundLiveData.getValue()));
            }
        } else {
            ApiModule.getInstance().getService().serviceCategorySoundList().enqueue(new RetrofitCallback<ArrayList<Sound>>() {
                @Override
                public void failure(Call<ArrayList<Sound>> call, Throwable t) {
                    soundLiveData.setValue(BeanFactory.error(null, new RSError(RSError.DEFAULT_ERROR, R.string.default_error)));
                }

                @Override
                public void success(Call<ArrayList<Sound>> call, ArrayList<Sound> response) {
                    if (response != null && response.size() > 0) {
                        List<Sound> list = new ArrayList<>();
                        for (Sound sound : response) {
                            if (sound.isFavorite()) {
                                list.add(sound);
                            }
                        }
                        RealmUtils.saveLikedSoundsToRealm(list);
                        soundLiveData.setValue(BeanFactory.success(list));
                    }
                }

                @Override
                public void noInternetConnection() {
                    soundLiveData.setValue(BeanFactory.error(null, new RSError(RSError.NO_CONNECTION, R.string.no_connection)));
                }
            });
        }
    }


    public void getCategorySounds(final Category category) {
        soundLiveData.setValue(BeanFactory.fetching(null));
        ApiModule.getInstance().getService().serviceCategorySoundList().enqueue(new RetrofitCallback<ArrayList<Sound>>() {
            @Override
            public void failure(Call<ArrayList<Sound>> call, Throwable t) {
                soundLiveData.setValue(BeanFactory.error(null, new RSError(RSError.DEFAULT_ERROR, R.string.default_error)));
            }

            @Override
            public void success(Call<ArrayList<Sound>> call, ArrayList<Sound> response) {
                if (response != null && response.size() > 0) {
                    List<Sound> list = new ArrayList<>();
                    for (Sound sound : response) {
                        if (sound.getCategory().equalsIgnoreCase(category.getKey())) {
                            if (likedSoundLiveData.getValue().contains(sound)) {
                                sound.setFavorite(true);
                            } else {
                                sound.setFavorite(false);
                            }
                            list.add(sound);
                        }
                    }
                    soundLiveData.setValue(BeanFactory.success(list));
                }
            }

            @Override
            public void noInternetConnection() {
                soundLiveData.setValue(BeanFactory.error(null, new RSError(RSError.NO_CONNECTION, R.string.no_connection)));
            }
        });
    }

    public MutableLiveData<DataWrapper<List<Sound>>> getSoundLiveData() {
        return soundLiveData;
    }

    public void updatePlayingRows(List<Sound> list, Set<String> ids) {
        if (list != null) {
            for (Sound sound : list) {
                if (ids.contains(sound.getId())) {
                    sound.setPlaying(true);
                } else
                    sound.setPlaying(false);
            }
        }
    }

    public void updateEmptyCase() {
        if (soundLiveData.getValue() != null && soundLiveData.getValue().getData().size() == 0) {
            soundLiveData.setValue(BeanFactory.error(null, new RSError(RSError.NO_RESULT, R.string.no_liked_sound)));
        }
    }
}
