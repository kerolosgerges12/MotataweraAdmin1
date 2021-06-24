package com.kmk.motatawera.admin.data;



import com.kmk.motatawera.admin.model.SenderNotification;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.kmk.motatawera.admin.app.Const.BASE_URL;


public class RetrofitClint {

    private final Retrofit retrofit;
    private static RetrofitClint mInstance;

    public static synchronized RetrofitClint getInstance() {
        if (mInstance == null)
            mInstance = new RetrofitClint();
        return mInstance;
    }

    public RetrofitClint() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public API getApi() {
        return retrofit.create(API.class);
    }

    public Call<Response> send(SenderNotification body) {
        return getApi().sendNotification(body);
    }
    
}
