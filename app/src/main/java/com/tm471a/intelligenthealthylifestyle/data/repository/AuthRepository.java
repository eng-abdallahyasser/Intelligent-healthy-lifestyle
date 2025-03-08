package com.tm471a.intelligenthealthylifestyle.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.*;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tm471a.intelligenthealthylifestyle.data.model.User;
import com.tm471a.intelligenthealthylifestyle.utils.Resource;

import java.util.ArrayList;


public class AuthRepository {
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public interface LoginCallback {
        void onSuccess(User user);
        void onError(String message);
    }

    public void loginUser(String email, String password, LoginCallback callback) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        fetchUserData(auth.getCurrentUser().getUid(), callback);
                    } else {
                        callback.onError(task.getException() != null ?
                                task.getException().getMessage() :
                                "Login failed");
                    }
                });
    }

    private void fetchUserData(String uid, LoginCallback callback) {
        db.collection("Users").document(uid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    User user = documentSnapshot.toObject(User.class);
                    if (user != null) {
                        callback.onSuccess(user);
                    } else {
                        callback.onError("User data not found");
                    }
                })
                .addOnFailureListener(e -> callback.onError(e.getMessage()));
    }
    // Add interface
    public interface SignupCallback {
        void onSuccess(User user);
        void onError(String message);
    }

    // Add method
    public void registerUser(String name, String email, String password,
                             double height, double weight,int age, SignupCallback callback) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        User user = new User(
                                auth.getCurrentUser().getUid(),
                                name,
                                email,
                                height,
                                weight,
                                new ArrayList<>(), // fitnessGoals
                                new ArrayList<>(),  // dietaryPreferences
                                age
                        );

                        db.collection("Users").document(user.getUid())
                                .set(user)
                                .addOnSuccessListener(aVoid -> callback.onSuccess(user))
                                .addOnFailureListener(e -> callback.onError(e.getMessage()));
                    } else {
                        callback.onError(task.getException() != null ?
                                task.getException().getMessage() :
                                "Registration failed");
                    }
                });
    }
}
