package com.example.pocketmonsters.ui.main;

import android.util.Log;

import com.example.pocketmonsters.api.ObjectsResponse;
import com.example.pocketmonsters.api.ObjectsResponseId;
import com.example.pocketmonsters.api.ResponseUsers;
import com.example.pocketmonsters.api.ResponseUsersId;
import com.example.pocketmonsters.api.RetrofitProvider;
import com.example.pocketmonsters.database.room.virtualObj.VirtualObjDBHelper;
import com.example.pocketmonsters.models.Player;
import com.example.pocketmonsters.models.VirtualObj;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class MainRepository {

    private String sid;
    RetrofitProvider retrofitProvider = new RetrofitProvider();
    private static final double EARTH_RADIUS = 6371;
    int count;

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

                count = 0;
                for (ResponseUsers obj : resultList) {

                    double distance = calculateDistance(lat, lon, obj.lat, obj.lon);
                    count++;

                    if(resultList.size() == count) {
                        mainPlayersListener.onSuccess(players);
                    }

                    if(distance < 100) {

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
                                player.setPositionSharing(result.positionshare);
                                player.setLat(result.lat);
                                player.setLon(result.lon);
                                players.add(player);
                                if(resultList.size() == count) {
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
            }
            @Override
            public void onFailure(Call<List<ResponseUsers>> call, Throwable t) {
                Log.d("Lak", "Error: " + t.getMessage());
                mainPlayersListener.onFailure();
            }
        });

    }

    public void getNearbyVirtualObjs(String sid, double lat, double lon, VirtualObjDBHelper virtualObjDBHelper, double maxDistance, MainVirtualObjsListener mainVirtualObjsListener) {

        List<VirtualObj> virtualObjs = new ArrayList<>();

        Call<List<ObjectsResponse>> call = retrofitProvider.getApiInterface().getObjects(sid, lat, lon);
        call.enqueue(new Callback<List<ObjectsResponse>>() {
            @Override
            public void onResponse(Call<List<ObjectsResponse>> call, retrofit2.Response<List<ObjectsResponse>> response) {
                if (!response.isSuccessful()) {
                    Log.d("Lak-MainRepository", "Error: " + response.code());
                    return;
                }
                List<ObjectsResponse> result = response.body();
                count = 1;

                for (ObjectsResponse obj : result) {

                    if(result.size() == count) {
                        mainVirtualObjsListener.onSuccess(virtualObjs);
                    }

                    double distance = calculateDistance(lat, lon, obj.lat, obj.lon);
                    count++;

                    if(distance > maxDistance) {
                        continue;
                    }

                    Log.d("Lak-MainRepository", "Distance: " + distance + "m");

                    if(virtualObjDBHelper.loadById(obj.id) == null) {

                        Call<ObjectsResponseId> call2 = retrofitProvider.getApiInterface().getObject(obj.id, sid);
                        call2.enqueue(new Callback<ObjectsResponseId>() {
                            @Override
                            public void onResponse(Call<ObjectsResponseId> call2, retrofit2.Response<ObjectsResponseId> response) {
                                if (!response.isSuccessful()) {
                                    Log.d("Lak-MainRepository", "Error: " + response.code());
                                    mainVirtualObjsListener.onFailure();
                                    return;
                                }
                                ObjectsResponseId resultById = response.body();

                                VirtualObj virtualObj = new VirtualObj(resultById.id, resultById.name, resultById.type, resultById.level, resultById.image, resultById.lat, resultById.lon);
                                virtualObjDBHelper.insert(virtualObj);
                                virtualObjs.add(virtualObj);

                                if(result.size() == count) {
                                    mainVirtualObjsListener.onSuccess(virtualObjs);
                                }
                            }
                            @Override
                            public void onFailure(Call<ObjectsResponseId> call, Throwable t) {
                                Log.d("Lak-MainRepository", "Error: " + t.getMessage());
                                mainVirtualObjsListener.onFailure();
                            }
                        });

                    } else {
                        VirtualObj virtualObj = virtualObjDBHelper.loadById(obj.id);
                        virtualObjs.add(virtualObj);

                        if(result.size() == count) {
                            mainVirtualObjsListener.onSuccess(virtualObjs);
                        }
                    }

                }
            }
            @Override
            public void onFailure(Call<List<ObjectsResponse>> call, Throwable t) {
                Log.d("Lak-MainRepository", "Error: " + t.getMessage());
                mainVirtualObjsListener.onFailure();
            }
        });


    }

    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        // Convert latitude and longitude from degrees to radians
        double lat1Rad = Math.toRadians(lat1);
        double lon1Rad = Math.toRadians(lon1);
        double lat2Rad = Math.toRadians(lat2);
        double lon2Rad = Math.toRadians(lon2);

        // Calculate differences
        double deltaLat = lat2Rad - lat1Rad;
        double deltaLon = lon2Rad - lon1Rad;

        // Haversine formula
        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) +
                Math.cos(lat1Rad) * Math.cos(lat2Rad) *
                        Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        // Calculate the distance
        double distance = EARTH_RADIUS * c * 1000;

        return distance;
    }

}
