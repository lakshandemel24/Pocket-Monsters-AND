package com.example.pocketmonsters.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.pocketmonsters.models.User;
import com.google.android.gms.maps.model.LatLng;

public class SharedViewModel extends ViewModel {


    private final MutableLiveData<User> user = new MutableLiveData<User>(null);

    public LiveData<User> getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user.setValue(user);
    }

    /*

    private final MutableLiveData<LatLng> position = new MutableLiveData<LatLng>(null);

    public LiveData<LatLng> getPosition() {
        return position;
    }

    public void setPosition(LatLng position) {
        this.position.setValue(position);
    }

     */

}
