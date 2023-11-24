package com.example.pocketmonsters.ui.main;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.pocketmonsters.R;
import com.example.pocketmonsters.databinding.FragmentMainBinding;
import com.example.pocketmonsters.ui.SharedViewModel;

import java.io.ByteArrayOutputStream;

public class MainFragment extends Fragment {

    private FragmentMainBinding binding;
    SharedViewModel sharedViewModel;
    MainViewModel viewModel;
    NavController navController;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMainBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.navController = Navigation.findNavController(view);

        setNavBtn();

        sharedViewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                binding.userName.setText(user.getName());
                binding.userLife.setText(String.valueOf(user.getLife()));
                binding.userLevel.setText(String.valueOf(user.getExperience()/100));
                binding.progressExpBar.setProgress(user.getExperience() % 100);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] imageBytes = baos.toByteArray();
                imageBytes = Base64.decode(user.getPicture(), Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                binding.buttonProfile.setImageBitmap(bitmap);
            }
        });

    }

    public void setNavBtn() {

        binding.buttonProfile.setOnClickListener(v -> navController.navigate(R.id.action_mainFragment_to_profileFragment));
        binding.buttonMonsters.setOnClickListener(v -> navController.navigate(R.id.action_mainFragment_to_monstersFragment));
        binding.buttonClassification.setOnClickListener(v -> navController.navigate(R.id.action_mainFragment_to_classificationFragment));

    }

}