package com.tm471a.intelligenthealthylifestyle.data.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.tm471a.intelligenthealthylifestyle.data.model.ChatMessage;
import com.tm471a.intelligenthealthylifestyle.data.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class AssistantRepository {
    private static final String BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent";
    private final OkHttpClient client;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private User userData= new User();
    private final Gson gson;
    private String geminiApiKey;


    public interface ResponseCallback {
        void onResponse(String response);
        void onError(String error);
    }

    public AssistantRepository() {
        loadUserData();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Keys").document("gemimi_api_key").get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        geminiApiKey = documentSnapshot.getString("apiKey");
                    } else {
                        Log.e("API_KEY", "No API key document found");
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("API_KEY", "Error loading key: " + e.getMessage());
                });

        this.client = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();
        this.gson = new Gson();
    }


    private void loadUserData() {
        String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        db.collection("Users").document(uid).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        User user = documentSnapshot.toObject(User.class);
                        userData=user;
                    } else {
                        // User document does not exist
                        userData=null; // Or handle this case appropriately
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle failure
                    userData=null; // Or handle this case appropriately
                });
    }
    private JSONArray buildContentsArray(List<ChatMessage> messages) throws JSONException {
        JSONArray contentsArray = new JSONArray();
        for (ChatMessage message : messages) {
            contentsArray.put(new JSONObject()
                    .put("role", message.isBot() ? "model" : "user")
                    .put("parts", new JSONArray()
                            .put(new JSONObject().put("text", message.getContent()))
                    )
            );
        }
        return contentsArray;
    }

    public void sendMessage(List<ChatMessage> messages, ResponseCallback callback) {
        try {
            // 1. Create system instruction with user data
            String systemInstruction = String.format(
                    "You are a fitness assistant helping %s (%d years old). " +
                            "They are %.1f cm tall and weigh %.1f kg. " +
                            "Fitness goals: %s. Dietary preferences: %s. " +
                            "Provide personalized, specific advice.",
                    userData.getName(),
                    userData.getAge(),
                    userData.getHeight(),
                    userData.getWeight(),
                    String.join(", ", userData.getFitnessGoals()),
                    String.join(", ", userData.getDietaryPreferences())
            );

            // 2. Build JSON request
            JSONObject requestBody = new JSONObject()
                    .put("contents", buildContentsArray(messages))
                    .put("systemInstruction", new JSONObject()
                            .put("parts", new JSONArray()
                                    .put(new JSONObject()
                                            .put("text", systemInstruction)
                                    )
                            )
                    )
                    .put("generationConfig", new JSONObject()
                            .put("temperature", 0.9)
                            .put("topP", 0.95)
                            .put("maxOutputTokens", 250)
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
                public void onFailure(Call call, IOException e) {
                    callback.onError("Network error: " + e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (!response.isSuccessful()) {
                        callback.onError("API error: " + response.code());
                        return;
                    }

                    try {
                        String responseBody = response.body().string();
                        GeminiResponse geminiResponse = gson.fromJson(responseBody, GeminiResponse.class);
                        if (geminiResponse != null && geminiResponse.candidates != null &&
                                !geminiResponse.candidates.isEmpty()) {
                            callback.onResponse(geminiResponse.candidates.get(0).content.parts.get(0).text);
                        } else {
                            callback.onError("No response from AI");
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

    // Response model classes
    private static class GeminiResponse {
        List<Candidate> candidates;
    }
    private static class Candidate {
        Content content;
    }

    private static class Content {
        List<Part> parts;
    }

    private static class Part {
        String text;
    }
}