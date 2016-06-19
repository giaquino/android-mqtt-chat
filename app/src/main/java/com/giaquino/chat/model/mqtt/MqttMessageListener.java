package com.giaquino.chat.model.mqtt;

import android.support.annotation.MainThread;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * @author Gian Darren Azriel Aquino
 * @since 6/13/16
 */
public interface MqttMessageListener {

    @MainThread void onMessageArrived(String topic, MqttMessage message);
}