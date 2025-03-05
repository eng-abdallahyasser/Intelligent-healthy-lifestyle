package com.tm471a.intelligenthealthylifestyle.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.*;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tm471a.intelligenthealthylifestyle.data.model.User;
import com.tm471a.intelligenthealthylifestyle.utils.Resource;


public class AuthRepository {
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public LiveData<Resource<User>> loginUser(String email, String password) {
        MutableLiveData<Resource<User>> result = new MutableLiveData<>();
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Fetch user data from Firestore
                        db.collection("Users").document(auth.getUid())
                                .get().addOnSuccessListener(documentSnapshot -> {
                                    User user = documentSnapshot.toObject(User.class);
                                    result.postValue(Resource.success(user));
                                });
                    } else {
                        result.postValue(Resource.error("Login failed", null));
                    }
                });
        return result;
    }
}
