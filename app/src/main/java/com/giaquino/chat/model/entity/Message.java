package com.giaquino.chat.model.entity;

import android.os.Parcelable;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import com.google.auto.value.AutoValue;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Gian Darren Azriel Aquino
 * @since 6/12/16
 */
@SuppressWarnings("mutable") @AutoValue public abstract class Message implements Parcelable {

    @IntDef({
        MESSAGE_STATE_DELIVERED, MESSAGE_STATE_ARRIVED, MESSAGE_STATE_SENDING, MESSAGE_STATE_FAILED
    }) @Retention(RetentionPolicy.SOURCE) public @interface MessageState {
    }

    public static final int MESSAGE_STATE_NONE = -1;
    public static final int MESSAGE_STATE_FAILED = 0;
    public static final int MESSAGE_STATE_SENDING = 1;
    public static final int MESSAGE_STATE_ARRIVED = 2;
    public static final int MESSAGE_STATE_DELIVERED = 3;

    @MessageState private int state = MESSAGE_STATE_NONE;

    public static Message create(@NonNull String topic, @NonNull byte[] payload, boolean retained) {
        return new AutoValue_Message(topic, payload, retained);
    }

    @MessageState public int state() {
        return state;
    }

    public void state(@MessageState int state) {
        this.state = state;
    }

    @NonNull public abstract String topic();

    @NonNull public abstract byte[] payload();

    public abstract boolean retained();
}
