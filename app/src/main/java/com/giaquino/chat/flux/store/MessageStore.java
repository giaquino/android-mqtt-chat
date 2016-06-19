package com.giaquino.chat.flux.store;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.giaquino.chat.flux.action.Action;
import com.giaquino.chat.model.entity.Message;

/**
 * @author Gian Darren Azriel Aquino
 * @since 6/17/16
 */
public class MessageStore extends Store<MessageStore> {

    public static final String ACTION_UPDATE_MESSAGE = "MessageStore.UPDATE_MESSAGE";

    private Message message;

    @Nullable public Message message() {
        return message;
    }

    @Override public void dispatchAction(@NonNull Action action) {
        switch (action.type()) {
            case ACTION_UPDATE_MESSAGE:
                message = (Message) action.data();
                notifyStoreChanged(this);
                break;
        }
    }
}
