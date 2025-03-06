package com.tm471a.intelligenthealthylifestyle.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.tm471a.intelligenthealthylifestyle.data.model.MealPlan;

import java.util.ArrayList;
import java.util.List;

public class NutritionRepository {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final MutableLiveData<List<MealPlan>> mealPlans = new MutableLiveData<>();

    public LiveData<List<MealPlan>> getMealPlans() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        db.collection("users").document(userId)
                .collection("meal_plans")
                .orderBy("date", Query.Direction.DESCENDING)
                .addSnapshotListener((value, error) -> {
                    if (error != null) return;

                    List<MealPlan> plans = new ArrayList<>();
                    for (DocumentSnapshot doc : value.getDocuments()) {
                        MealPlan plan = doc.toObject(MealPlan.class);
                        plans.add(plan);
                    }
                    mealPlans.postValue(plans);
                });

        return mealPlans;
    }
}