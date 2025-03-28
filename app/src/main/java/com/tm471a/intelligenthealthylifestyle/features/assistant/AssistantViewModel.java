package com.tm471a.intelligenthealthylifestyle.features.assistant;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.tm471a.intelligenthealthylifestyle.data.model.ChatMessage;
import com.tm471a.intelligenthealthylifestyle.data.repository.AssistantRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AssistantViewModel extends AndroidViewModel {
    private final AssistantRepository repository;
    private final MutableLiveData<List<ChatMessage>> messages = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(true);
    public AssistantViewModel(@NonNull Application application) {

        super(application);

        this.repository = new AssistantRepository();
        this.messages.setValue(repository.getMessages());
        repository.getIsInitialized().observeForever(isReady -> {
            if (isReady) {
                welcomeUser();
            }
        });
    }
    private  void welcomeUser(){
        repository.welcomeUser(
                new AssistantRepository.ResponseCallback() {
                    @Override
                    public void onResponse(String response) {
                        ChatMessage botMsg = new ChatMessage(response != null ? response :"response is null", true);
                        List<ChatMessage> current = messages.getValue();
                        if (current != null) {
                            current.add(botMsg);
                        }
                        repository.saveMessages(botMsg);
                        messages.postValue(current);
                        isLoading.postValue(false);
                        Log.i("iop", "onResponse: " + response  );
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
    public void sendMessage(String message) {

        // Add user message immediately
        List<ChatMessage> current = new ArrayList<>(Objects.requireNonNull(messages.getValue()));
        current.add(new ChatMessage(message, false));
        messages.postValue(current);

        repository.saveMessages(new ChatMessage(message, false));

        isLoading.postValue(true);
        repository.sendMessage(current,
                new AssistantRepository.ResponseCallback() {
                    @Override
                    public void onResponse(String response) {
                        ChatMessage botMsg = new ChatMessage(response != null ? response :"response is null", true);
                        List<ChatMessage> current = messages.getValue();
                        if (current != null) {
                            current.add(botMsg);
                        }
                        repository.saveMessages(botMsg);
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

    public LiveData<List<ChatMessage>> getMessages() {
        return messages;
    }

    public LiveData<Boolean> getLoadingStatus() {
        return isLoading;
    }
}