package com.tm471a.intelligenthealthylifestyle.features.assistant;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.tm471a.intelligenthealthylifestyle.R;
import com.tm471a.intelligenthealthylifestyle.data.model.ChatMessage;
import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_USER = 0;
    private static final int TYPE_BOT = 1;
    private static final int TYPE_LOADING = 2;

    final private List<ChatMessage> messages = new ArrayList<>();
    private boolean isLoadingAdded = false;
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case TYPE_USER:
                return new UserViewHolder(inflater.inflate(R.layout.item_chat_user, parent, false));
            case TYPE_BOT:
                return new BotViewHolder(inflater.inflate(R.layout.item_chat_bot, parent, false));
            case TYPE_LOADING:
                return new LoadingViewHolder(inflater.inflate(R.layout.item_chat_loading, parent, false));
            default:
                throw new IllegalArgumentException("Invalid view type");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatMessage message = messages.get(position);

        if (holder instanceof UserViewHolder) {
            ((UserViewHolder) holder).bind(message);
        } else if (holder instanceof BotViewHolder) {
            ((BotViewHolder) holder).bind(message);
        } else if (holder instanceof LoadingViewHolder) {
            ((LoadingViewHolder) holder).bind();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isLoadingAdded && position == messages.size() - 1) {
            return TYPE_LOADING;
        }
        return messages.get(position).isBot() ? TYPE_BOT : TYPE_USER;
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    // ViewHolder classes
    static class UserViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvMessage;
        private final MaterialCardView cardView;

        UserViewHolder(View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.tv_message);
            cardView = itemView.findViewById(R.id.card_view);
        }

        void bind(ChatMessage message) {
            tvMessage.setText(message.getContent());
            cardView.setCardBackgroundColor(
                    itemView.getContext().getColor(R.color.user_message_background)
            );
        }
    }

    static class BotViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvMessage;
        private final MaterialCardView cardView;

        BotViewHolder(View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.tv_message);
            cardView = itemView.findViewById(R.id.card_view);
        }

        void bind(ChatMessage message) {
            tvMessage.setText(message.getContent());
            cardView.setCardBackgroundColor(
                    itemView.getContext().getColor(R.color.bot_message_background)
            );
        }
    }

    static class LoadingViewHolder extends RecyclerView.ViewHolder {
        private final CircularProgressIndicator progressBar;

        LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }

        void bind() {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setIndeterminate(true);
        }
    }

    // Adapter methods
    @SuppressLint("NotifyDataSetChanged")
    public void submitList(List<ChatMessage> newMessages) {
        messages.clear();
        messages.addAll(newMessages);
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void showLoading() {
        isLoadingAdded = true;
        // Create empty bot message to maintain list consistency
        messages.add(new ChatMessage("", true));
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void hideLoading() {
        if (isLoadingAdded) {
            isLoadingAdded = false;
            for(int i = 0; i < messages.size();){
                if(messages.get(i).getContent().isEmpty()){
                    messages.remove(i);
                }else{
                    i++;
                }
            }
            notifyDataSetChanged();
        }
    }
}