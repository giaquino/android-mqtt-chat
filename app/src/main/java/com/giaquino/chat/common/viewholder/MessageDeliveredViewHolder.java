package com.giaquino.chat.common.viewholder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.giaquino.chat.R;
import com.giaquino.chat.model.entity.ChatMessage;
import com.giaquino.chat.model.entity.Message;

/**
 * @author Gian Darren Azriel Aquino
 * @since 6/15/16
 */
public class MessageDeliveredViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.chat_view_message_text_view_message) TextView textViewMessage;
    @BindView(R.id.chat_view_message_image_view_status) ImageView imageViewStatus;

    public static MessageDeliveredViewHolder create(@NonNull LayoutInflater inflater,
        @NonNull ViewGroup container) {
        return new MessageDeliveredViewHolder(
            inflater.inflate(R.layout.chat_view_message_delivered, container, false));
    }

    public MessageDeliveredViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(@NonNull ChatMessage chatMessage) {
        textViewMessage.setText(chatMessage.message());
        switch (chatMessage.messageState()) {
            case Message.MESSAGE_STATE_DELIVERED:
                imageViewStatus.setBackgroundResource(R.drawable.chat_bg_message_status_delivered);
                break;
            case Message.MESSAGE_STATE_SENDING:
                imageViewStatus.setBackgroundResource(R.drawable.chat_bg_message_status_sending);
                break;
            case Message.MESSAGE_STATE_FAILED:
                imageViewStatus.setBackgroundResource(R.drawable.chat_bg_message_status_failed);
                break;
            case Message.MESSAGE_STATE_ARRIVED:
                break;
        }
    }
}
