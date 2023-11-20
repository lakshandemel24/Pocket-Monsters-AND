package com.example.pocketmonsters.ui.profile;

import com.example.pocketmonsters.models.User;

public interface ProfileListener {

    public void onSuccess(User user);

    public void onFailure();

}
