package com.example.pocketmonsters.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.os.SharedMemory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.pocketmonsters.R;
import com.example.pocketmonsters.api.ResponseUsersId;
import com.example.pocketmonsters.api.SignUpResponse;
import com.example.pocketmonsters.api.RetrofitProvider;

import retrofit2.Call;

public class MainFragment extends Fragment {

    private NavController navController;
    RetrofitProvider retrofitProvider = new RetrofitProvider();
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ImageButton btnProfile;
    Button btnMonsters;
    ImageButton btnClassification;
    TextView userName;
    TextView userLevel;
    TextView userLife;
    int userExp;
    ProgressBar progressExpBar;

    public MainFragment(){
        super(R.layout.fragment_main);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.navController = Navigation.findNavController(view);

        userName = view.findViewById(R.id.userName);
        userLevel = view.findViewById(R.id.userLevel);
        userLife = view.findViewById(R.id.userLife);
        progressExpBar = view.findViewById(R.id.progressExpBar);
        initializeUser(); //TO DO - if new user, register

        btnProfile = view.findViewById(R.id.buttonProfile);
        btnMonsters = view.findViewById(R.id.buttonMonsters);
        btnClassification = view.findViewById(R.id.buttonClassification);
        setNavBtn();

    }

    public void initializeUser() {

        sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        String sid = sharedPreferences.getString("sid", "default");
        int uid = sharedPreferences.getInt("uid", 0);
        if(sid.equals("default")) {
            getSid();
        } else {
            Log.d("MainFragment", "sid: " + sid);
            Log.d("MainFragment", "uid: " + uid);
            getUserDetails(sid, uid);
            /*
            editor.putString("sid", "ePzuGF55G6Z5ZRj6Vj7J");
            editor.putInt("uid", 1007);
            editor.apply();
             */
        }

    }

    public void getSid() {

        Call<SignUpResponse> call = retrofitProvider.getApiInterface().register();
        call.enqueue(new retrofit2.Callback<SignUpResponse>() {
            @Override
            public void onResponse(Call<SignUpResponse> call, retrofit2.Response<SignUpResponse> response) {
                if(!response.isSuccessful()) {
                    Log.d("MainFragment", "Error: " + response.code());
                    return;
                }
                SignUpResponse signUpResponse = response.body();
                Log.d("MainFragment", "New sid: " + signUpResponse.sid);
                Log.d("MainFragment", "New uid: " + signUpResponse.uid);
                editor.putString("sid", signUpResponse.sid);
                editor.putInt("uid", signUpResponse.uid);
                editor.apply();
                getUserDetails(signUpResponse.sid, signUpResponse.uid);
            }
            @Override
            public void onFailure(Call<SignUpResponse> call, Throwable t) {
                Log.d("MainFragment", "onFailure: " + t.getMessage());
            }
        });

    }

    private void getUserDetails(String sid, int uid) {

        Call<ResponseUsersId> call = retrofitProvider.getApiInterface().getUser(uid, sid);
        call.enqueue(new retrofit2.Callback<ResponseUsersId>() {
            @Override
            public void onResponse(Call<ResponseUsersId> call, retrofit2.Response<ResponseUsersId> response) {
                if(!response.isSuccessful()) {
                    Log.d("MainFragment", "Error: " + response.code());
                    return;
                }
                ResponseUsersId responseUsersId = response.body();
                userName.setText(responseUsersId.name);
                userLife.setText(String.valueOf(responseUsersId.life));
                userLevel.setText(String.valueOf(responseUsersId.experience/100));
                progressExpBar.setProgress(responseUsersId.experience%100);
                Log.d("MainFragment", "Name: " + responseUsersId.name);
                Log.d("MainFragment", "Experience: " + responseUsersId.experience);
                Log.d("MainFragment", "Life: " + responseUsersId.life);
            }
            @Override
            public void onFailure(Call<ResponseUsersId> call, Throwable t) {
                Log.d("MainFragment", "onFailure: " + t.getMessage());
            }
        });

    }

    public void setNavBtn() {

        btnProfile.setOnClickListener(v -> navController.navigate(R.id.action_mainFragment_to_profileFragment));
        btnMonsters.setOnClickListener(v -> navController.navigate(R.id.action_mainFragment_to_monstersFragment));
        btnClassification.setOnClickListener(v -> navController.navigate(R.id.action_mainFragment_to_classificationFragment));

    }

}