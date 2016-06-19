package com.giaquino.chat.model.entity;

import android.support.annotation.NonNull;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auto.value.AutoValue;
import java.io.IOException;
import java.util.UUID;

/**
 * @author Gian Darren Azriel Aquino
 * @since 6/15/16
 */
@AutoValue public abstract class ChatMessage {

    private static final String JSON_PROPERTY_MESSAGE_ID = "message_id";
    private static final String JSON_PROPERTY_FACEBOOK_ID = "facebook_id";
    private static final String JSON_PROPERTY_FACEBOOK_PHOTO = "facebook_photo";
    private static final String JSON_PROPERTY_NAME = "name";
    private static final String JSON_PROPERTY_MESSAGE = "message";

    @Message.MessageState @JsonIgnore private int messageState = Message.MESSAGE_STATE_NONE;

    @JsonCreator public static ChatMessage create(
        @NonNull @JsonProperty(JSON_PROPERTY_FACEBOOK_ID) String facebookId,
        @NonNull @JsonProperty(JSON_PROPERTY_FACEBOOK_PHOTO) String facebookPhoto,
        @NonNull @JsonProperty(JSON_PROPERTY_NAME) String name,
        @NonNull @JsonProperty(JSON_PROPERTY_MESSAGE) String message) {
        return new AutoValue_ChatMessage(UUID.randomUUID().toString(), facebookId, facebookPhoto, name, message);
    }

    @JsonProperty(JSON_PROPERTY_MESSAGE_ID) public abstract String messageId();

    @JsonProperty(JSON_PROPERTY_FACEBOOK_ID) public abstract String facebookId();

    @JsonProperty(JSON_PROPERTY_FACEBOOK_PHOTO) public abstract String facebookPhoto();

    @JsonProperty(JSON_PROPERTY_NAME) public abstract String name();

    @JsonProperty(JSON_PROPERTY_MESSAGE) public abstract String message();

    @Message.MessageState public int messageState() {
        return messageState;
    }

    public void messageState(@Message.MessageState int messageState) {
        this.messageState = messageState;
    }

    public static byte[] serialize(ObjectMapper mapper, ChatMessage message)
        throws JsonProcessingException {
        return mapper.writeValueAsBytes(message);
    }

    public static ChatMessage deserialize(ObjectMapper mapper, Message message) throws IOException {
        ChatMessage chatMessage = mapper.readValue(message.payload(), ChatMessage.class);
        chatMessage.messageState(message.state());
        return chatMessage;
    }
}
