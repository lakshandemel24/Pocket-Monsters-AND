package com.example.pocketmonsters.ui.nearby;

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
    private double lat;
    private double lon;

    RetrofitProvider retrofitProvider = new RetrofitProvider();
    VirtualObjDBHelper virtualObjDBHelper;

    public NearbyRepository(String sid, double lat, double lon) {
        this.sid = sid;
        this.lat = lat;
        this.lon = lon;
    }

    public void getVirtualObj(String sid, double lat, double lon, NearbyListener nearbyListener) {

        List<VirtualObj> virtualObjList = new ArrayList<>();
        virtualObjDBHelper = new VirtualObjDBHelper(null);

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
                for (ObjectsResponse obj : result) {

                    Call<ObjectsResponseId> call2 = retrofitProvider.getApiInterface().getObject(obj.id, sid);

                    if(virtualObjDBHelper.loadById(obj.id) != null) {

                        virtualObjList.add(virtualObjDBHelper.loadById(obj.id));
                        if(virtualObjList.size() == result.size()) {
                            nearbyListener.onSuccess(virtualObjList);
                        }

                    } else {

                        call2.enqueue(new Callback<ObjectsResponseId>() {
                            @Override
                            public void onResponse(Call<ObjectsResponseId> call2, retrofit2.Response<ObjectsResponseId> response) {
                                if (!response.isSuccessful()) {
                                    Log.d("Lak-NearbyRepository", "Error: " + response.code());
                                    nearbyListener.onFailure();
                                    return;
                                }
                                ObjectsResponseId resultById = response.body();
                                virtualObjDBHelper.insertUser(new VirtualObj(resultById.id, resultById.type, resultById.name, resultById.level, resultById.image, resultById.lat, resultById.lon));
                                virtualObjList.add(new VirtualObj(resultById.id, resultById.type, resultById.name, resultById.level, resultById.image, resultById.lat, resultById.lon));
                                if(virtualObjList.size() == result.size()) {
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

                }
            }
            @Override
            public void onFailure(Call<List<ObjectsResponse>> call, Throwable t) {
                Log.d("Lak-NearbyRepository", "Error: " + t.getMessage());
                nearbyListener.onFailure();
            }
        });

    }

}