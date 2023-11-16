package com.example.pocketmonsters.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitProvider {

    private static ApiInterface apiInterface = null;

    public static ApiInterface getApiInterface() {
        if (apiInterface == null) {
            String BASE_URL = "https://develop.ewlab.di.unimi.it/mc/mostri/";
            apiInterface = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build().create(ApiInterface.class);
        }
        return apiInterface;
    }

}
