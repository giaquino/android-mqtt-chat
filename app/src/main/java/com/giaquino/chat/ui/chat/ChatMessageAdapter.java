package com.giaquino.chat.ui.chat;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.giaquino.chat.common.viewholder.MessageArrivedViewHolder;
import com.giaquino.chat.common.viewholder.MessageDeliveredViewHolder;
import com.giaquino.chat.model.entity.ChatMessage;
import com.giaquino.chat.model.entity.Message;
import com.giaquino.chat.model.image.ImageLoader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Gian Darren Azriel Aquino
 * @since 6/15/16
 */
public class ChatMessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_MESSAGE_ARRIVED = 1;
    private static final int VIEW_TYPE_MESSAGE_DELIVERED = 2;

    public interface OnMessageClickListener {
        void onMessageClickListener(ChatMessage message);
    }

    private ImageLoader imageLoader;
    private LayoutInflater inflater;
    private OnMessageClickListener messageClickListener;
    private List<ChatMessage> chatMessages = new ArrayList<>(25);

    public ChatMessageAdapter(@NonNull Context context, @NonNull ImageLoader loader,
        @NonNull OnMessageClickListener listener) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader = loader;
        messageClickListener = listener;
    }

    @Override public int getItemViewType(int position) {
        if (chatMessages.get(position).messageState() == Message.MESSAGE_STATE_ARRIVED) {
            return VIEW_TYPE_MESSAGE_ARRIVED;
        } else {
            return VIEW_TYPE_MESSAGE_DELIVERED;
        }
    }

    @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_MESSAGE_ARRIVED:
                return MessageArrivedViewHolder.create(inflater, parent, imageLoader);

            case VIEW_TYPE_MESSAGE_DELIVERED:
                return MessageDeliveredViewHolder.create(inflater, parent);

            default:
                throw new IllegalArgumentException("Invalid viewType :" + viewType);
        }
    }

    @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ChatMessage chatMessage = chatMessages.get(position);
        switch (getItemViewType(position)) {
            case VIEW_TYPE_MESSAGE_ARRIVED:
                ((MessageArrivedViewHolder) holder).bind(chatMessage);
                break;
            case VIEW_TYPE_MESSAGE_DELIVERED:
                ((MessageDeliveredViewHolder) holder).bind(chatMessage);
                break;
        }
        holder.itemView.setOnClickListener(
            v -> messageClickListener.onMessageClickListener(chatMessage));
    }

    @Override public int getItemCount() {
        return chatMessages.size();
    }

    public void addMessage(@NonNull ChatMessage chatMessage) {
        if (chatMessages.contains(chatMessage)) {
            int index = chatMessages.indexOf(chatMessage);
            chatMessages.set(index, chatMessage);
            notifyItemChanged(index);
        } else {
            chatMessages.add(0, chatMessage);
            notifyItemInserted(0);
        }
    }
}
