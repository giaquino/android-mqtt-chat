package com.giaquino.chat.model.mqtt;

import android.support.annotation.MainThread;

/**
 * @author Gian Darren Azriel Aquino
 * @since 6/13/16
 */
public interface MqttSubscriptionListener {

    @MainThread void onSubscriptionSuccess(String topic, int qos);

    @MainThread void onSubscriptionFailure(String topic, int qos);

    @MainThread void onUnsubscriptionSuccess(String topic);

    @MainThread void onUnsubscriptionFailure(String topic);
}
