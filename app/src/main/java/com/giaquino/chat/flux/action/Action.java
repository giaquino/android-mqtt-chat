package com.giaquino.chat.flux.action;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;

/**
 * @author Gian Darren Azriel Aquino
 * @since 6/17/16
 */
@AutoValue public abstract class Action {

    public static Action create(@NonNull String type, @Nullable Object data) {
        return new AutoValue_Action(type, data);
    }

    @NonNull public abstract String type();

    @Nullable public abstract Object data();
}