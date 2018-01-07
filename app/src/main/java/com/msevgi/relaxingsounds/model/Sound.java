package com.msevgi.relaxingsounds.model;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by mustafasevgi on 3.01.2018.
 */

public class Sound extends RealmObject implements Parcelable {
    @PrimaryKey
    private String id;
    private String name;
    private String category;
    private boolean isFavorite;
    private boolean isPlaying = false;
    private int volume = 25;
    private String uri;

    public Sound() {
    }

    public Sound(String id, String name, String category, boolean isFavorite, boolean isPlaying, int volume, String uri) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.isFavorite = isFavorite;
        this.isPlaying = isPlaying;
        this.volume = volume;
        this.uri = uri;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.category);
        dest.writeByte(this.isFavorite ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isPlaying ? (byte) 1 : (byte) 0);
        dest.writeInt(this.volume);
        dest.writeString(this.uri);
    }

    protected Sound(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.category = in.readString();
        this.isFavorite = in.readByte() != 0;
        this.isPlaying = in.readByte() != 0;
        this.volume = in.readInt();
        this.uri = in.readString();
    }

    public static final Parcelable.Creator<Sound> CREATOR = new Parcelable.Creator<Sound>() {
        @Override
        public Sound createFromParcel(Parcel source) {
            return new Sound(source);
        }

        @Override
        public Sound[] newArray(int size) {
            return new Sound[size];
        }
    };

    @Override
    public boolean equals(Object obj) {
        if (obj != null) {
            if (obj instanceof Sound) {
                if (((Sound) obj).getId().equals(getId())) {
                    return true;
                }
            }
        }
        return false;
    }

}
