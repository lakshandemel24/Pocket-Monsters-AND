package com.example.pocketmonsters;

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

/*
NavHostFragment navHostFragment = (NavHostFragment)
                getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);

        NavController navController = navHostFragment.getNavController();

        Button btnProfile = mainView.findViewById(R.id.buttonProfile);
        btnProfile.setOnClickListener(v -> navController.navigate(R.id.action_mainFragment_to_profileFragment));

        Button btnMonsters = mainView.findViewById(R.id.buttonMonsters);
        btnMonsters.setOnClickListener(v -> navController.navigate(R.id.action_mainFragment_to_monstersFragment));

        Button btnClassification = mainView.findViewById(R.id.buttonClassification);
        btnClassification.setOnClickListener(v -> navController.navigate(R.id.action_mainFragment_to_classificationFragment));
*/

/*
ImageButton Profile_Button = mainView.findViewById(R.id.buttonProfile);
        Profile_Button.setOnClickListener(v -> {

            getActivity().getSupportFragmentManager().popBackStack();

            getActivity().getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .setCustomAnimations(R.anim.slide_in_up, R.anim.slide_in_down, R.anim.slide_out_down, R.anim.slide_out_up)
                    .replace(R.id.container, ProfileFragment.class, null)
                    .addToBackStack(null)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit();
        });

        Button btnMosters = mainView.findViewById(R.id.buttonMonsters);
        btnMosters.setOnClickListener(v -> {

            getActivity().getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .setCustomAnimations(R.anim.slide_in_up, R.anim.slide_in_down, R.anim.slide_out_down, R.anim.slide_out_up)
                    .replace(R.id.container, MonstersFragment.class, null)
                    .addToBackStack(null)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit();
        });

        ImageButton btnClassification = mainView.findViewById(R.id.buttonClassification);
        btnClassification.setOnClickListener(v -> {

            getActivity().getSupportFragmentManager().popBackStack();

            getActivity().getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .setCustomAnimations(R.anim.slide_in_up, R.anim.slide_in_down, R.anim.slide_out_down, R.anim.slide_out_up)
                    .replace(R.id.container, ClassificationFragment.class, null)
                    .addToBackStack(null)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit();
        });
*/