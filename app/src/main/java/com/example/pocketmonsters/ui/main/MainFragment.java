package com.example.pocketmonsters.ui.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
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
import com.example.pocketmonsters.models.User;

import retrofit2.Call;

public class MainFragment extends Fragment {

    MainViewModel viewModel;
    User user;
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

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

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
            viewModel.setUser();
            editor.putString("sid", user.getSid());
            editor.putInt("uid", user.getUid());
            editor.apply();
            setUserDet();
        } else {

            new Thread(new Runnable() {
                @Override
                public void run() {

                    viewModel.setUserDet(sid, uid);

                    userName.post(new Runnable() {
                        @Override
                        public void run() {
                            //userName.setText(viewModel.getUser().getName());
                            //setUserDet();
                        }
                    });

                }
            }).start();

        }

    }

    public void setUserDet() {
        userName.setText(viewModel.getUser().getName());
        userLife.setText(String.valueOf(viewModel.getUser().getLife()));
        userLevel.setText(String.valueOf(viewModel.getUser().getExperience()/100));
        progressExpBar.setProgress(viewModel.getUser().getExperience()%100);
    }

    public void setNavBtn() {

        btnProfile.setOnClickListener(v -> navController.navigate(R.id.action_mainFragment_to_profileFragment));
        btnMonsters.setOnClickListener(v -> navController.navigate(R.id.action_mainFragment_to_monstersFragment));
        btnClassification.setOnClickListener(v -> navController.navigate(R.id.action_mainFragment_to_classificationFragment));

    }

}