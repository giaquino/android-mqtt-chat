package com.giaquino.chat.model;

import android.support.annotation.NonNull;
import com.giaquino.chat.model.entity.Message;
import com.giaquino.chat.model.mqtt.MqttManager;
import java.util.concurrent.Executor;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.subjects.PublishSubject;
import timber.log.Timber;

/**
 * @author Gian Darren Azriel Aquino
 * @since 6/13/16
 */
public class MessageModel {

    private MqttManager mqttManager;
    private Executor backgroundExecutor;
    private PublishSubject<Message> messageDelivered = PublishSubject.create();
    private PublishSubject<Message> messageArrived = PublishSubject.create();
    private PublishSubject<Message> messageFailed = PublishSubject.create();
    private PublishSubject<Message> messageSending = PublishSubject.create();

    public MessageModel(@NonNull MqttManager manager, @NonNull Executor executor) {
        mqttManager = manager;
        backgroundExecutor = executor;
        mqttManager.setMqttMessageListener(this::handleMessage);
    }

    @NonNull public Observable<Message> messagesDelivered() {
        return messageDelivered.observeOn(AndroidSchedulers.mainThread())
            .doOnNext(message -> message.state(Message.MESSAGE_STATE_DELIVERED));
    }

    @NonNull public Observable<Message> messagesArrived() {
        return messageArrived.observeOn(AndroidSchedulers.mainThread())
            .doOnNext(message -> message.state(Message.MESSAGE_STATE_ARRIVED));
    }

    @NonNull public Observable<Message> messageFailed() {
        return messageFailed.observeOn(AndroidSchedulers.mainThread())
            .doOnNext(message -> message.state(Message.MESSAGE_STATE_FAILED));
    }

    @NonNull public Observable<Message> messageSending() {
        return messageSending.observeOn(AndroidSchedulers.mainThread())
            .doOnNext(message -> message.state(Message.MESSAGE_STATE_SENDING));
    }

    public void sendMessage(@NonNull String topic, @NonNull byte[] payload, boolean retain) {
        Message message = Message.create(topic, payload, retain);
        Timber.d("Sending message: %s", message);
        sendMessage(message);
        messageSending.onNext(message);
    }

    @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
    private void sendMessage(@NonNull Message message) {
        backgroundExecutor.execute(() -> {
            try {
                IMqttDeliveryToken token = mqttManager.publish(message.topic(), message.payload(),
                    message.retained());
                token.waitForCompletion();
                if (token.getException() == null) {
                    Timber.d("Message delivered: %s", message);
                    messageDelivered.onNext(message);
                } else {
                    Timber.d("Message failed: %s", message);
                    messageFailed.onNext(message);
                }
            } catch (Exception e) {
                Timber.d("Message failed: %s", message);
                messageFailed.onNext(message);
            }
        });
    }

    private void handleMessage(@NonNull String topic, @NonNull MqttMessage mqttMessage) {
        Message message = Message.create(topic, mqttMessage.getPayload(), mqttMessage.isRetained());
        Timber.d("Message arrived: %s", message);
        messageArrived.onNext(message);
    }
}
