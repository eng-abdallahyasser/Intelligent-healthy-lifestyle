package com.tm471a.intelligenthealthylifestyle.data.repository;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.tm471a.intelligenthealthylifestyle.data.model.NutritionAdvice;
import com.tm471a.intelligenthealthylifestyle.data.model.User;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
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

public class NutritionRepository {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final OkHttpClient client;
    private final MutableLiveData<Boolean> isInitialized = new MutableLiveData<>(false);
    private final String BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent";
    private String geminiApiKey;
    private User userData;

    public MutableLiveData<Boolean> getIsInitialized() {
        return isInitialized;
    }

    public NutritionRepository() {
        this.client = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();
        loadApiKey();
        loadUserData();
    }
    public interface ResponseCallback {
        void onResponse(String response);
        void onError(String error);
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
    public void generateNutritionAdvice( NutritionRepository.ResponseCallback callback) {
        try {
            // 1. Create system instruction with user data
            @SuppressLint("DefaultLocale") String systemInstruction = String.format(
                    "You are a fitness and nutrition assistant helping %s (%d years old). " +
                            "They are %.1f cm tall and weigh %.1f kg. " +
                            "Fitness goals: %s. Dietary preferences: %s. " +
                            "Generate a JSON-formatted nutrition advice result with the following structure: " +
                            "{ " +
                            "  \"exercise_name\": \"name of the recommended exercise\", " +
                            "  \"description\": \"detailed explanation of the exercise and its benefits\", " +
                            "  \"goal\": \"specific fitness goal (e.g., weight loss, muscle gain, endurance)\", " +
                            "  \"calories_per_day\": 2000, " +
                            "  \"macronutrients\": { " +
                            "    \"carbohydrates\": \" percentage amount \", " +
                            "    \"proteins\": \" percentage amount \", " +
                            "    \"fats\": \" percentage amount \" " +
                            "  }, " +
                            "  \"meal_suggestions\": [ " +
                            "    { " +
                            "      \"meal\": \"meal type (e.g., breakfast, lunch, dinner)\", " +
                            "      \"suggestions\": [ " +
                            "        { " +
                            "          \"name\": \"name of the meal\", " +
                            "          \"ingredients\": [\"ingredient1\", \"ingredient2\"], " +
                            "          \"calories\": 500 " +
                            "        } " +
                            "      ] " +
                            "    } " +
                            "  ], " +
                            "  \"seo_title\": \"SEO-optimized title for search engines\", " +
                            "  \"seo_content\": \"Short description optimized for SEO\", " +
                            "  \"seo_keywords\": \"comma-separated relevant keywords\" " +
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
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
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
        List<NutritionRepository.Candidate> candidates;
    }
    private static class Candidate {
        NutritionRepository.Content content;
    }

    private static class Content {
        List<NutritionRepository.Part> parts;
    }

    private static class Part {
        String text;
    }
}