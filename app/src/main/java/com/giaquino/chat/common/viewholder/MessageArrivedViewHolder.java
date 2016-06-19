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
import com.giaquino.chat.model.image.ImageLoader;

/**
 * @author Gian Darren Azriel Aquino
 * @since 6/15/16
 */
public class MessageArrivedViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.chat_view_message_image_view_profile) ImageView imageViewProfile;
    @BindView(R.id.chat_view_message_text_view_name) TextView textViewName;
    @BindView(R.id.chat_view_message_text_view_message) TextView textViewMessage;

    private ImageLoader imageLoader;

    @NonNull public static MessageArrivedViewHolder create(@NonNull LayoutInflater inflater,
        @NonNull ViewGroup container, @NonNull ImageLoader imageLoader) {
        return new MessageArrivedViewHolder(
            inflater.inflate(R.layout.chat_view_message_arrived, container, false), imageLoader);
    }

    public MessageArrivedViewHolder(@NonNull View itemView, @NonNull ImageLoader imageLoader) {
        super(itemView);
        this.imageLoader = imageLoader;
        ButterKnife.bind(this, itemView);
    }

    public void bind(@NonNull ChatMessage message) {
        textViewName.setText(message.name());
        textViewMessage.setText(message.message());
        imageLoader.downloadImageInto(message.facebookPhoto(), imageViewProfile);
    }
}
