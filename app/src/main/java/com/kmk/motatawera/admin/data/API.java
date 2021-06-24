package com.kmk.motatawera.admin.data;

import com.kmk.motatawera.admin.model.SenderNotification;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

import static com.kmk.motatawera.admin.app.Const.AUTHORIZATION;
import static com.kmk.motatawera.admin.app.Const.CONTENT_TYPE;

public interface API {
    @Headers({CONTENT_TYPE, AUTHORIZATION})
    @POST("fcm/send")
    Call<Response> sendNotification(@Body SenderNotification body);
}
