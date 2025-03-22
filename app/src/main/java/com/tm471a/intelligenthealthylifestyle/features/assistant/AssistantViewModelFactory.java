package com.tm471a.intelligenthealthylifestyle.features.assistant;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class AssistantViewModelFactory implements ViewModelProvider.Factory {
    private final Application application;

    public AssistantViewModelFactory(Application application) {
        this.application = application;
    }
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(AssistantViewModel.class)) {
            return (T) new AssistantViewModel(application);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
