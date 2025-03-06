package com.tm471a.intelligenthealthylifestyle.dashboard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.tm471a.intelligenthealthylifestyle.data.model.ChatMessage;
import com.tm471a.intelligenthealthylifestyle.data.repository.AssistantRepository;
import java.util.List;

public class AssistantViewModel extends ViewModel {
    private final AssistantRepository repository = new AssistantRepository();
    private final MutableLiveData<List<ChatMessage>> messages = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    public void sendMessage(String message) {
        isLoading.postValue(true);
        repository.sendMessage(message, new AssistantRepository.ResponseCallback() {
            @Override
            public void onResponse(String response) {
                ChatMessage userMsg = new ChatMessage(message, false);
                ChatMessage botMsg = new ChatMessage(response, true);
                List<ChatMessage> current = messages.getValue();
                current.add(userMsg);
                current.add(botMsg);
                messages.postValue(current);
                isLoading.postValue(false);
            }

            @Override
            public void onError(String error) {
                isLoading.postValue(false);
                // Handle error
            }
        });
    }

    public LiveData<List<ChatMessage>> getMessages() {
        return messages;
    }

    public LiveData<Boolean> getLoadingStatus() {
        return isLoading;
    }
}