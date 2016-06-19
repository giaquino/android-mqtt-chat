package com.giaquino.chat.flux.action;

import android.support.annotation.NonNull;
import com.giaquino.chat.flux.dispatcher.Dispatcher;
import com.giaquino.chat.flux.store.ConnectionStore;
import com.giaquino.chat.model.ConnectionModel;

/**
 * @author Gian Darren Azriel Aquino
 * @since 6/17/16
 */
public class ConnectionActionCreator {

    private ConnectionModel connectionModel;

    public ConnectionActionCreator(@NonNull Dispatcher dispatcher, @NonNull ConnectionModel model) {
        connectionModel = model;
        connectionModel.connectionState().subscribe(connectionState -> {
            dispatcher.dispatch(
                Action.create(ConnectionStore.ACTION_UPDATE_CONNECTION_STATE, connectionState));
        });
    }

    public void connect() {
        connectionModel.connect();
    }

    public void disconnect() {
        connectionModel.disconnect();
    }

    public void close() {
        connectionModel.close();
    }
}
