package com.example.pocketmonsters.ui.profile;

import android.content.Context;
import android.widget.TextView;

import androidx.lifecycle.ViewModel;

import com.example.pocketmonsters.models.User;

public class ProfileViewModel extends ViewModel {

    private final ProfileModel profileModel;

    public ProfileViewModel() {
        super();
        profileModel = new ProfileModel();
    }

    public User getUser() {
        return profileModel.getUser();
    }

    public void setUser(Context context, TextView userNameText, TextView userLifeText) {

        ProfileRepository profileRepository = new ProfileRepository();

        profileRepository.getUserRep(context, new ProfileListener() {
            @Override
            public void onSuccess(User user) {
                profileModel.setUser(user);
                userNameText.setText(user.getName());
                userLifeText.setText(String.valueOf(user.getLife()));
            }

            @Override
            public void onFailure() {
                profileModel.setUser(null);
            }
        });

    }

}
