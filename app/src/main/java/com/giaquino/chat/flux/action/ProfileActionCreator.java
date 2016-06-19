package com.giaquino.chat.flux.action;

import android.support.annotation.NonNull;
import com.giaquino.chat.flux.dispatcher.Dispatcher;
import com.giaquino.chat.flux.store.ProfileStore;
import com.giaquino.chat.model.ProfileModel;

/**
 * @author Gian Darren Azriel Aquino
 * @since 6/17/16
 */
public class ProfileActionCreator {

    private ProfileModel profileModel;

    public ProfileActionCreator(@NonNull Dispatcher dispatcher, @NonNull ProfileModel model) {
        profileModel = model;
        profileModel.profile().subscribe(profile -> {
            dispatcher.dispatch(Action.create(ProfileStore.ACTION_UPDATE_PROFILE, profile));
        });
    }

    public void setProfile(@NonNull String facebookId, @NonNull String firstName,
        @NonNull String lastName, @NonNull String picture) {
        profileModel.setProfile(facebookId, firstName, lastName, picture);
    }
}
