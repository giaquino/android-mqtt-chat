package com.giaquino.chat.flux.action;

import android.support.annotation.NonNull;
import com.giaquino.chat.flux.dispatcher.Dispatcher;
import com.giaquino.chat.flux.store.MessageStore;
import com.giaquino.chat.model.MessageModel;
import com.giaquino.chat.model.entity.Message;

/**
 * @author Gian Darren Azriel Aquino
 * @since 6/17/16
 */
public class MessageActionCreator {

    private MessageModel messageModel;

    public MessageActionCreator(@NonNull Dispatcher dispatcher, @NonNull MessageModel model) {
        messageModel = model;
        messageModel.messageFailed().subscribe(message -> {
            message.state(Message.MESSAGE_STATE_FAILED);
            dispatcher.dispatch(Action.create(MessageStore.ACTION_UPDATE_MESSAGE, message));
        });
        messageModel.messageSending().subscribe(message -> {
            message.state(Message.MESSAGE_STATE_SENDING);
            dispatcher.dispatch(Action.create(MessageStore.ACTION_UPDATE_MESSAGE, message));
        });
        messageModel.messagesArrived().subscribe(message -> {
            message.state(Message.MESSAGE_STATE_ARRIVED);
            dispatcher.dispatch(Action.create(MessageStore.ACTION_UPDATE_MESSAGE, message));
        });
        messageModel.messagesDelivered().subscribe(message -> {
            message.state(Message.MESSAGE_STATE_DELIVERED);
            dispatcher.dispatch(Action.create(MessageStore.ACTION_UPDATE_MESSAGE, message));
        });
    }

    public void sendMessage(@NonNull String topic, @NonNull byte[] payload, boolean retain) {
        messageModel.sendMessage(topic, payload, retain);
    }
}
