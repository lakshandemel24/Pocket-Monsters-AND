package com.example.pocketmonsters.ui.main;

import com.example.pocketmonsters.models.Player;

import java.util.List;

public interface MainPlayersListener {

    public void onSuccess(List<Player> userList);

    public void onFailure();

}
