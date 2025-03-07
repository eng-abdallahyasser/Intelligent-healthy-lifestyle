package com.tm471a.intelligenthealthylifestyle.dashboard;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.tm471a.intelligenthealthylifestyle.data.model.ChatMessage;
import com.tm471a.intelligenthealthylifestyle.data.repository.AssistantRepository;

import java.util.ArrayList;
import java.util.List;

public class AssistantViewModel extends ViewModel {
    private final AssistantRepository repository = new AssistantRepository();
    private final MutableLiveData<List<ChatMessage>> messages = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);

    public void sendMessage(String message) {
        isLoading.postValue(true);
        repository.sendMessage(message, new AssistantRepository.ResponseCallback() {
            @Override
            public void onResponse(String response) {
                ChatMessage userMsg = new ChatMessage(message, false);
                ChatMessage botMsg = new ChatMessage(response != null ? response :"I'm sorry, I didn't understand that", true);
                List<ChatMessage> current = messages.getValue();
                current.add(userMsg);
                current.add(botMsg);
                messages.postValue(current);
                isLoading.postValue(false);
                Log.i("iop", "onResponse: " + response + " " + message );
            }

            @Override
            public void onError(String error) {
                isLoading.postValue(false);
                Log.i("iop", "onError: " + error);
                // Handle error
            }
        });
    }
    public void addMessage(ChatMessage message) {
                List<ChatMessage> current = messages.getValue();
                current.add(message);
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