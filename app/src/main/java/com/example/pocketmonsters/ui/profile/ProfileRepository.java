package com.example.pocketmonsters.ui.profile;

import android.util.Log;

import com.example.pocketmonsters.api.ObjectsResponseId;
import com.example.pocketmonsters.api.RetrofitProvider;
import com.example.pocketmonsters.models.VirtualObj;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class ProfileRepository {

    RetrofitProvider retrofitProvider = new RetrofitProvider();

    List<VirtualObj> virtualObjList = new ArrayList<>();

    public void getUserArtifacts(int[] uidObj, String sid, ProfileListener profileListener) {

        for (int i = 0; i < 3; i++) {

            if(uidObj[i] == 0) {
                continue;
            }

            Call<ObjectsResponseId> call = retrofitProvider.getApiInterface().getObject(uidObj[i], sid);
            call.enqueue(new Callback<ObjectsResponseId>() {
                @Override
                public void onResponse(Call<ObjectsResponseId> call, retrofit2.Response<ObjectsResponseId> response) {
                    if (!response.isSuccessful()) {
                        Log.d("Lak-ProfileRepository", "Error: " + response.code());
                        return;
                    }
                    ObjectsResponseId result = response.body();

                    virtualObjList.add(new VirtualObj(result.id, result.name, result.type, result.level, result.image, 0, 0));
                    profileListener.onSuccess(virtualObjList);

                }
                @Override
                public void onFailure(Call<ObjectsResponseId> call, Throwable t) {
                    Log.d("Lak-ProfileRepository", "Error: " + t.getMessage());
                    profileListener.onFailure();
                }
            });

        }

    }

}

