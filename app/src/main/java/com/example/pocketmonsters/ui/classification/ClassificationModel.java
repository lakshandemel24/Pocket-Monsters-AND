package com.example.pocketmonsters.ui.classification;

import android.util.Log;

import com.example.pocketmonsters.api.ResponseUsersId;
import com.example.pocketmonsters.api.ResponseUsersRanking;
import com.example.pocketmonsters.api.RetrofitProvider;
import com.example.pocketmonsters.models.Player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class ClassificationModel {

    private final List<Player> players = new ArrayList<>();

    public int getPlayersCount() {
        return players.size();
    }
    public Player getPlayers(int position) {
        return players.get(position);
    }
    public void appendPlayers(List<Player> p) {
        players.addAll(p);
        players.sort(Comparator.comparingInt(Player::getExpPoits).reversed().thenComparingInt(Player::getLifePoints));
    }

}
