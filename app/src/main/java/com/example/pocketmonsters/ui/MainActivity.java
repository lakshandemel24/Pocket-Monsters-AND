package com.example.pocketmonsters.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;

import com.example.pocketmonsters.R;
import com.example.pocketmonsters.api.ResponseUsersId;
import com.example.pocketmonsters.api.RetrofitProvider;
import com.example.pocketmonsters.api.SignUpResponse;
import com.example.pocketmonsters.database.room.UserDBHelper;
import com.example.pocketmonsters.databinding.ActivityMainBinding;
import com.example.pocketmonsters.models.User;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import retrofit2.Call;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Lak-MainActivity";
    private ActivityMainBinding binding;
    private SharedViewModel viewModel;

    RetrofitProvider retrofitProvider;
    UserDBHelper userDBHelper;
    int uid;
    String sid;
    User user;

    private boolean requestingLocationUpdates = false;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get the navigation host fragment from this Activity
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);

        // Instantiate the navController using the NavHostFragment
        NavController navController = navHostFragment.getNavController();

        viewModel = new ViewModelProvider(this).get(SharedViewModel.class);

        // Check sid
        retrofitProvider = new RetrofitProvider();
        userDBHelper = new UserDBHelper(this);
        getUser();


    }

    private void getUser() {

        if (userDBHelper.count() == 0) {

            Call<SignUpResponse> call = retrofitProvider.getApiInterface().register();
            call.enqueue(new retrofit2.Callback<SignUpResponse>() {
                @Override
                public void onResponse(Call<SignUpResponse> call, retrofit2.Response<SignUpResponse> response) {
                    if(!response.isSuccessful()) {
                        Log.d(TAG, "Error: " + response.code());
                        return;
                    }
                    SignUpResponse signUpResponse = response.body();
                    Log.d(TAG, "New sid: " + signUpResponse.sid);
                    Log.d(TAG, "New uid: " + signUpResponse.uid);
                    uid = signUpResponse.uid; //1007
                    sid = signUpResponse.sid; //"ePzuGF55G6Z5ZRj6Vj7J"

                    sid = "ePzuGF55G6Z5ZRj6Vj7J";
                    uid = 1007;

                    Call<ResponseUsersId> call2 = retrofitProvider.getApiInterface().getUser(uid, sid);
                    call2.enqueue(new retrofit2.Callback<ResponseUsersId>() {
                        @Override
                        public void onResponse(Call<ResponseUsersId> call, retrofit2.Response<ResponseUsersId> response) {
                            if(!response.isSuccessful()) {
                                Log.d(TAG, "Error: " + response.code());
                                return;
                            }
                            ResponseUsersId responseUsersId = response.body();
                            user = new User(sid, uid, responseUsersId.name, responseUsersId.lat, responseUsersId.lon, responseUsersId.time, responseUsersId.life, responseUsersId.experience, responseUsersId.weapon, responseUsersId.armor, responseUsersId.amulet, responseUsersId.picture, responseUsersId.profileversion, responseUsersId.positionshare);
                            registerUSerName(user);

                            Log.d(TAG, "Name: " + responseUsersId.name);
                            Log.d(TAG, "Experience: " + responseUsersId.experience);
                            Log.d(TAG, "Life: " + responseUsersId.life);
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

            user = userDBHelper.getUser();
            viewModel.setUser(user);
            Log.d(TAG, "User: " + user.getName());

        }

    }

    public void registerUSerName(User user) {
        //TO DO: dialog with edit text -> server patch -> update user in DB

        viewModel.setUser(user);
        userDBHelper.insertUser(user);
    }

}