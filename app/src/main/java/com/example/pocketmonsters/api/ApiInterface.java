package com.example.pocketmonsters.api;

import com.google.gson.JsonElement;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    @POST("users")
    Call<SignUpResponse> register();

    @GET("objects")
    Call<List<ObjectsResponse>> getObjects(@Query("sid") String sid, @Query("lat") double lat, @Query("lon") double lon);

    @GET("objects/{id}")
    Call<ObjectsResponseId> getObject(@Path("id") int id, @Query("sid") String sid);

    @POST("objects/{id}/activate")
    @FormUrlEncoded
    Call<ResponseUserData> activateObject(@Path("id") int id, @Field("sid") String sid);

    @GET("users")
    Call<List<ResponseUsers>> getUsers(@Query("sid") String sid, @Query("lat") double lat, @Query("lon") double lon);

    @GET("users/{id}")
    Call<ResponseUsersId> getUser(@Path("id") int id, @Query("sid") String sid);

    @GET("ranking")
    Call<List<ResponseUsersRanking>> getRanking(@Query("sid") String sid);

}
