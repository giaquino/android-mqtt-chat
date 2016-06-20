package com.giaquino.chat.model.mqtt;

import android.support.annotation.MainThread;

/**
 * @author Gian Darren Azriel Aquino
 * @since 6/13/16
 */
public interface MqttConnectionListener {

    @MainThread void onConnectionSuccess();

    @MainThread void onConnectionFailure(Throwable cause);

    @MainThread void onDisconnectionSuccess();

    @MainThread void onDisconnectionFailure(Throwable cause);

    @MainThread void onConnectionLost(Throwable cause);
}