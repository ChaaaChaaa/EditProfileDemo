package com.practice.edituserprofiledemo;



import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class BasicAuthInterceptor implements Interceptor {
//    private String token;
//
//    public BasicAuthInterceptor(String token){
//        this.token = token;
//    }


    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        String token = UserPreference.getInstance().getString(Config.KEY_TOKEN);
        Request request = chain.request();
        Request authenticatedRequest = request.newBuilder()
                .header("authorization",token)
                .build();
        return chain.proceed(authenticatedRequest);
    }
}