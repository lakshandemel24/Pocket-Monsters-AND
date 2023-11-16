package com.example.pocketmonsters.ui.classification;

import android.util.Log;

import com.example.pocketmonsters.api.ResponseUsersId;
import com.example.pocketmonsters.api.ResponseUsersRanking;
import com.example.pocketmonsters.api.RetrofitProvider;
import com.example.pocketmonsters.models.Player;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class ClassificationModel {


    private final List<Player> players = new ArrayList<>();

    public void appendPlayers(List<Player> pls) {
        players.addAll(pls);
    }

    public void getRankingUsers() {

        players.add(new Player("Lak", 620, 200));
        players.add(new Player("Bea", 100, 100));
        players.add(new Player("Pao", 250, 56));
        players.add(new Player("Simo", 100, 120));
        players.add(new Player("Vicky", 70, 69));
        players.add(new Player("Lore", 30, 100));
        players.add(new Player("Ile", 125, 150));
        players.add(new Player("Fra", 20, 100));
        players.add(new Player("Gigi", 5, 110));
        players.add(new Player("Marco", 70, 50));
        players.add(new Player("Mao", 470, 100));
        players.add(new Player("Leo", 250, 75));
        players.add(new Player("Tao", 350, 100));
        players.add(new Player("Pio", 80, 120));
        players.add(new Player("Gio", 220, 190));
        players.add(new Player("Zaza", 30, 100));
        players.add(new Player("Luke", 125, 140));
        players.add(new Player("Fafa", 215, 110));
        players.add(new Player("Gafa", 15, 90));
        players.add(new Player("Bao", 430, 50));

        /*
        Call<List<ResponseUsersRanking>> call = retrofitProvider.getApiInterface().getRanking(sid);
        call.enqueue(new Callback<List<ResponseUsersRanking>>() {
            @Override
            public void onResponse(Call<List<ResponseUsersRanking>> call, retrofit2.Response<List<ResponseUsersRanking>> response) {
                if (!response.isSuccessful()) {
                    Log.d("Lak", "Error: " + response.code());
                    return;
                }
                List<ResponseUsersRanking> result = response.body();
                for (ResponseUsersRanking obj : result) {

                    Call<ResponseUsersId> call2 = retrofitProvider.getApiInterface().getUser(obj.uid, sid);
                    call2.enqueue(new Callback<ResponseUsersId>() {
                        @Override
                        public void onResponse(Call<ResponseUsersId> call, retrofit2.Response<ResponseUsersId> response) {
                            if (!response.isSuccessful()) {
                                Log.d("Lak", "Error: " + response.code());
                                return;
                            }
                            ResponseUsersId resultUs = response.body();
                            players.add(new Player(resultUs.name, resultUs.experience, resultUs.life));
                            Log.d("Lak", "Player: " + resultUs.name);
                        }
                        @Override
                        public void onFailure(Call<ResponseUsersId> call, Throwable t) {
                            Log.d("Lak", "Error: " + t.getMessage());
                        }
                    });

                }
            }
            @Override
            public void onFailure(Call<List<ResponseUsersRanking>> call, Throwable t) {
                Log.d("Api", "Error: " + t.getMessage());
            }
        });
        */

    }

    public void sortPlayers(List<Player> players_to_sort) {
        players_to_sort.sort((o1, o2) -> {
            if (o1.getExpPoits() > o2.getExpPoits()) {
                return -1;
            } else if (o1.getExpPoits() < o2.getExpPoits()) {
                return 1;
            } else {
                if(o1.getLifePoints() > o2.getLifePoints())
                    return -1;
                else if(o1.getLifePoints() < o2.getLifePoints())
                    return 1;
                else
                    return 0;
            }
        });
    }
    public int getPlayersCount() {
        return players.size();
    }
    public Player getPlayers(int position) {
        return players.get(position);
    }

}

/*
        players.add(new Player("Lak", 620, 200));
        players.add(new Player("Bea", 100, 100));
        players.add(new Player("Pao", 250, 56));
        players.add(new Player("Simo", 100, 120));
        players.add(new Player("Vicky", 70, 69));
        players.add(new Player("Lore", 30, 100));
        players.add(new Player("Ile", 125, 150));
        players.add(new Player("Fra", 20, 100));
        players.add(new Player("Gigi", 5, 110));
        players.add(new Player("Marco", 70, 50));
        players.add(new Player("Mao", 470, 100));
        players.add(new Player("Leo", 250, 75));
        players.add(new Player("Tao", 350, 100));
        players.add(new Player("Pio", 80, 120));
        players.add(new Player("Gio", 220, 190));
        players.add(new Player("Zaza", 30, 100));
        players.add(new Player("Luke", 125, 140));
        players.add(new Player("Fafa", 215, 110));
        players.add(new Player("Gafa", 15, 90));
        players.add(new Player("Bao", 430, 50));
 */
