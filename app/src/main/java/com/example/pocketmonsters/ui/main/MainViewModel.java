package com.example.pocketmonsters.ui.main;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;

import com.example.pocketmonsters.database.room.user.UserDBHelper;
import com.example.pocketmonsters.database.room.virtualObj.VirtualObjDBHelper;
import com.example.pocketmonsters.models.Player;
import com.example.pocketmonsters.models.User;
import com.example.pocketmonsters.ui.SharedViewModel;
import com.google.android.gms.maps.GoogleMap;

import java.util.List;

public class MainViewModel extends ViewModel {

    private final MainModel mainModel;
    public MainViewModel() {
        super();
        mainModel = new MainModel();
    }

    public User getUser() {
        return mainModel.getUser();
    }

    public void addMarkers(GoogleMap map, double lat, double lon, VirtualObjDBHelper virtualObjDBHelper, SharedViewModel sharedViewModel, UserDBHelper userDBHelper, Context context) {

        String sid = sharedViewModel.getUser().getValue().getSid();//Listener del sid!!!!!!!!!!!!!!!

        Log.d("MainViewModel", "SID: " + sid);

        MainRepository mainRepository = new MainRepository(sid);

        mainRepository.getNearbyPlayers(sid, lat, lon, new MainPlayersListener() {

            @Override
            public void onSuccess(List<Player> userList) {

                for (Player player : userList) {

                    Log.d("MainViewModel", "Player: " + player.getName() + " " + player.getLat() + " " + player.getLon());

                }

            }

            @Override
            public void onFailure() {
                Toast.makeText(context, "Error loading players, try again later...", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
