package com.tm471a.intelligenthealthylifestyle.auth;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.tm471a.intelligenthealthylifestyle.data.model.User;
import com.tm471a.intelligenthealthylifestyle.data.repository.AuthRepository;
import com.tm471a.intelligenthealthylifestyle.utils.Resource;

// AuthViewModel.java
public class AuthViewModel extends AndroidViewModel {
    private AuthRepository authRepository;
    private MutableLiveData<Resource<User>> userLiveData = new MutableLiveData<>();

    public AuthViewModel(Application application) {
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
    public void signUp(String name, String email, String password, double height, double weight,int age) {
        userLiveData.setValue(Resource.loading(null));
        authRepository.registerUser(name, email, password,  height, weight,age, new AuthRepository.SignupCallback() {
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
