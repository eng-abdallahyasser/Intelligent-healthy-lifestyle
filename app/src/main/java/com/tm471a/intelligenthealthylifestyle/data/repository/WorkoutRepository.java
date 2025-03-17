package com.tm471a.intelligenthealthylifestyle.data.repository;



import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.gson.Gson;
import com.tm471a.intelligenthealthylifestyle.data.model.User;
import com.tm471a.intelligenthealthylifestyle.data.model.WorkoutPlan;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class WorkoutRepository {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final MutableLiveData<List<WorkoutPlan>> workoutPlans = new MutableLiveData<>();
    private final String BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent";
    private final OkHttpClient client;
    private final MutableLiveData<Boolean> isInitialized = new MutableLiveData<>(false);
    private String geminiApiKey;
    private User userData= new User();

    public WorkoutRepository() {
        loadUserData();
        loadApiKey();

        this.client = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();
    }


    private void loadApiKey() {
        db.collection("Keys").document("gemimi_api_key").get()
                .addOnSuccessListener(documentSnapshot -> {
                    geminiApiKey = documentSnapshot.getString("apiKey");
                    checkInitialization();
                });
    }
    private void loadUserData() {
        String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        db.collection("Users").document(uid).get()
                .addOnSuccessListener(documentSnapshot -> {
                    userData = documentSnapshot.toObject(User.class);
                    checkInitialization();
                });
    }
    private void checkInitialization() {
        if (userData != null && geminiApiKey != null) {
            isInitialized.postValue(true);
        }
    }
    public LiveData<Boolean> getIsInitialized() {
        return isInitialized;
    }
    public MutableLiveData<List<WorkoutPlan>> getWorkoutPlans(String userId) {
        db.collection("Users").document(userId)
                .collection("workout_plans")
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        workoutPlans.postValue(null);
                        return;
                    }

                    List<WorkoutPlan> plans = new ArrayList<>();
                    assert value != null;
                    for (QueryDocumentSnapshot doc : value) {
                        WorkoutPlan plan = doc.toObject(WorkoutPlan.class);
                        plans.add(plan);
                    }
                    workoutPlans.postValue(plans);
                });

        return workoutPlans;
    }
    public interface ResponseCallback {
        void onResponse(String response);
        void onError(String error);
    }
    public void generateWorkoutPlan( WorkoutRepository.ResponseCallback callback) {
        try {
            // 1. Create system instruction with user data
            @SuppressLint("DefaultLocale") String systemInstruction = String.format(
                    "You are a fitness assistant helping %s (%d years old). " +
                            "They are %.1f cm tall and weigh %.1f kg. " +
                            "Fitness goals: %s. Dietary preferences: %s. " +
                            "Generate a JSON-formatted workout plan with this structure: " +
                            "{ " +
                            "  \"plan_name\": \"name\", " +
                            "  \"duration\": \"duration\", " +
                            "  \"difficulty\": \"level\", " +
                            "  \"exercises\": [ " +
                            "    { " +
                            "      \"name\": \"exercise name\", " +
                            "      \"description\": \"detailed instructions\", " +
                            "      \"primary_muscles\": [\"muscle1\", \"muscle2\"], " +
                            "      \"equipment\": [\"equipment1\"], " +
                            "      \"sets\": \" 3 \", " +
                            "      \"reps\": \"12 \"" +
                            "    } " +
                            "  ] " +
                            "}",
                    userData.getName(),
                    userData.getAge(),
                    userData.getHeight(),
                    userData.getWeight(),
                    String.join(", ", userData.getFitnessGoals()),
                    String.join(", ", userData.getDietaryPreferences())
            );

            // 2. Build JSON request
            JSONObject requestBody = new JSONObject()
                    .put("contents", new JSONArray()
                            .put(new JSONObject()
                                    .put("parts", new JSONArray()
                                            .put(new JSONObject()
                                                    .put("text", systemInstruction)
                                            )
                                    )
                            )
                    )
                    .put("generationConfig", new JSONObject()
                            .put("temperature", 0.7)
                            .put("topP", 0.95)
                    );

            RequestBody body = RequestBody.create(
                    requestBody.toString(),
                    MediaType.parse("application/json; charset=utf-8")
            );

            Request request = new Request.Builder()
                    .url(BASE_URL + "?key=" + geminiApiKey)
                    .post(body)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    callback.onError("Network error: " + e.getMessage());
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) {
                    if (!response.isSuccessful()) {
                        callback.onError("API error: " + response.code());
                        return;
                    }

                    try {
                        assert response.body() != null;
                        String responseBody = response.body().string();
                        JSONObject jsonResponse = new JSONObject(responseBody);
                        JSONArray candidates = jsonResponse.getJSONArray("candidates");

                        if (candidates.length() > 0) {
                            String planJson = candidates.getJSONObject(0)
                                    .getJSONObject("content")
                                    .getJSONArray("parts")
                                    .getJSONObject(0)
                                    .getString("text");

                            callback.onResponse(planJson);
                        }
                    } catch (Exception e) {
                        callback.onError("Parsing error: " + e.getMessage());
                    }

                }
            });
        } catch (Exception e) {
            callback.onError("Request error: " + e.getMessage());
        }
    }
    private static class GeminiResponse {
        List<WorkoutRepository.Candidate> candidates;
    }
    private static class Candidate {
        WorkoutRepository.Content content;
    }

    private static class Content {
        List<WorkoutRepository.Part> parts;
    }

    private static class Part {
        String text;
    }
}