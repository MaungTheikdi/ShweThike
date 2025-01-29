package com.theikdi.shwethike.API;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit;
    private static final String BASE_URL = "https://srv691456.hstgr.cloud/shwethike/api/";
    //https://srv691456.hstgr.cloud/shwethike/api/customers.php
    public static final String SECRET_KEY = "aWAo1E5fXn_CVmXX1526eERemAE1This";

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

}
