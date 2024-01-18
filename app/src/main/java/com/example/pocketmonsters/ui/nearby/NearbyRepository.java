package com.example.pocketmonsters.ui.nearby;

import android.content.Context;
import android.util.Log;

import com.example.pocketmonsters.api.ObjectsResponse;
import com.example.pocketmonsters.api.ObjectsResponseId;
import com.example.pocketmonsters.api.RetrofitProvider;
import com.example.pocketmonsters.database.room.virtualObj.VirtualObjDBHelper;
import com.example.pocketmonsters.models.VirtualObj;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class NearbyRepository {

    private String sid;

    private static final double EARTH_RADIUS = 6371;

    RetrofitProvider retrofitProvider = new RetrofitProvider();
    VirtualObjDBHelper virtualObjDBHelper;
    int count;

    public NearbyRepository(String sid) {
        this.sid = sid;
    }

    public void getVirtualObj(String sid, double lat, double lon, Context context, double maxDistance, NearbyListener nearbyListener) {

        List<VirtualObj> virtualObjList = new ArrayList<>();
        virtualObjDBHelper = new VirtualObjDBHelper(context);

        Call<List<ObjectsResponse>> call = retrofitProvider.getApiInterface().getObjects(sid, lat, lon);
        call.enqueue(new Callback<List<ObjectsResponse>>() {
            @Override
            public void onResponse(Call<List<ObjectsResponse>> call, retrofit2.Response<List<ObjectsResponse>> response) {
                if (!response.isSuccessful()) {
                    Log.d("Lak-NearbyRepository", "Error: " + response.code());
                    nearbyListener.onFailure();
                    return;
                }
                List<ObjectsResponse> result = response.body();

                count = 1;
                for (ObjectsResponse obj : result) {

                    double distance = calculateDistance(lat, lon, obj.lat, obj.lon);
                    count++;

                    if(distance < maxDistance) {

                        if(virtualObjDBHelper.loadById(obj.id) != null) {

                            virtualObjList.add(virtualObjDBHelper.loadById(obj.id));
                            if(result.size() == count) {
                                Log.d("Lak-NearbyRepository", "DB");
                                nearbyListener.onSuccess(virtualObjList);
                            }

                        } else {

                            Call<ObjectsResponseId> call2 = retrofitProvider.getApiInterface().getObject(obj.id, sid);
                            call2.enqueue(new Callback<ObjectsResponseId>() {
                                @Override
                                public void onResponse(Call<ObjectsResponseId> call2, retrofit2.Response<ObjectsResponseId> response) {
                                    if (!response.isSuccessful()) {
                                        Log.d("Lak-NearbyRepository", "Error: " + response.code());
                                        nearbyListener.onFailure();
                                        return;
                                    }
                                    ObjectsResponseId resultById = response.body();
                                    virtualObjDBHelper.insert(new VirtualObj(resultById.id, resultById.name, resultById.type, resultById.level, resultById.image, resultById.lat, resultById.lon));
                                    virtualObjList.add(new VirtualObj(resultById.id, resultById.name, resultById.type, resultById.level, resultById.image, resultById.lat, resultById.lon));
                                    if(result.size() == count) {
                                        Log.d("Lak-NearbyRepository", "API");
                                        nearbyListener.onSuccess(virtualObjList);
                                    }
                                }
                                @Override
                                public void onFailure(Call<ObjectsResponseId> call, Throwable t) {
                                    Log.d("Lak-NearbyRepository", "Error: " + t.getMessage());
                                    nearbyListener.onFailure();
                                }
                            });

                        }

                    } else {
                        if(result.size() == count) {
                            Log.d("Lak-NearbyRepository", "other");
                            nearbyListener.onSuccess(virtualObjList);
                        }
                    }

                }
            }
            @Override
            public void onFailure(Call<List<ObjectsResponse>> call, Throwable t) {
                Log.d("Lak-NearbyRepository", "Error: " + t.getMessage());
                nearbyListener.onFailure();
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
