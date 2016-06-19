package com.giaquino.chat.common.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.giaquino.chat.R;
import com.giaquino.chat.model.ConnectionModel;
import java.io.Serializable;

/**
 * @author Gian Darren Azriel Aquino
 * @since 6/15/16
 */
public class ConnectionStateView extends FrameLayout {

    private static final String KEY_CONNECTION_SATE = "ConnectionStateView.CONNECTION_STATE";

    @BindView(R.id.chat_connection_state_text) TextView textViewConnectionStatus;
    @BindDrawable(R.drawable.chat_ic_refresh) Drawable drawableRefresh;

    private ConnectionModel.ConnectionState currentConnectionState;

    public ConnectionStateView(Context context) {
        super(context);
    }

    public ConnectionStateView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ConnectionStateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }

    public void setConnectionState(@NonNull ConnectionModel.ConnectionState state) {
        currentConnectionState = state;
        switch (state) {
            case CONNECTING:
                setVisibility(View.VISIBLE);
                setBackgroundResource(android.R.color.holo_red_light);
                textViewConnectionStatus.setText(R.string.chat_label_connecting);
                textViewConnectionStatus.setCompoundDrawables(null, null, null, null);
                break;
            case CONNECTED:
                postDelayed(() -> setVisibility(View.GONE), 1000);
                setBackgroundResource(R.color.chat_accent);
                textViewConnectionStatus.setText(R.string.chat_label_connected);
                textViewConnectionStatus.setCompoundDrawables(null, null, null, null);
                break;
            case DISCONNECTED:
                setVisibility(View.VISIBLE);
                setBackgroundResource(android.R.color.holo_red_dark);
                textViewConnectionStatus.setText(R.string.chat_label_disconnected);
                textViewConnectionStatus.setCompoundDrawables(null, null, drawableRefresh, null);
                break;
        }
    }

    @Override protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_CONNECTION_SATE, currentConnectionState);
        return super.onSaveInstanceState();
    }

    @Override protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
        if (state instanceof Bundle) {
            Serializable serializable = ((Bundle) state).getSerializable(KEY_CONNECTION_SATE);
            if (serializable instanceof ConnectionModel.ConnectionState) {
                currentConnectionState = (ConnectionModel.ConnectionState) serializable;
                setConnectionState(currentConnectionState);
            }
        }
    }
}
