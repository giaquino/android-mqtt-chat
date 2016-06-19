package com.giaquino.chat.flux.store;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.giaquino.chat.flux.action.Action;
import com.giaquino.chat.model.entity.Profile;

/**
 * @author Gian Darren Azriel Aquino
 * @since 6/17/16
 */
public class ProfileStore extends Store<ProfileStore> {

    public static final String ACTION_UPDATE_PROFILE = "ProfileStore.UPDATE_PROFILE";

    private Profile profile;

    @Nullable public Profile profile() {
        return profile;
    }

    @Override public void dispatchAction(@NonNull Action action) {
        switch (action.type()) {
            case ACTION_UPDATE_PROFILE:
                profile = (Profile) action.data();
                notifyStoreChanged(this);
                break;
        }
    }
}
