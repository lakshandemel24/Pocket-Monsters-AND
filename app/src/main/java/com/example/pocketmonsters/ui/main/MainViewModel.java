package com.example.pocketmonsters.ui.main;

import androidx.lifecycle.ViewModel;

import com.example.pocketmonsters.models.User;

public class MainViewModel extends ViewModel {

    private final MainModel mainModel;
    public MainViewModel() {
        super();
        mainModel = new MainModel();
    }


    public User getUser() {
        return mainModel.getUser();
    }

}
