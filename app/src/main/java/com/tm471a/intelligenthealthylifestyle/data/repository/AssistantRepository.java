package com.tm471a.intelligenthealthylifestyle.data.repository;

import android.content.Context;
import com.google.gson.Gson;
import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;

import java.io.IOException;
import java.util.List;

public class AssistantRepository {
    private static final String BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent";
    private final OkHttpClient client;
    private final Gson gson;
    private final String apiKey = "AIzaSyBrin0QWPkdjgB3Iu95d9iDc-6ZoXRnkeY";

    public interface ResponseCallback {
        void onResponse(String response);
        void onError(String error);
    }

    public AssistantRepository(Context context) {
        this.client = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();
        this.gson = new Gson();
    }

    public void sendMessage(String message, ResponseCallback callback) {
        try {
            // Create JSON request body
            String json = "{ \"contents\":[{ \"parts\":[{ \"text\": \"" + message + "\" }] }] }";

            RequestBody body = RequestBody.create(
                    json,
                    MediaType.parse("application/json; charset=utf-8")
            );

            Request request = new Request.Builder()
                    .url(BASE_URL + "?key=" + apiKey)
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