package com.example.pocketmonsters.ui.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.pocketmonsters.api.ResponseUsersId;
import com.example.pocketmonsters.api.RetrofitProvider;
import com.example.pocketmonsters.api.SignUpResponse;
import com.example.pocketmonsters.models.User;

import retrofit2.Call;

public class MainModel {

    private User user;

    RetrofitProvider retrofitProvider = new RetrofitProvider();

    public void setUser() {

        Call<SignUpResponse> call = retrofitProvider.getApiInterface().register();
        call.enqueue(new retrofit2.Callback<SignUpResponse>() {
            @Override
            public void onResponse(Call<SignUpResponse> call, retrofit2.Response<SignUpResponse> response) {
                if(!response.isSuccessful()) {
                    Log.d("MainModel", "Error: " + response.code());
                    return;
                }
                SignUpResponse signUpResponse = response.body();
                Log.d("MainModel", "New sid: " + signUpResponse.sid);
                Log.d("MainModel", "New uid: " + signUpResponse.uid);
                setUserDet(signUpResponse.sid, signUpResponse.uid);
            }
            @Override
            public void onFailure(Call<SignUpResponse> call, Throwable t) {
                Log.d("MainFragment", "onFailure: " + t.getMessage());
            }
        });

    }

    public void setUserDet(String sid, int uid) {

        Call<ResponseUsersId> call = retrofitProvider.getApiInterface().getUser(uid, sid);
        call.enqueue(new retrofit2.Callback<ResponseUsersId>() {
            @Override
            public void onResponse(Call<ResponseUsersId> call, retrofit2.Response<ResponseUsersId> response) {
                if(!response.isSuccessful()) {
                    Log.d("MainModel", "Error: " + response.code());
                    return;
                }
                ResponseUsersId responseUsersId = response.body();
                user = new User(sid, uid, responseUsersId.name, responseUsersId.lat, responseUsersId.lon, responseUsersId.time, responseUsersId.life, responseUsersId.experience, responseUsersId.weapon, responseUsersId.armor, responseUsersId.amulet, responseUsersId.picture, responseUsersId.profileversion, responseUsersId.positionshare);
                Log.d("MainModel", "Name: " + responseUsersId.name);
                Log.d("MainModel", "Experience: " + responseUsersId.experience);
                Log.d("MainModel", "Life: " + responseUsersId.life);
            }
            @Override
            public void onFailure(Call<ResponseUsersId> call, Throwable t) {
                Log.d("MainModel", "onFailure: " + t.getMessage());
            }
        });

    }

    public User getUser() {
        return user;
    }

}
