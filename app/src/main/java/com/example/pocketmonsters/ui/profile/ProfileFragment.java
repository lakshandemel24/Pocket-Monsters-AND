package com.example.pocketmonsters.ui.profile;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.pocketmonsters.R;
import com.example.pocketmonsters.database.room.user.UserDBHelper;
import com.example.pocketmonsters.databinding.FragmentMainBinding;
import com.example.pocketmonsters.databinding.FragmentProfileBinding;
import com.example.pocketmonsters.models.User;
import com.example.pocketmonsters.models.VirtualObj;
import com.example.pocketmonsters.ui.SharedViewModel;
import com.example.pocketmonsters.ui.main.MainViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    SharedViewModel sharedViewModel;
    ProfileViewModel profileViewModel;
    UserDBHelper userDBHelper;

    ActivityResultLauncher<PickVisualMediaRequest> launcher = registerForActivityResult(
            new ActivityResultContracts.PickVisualMedia(), new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri o) {
                    if(o == null) {
                        Toast.makeText(getContext(), "No image selected", Toast.LENGTH_SHORT).show();
                    } else{

                        long size = FileUtils.getImageSizeInKB(getContext(), o);

                        if(size > 99) {
                            Toast.makeText(getContext(), "Image too big", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        Log.d("Lak-ProfileFragment", "Image size: " + size);

                        Glide.with(getContext()).load(o).into(binding.userImage);
                        profileViewModel.changeUserImage(o, sharedViewModel, getContext(), userDBHelper);

                    }
                }
            });

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        userDBHelper = new UserDBHelper(getContext());
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
                //Log.d("Lak-ProfileFragment", "Position sharing: " + user.isPositionshare());
                binding.userName.setText(user.getName());
                binding.userLife.setText(String.valueOf(user.getLife()));
                binding.userLevel.setText("LIVELLO " + String.valueOf(user.getExperience()/100));
                binding.progressExpBar.setProgress(user.getExperience() % 100);
                binding.userExp.setText(String.valueOf(user.getExperience() % 100) + "/100");
                binding.posSharingSwitch.setChecked(user.isPositionshare());

                if(user.getPicture() != null) {

                    /*
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    byte[] imageBytes = baos.toByteArray();
                    imageBytes = Base64.decode(user.getPicture(), Base64.DEFAULT);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                    binding.userImage.setImageBitmap(bitmap);
                    */

                    byte[] imageByteArray = Base64.decode(user.getPicture(), Base64.DEFAULT);

                    Glide.with(getContext())
                            .asBitmap()
                            .load(imageByteArray)
                            .into(binding.userImage);

                }

                profileViewModel.setUserArtifacts(user.getArtifacts(), user.getSid(), binding.weapon, binding.armor, binding.amulet, binding.progressBar1, binding.progressBar2, binding.progressBar3, binding.textViewWeapon, binding.textViewArmor, binding.textViewAmulet, getContext());

                binding.posSharingSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if(isChecked) {
                        profileViewModel.setPosSharing(true, user.getSid(), user.getUid());
                        sharedViewModel.setUser(new User(user.getSid(), user.getUid(), user.getName(), user.getLat(), user.getLon(), user.getTime(), user.getLife(), user.getExperience(), user.getWeapon(), user.getArmor(), user.getAmulet(), user.getPicture(), user.getProfileversion(), true));
                        userDBHelper.clearUsers();
                        userDBHelper.insertUser(new User(user.getSid(), user.getUid(), user.getName(), user.getLat(), user.getLon(), user.getTime(), user.getLife(), user.getExperience(), user.getWeapon(), user.getArmor(), user.getAmulet(), user.getPicture(), user.getProfileversion(), true));
                    } else {
                        profileViewModel.setPosSharing(false, user.getSid(), user.getUid());
                        sharedViewModel.setUser(new User(user.getSid(), user.getUid(), user.getName(), user.getLat(), user.getLon(), user.getTime(), user.getLife(), user.getExperience(), user.getWeapon(), user.getArmor(), user.getAmulet(), user.getPicture(), user.getProfileversion(), false));
                        userDBHelper.clearUsers();
                        userDBHelper.insertUser(new User(user.getSid(), user.getUid(), user.getName(), user.getLat(), user.getLon(), user.getTime(), user.getLife(), user.getExperience(), user.getWeapon(), user.getArmor(), user.getAmulet(), user.getPicture(), user.getProfileversion(), false));
                    }
                });

                binding.editProfile.setOnClickListener(v -> {

                    launcher.launch(new PickVisualMediaRequest.Builder()
                            .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                            .build()
                    );

                });

                binding.editProfileName.setOnClickListener(v -> {

                    new MaterialAlertDialogBuilder(getContext())
                            .setTitle("Account name")
                            .setMessage("Insert a new name:")
                            .setPositiveButton("OK", (dialog, which) -> {
                                EditText userInput = ((Dialog) dialog).findViewById(R.id.etUserInput);
                                String name = userInput.getText().toString();
                                if(name.isEmpty()) {
                                    Toast.makeText(getContext(), "Name cannot be empty", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                profileViewModel.changeUserName(user.getSid(), user.getUid(), name, sharedViewModel, userDBHelper);
                                sharedViewModel.setUser(new User(user.getSid(), user.getUid(), name, user.getLat(), user.getLon(), user.getTime(), user.getLife(), user.getExperience(), user.getWeapon(), user.getArmor(), user.getAmulet(), user.getPicture(), user.getProfileversion(), user.isPositionshare()));
                            })
                            .setView(R.layout.dialog_name)
                            .setCancelable(false)
                            .show();

                });

            }
        });

    }

}