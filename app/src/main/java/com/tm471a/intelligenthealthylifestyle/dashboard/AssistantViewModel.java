package com.tm471a.intelligenthealthylifestyle.dashboard;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.tm471a.intelligenthealthylifestyle.data.model.ChatMessage;
import com.tm471a.intelligenthealthylifestyle.data.repository.AssistantRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AssistantViewModel extends AndroidViewModel {
    private final AssistantRepository repository;
    private final MutableLiveData<List<ChatMessage>> messages = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    public AssistantViewModel(@NonNull Application application) {
        super(application);
        this.repository = new AssistantRepository(application); // Initialize with Context
    }
    public void sendMessage(String message) {
        isLoading.postValue(true);

        // Add user message immediately
        List<ChatMessage> current = new ArrayList<>(Objects.requireNonNull(messages.getValue()));
        current.add(new ChatMessage(message, false));
        messages.postValue(current);

        repository.sendMessage(message, new AssistantRepository.ResponseCallback() {
            @Override
            public void onResponse(String response) {
                ChatMessage botMsg = new ChatMessage(response != null ? response :"I'm sorry, I didn't understand that", true);
                List<ChatMessage> current = messages.getValue();
                if (current != null) {
                    current.add(botMsg);
                }
                messages.postValue(current);
                isLoading.postValue(false);
                Log.i("iop", "onResponse: " + response + " " + message );
            }

            @Override
            public void onError(String error) {
                // Handling error
                List<ChatMessage> updated = new ArrayList<>(messages.getValue());
                updated.add(new ChatMessage("Error: " + error, true));
                messages.postValue(updated);
                isLoading.postValue(false);
                Log.i("iop", "onError: " + error);
            }
        });
    }
    public void addMessage(ChatMessage message) {
        List<ChatMessage> current = messages.getValue();
        if (current != null) {
            current.add(message);
        }
        messages.postValue(current);
        isLoading.postValue(false);
    }

    public LiveData<List<ChatMessage>> getMessages() {
        return messages;
    }

    public LiveData<Boolean> getLoadingStatus() {
        return isLoading;
    }
}