package com.tm471a.intelligenthealthylifestyle.dashboard;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tm471a.intelligenthealthylifestyle.data.model.User;

public class ProfileViewModel extends ViewModel {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private MutableLiveData<User> userData = new MutableLiveData<>();
    private MutableLiveData<Boolean> updateStatus = new MutableLiveData<>();

    public ProfileViewModel() {
        loadUserData();
    }

    private void loadUserData() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        if (uid != null) {
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
            Log.i("dddd", "loadUserData: ssssssssssssssssssssssssssssssssssssssss");
        }else{
            userData.postValue(null);
            Log.i("dddd", "loadUserData: faild");
        }
    }

    public void updateProfile(User user) {
        db.collection("Users").document(user.getUid())
                .set(user)
                .addOnSuccessListener(aVoid -> updateStatus.postValue(true))
                .addOnFailureListener(e -> updateStatus.postValue(false));
    }

    // Expose LiveData
    public LiveData<User> getUserData() { return userData; }
    public LiveData<Boolean> getUpdateStatus() { return updateStatus; }
}