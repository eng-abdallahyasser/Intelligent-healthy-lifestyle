package com.tm471a.intelligenthealthylifestyle.data;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseSource {
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    public FirebaseSource() {
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    public Task<DocumentSnapshot> getUserDocument(String uid) {
        return db.collection("Users").document(uid).get();
    }
}
