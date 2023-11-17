package com.example.pocketmonsters.ui.main;

import androidx.lifecycle.ViewModel;

import com.example.pocketmonsters.models.User;

public class MainViewModel extends ViewModel {

    private final MainModel mainModel;
    public MainViewModel() {
        super();
        mainModel = new MainModel();
    }

    public void setUser() {
        mainModel.setUser();
    }

    public void setUserDet(String sid, int uid) {
        mainModel.setUserDet(sid, uid);
    }

    public User getUser() {
        return mainModel.getUser();
    }

}
