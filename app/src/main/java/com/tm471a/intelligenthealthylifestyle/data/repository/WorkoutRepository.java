package com.tm471a.intelligenthealthylifestyle.data.repository;



import android.annotation.SuppressLint;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.tm471a.intelligenthealthylifestyle.data.model.User;
import com.tm471a.intelligenthealthylifestyle.data.model.WorkoutDay;
import com.tm471a.intelligenthealthylifestyle.data.model.WorkoutPlan;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;


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
    private final String BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent";
    private final OkHttpClient client;
    final MutableLiveData<Boolean> isInitialized = new MutableLiveData<>(false);
    private String geminiApiKey;
    private User userData;

    public WorkoutRepository() {
        loadUserData();
        loadApiKey();

        this.client = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .connectTimeout(60, TimeUnit.SECONDS)  // Increase connection timeout
                .readTimeout(60, TimeUnit.SECONDS)     // Increase read timeout
                .writeTimeout(60, TimeUnit.SECONDS)    // Increase write timeout
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
        if (Boolean.FALSE.equals(isInitialized.getValue()) && userData != null && geminiApiKey != null) {
            isInitialized.postValue(true);
        }
    }

    public void saveWorkoutPlan(WorkoutPlan workoutPlan) {

        try {
            // Test serialization first
            Map<String, Object> data = new HashMap<>();
            data.put("test", workoutPlan);
            Log.d("SERIALIZATION", "Serialized data: " + data);
        } catch (Exception e) {
            Log.e("SERIALIZATION", "Failed to serialize", e);
        }

        db.collection("Users").document(userData.getUid())
                .collection("workout_plans")
                .add(workoutPlan)
                .addOnSuccessListener(documentReference -> Log.d("Firestore", "Workout plan added successfully"))
                .addOnFailureListener(e -> Log.e("FirestoreError", "Failed to add Workout plan ", e));
    }
    public MutableLiveData<Boolean> getIsInitialized() {
        return isInitialized;
    }
    public void fetchWorkoutPlans(OnWorkoutPlansFetchedListener listener) {
        // Verify userData is initialized
        if (userData == null) {
            if (listener != null) {
                listener.onFailure(new Exception("User data not initialized"));
            }
            return;
        }
        db.collection("Users").document(userData.getUid()).collection("workout_plans")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    Log.d("Firestore", "Query successful. Found " + querySnapshot.size() + " documents");

                    List<WorkoutPlan> retrievedWorkoutPlans = new ArrayList<>();
                    for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                        WorkoutPlan workoutPlan = doc.toObject(WorkoutPlan.class);
                        if (workoutPlan != null) {
                            Log.d("Firestore", "Document ID: " + doc.getId() + ", Data: " + doc.getData());
                            retrievedWorkoutPlans.add(workoutPlan);
                        } else {
                            Log.e("Firestore", "Failed to convert document " + doc.getId() + " to WorkoutPlan object");
                        }
                    }

                    if (listener != null) {
                        if (!retrievedWorkoutPlans.isEmpty()) {
                            listener.onSuccess(retrievedWorkoutPlans);
                        } else {
                            listener.onFailure(new Exception("No workout plans found in database"));
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Error fetching workout plans: ", e);
                    if (listener != null) {
                        listener.onFailure(e);
                    }
                });
    }
    public interface OnWorkoutPlansFetchedListener {
        void onSuccess(List<WorkoutPlan> workoutPlans);
        void onFailure(Exception e);
    }

    public void subscribeToWorkoutPlan(WorkoutPlan workoutPlan) {
        String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        db.collection("Users").document(uid)
                .collection("workout_plans")
                .document("subscribed_workout_plan")
                .set(workoutPlan)
                .addOnSuccessListener(aVoid -> Log.d("Firestore", "Successfully subscribed!"))
                .addOnFailureListener(e -> Log.e("Firestore", "Error subscribing", e));
        ;
    }
    public void updateExerciseCompletion(int position,  String workoutDay, boolean completed) {
        String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        DocumentReference docRef = db.collection("Users").document(uid)
                .collection("workout_plans").document("subscribed_workout_plan");

        docRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                WorkoutPlan workoutPlan=documentSnapshot.toObject(WorkoutPlan.class);
                List<WorkoutDay> workoutDayList = workoutPlan.getWorkoutDayList();
                for(WorkoutDay day : workoutDayList) {

                    if (day.getDay().equals(workoutDay)&& position < day.getExercises().size()) {
                        // Update the specific item
                        day.getExerciseCompleted().set(position, completed);

                        workoutPlan.checkWeekCoplated();
                        // Push the modified list back to Firestore
                        docRef.set(workoutPlan)
                                .addOnSuccessListener(aVoid -> Log.d("Firestore", "Workout day updated successfully"))
                                .addOnFailureListener(e -> Log.e("Firestore", "Error updating workout day", e));
                    }
                }
            }
        }).addOnFailureListener(e -> Log.e("Firestore", "Error fetching workout plan", e));
    }

    public WorkoutPlan getSubscribedWorkoutPlan(OnWorkoutPlanFetchedListener listener) {
        String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        db.collection("Users").document(uid)
                .collection("workout_plans")
                .document("subscribed_workout_plan")
                .addSnapshotListener((value, error) -> {
                    if (error != null || value == null || !value.exists()) {
                        listener.onError("Error fetching workout plan");
                        return;
                    }
                    WorkoutPlan plan = value.toObject(WorkoutPlan.class);
                    listener.onSuccess(plan);
                });
        return null;
    }
    public interface OnWorkoutPlanFetchedListener {
        void onSuccess(WorkoutPlan workoutPlan);
        void onError(String errorMessage);
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
                            "Fitness level: %s. " +
                            "Fitness goals: %s. Dietary preferences: %s. " +
                            "Generate a JSON-formatted workout plan with this structure: " +
                            "{ " +
                            "  \"planName\": \"name\", " +
                            "  \"goal\": \"workout goal\", " +
                            "  \"duration\": \"duration\", " +
                            "  \"daysPerWeek\": \"days per week\", " +
                            "  \"numberOfCompletedWeeks\": \"0\", " +
                            "  \"sessionDuration\": \"session duration for one day\", " +
                            "  \"difficulty\": \"level\", " +
                            "  \"workoutDayList\": [ " +
                            "    { " +
                            "      \"day\": \"day\", " +
                            "      \"exercises\": [ " +
                            "          { " +
                            "          \"name\": \"exercise name\", " +
                            "          \"description\": \"detailed instructions\", " +
                            "          \"primary_muscles\": [\"muscle1\", \"muscle2\"], " +
                            "          \"equipment\": [\"equipment1\"], " +
                            "          \"sets\": \" 3 \", " +
                            "          \"reps\": \"12 \"" +
                            "        } " +
                            "      ] " +
                            "    } " +
                            "  ] " +
                            "}",
                    userData.getName(),
                    userData.getAge(),
                    userData.getHeight(),
                    userData.getWeight(),
                    userData.getCurrentFitnessLevel(),
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
    public void initWorkoutPlans(WorkoutRepository.ResponseCallback callback) {
        try {
            // 1. Create system instruction with user data
            @SuppressLint("DefaultLocale") String systemInstruction = String.format(
                    "You are a fitness assistant helping %s (%d years old). " +
                            "They are %.1f cm tall and weigh %.1f kg. " +
                            "Fitness level: %s. " +
                            "Fitness goals: %s. Dietary preferences: %s. " +
                            "Generate a JSON-formatted 3 suggested workouts plans with different durations use this structure: " +
                            "{ " +
                            "  \"planName\": \"name\", " +
                            "  \"goal\": \"workout goal\", " +
                            "  \"duration\": \"duration\", " +
                            "  \"daysPerWeek\": \"days per week\", " +
                            "  \"numberOfCompletedWeeks\": \"0\", " +
                            "  \"sessionDuration\": \"session duration for one day\", " +
                            "  \"difficulty\": \"level\", " +
                            "  \"workoutDayList\": [ " +
                            "    { " +
                            "      \"day\": \"day\", " +
                            "      \"exercises\": [ " +
                            "          { " +
                            "          \"name\": \"exercise name\", " +
                            "          \"description\": \"detailed instructions\", " +
                            "          \"primary_muscles\": [\"muscle1\", \"muscle2\"], " +
                            "          \"equipment\": [\"equipment1\"], " +
                            "          \"sets\": \" 3 \", " +
                            "          \"reps\": \"12 \"" +
                            "        } " +
                            "      ] " +
                            "    } " +
                            "  ] " +
                            "}",
                    userData.getName(),
                    userData.getAge(),
                    userData.getHeight(),
                    userData.getWeight(),
                    userData.getCurrentFitnessLevel(),
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