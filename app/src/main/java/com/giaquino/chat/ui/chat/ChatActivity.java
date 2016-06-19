package com.giaquino.chat.ui.chat;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;
import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.giaquino.chat.ChatApplication;
import com.giaquino.chat.R;
import com.giaquino.chat.common.app.BaseActivity;
import com.giaquino.chat.common.widget.ConnectionStateView;
import com.giaquino.chat.common.widget.MarginDecoration;
import com.giaquino.chat.flux.action.ConnectionActionCreator;
import com.giaquino.chat.flux.action.MessageActionCreator;
import com.giaquino.chat.flux.action.ProfileActionCreator;
import com.giaquino.chat.flux.action.SubscriptionActionCreator;
import com.giaquino.chat.flux.store.ConnectionStore;
import com.giaquino.chat.flux.store.MessageStore;
import com.giaquino.chat.flux.store.ProfileStore;
import com.giaquino.chat.flux.store.SubscriptionStore;
import com.giaquino.chat.model.ConnectionModel;
import com.giaquino.chat.model.entity.ChatMessage;
import com.giaquino.chat.model.entity.Message;
import com.giaquino.chat.model.entity.Profile;
import com.giaquino.chat.model.image.ImageLoader;
import java.io.IOException;
import javax.inject.Inject;

/**
 * @author Gian Darren Azriel Aquino
 * @since 6/14/16
 */
public class ChatActivity extends BaseActivity
    implements ChatMessageAdapter.OnMessageClickListener {

    private static final String TOPIC = "com.giaquino.chat/messages";

    @Inject ObjectMapper mapper;
    @Inject ImageLoader imageLoader;

    @Inject ConnectionStore connectionStore;
    @Inject ConnectionActionCreator connectionActionCreator;

    @Inject SubscriptionStore subscriptionStore;
    @Inject SubscriptionActionCreator subscriptionActionCreator;

    @Inject MessageStore messageStore;
    @Inject MessageActionCreator messageActionCreator;

    @Inject ProfileStore profileStore;
    @Inject ProfileActionCreator profileActionCreator;

    @BindView(R.id.chat_connection_state) ConnectionStateView mConnectionStateView;
    @BindView(R.id.chat_view_chat_edit_text_message) EditText editTextMessage;
    @BindView(R.id.chat_view_chat_image_view_send) ImageView mImageViewSend;
    @BindView(R.id.chat_view_chat_recycler_view) RecyclerView mRecyclerView;

    @BindDimen(R.dimen.chat_margin_message_vertical) int mVerticalMargin;
    @BindDimen(R.dimen.chat_margin_message_horizontal) int mHorizontalMargin;

    @OnClick(R.id.chat_connection_state) public void onConnectionStateClick() {
        if (connectionStore.connectionState() == ConnectionModel.ConnectionState.DISCONNECTED) {
            connectionActionCreator.connect();
        }
    }

    @OnClick(R.id.chat_view_chat_image_view_send) public void onSendMessageClick() {
        String message = editTextMessage.getText().toString();
        if (TextUtils.isEmpty(message)) return;
        byte[] data = createChatMessage(editTextMessage.getText().toString());
        messageActionCreator.sendMessage(TOPIC, data, false);
        editTextMessage.setText("");
    }

    @Override public void onMessageClickListener(ChatMessage message) {
        if (message.messageState() != Message.MESSAGE_STATE_FAILED) return;
        try {
            messageActionCreator.sendMessage(TOPIC, ChatMessage.serialize(mapper, message), false);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private ChatMessageAdapter chatMessageAdapter;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity_chat);
        ButterKnife.bind(this);
        ChatApplication.get(this).applicationComponent().inject(this);
        initialize();
    }

    public void initialize() {
        chatMessageAdapter = new ChatMessageAdapter(this, imageLoader, this);
        mRecyclerView.setLayoutManager(
            new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));
        mRecyclerView.setAdapter(chatMessageAdapter);
        mRecyclerView.addItemDecoration(new MarginDecoration(mVerticalMargin, mHorizontalMargin));
        if (connectionStore.connectionState() == ConnectionModel.ConnectionState.DISCONNECTED) {
            connectionActionCreator.connect();
        }
        addSubscriptionToUnsubscribe(connectionStore.asObservable().subscribe(store -> {
            mConnectionStateView.setConnectionState(store.connectionState());
            switch (store.connectionState()) {
                case CONNECTED:
                    subscriptionActionCreator.subscribe(TOPIC);
                    break;
            }
        }));
        addSubscriptionToUnsubscribe(messageStore.asObservable()
            .map(store -> {
                try {
                    return ChatMessage.deserialize(mapper, store.message());
                } catch (IOException ignored) {
                }
                return null;
            })
            .filter(message -> message != null)
            .filter(message -> !(message.messageState() == Message.MESSAGE_STATE_ARRIVED
                && message.facebookId().equals(profileStore.profile().facebookId())))
            .subscribe(message -> {
                chatMessageAdapter.addMessage(message);
                mRecyclerView.scrollToPosition(0);
            }));
    }

    public byte[] createChatMessage(String message) {
        try {
            Profile profile = profileStore.profile();
            ChatMessage chatMessage = ChatMessage.create(profile.facebookId(), profile.picture(),
                profile.name(), message);
            return ChatMessage.serialize(mapper, chatMessage);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
