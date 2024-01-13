package com.example.pocketmonsters.ui.nearby;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.lifecycle.ViewModel;

import com.example.pocketmonsters.api.ResponseUserData;
import com.example.pocketmonsters.api.RetrofitProvider;
import com.example.pocketmonsters.database.room.user.UserDBHelper;
import com.example.pocketmonsters.models.User;
import com.example.pocketmonsters.models.VirtualObj;
import com.example.pocketmonsters.ui.SharedViewModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class NearbyViewModel extends ViewModel {

    public List<VirtualObj> virtualObjList;
    private final NearbyModel nearbyModel;
    String sidN;
    private RetrofitProvider retrofitProvider = new RetrofitProvider();
    UserDBHelper userDBHelper;
    SharedViewModel sharedViewModelN;
    User user;

    public NearbyViewModel() {
        super();
        nearbyModel = new NearbyModel();
    }

    public void loadNearbyVirtualObj(String sid, double lat, double lon, NearbyAdapter adapter, ProgressBar progressBar, TextView errorText, SharedViewModel sharedViewModel, Context context){

        this.sidN = sid;
        this.userDBHelper = new UserDBHelper(context);
        this.sharedViewModelN = sharedViewModel;

        NearbyRepository nearbyRepository = new NearbyRepository(sid);

        nearbyRepository.getVirtualObj(sid, lat, lon, context, new NearbyListener() {
            @Override
            public void onSuccess(List<VirtualObj> virtualObjList) {
                nearbyModel.appendVirtualObjlist(virtualObjList);
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(ProgressBar.INVISIBLE);
            }

            @Override
            public void onFailure(){
                nearbyModel.appendVirtualObjlist(new ArrayList<>());
                progressBar.setVisibility(ProgressBar.INVISIBLE);
                errorText.setText("Error loading classification, try again later...");
            }

        });

    }

    public int getVirtualObjCount() {
        return  nearbyModel.getVirtualObjCount();
    }

    public VirtualObj getVirtualObj(int position) {
        return nearbyModel.getVirtualObj(position);
    }


    public void activateVirtualObj(int id, NearbyActiteListener nearbyActiteListener) {

        Call<ResponseUserData> call = retrofitProvider.getApiInterface().activateObject(id, sidN);
        call.enqueue(new Callback<ResponseUserData>() {
            @Override
            public void onResponse(Call<ResponseUserData> call, retrofit2.Response<ResponseUserData> response) {
                if (!response.isSuccessful()) {
                    Log.d("Lak-NearbyViewModel", "Error: " + response.code());
                    nearbyActiteListener.onFailure();
                    return;
                }
                ResponseUserData result = response.body();
                Log.d("Lak-NearbyViewModel", "Diend: " + result.died);
                Log.d("Lak-NearbyViewModel", "Life: " + result.life);
                Log.d("Lak-NearbyViewModel", "Exp: " + result.experience);
                Log.d("Lak-NearbyViewModel", "Weapon: " + result.weapon);
                Log.d("Lak-NearbyViewModel", "Armor: " + result.armor);
                Log.d("Lak-NearbyViewModel", "Amulet: " + result.amulet);

                sharedViewModelN.getUser();

                user = userDBHelper.getUser();
                sharedViewModelN.setUser(new User(user.getSid(), user.getUid(), user.getName(), user.getLat(), user.getLon(), user.getTime(), result.life, result.experience, result.weapon, result.armor, result.amulet, user.getPicture(), user.getProfileversion(), user.isPositionshare()));
                userDBHelper.clearUsers();
                userDBHelper.insertUser(new User(user.getSid(), user.getUid(), user.getName(), user.getLat(), user.getLon(), user.getTime(), result.life, result.experience, result.weapon, result.armor, result.amulet, user.getPicture(), user.getProfileversion(), user.isPositionshare()));
                nearbyActiteListener.onSuccess();

            }
            @Override
            public void onFailure(Call<ResponseUserData> call, Throwable t) {
                Log.d("Lak", "Error: " + t.getMessage());
                nearbyActiteListener.onFailure();
            }
        });

    }
}
