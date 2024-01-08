package com.example.pocketmonsters.ui.classification;

import android.util.Log;
import android.view.View;

import com.example.pocketmonsters.api.ResponseUsersId;
import com.example.pocketmonsters.api.ResponseUsersRanking;
import com.example.pocketmonsters.api.RetrofitProvider;
import com.example.pocketmonsters.models.Player;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class ClassificationRepository {

    private String sid;
    RetrofitProvider retrofitProvider = new RetrofitProvider();

    public ClassificationRepository(String sid) {
        this.sid = sid;
    }

    public void getPlayers(String sid, ClassificationListener classificationListener) {

        List<Player> players = new ArrayList<>();

        Call<List<ResponseUsersRanking>> call = retrofitProvider.getApiInterface().getRanking(sid);
        call.enqueue(new Callback<List<ResponseUsersRanking>>() {
            @Override
            public void onResponse(Call<List<ResponseUsersRanking>> call, retrofit2.Response<List<ResponseUsersRanking>> response) {
                if (!response.isSuccessful()) {
                    Log.d("Lak-ClassificationRepository", "Error: " + response.code());
                    classificationListener.onFailure();
                    return;
                }
                List<ResponseUsersRanking> result = response.body();
                for (ResponseUsersRanking obj : result) {

                    Call<ResponseUsersId> call2 = retrofitProvider.getApiInterface().getUser(obj.uid, sid);
                    call2.enqueue(new Callback<ResponseUsersId>() {
                        @Override
                        public void onResponse(Call<ResponseUsersId> call, retrofit2.Response<ResponseUsersId> response) {
                            if (!response.isSuccessful()) {
                                Log.d("Lak-ClassificationRepository", "Error: " + response.code());
                                classificationListener.onFailure();
                                return;
                            }
                            ResponseUsersId resultUs = response.body();
                            players.add(new Player(resultUs.name, resultUs.experience, resultUs.life, resultUs.picture));
                            if(players.size() == 20) {
                                classificationListener.onSuccess(players);
                            }
                        }
                        @Override
                        public void onFailure(Call<ResponseUsersId> call, Throwable t) {
                            Log.d("Lak-ClassificationRepository", "Error: " + t.getMessage());
                            classificationListener.onFailure();
                        }
                    });
                }
            }
            @Override
            public void onFailure(Call<List<ResponseUsersRanking>> call, Throwable t) {
                Log.d("Lak-ClassificationRepository", "Error: " + t.getMessage());
                classificationListener.onFailure();
            }
        });

    }

}
