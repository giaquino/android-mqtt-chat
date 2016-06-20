package com.giaquino.chat.model.mqtt;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.Executor;
import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * @author Gian Darren Azriel Aquino
 * @since 6/10/16
 */
public class MqttManager implements MqttCallback {

    private static final Handler MAIN_THREAD_HANDLER = new Handler(Looper.getMainLooper());

    private Executor backgroundExecutor;
    private MqttAndroidClient mqttClient;
    private MqttMessageListener mqttMessageListener;
    private MqttConnectionListener mqttConnectionListener;
    private MqttSubscriptionListener mqttSubscriptionListener;


    public MqttManager(@NonNull Application application, @NonNull String url,
        @NonNull Executor executor) {
        mqttClient = new MqttAndroidClient(application, url, MqttClient.generateClientId());
        mqttClient.setCallback(this);
        backgroundExecutor = executor;
    }

    public void connect() {
        try {
            watchConnection(mqttClient.connect());
        } catch (MqttException e) {
            notifyConnectionFailure(e);
        }
    }

    public void connect(@NonNull String topic, @NonNull String payload, int qos, boolean retain) {
        try {
            MqttConnectOptions options = new MqttConnectOptions();
            options.setWill(topic, payload.getBytes(), qos, retain);
            watchConnection(mqttClient.connect(options));
        } catch (MqttException e) {
            notifyConnectionFailure(e);
        }
    }

    public void disconnect() {
        try {
            watchDisconnection(mqttClient.disconnect());
        } catch (MqttException e) {
            notifyDisconnectionFailure(e);
        }
    }

    public void close() {
        mqttClient.close();
    }

    public boolean isConnected() {
        return mqttClient.isConnected();
    }

    public void subscribe(String topic, int qos) {
        try {
            watchSubscription(mqttClient.subscribe(topic, qos), topic, qos);
        } catch (MqttException e) {
            notifySubscriptionFailure(topic, qos);
        }
    }

    public void unsubscribe(String topic) {
        try {
            watchUnsubscription(mqttClient.unsubscribe(topic), topic);
        } catch (MqttException e) {
            notifyUnsubscriptionFailure(topic);
        }
    }

    public IMqttDeliveryToken publish(String topic, byte[] payload, boolean retain)
        throws UnsupportedEncodingException, MqttException {
        MqttMessage message = new MqttMessage(payload);
        message.setRetained(retain);
        return mqttClient.publish(topic, message);
    }

    public void setMqttMessageListener(@NonNull MqttMessageListener mqttMessageListener) {
        this.mqttMessageListener = mqttMessageListener;
    }

    public void setConnectionListener(@NonNull MqttConnectionListener connectionListener) {
        this.mqttConnectionListener = connectionListener;
    }

    public void setMqttSubscriptionListener(
        @NonNull MqttSubscriptionListener mqttSubscriptionListener) {
        this.mqttSubscriptionListener = mqttSubscriptionListener;
    }

    @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
    private void watchConnection(IMqttToken connectionToken) {
        backgroundExecutor.execute(() -> {
            try {
                connectionToken.waitForCompletion();
                if (connectionToken.isComplete() && connectionToken.getException() == null) {
                    notifyConnectionSuccess();
                } else {
                    notifyConnectionFailure(connectionToken.getException());
                }
            } catch (MqttException cause) {
                notifyConnectionFailure(cause);
            }
        });
    }

    @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
    private void watchDisconnection(IMqttToken disconnectionToken) {
        backgroundExecutor.execute(() -> {
            try {
                disconnectionToken.waitForCompletion();
                if (disconnectionToken.isComplete() && disconnectionToken.getException() == null) {
                    notifyDisconnectionSuccess();
                } else {
                    notifyDisconnectionFailure(disconnectionToken.getException());
                }
            } catch (MqttException cause) {
                notifyDisconnectionFailure(cause);
            }
        });
    }

    @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
    private void watchSubscription(IMqttToken subscriptionToken, String topic, int qos) {
        backgroundExecutor.execute(() -> {
            try {
                subscriptionToken.waitForCompletion();
                if (subscriptionToken.isComplete() && subscriptionToken.getException() == null) {
                    notifySubscriptionSuccess(topic, qos);
                } else {
                    notifySubscriptionFailure(topic, qos);
                }
            } catch (MqttException cause) {
                notifySubscriptionFailure(topic, qos);
            }
        });
    }

    @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
    private void watchUnsubscription(IMqttToken unsubscriptionToken, String topic) {
        backgroundExecutor.execute(() -> {
            try {
                unsubscriptionToken.waitForCompletion();
                if (unsubscriptionToken.isComplete() && unsubscriptionToken.getException() == null) {
                    notifyUnsubscriptionSuccess(topic);
                } else {
                    notifyUnsubscriptionFailure(topic);
                }
            } catch (MqttException cause) {
                notifyUnsubscriptionFailure(topic);
            }
        });
    }

    private void notifyConnectionSuccess() {
        MAIN_THREAD_HANDLER.post(() -> {
            if (mqttConnectionListener != null) mqttConnectionListener.onConnectionSuccess();
        });
    }

    private void notifyConnectionFailure(Throwable cause) {
        MAIN_THREAD_HANDLER.post(() -> {
            if (mqttConnectionListener != null) mqttConnectionListener.onConnectionFailure(cause);
        });
    }

    private void notifyDisconnectionSuccess() {
        MAIN_THREAD_HANDLER.post(() -> {
            if (mqttConnectionListener != null) mqttConnectionListener.onDisconnectionSuccess();
        });
    }

    private void notifyDisconnectionFailure(Throwable cause) {
        MAIN_THREAD_HANDLER.post(() -> {
            if (mqttConnectionListener != null) {
                mqttConnectionListener.onDisconnectionFailure(cause);
            }
        });
    }

    private void notifyConnectionLost(Throwable cause) {
        MAIN_THREAD_HANDLER.post(() -> {
            if (mqttConnectionListener != null) {
                mqttConnectionListener.onConnectionLost(cause);
            }
        });
    }

    private void notifySubscriptionSuccess(String topic, int qos) {
        MAIN_THREAD_HANDLER.post(() -> {
            if (mqttSubscriptionListener != null) {
                mqttSubscriptionListener.onSubscriptionSuccess(topic, qos);
            }
        });
    }

    private void notifySubscriptionFailure(String topic, int qos) {
        MAIN_THREAD_HANDLER.post(() -> {
            if (mqttSubscriptionListener != null) {
                mqttSubscriptionListener.onSubscriptionFailure(topic, qos);
            }
        });
    }

    private void notifyUnsubscriptionSuccess(String topic) {
        MAIN_THREAD_HANDLER.post(() -> {
            if (mqttSubscriptionListener != null) {
                mqttSubscriptionListener.onUnsubscriptionSuccess(topic);
            }
        });
    }

    private void notifyUnsubscriptionFailure(String topic) {
        MAIN_THREAD_HANDLER.post(() -> {
            if (mqttSubscriptionListener != null) {
                mqttSubscriptionListener.onUnsubscriptionFailure(topic);
            }
        });
    }

    @Override public void connectionLost(Throwable cause) {
        notifyConnectionLost(cause);
    }

    @Override public void messageArrived(String topic, MqttMessage message) throws Exception {
        MAIN_THREAD_HANDLER.post(() -> mqttMessageListener.onMessageArrived(topic, message));
    }

    @Override public void deliveryComplete(IMqttDeliveryToken token) {
    }
}
