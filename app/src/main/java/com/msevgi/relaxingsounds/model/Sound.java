package com.msevgi.relaxingsounds.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mustafasevgi on 3.01.2018.
 */

public class Sound implements Parcelable {

    private String id;
    private String name;
    private String category;
    private int resourceId;
    private boolean isFavorite;
    private boolean isPlaying=false;

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
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

    public Sound() {
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
        dest.writeInt(this.resourceId);
        dest.writeByte(this.isFavorite ? (byte) 1 : (byte) 0);
    }

    protected Sound(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.category = in.readString();
        this.resourceId = in.readInt();
        this.isFavorite = in.readByte() != 0;
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
}
