package com.example.pocketmonsters.ui.profile;

import android.content.Context;
import android.util.Log;

import com.example.pocketmonsters.api.ResponseUsersId;
import com.example.pocketmonsters.api.RetrofitProvider;
import com.example.pocketmonsters.api.SignUpResponse;
import com.example.pocketmonsters.database.room.UserDBHelper;
import com.example.pocketmonsters.models.User;

import retrofit2.Call;

public class ProfileRepository {

    private String sid;
    public User user;
    public int uid;

    RetrofitProvider retrofitProvider = new RetrofitProvider();

    public void getUserRep(Context context, ProfileListener profileListener) {

        UserDBHelper userDBHelper = new UserDBHelper(context);

        if (userDBHelper.count() == 0) {

            Call<SignUpResponse> call = retrofitProvider.getApiInterface().register();
            call.enqueue(new retrofit2.Callback<SignUpResponse>() {
                @Override
                public void onResponse(Call<SignUpResponse> call, retrofit2.Response<SignUpResponse> response) {
                    if(!response.isSuccessful()) {
                        Log.d("Lak-ProfileRepository", "Error: " + response.code());
                        return;
                    }
                    SignUpResponse signUpResponse = response.body();
                    Log.d("Lak-ProfileRepository", "New sid: " + signUpResponse.sid);
                    Log.d("Lak-ProfileRepository", "New uid: " + signUpResponse.uid);
                    uid = signUpResponse.uid; //1007
                    sid = signUpResponse.sid; //ZhyeEJ5lbtgFJ5BFBTvi

                    Call<ResponseUsersId> call2 = retrofitProvider.getApiInterface().getUser(uid, sid);
                    call2.enqueue(new retrofit2.Callback<ResponseUsersId>() {
                        @Override
                        public void onResponse(Call<ResponseUsersId> call, retrofit2.Response<ResponseUsersId> response) {
                            if(!response.isSuccessful()) {
                                Log.d("ProfileRepository", "Error: " + response.code());
                                return;
                            }
                            ResponseUsersId responseUsersId = response.body();
                            user = new User(sid, uid, responseUsersId.name, responseUsersId.lat, responseUsersId.lon, responseUsersId.time, responseUsersId.life, responseUsersId.experience, responseUsersId.weapon, responseUsersId.armor, responseUsersId.amulet, responseUsersId.picture, responseUsersId.profileversion, responseUsersId.positionshare);
                            profileListener.onSuccess(user);
                            userDBHelper.insertUser(user);
                            Log.d("Lak-ProfileRepository", "Name: " + responseUsersId.name);
                            Log.d("Lak-ProfileRepository", "Experience: " + responseUsersId.experience);
                            Log.d("Lak-ProfileRepository", "Life: " + responseUsersId.life);
                        }
                        @Override
                        public void onFailure(Call<ResponseUsersId> call, Throwable t) {
                            Log.d("Lak-ProfileRepository", "onFailure: " + t.getMessage());
                        }
                    });

                }
                @Override
                public void onFailure(Call<SignUpResponse> call, Throwable t) {
                    Log.d("ProfileRepository", "onFailure: " + t.getMessage());
                }
            });

        } else {

            //TO DO: check profileversion and update if needed
            sid = userDBHelper.getUser().getSid();
            uid = userDBHelper.getUser().getUid();

            //sid = "ZhyeEJ5lbtgFJ5BFBTvi";
            //uid = 1007;

            Call<ResponseUsersId> call2 = retrofitProvider.getApiInterface().getUser(uid, sid);
            call2.enqueue(new retrofit2.Callback<ResponseUsersId>() {
                @Override
                public void onResponse(Call<ResponseUsersId> call, retrofit2.Response<ResponseUsersId> response) {
                    if(!response.isSuccessful()) {
                        Log.d("ProfileRepository", "Error: " + response.code());
                        return;
                    }
                    ResponseUsersId responseUsersId = response.body();
                    user = new User(sid, uid, responseUsersId.name, responseUsersId.lat, responseUsersId.lon, responseUsersId.time, responseUsersId.life, responseUsersId.experience, responseUsersId.weapon, responseUsersId.armor, responseUsersId.amulet, responseUsersId.picture, responseUsersId.profileversion, responseUsersId.positionshare);

                    if(userDBHelper.getUser().getProfileversion() != user.getProfileversion()) {
                        userDBHelper.clearUsers();
                        userDBHelper.insertUser(user);
                        profileListener.onSuccess(user);
                    } else {
                        profileListener.onSuccess(user);
                    }

                    Log.d("Lak-ProfileRepository", "Name: " + responseUsersId.name);
                    Log.d("Lak-ProfileRepository", "Experience: " + responseUsersId.experience);
                    Log.d("Lak-ProfileRepository", "Life: " + responseUsersId.life);
                }
                @Override
                public void onFailure(Call<ResponseUsersId> call, Throwable t) {
                    Log.d("Lak-ProfileRepository", "onFailure: " + t.getMessage());
                }
            });

        }

    }

}

