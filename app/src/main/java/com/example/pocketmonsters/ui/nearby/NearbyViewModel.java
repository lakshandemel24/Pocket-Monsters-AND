package com.example.pocketmonsters.ui.nearby;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.pocketmonsters.R;
import com.example.pocketmonsters.api.ObjectsResponseId;
import com.example.pocketmonsters.api.ResponseUserData;
import com.example.pocketmonsters.api.RetrofitProvider;
import com.example.pocketmonsters.database.room.user.UserDBHelper;
import com.example.pocketmonsters.models.User;
import com.example.pocketmonsters.models.VirtualObj;
import com.example.pocketmonsters.ui.SharedViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

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
    double maxDistance = 100;

    public NearbyViewModel() {
        super();
        nearbyModel = new NearbyModel();
    }

    public void loadNearbyVirtualObj(String sid, double lat, double lon, NearbyAdapter adapter, ProgressBar progressBar, TextView errorText, SharedViewModel sharedViewModel, TextView radius,Context context){

        this.sidN = sid;
        this.userDBHelper = new UserDBHelper(context);
        this.sharedViewModelN = sharedViewModel;

        int idUserAmulet = sharedViewModelN.getUser().getValue().getAmulet();

        if(idUserAmulet != 0) {

            Call<ObjectsResponseId> call2 = retrofitProvider.getApiInterface().getObject(idUserAmulet, sidN);
            call2.enqueue(new Callback<ObjectsResponseId>() {
                @Override
                public void onResponse(Call<ObjectsResponseId> call2, retrofit2.Response<ObjectsResponseId> response) {
                    if (!response.isSuccessful()) {
                        Log.d("Lak-NearbyRepository", "Error: " + response.code());
                        return;
                    }
                    ObjectsResponseId resultById = response.body();

                    maxDistance += resultById.level;

                    radius.setText("Radius of search: " + (int)maxDistance + "m");

                    NearbyRepository nearbyRepository = new NearbyRepository(sid);

                    nearbyRepository.getVirtualObj(sid, lat, lon, context, maxDistance, new NearbyListener() {
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
                @Override
                public void onFailure(Call<ObjectsResponseId> call, Throwable t) {
                    Log.d("Lak-NearbyRepository", "Error: " + t.getMessage());
                }
            });

        } else {

            radius.setText("Radius of search: " + (int)maxDistance + "m");

            NearbyRepository nearbyRepository = new NearbyRepository(sid);

            nearbyRepository.getVirtualObj(sid, lat, lon, context, maxDistance, new NearbyListener() {
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





    }

    public int getVirtualObjCount() {
        return  nearbyModel.getVirtualObjCount();
    }

    public VirtualObj getVirtualObj(int position) {
        return nearbyModel.getVirtualObj(position);
    }


    public void activateVirtualObj(int id, int level, String type, View itemView, NearbyActiteListener nearbyActiteListener) {

        NavController navController = Navigation.findNavController(itemView);

        if(sharedViewModelN.getUser().getValue().getLife() <= level && type.equals("monster")){

            //navController.navigate(R.id.action_nearbyFragment_to_mainFragment);

            new MaterialAlertDialogBuilder(itemView.getContext())
                    .setTitle("DANGER")
                    .setMessage("Your life is in danger, there is a good chance that you will die in this battle. Are you sure you want to fight it?")
                    .setPositiveButton("Fight", (dialog, which) -> {


                        Call<ResponseUserData> call = retrofitProvider.getApiInterface().activateObject(id, sidN);
                        call.enqueue(new Callback<ResponseUserData>() {
                            @Override
                            public void onResponse(Call<ResponseUserData> call, retrofit2.Response<ResponseUserData> response) {
                                if (!response.isSuccessful()) {
                                    Log.d("Lak-NearbyViewModel", "Error: " + response.code());
                                    Toast.makeText(itemView.getContext(), "Error activating", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                ResponseUserData result = response.body();

                                if(result.died == true) {

                                    navController.navigate(R.id.action_nearbyFragment_to_mainFragment);

                                    new MaterialAlertDialogBuilder(itemView.getContext())
                                            .setTitle("GAME OVER")
                                            .setMessage("You died in this fight, all the artifacts you had are lost...")
                                            .setNegativeButton("Ok", (dialog1, which1) -> {
                                                dialog1.dismiss();
                                            })
                                            .show();

                                }

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
                                Toast.makeText(itemView.getContext(), "Error activating", Toast.LENGTH_SHORT).show();
                            }
                        });

                    })
                    .setNegativeButton("Cancel", (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .show();

        } else {

            navController.popBackStack();

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


    public void showDamage(TextView damageView, int level) {

        int weapon = sharedViewModelN.getUser().getValue().getWeapon();

        if(weapon != 0) {

            Call<ObjectsResponseId> call2 = retrofitProvider.getApiInterface().getObject(weapon, sidN);
            call2.enqueue(new Callback<ObjectsResponseId>() {
                @Override
                public void onResponse(Call<ObjectsResponseId> call2, retrofit2.Response<ObjectsResponseId> response) {
                    if (!response.isSuccessful()) {
                        Log.d("Lak-NearbyRepository", "Error: " + response.code());
                        return;
                    }
                    ObjectsResponseId resultById = response.body();

                    double levelWeapon = resultById.level;
                    double damage = level - (levelWeapon * level / 100);
                    damage = Math.round(damage);
                    damageView.setText("possible damage: " + (int)damage + " - " + (int)damage * 2);
                    damageView.setVisibility(View.VISIBLE);

                }
                @Override
                public void onFailure(Call<ObjectsResponseId> call, Throwable t) {
                    Log.d("Lak-NearbyRepository", "Error: " + t.getMessage());
                }
            });

        } else {

                damageView.setText("possible damage: " + level + " - " + level * 2);
                damageView.setVisibility(View.VISIBLE);
        }


    }

}
