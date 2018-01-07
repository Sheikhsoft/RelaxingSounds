package com.msevgi.relaxingsounds.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by mustafasevgi on 7.01.2018.
 */

public class RealmSound extends RealmObject {
    public static final String KEY = "liked_sound";
    @PrimaryKey
    private String id;
    private RealmList<Sound> likedSoundList = new RealmList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public RealmList<Sound> getLikedSoundList() {
        return likedSoundList;
    }

    public void setLikedSoundList(RealmList<Sound> likedSoundList) {
        this.likedSoundList = likedSoundList;
    }
}
