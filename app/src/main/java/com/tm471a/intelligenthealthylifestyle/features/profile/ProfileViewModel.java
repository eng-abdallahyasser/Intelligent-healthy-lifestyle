package com.tm471a.intelligenthealthylifestyle.features.profile;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tm471a.intelligenthealthylifestyle.data.model.User;
import com.tm471a.intelligenthealthylifestyle.data.model.WeightLog;

import java.util.Objects;

public class ProfileViewModel extends ViewModel {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final MutableLiveData<User> userData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> updateStatus = new MutableLiveData<>();

    public ProfileViewModel() {
        loadUserData();
    }

    private void loadUserData() {
        String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        db.collection("Users").document(uid).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        User user = documentSnapshot.toObject(User.class);
                        userData.postValue(user);
                    } else {
                        // User document does not exist
                        userData.postValue(null); // Or handle this case appropriately
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle failure
                    userData.postValue(null); // Or handle this case appropriately
                });
    }

    public void updateProfile(User user) {
        db.collection("Users").document(user.getUid())
                .set(user)
                .addOnSuccessListener(aVoid -> updateStatus.postValue(true))
                .addOnFailureListener(e -> updateStatus.postValue(false));

        db.collection("Users").document(user.getUid())
                .collection("weight_logs")
                .add(new WeightLog(user.getWeight()).toMap())
                .addOnSuccessListener(documentReference ->
                        Log.d("Profile vm", "Weight logged successfully"))
                .addOnFailureListener(e ->
                        Log.w("Profile vm", "Error logging weight", e));
    }

    // Expose LiveData
    public LiveData<User> getUserData() { return userData; }
    public LiveData<Boolean> getUpdateStatus() { return updateStatus; }
}