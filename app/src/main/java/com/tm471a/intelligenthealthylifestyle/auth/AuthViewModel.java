package com.tm471a.intelligenthealthylifestyle.auth;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tm471a.intelligenthealthylifestyle.data.model.User;
import com.tm471a.intelligenthealthylifestyle.data.model.WorkoutPlan;
import com.tm471a.intelligenthealthylifestyle.data.repository.AuthRepository;
import com.tm471a.intelligenthealthylifestyle.data.repository.WorkoutRepository;
import com.tm471a.intelligenthealthylifestyle.utils.Resource;

import java.lang.reflect.Type;
import java.util.List;

// AuthViewModel.java
public class AuthViewModel extends AndroidViewModel {
    private final AuthRepository authRepository;
    private final MutableLiveData<Resource<User>> userLiveData = new MutableLiveData<>();
    public AuthViewModel(@NonNull Application application) {
        super(application);
        authRepository = new AuthRepository();
    }

    public void login(String email, String password) {
        userLiveData.setValue(Resource.loading(null));
        authRepository.loginUser(email, password, new AuthRepository.LoginCallback() {
            @Override
            public void onSuccess(User user) {
                userLiveData.setValue(Resource.success(user));
            }

            @Override
            public void onError(String message) {
                userLiveData.setValue(Resource.error(message, null));
            }
        });
    }
    // Add to AuthViewModel class
    public void signUp(String name, String email, String password, double height, double weight,int age, String gender, String medicalConditionsOrInjuries, String currentFitnessLevel,
                       List<String> fitnessGoals, List<String> dietaryPreferences) {
        userLiveData.setValue(Resource.loading(null));
        authRepository.registerUser(name, email, password,  height, weight,age,
                gender, medicalConditionsOrInjuries, currentFitnessLevel, fitnessGoals, dietaryPreferences,
                new AuthRepository.SignupCallback() {
            @Override
            public void onSuccess(User user) {
                userLiveData.setValue(Resource.success(user));
            }

            @Override
            public void onError(String message) {
                userLiveData.setValue(Resource.error(message, null));
            }
        });
    }

    public LiveData<Resource<User>> getUserLiveData() {
        return userLiveData;
    }
}
