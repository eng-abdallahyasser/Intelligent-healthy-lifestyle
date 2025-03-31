package com.tm471a.intelligenthealthylifestyle.features.assistant;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
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
import com.tm471a.intelligenthealthylifestyle.utils.BoldTextFormatter;
import com.tm471a.intelligenthealthylifestyle.utils.Typewriter;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_USER = 0;
    private static final int TYPE_BOT = 1;
    private static final int TYPE_LOADING = 2;

    private static final int TYPE_BOT_TYPING = 3;


    static final private List<ChatMessage> messages = new ArrayList<>();
    private boolean isLoadingAdded = false;

    private boolean isTyping = false;
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
            case TYPE_BOT_TYPING:
                return new BotTypingViewHolder(inflater.inflate(R.layout.item_chat_bot, parent, false));
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
        else if (holder instanceof BotTypingViewHolder) {

            ((BotTypingViewHolder) holder).bind(message);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isLoadingAdded && position == messages.size() - 1) {
            return TYPE_LOADING;
        }
        if (position == messages.size() - 1 && isTyping) {
            return TYPE_BOT_TYPING;
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

            String[] parts = message.getContent().split("\\*\\*");

            SpannableStringBuilder spannableBuilder = new SpannableStringBuilder();

            for (int i = 0; i < parts.length; i++) {
                String part = parts[i];
                if (part.isEmpty()) continue; // Skip empty parts (e.g., if string starts/ends with "**")

                if (i % 2 == 1) { // Odd indices: Text between "**" (bold)
                    int start = spannableBuilder.length();
                    spannableBuilder.append(part);
                    spannableBuilder.setSpan(
                            new StyleSpan(Typeface.BOLD),
                            start,
                            spannableBuilder.length(),
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    );
                } else { // Even indices: Normal text
                    spannableBuilder.append(part);
                }
            }

            tvMessage.setText(spannableBuilder);
            cardView.setCardBackgroundColor(
                    itemView.getContext().getColor(R.color.bot_message_background)
            );
        }
    }
    class BotTypingViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvMessage;
        private final MaterialCardView cardView;
        private Typewriter typewriter;
        private int position = -1;

        BotTypingViewHolder(View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.tv_message);
            cardView = itemView.findViewById(R.id.card_view);

            // Initialize Typewriter
            typewriter = new Typewriter(tvMessage) {
                @Override
                public void animationEnded() {
                    isTyping=false;
                }
            };
            typewriter.setCharacterDelay(10); // Set typing speed (50ms per character)
        }

        void bind(ChatMessage message) {
            this.position = getAdapterPosition();

            // Cancel any ongoing animation
            typewriter.mHandler.removeCallbacks(typewriter.characterAdder);

            // Reset and start new animation
            cardView.setCardBackgroundColor(
                    itemView.getContext().getColor(R.color.bot_message_background)
            );
            typewriter.animateText(message.getContent());
        }

        void clearAnimation() {
            typewriter.mHandler.removeCallbacks(typewriter.characterAdder);
        }
    }

    @Override
    public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
        if (holder instanceof BotTypingViewHolder) {
            ((BotTypingViewHolder) holder).clearAnimation();
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
    public void addMessage(ChatMessage message) {
        messages.add(message);
        notifyItemInserted(messages.size() - 1);
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
            isTyping=true;
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