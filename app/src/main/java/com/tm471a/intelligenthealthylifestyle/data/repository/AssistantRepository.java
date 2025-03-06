package com.tm471a.intelligenthealthylifestyle.data.repository;

import androidx.lifecycle.MutableLiveData;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tm471a.intelligenthealthylifestyle.data.model.ChatMessage;
import java.util.ArrayList;
import java.util.List;

public class AssistantRepository {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final MutableLiveData<List<ChatMessage>> chatHistory = new MutableLiveData<>();
    private final List<ChatMessage> messages = new ArrayList<>();

    public interface ResponseCallback {
        void onResponse(String response);
        void onError(String error);
    }

    public AssistantRepository() {
        chatHistory.postValue(messages);
    }

    public void sendMessage(String message, ResponseCallback callback) {
        // Add user message
        messages.add(new ChatMessage(message, false));
        chatHistory.postValue(new ArrayList<>(messages));

        // Simulate AI response (replace with actual AI integration)
        db.collection("ai_responses").document("default")
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    String response = documentSnapshot.getString("response");
                    messages.add(new ChatMessage(response != null ? response : "I'm sorry, I didn't understand that", true));
                    chatHistory.postValue(new ArrayList<>(messages));
                    callback.onResponse(response);
                })
                .addOnFailureListener(e -> {
                    callback.onError(e.getMessage());
                });
    }

    public MutableLiveData<List<ChatMessage>> getChatHistory() {
        return chatHistory;
    }
}