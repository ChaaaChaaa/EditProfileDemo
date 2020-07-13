package com.practice.edituserprofiledemo;


import android.util.Log;

import org.jetbrains.annotations.NotNull;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    public static RetrofitInterface buildHTTPClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.ppizil.kro.kr/review/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(getClient())
                .build();

        return retrofit.create(RetrofitInterface.class);
    }

    private static OkHttpClient getClient() {
        HttpLoggingInterceptor headerInterceptor = new HttpLoggingInterceptor();
        headerInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);

        HttpLoggingInterceptor bodyInterceptor = new HttpLoggingInterceptor();
        bodyInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder()
                .addInterceptor(headerInterceptor)
                .addInterceptor(bodyInterceptor)
                //.addInterceptor(new BasicAuthInterceptor())
                .build();
    }

    private static HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(@NotNull String message) {
                Log.d("HTTP", message);
            }
        });
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return httpLoggingInterceptor;
    }

    public static RetrofitInterface getRestMethods() {
        return buildHTTPClient();
    }
}