package com.example.pocketmonsters.ui.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.pocketmonsters.api.ResponseUsersId;
import com.example.pocketmonsters.api.RetrofitProvider;
import com.example.pocketmonsters.api.SignUpResponse;
import com.example.pocketmonsters.models.User;
import com.example.pocketmonsters.models.VirtualObj;

import java.util.List;

import retrofit2.Call;

public class MainModel {

    private User user;

    public User getUser() {
        return user;
    }

}
