package com.msevgi.relaxingsounds.utils;

import com.msevgi.relaxingsounds.model.RealmSound;
import com.msevgi.relaxingsounds.model.Sound;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

/**
 * Created by mustafasevgi on 7.01.2018.
 */

public class RealmUtils {

    public static void saveLikedSoundsToRealm(final List<Sound> list) {
        try {
            Realm realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    RealmSound realmSound = realm.where(RealmSound.class).equalTo("id", RealmSound.KEY).findFirst();
                    if (realmSound == null) {
                        realmSound = realm.createObject(RealmSound.class, RealmSound.KEY);
                    }
                    realmSound.getLikedSoundList().addAll(list);
                    realm.insertOrUpdate(realmSound);
                }
            });
        } catch (Exception e) {
        }
    }

    public static void saveSoundToRealm(final Sound sound) {
        try {
            Realm realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    RealmSound realmSound = realm.where(RealmSound.class).equalTo("id", RealmSound.KEY).findFirst();
                    if (realmSound == null) {
                        realmSound = realm.createObject(RealmSound.class, RealmSound.KEY);
                    }
                    if (!realmSound.getLikedSoundList().contains(sound)) {
                        realmSound.getLikedSoundList().add(sound);
                        realm.insertOrUpdate(realmSound);
                    }
                }
            });
        } catch (Exception e) {
        }
    }

    public static void removeSoundFromRealm(final Sound sound) {
        try {
            Realm realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    RealmSound realmSound = realm.where(RealmSound.class).equalTo("id", RealmSound.KEY).findFirst();
                    if (realmSound == null) {
                        realmSound = realm.createObject(RealmSound.class, RealmSound.KEY);
                    }
                    realmSound.getLikedSoundList().remove(sound);
                    realm.insertOrUpdate(realmSound);
                }
            });
        } catch (Exception e) {
        }
    }

    public static List<Sound> readLikedSoundsFromRealm() {
        final List<Sound> list = new ArrayList<>();
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmSound realmSound = realm.where(RealmSound.class).equalTo("id", RealmSound.KEY).findFirst();
                if (realmSound != null && realmSound.getLikedSoundList().size() > 0) {
                    for (Sound sound : realmSound.getLikedSoundList()) {
                        list.add(new Sound(sound.getId(), sound.getName(), sound.getCategory(), sound.isFavorite(), sound.isPlaying(), sound.getVolume(), sound.getUri()));
                    }
                }
            }
        });
        return list;
    }
}
