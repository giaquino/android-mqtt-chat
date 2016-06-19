package com.giaquino.chat.flux.store;

import android.support.annotation.NonNull;
import com.giaquino.chat.flux.action.Action;
import com.giaquino.chat.model.ConnectionModel.ConnectionState;

/**
 * @author Gian Darren Azriel Aquino
 * @since 6/17/16
 */
public class ConnectionStore extends Store<ConnectionStore> {

    public static final String ACTION_UPDATE_CONNECTION_STATE = "ConnectionStore.UPDATE_CONNECTION_STATE";

    private ConnectionState connectionState = ConnectionState.DISCONNECTED;

    @NonNull public ConnectionState connectionState() {
        return connectionState;
    }

    @Override public void dispatchAction(@NonNull Action action) {
        switch (action.type()) {
            case ACTION_UPDATE_CONNECTION_STATE:
                connectionState = (ConnectionState) action.data();
                notifyStoreChanged(this);
                break;
        }
    }
}
