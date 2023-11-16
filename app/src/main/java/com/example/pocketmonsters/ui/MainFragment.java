package com.example.pocketmonsters.ui;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.pocketmonsters.R;

public class MainFragment extends Fragment {

    private NavController navController;

    public MainFragment(){
        super(R.layout.fragment_main);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.navController = Navigation.findNavController(view);

        ImageButton btnProfile = view.findViewById(R.id.buttonProfile);
        btnProfile.setOnClickListener(v -> navController.navigate(R.id.action_mainFragment_to_profileFragment));

        Button btnMonsters = view.findViewById(R.id.buttonMonsters);
        btnMonsters.setOnClickListener(v -> navController.navigate(R.id.action_mainFragment_to_monstersFragment));

        ImageButton btnClassification = view.findViewById(R.id.buttonClassification);
        btnClassification.setOnClickListener(v -> navController.navigate(R.id.action_mainFragment_to_classificationFragment));
    }


}