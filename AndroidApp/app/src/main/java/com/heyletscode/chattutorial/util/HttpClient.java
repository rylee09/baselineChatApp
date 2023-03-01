package com.heyletscode.chattutorial.util;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class HttpClient {
    private static final OkHttpClient client = new OkHttpClient();
    private static HttpClient instance = null;
    private final String BASE_URL;

    private HttpClient(String baseUrl) {
        // Private constructor to prevent instantiation from outside the class
        this.BASE_URL = baseUrl;
    }

    public static synchronized HttpClient getInstance(String baseUrl) {
        if (instance == null) {
            instance = new HttpClient(baseUrl);
        }
        return instance;
    }

    public void doGet(String endpoint, Callback callback) {
        String url = BASE_URL + endpoint;
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public void doPost(String endpoint, RequestBody body, Callback callback) {
        String url = BASE_URL + endpoint;
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }
}
