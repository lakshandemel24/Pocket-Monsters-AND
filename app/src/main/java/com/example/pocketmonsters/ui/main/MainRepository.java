package com.example.pocketmonsters.ui.main;

import android.util.Log;

import com.example.pocketmonsters.api.ResponseUsers;
import com.example.pocketmonsters.api.ResponseUsersId;
import com.example.pocketmonsters.api.RetrofitProvider;
import com.example.pocketmonsters.models.Player;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class MainRepository {

    private String sid;
    RetrofitProvider retrofitProvider = new RetrofitProvider();

    public MainRepository(String sid) {
        this.sid = sid;
    }

    public void getNearbyPlayers(String sid, double lat, double lon, MainPlayersListener mainPlayersListener) {

        List<Player> players = new ArrayList<>();

        Call<List<ResponseUsers>> call = retrofitProvider.getApiInterface().getUsers(sid, lat, lon);
        call.enqueue(new Callback<List<ResponseUsers>>() {
            @Override
            public void onResponse(Call<List<ResponseUsers>> call, retrofit2.Response<List<ResponseUsers>> response) {
                if (!response.isSuccessful()) {
                    Log.d("Lak", "Error: " + response.code());
                    mainPlayersListener.onFailure();
                    return;
                }
                List<ResponseUsers> resultList = response.body();
                for (ResponseUsers obj : resultList) {

                    Call<ResponseUsersId> call2 = retrofitProvider.getApiInterface().getUser(obj.uid, sid);
                    call2.enqueue(new Callback<ResponseUsersId>() {
                        @Override
                        public void onResponse(Call<ResponseUsersId> call2, retrofit2.Response<ResponseUsersId> response) {
                            if (!response.isSuccessful()) {
                                Log.d("Lak", "Error: " + response.code());
                                return;
                            }
                            ResponseUsersId result = response.body();

                            Player player = new Player(result.name, result.experience, result.life, result.picture);
                            player.setLat(result.lat);
                            player.setLon(result.lon);
                            players.add(player);

                            if(resultList.size() == players.size()) {
                                mainPlayersListener.onSuccess(players);
                            }

                        }
                        @Override
                        public void onFailure(Call<ResponseUsersId> call2, Throwable t) {
                            Log.d("Lak", "Error: " + t.getMessage());
                            mainPlayersListener.onFailure();
                        }
                    });

                }
            }
            @Override
            public void onFailure(Call<List<ResponseUsers>> call, Throwable t) {
                Log.d("Lak", "Error: " + t.getMessage());
                mainPlayersListener.onFailure();
            }
        });

    }

}
