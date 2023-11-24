package com.example.pocketmonsters.ui.profile;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.pocketmonsters.R;
import com.example.pocketmonsters.databinding.FragmentMainBinding;
import com.example.pocketmonsters.databinding.FragmentProfileBinding;
import com.example.pocketmonsters.ui.SharedViewModel;
import com.example.pocketmonsters.ui.main.MainViewModel;

import java.io.ByteArrayOutputStream;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    SharedViewModel sharedViewModel;
    ProfileViewModel profileViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageButton back = view.findViewById(R.id.buttonBack);
        back.setOnClickListener(v -> {
            getActivity().getSupportFragmentManager().popBackStack();
        });

        sharedViewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                binding.userName.setText(user.getName());
                binding.userLife.setText(String.valueOf(user.getLife()));
                binding.userLevel.setText("LIVELLO " + String.valueOf(user.getExperience()/100));
                binding.progressExpBar.setProgress(user.getExperience() % 100);
                binding.userExp.setText(String.valueOf(user.getExperience() % 100) + "/100");

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] imageBytes = baos.toByteArray();
                imageBytes = Base64.decode(user.getPicture(), Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                binding.userImage.setImageBitmap(bitmap);

                profileViewModel.setUserArtifacts(user.getArtifacts(), user.getSid(), binding.weapon, binding.armor, binding.amulet, binding.progressBar1, binding.progressBar2, binding.progressBar3, binding.textViewWeapon, binding.textViewArmor, binding.textViewAmulet, getContext());

            }
        });



        /*
        new Thread(new Runnable() {
            @Override
            public void run() {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] imageBytes = baos.toByteArray();
                imageBytes = Base64.decode(tryImg, Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                tryImage.post(new Runnable() {
                    @Override
                    public void run() {
                        tryImage.setImageBitmap(bitmap);
                    }
                });
            }
        }).start();
         */


    }

}