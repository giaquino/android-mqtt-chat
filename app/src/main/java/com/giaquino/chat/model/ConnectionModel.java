package com.giaquino.chat.model;

import android.support.annotation.NonNull;
import com.giaquino.chat.model.mqtt.MqttConnectionListener;
import com.giaquino.chat.model.mqtt.MqttManager;
import rx.Observable;
import rx.subjects.BehaviorSubject;
import timber.log.Timber;

/**
 * This Class manages the connection to the {@link MqttManager}.
 *
 * @author Gian Darren Azriel Aquino
 * @since 6/13/16
 */
public class ConnectionModel {

    public enum ConnectionState {
        CONNECTING, CONNECTED, DISCONNECTING, DISCONNECTED, CLOSED
    }

    private MqttManager mqttManager;
    private ConnectionState lastConnectionState;
    private BehaviorSubject<ConnectionState> connectionState;

    public ConnectionModel(@NonNull MqttManager manager) {
        mqttManager = manager;
        mqttManager.setConnectionListener(new MqttConnectionListener() {
            @Override public void onConnectionSuccess() {
                Timber.d("Connection success!");
                publish(ConnectionState.CONNECTED);
            }

            @Override public void onConnectionFailure(Throwable cause) {
                Timber.d("Connection failure: %s", cause.getMessage());
                publish(ConnectionState.DISCONNECTED);
            }

            @Override public void onDisconnectionSuccess() {
                Timber.d("Disconnection success!");
                publish(ConnectionState.DISCONNECTED);
            }

            @Override public void onDisconnectionFailure(Throwable cause) {
                Timber.d("Disconnection failure: %s", cause.getMessage());
                publish(lastConnectionState);
            }
        });
        lastConnectionState =
            mqttManager.isConnected() ? ConnectionState.CONNECTED : ConnectionState.DISCONNECTED;
        connectionState = BehaviorSubject.create(lastConnectionState);
    }

    @NonNull public Observable<ConnectionState> connectionState() {
        return connectionState.asObservable();
    }

    public void connect() {
        publish(ConnectionState.CONNECTING);
        mqttManager.connect();
    }

    public void disconnect() {
        publish(ConnectionState.DISCONNECTING);
        mqttManager.disconnect();
    }

    public void close() {
        mqttManager.close();
        publish(ConnectionState.CLOSED);
        connectionState.onCompleted();
    }

    private void publish(@NonNull ConnectionState state) {
        lastConnectionState = state;
        connectionState.onNext(lastConnectionState);
    }
}
