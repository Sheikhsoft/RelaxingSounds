/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.msevgi.relaxingsounds.player;

import com.msevgi.relaxingsounds.model.Sound;
import com.msevgi.relaxingsounds.player.service.MusicService;

/**
 * Interface representing either Local or Remote Playback. The {@link MusicService} works
 * directly with an instance of the Playback object to make the various calls such as
 * playOrPause, pause etc.
 */
public interface Playback {

    /**
     * Stop the playback. All resources can be de-allocated by implementations here.
     *
     * @param notifyListeners if true and a callback has been set by setCallback,
     *                        callback.onPlaybackStatusChanged will be called after changing
     *                        the state.
     */
    void stop(boolean notifyListeners);

    /**
     * Get the current {@link android.media.session.PlaybackState#getState()}
     */
    int getState();

    /**
     * @return boolean indicating whether the player is playing or is supposed to be
     * playing when we gain audio focus.
     */
    boolean isPlaying();


    void play(Sound item);

    void play();

    void setSoundVolume(Sound sound);

    void pause();

    interface Callback {
        /**
         * On current music completed.
         */
        void onCompletion();

        /**
         * on Playback status changed
         * Implementations can use this callback to update
         * playback state on the media sessions.
         */
        void onPlaybackStatusChanged(int state);

        /**
         * @param error to be added to the PlaybackState
         */
        void onError(String error);

    }

    void setCallback(Callback callback);
}
