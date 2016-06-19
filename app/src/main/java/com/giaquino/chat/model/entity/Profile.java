package com.giaquino.chat.model.entity;

import android.os.Parcelable;
import android.support.annotation.NonNull;
import com.google.auto.value.AutoValue;

/**
 * @author Gian Darren Azriel Aquino
 * @since 6/11/16
 */
@AutoValue public abstract class Profile implements ProfileModel, Parcelable {

    public static Profile create(@NonNull String facebookId, @NonNull String name,
        @NonNull String picture) {
        return new AutoValue_Profile(0, facebookId, name, picture);
    }

    public static final Mapper<Profile> MAPPER = new Mapper<>(
        (Mapper.Creator<Profile>) AutoValue_Profile::new);

    public static final class Marshal extends ProfileMarshal<Marshal> {
    }
}
