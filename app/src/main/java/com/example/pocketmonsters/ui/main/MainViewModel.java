package com.example.pocketmonsters.ui.main;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.example.pocketmonsters.R;
import com.example.pocketmonsters.api.ObjectsResponseId;
import com.example.pocketmonsters.api.ResponseUserData;
import com.example.pocketmonsters.api.RetrofitProvider;
import com.example.pocketmonsters.database.room.user.UserDBHelper;
import com.example.pocketmonsters.database.room.virtualObj.VirtualObjDBHelper;
import com.example.pocketmonsters.models.Player;
import com.example.pocketmonsters.models.User;
import com.example.pocketmonsters.models.VirtualObj;
import com.example.pocketmonsters.ui.SharedViewModel;
import com.example.pocketmonsters.ui.nearby.NearbyListener;
import com.example.pocketmonsters.ui.nearby.NearbyRepository;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class MainViewModel extends ViewModel {

    private String sid;
    private SharedViewModel sharedViewModelM;
    private final MainModel mainModel;
    private RetrofitProvider retrofitProvider = new RetrofitProvider();
    double maxDistance = 100;
    public MainViewModel() {
        super();
        mainModel = new MainModel();
    }

    public User getUser() {
        return mainModel.getUser();
    }

    public void addMarkers(GoogleMap map, double lat, double lon, VirtualObjDBHelper virtualObjDBHelper, SharedViewModel sharedViewModel, UserDBHelper userDBHelper, Context context, View v) {

        map.clear();

        sid = sharedViewModel.getUser().getValue().getSid();
        sharedViewModelM = sharedViewModel;

        MainRepository mainRepository = new MainRepository(sid);

        mainRepository.getNearbyPlayers(sid, lat, lon, new MainPlayersListener() {
//TO UPGRADEEEEEE
            @Override
            public void onSuccess(List<Player> userList) {

                for (Player player : userList) {

                    BitmapDrawable bitmapdraw = (BitmapDrawable)context.getResources().getDrawable(R.drawable.player);

                    setMarkerPlayer(player, map, bitmapdraw);

                }

            }

            @Override
            public void onFailure() {
                Toast.makeText(context, "Error loading players, try again later...", Toast.LENGTH_SHORT).show();
            }
        });

        int idUserAmulet = sharedViewModelM.getUser().getValue().getAmulet();

        if(idUserAmulet != 0) {

            Call<ObjectsResponseId> call2 = retrofitProvider.getApiInterface().getObject(idUserAmulet, sid);
            call2.enqueue(new Callback<ObjectsResponseId>() {
                @Override
                public void onResponse(Call<ObjectsResponseId> call2, retrofit2.Response<ObjectsResponseId> response) {
                    if (!response.isSuccessful()) {
                        Log.d("Lak-MainViewModel", "Error: " + response.code());
                        return;
                    }
                    ObjectsResponseId resultById = response.body();

                    maxDistance = 100 + resultById.level;
                    mainRepository.getNearbyVirtualObjs(sid, lat, lon, virtualObjDBHelper, maxDistance, new MainVirtualObjsListener() {

                        @Override
                        public void onSuccess(List<VirtualObj> virtualObjList) {

                            for (VirtualObj virtualObj : virtualObjList) {

                                if(virtualObj.getType().equals("monster")) {

                                    BitmapDrawable bitmapdraw = (BitmapDrawable)context.getResources().getDrawable(R.drawable.monster);

                                    setMarkerVirtualObj(virtualObj, map, bitmapdraw);

                                } else if (virtualObj.getType().equals("candy")) {

                                    BitmapDrawable bitmapdraw = (BitmapDrawable)context.getResources().getDrawable(R.drawable.candy);

                                    setMarkerVirtualObj(virtualObj, map, bitmapdraw);

                                } else {

                                    BitmapDrawable bitmapdraw = (BitmapDrawable)context.getResources().getDrawable(R.drawable.artifact);

                                    setMarkerVirtualObj(virtualObj, map, bitmapdraw);

                                }

                            }

                        }

                        @Override
                        public void onFailure() {
                            Toast.makeText(context, "Error loading virtual objects, try again later...", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
                @Override
                public void onFailure(Call<ObjectsResponseId> call, Throwable t) {
                    Log.d("Lak-MainViewModel", "Error: " + t.getMessage());
                }
            });

        } else {

            maxDistance = 100;
            mainRepository.getNearbyVirtualObjs(sid, lat, lon, virtualObjDBHelper, maxDistance, new MainVirtualObjsListener() {

                @Override
                public void onSuccess(List<VirtualObj> virtualObjList) {

                    for (VirtualObj virtualObj : virtualObjList) {

                        if(virtualObj.getType().equals("monster")) {

                            BitmapDrawable bitmapdraw = (BitmapDrawable)context.getResources().getDrawable(R.drawable.monster);

                            setMarkerVirtualObj(virtualObj, map, bitmapdraw);

                        } else if (virtualObj.getType().equals("candy")) {

                            BitmapDrawable bitmapdraw = (BitmapDrawable)context.getResources().getDrawable(R.drawable.candy);

                            setMarkerVirtualObj(virtualObj, map, bitmapdraw);

                        } else {

                            BitmapDrawable bitmapdraw = (BitmapDrawable)context.getResources().getDrawable(R.drawable.artifact);

                            setMarkerVirtualObj(virtualObj, map, bitmapdraw);

                        }

                    }

                }

                @Override
                public void onFailure() {
                    Toast.makeText(context, "Error loading virtual objects, try again later...", Toast.LENGTH_SHORT).show();
                }
            });

        }

        mainRepository.getNearbyVirtualObjs(sid, lat, lon, virtualObjDBHelper, maxDistance, new MainVirtualObjsListener() {

            @Override
            public void onSuccess(List<VirtualObj> virtualObjList) {

                for (VirtualObj virtualObj : virtualObjList) {

                    if(virtualObj.getType().equals("monster")) {

                        BitmapDrawable bitmapdraw = (BitmapDrawable)context.getResources().getDrawable(R.drawable.monster);

                        setMarkerVirtualObj(virtualObj, map, bitmapdraw);

                    } else if (virtualObj.getType().equals("candy")) {

                        BitmapDrawable bitmapdraw = (BitmapDrawable)context.getResources().getDrawable(R.drawable.candy);

                        setMarkerVirtualObj(virtualObj, map, bitmapdraw);

                    } else {

                        BitmapDrawable bitmapdraw = (BitmapDrawable)context.getResources().getDrawable(R.drawable.artifact);

                        setMarkerVirtualObj(virtualObj, map, bitmapdraw);

                    }

                }

            }

            @Override
            public void onFailure() {
                Toast.makeText(context, "Error loading virtual objects, try again later...", Toast.LENGTH_SHORT).show();
            }
        });

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {

                if(marker.getTag().getClass() == VirtualObj.class) {

                    setBattleDialog(context, marker, userDBHelper, sharedViewModel, sid);

                    return false;
                }

//TO UPGRADEEEEEE
                Player p = (Player) marker.getTag();

                Bundle bundle = new Bundle();

                bundle.putString("origin", "main");
                bundle.putString("name", p.getName());
                bundle.putString("expPoints", String.format("%02d", p.getExpPoits()));
                bundle.putString("lifePoints", String.format("%02d", p.getLifePoints()));
                bundle.putString("level", String.format("%02d", p.getLevel()));
                bundle.putString("profilePicture", p.getProfilePicture());
                bundle.putBoolean("isPositionSharing", p.isPositionSharing());
                if(p.isPositionSharing()) {
                    bundle.putDouble("lat", lat);
                    bundle.putDouble("lon", lon);
                }

                NavController navController = Navigation.findNavController(v);
                navController.navigate(R.id.action_mainFragment_to_playerDetails, bundle);

                return false;
            }
        });

    }

    private void setBattleDialog(Context context, Marker marker, UserDBHelper userDBHelper, SharedViewModel sharedViewModel, String sid) {

        VirtualObj virtualObj = (VirtualObj) marker.getTag();

        Dialog builder = new Dialog(context);
        builder.setContentView(R.layout.dialog_box);
        builder.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        builder.getWindow().setBackgroundDrawableResource(R.drawable.custom_dialog_bg);

        TextView name = builder.findViewById(R.id.nameD);
        TextView expPoints = builder.findViewById(R.id.expPointsD);
        TextView lifePoints = builder.findViewById(R.id.lifePointsD);
        ImageView profilePicture = builder.findViewById(R.id.imageView);
        Button active = builder.findViewById(R.id.close);
        active.setText("activate");

        name.setText(virtualObj.getName());
        expPoints.setText(virtualObj.getType());
        lifePoints.setText("lv. " + virtualObj.getLevel());

        if(virtualObj.getImage() != null) {

            byte[] imageByteArray = Base64.decode(virtualObj.getImage(), Base64.DEFAULT);

            Glide.with(context)
                    .asBitmap()
                    .load(imageByteArray)
                    .into(profilePicture);

        }

        if(virtualObj.getType().equals("monster")) {
            active.setText("fight");
            showDamage(builder.findViewById(R.id.damage), virtualObj.getLevel());
        }

        active.setOnClickListener(v1 -> {

            if(sharedViewModel.getUser().getValue().getLife() <= virtualObj.getLevel() && virtualObj.getType().equals("monster")) {

                new MaterialAlertDialogBuilder(context)
                        .setTitle("DANGER")
                        .setMessage("Your life is in danger, there is a good chance that you will die in this battle. Are you sure you want to fight it?")
                        .setPositiveButton("Fight", (dialog, which) -> {

                            Call<ResponseUserData> call = retrofitProvider.getApiInterface().activateObject(virtualObj.getId(), sid);
                            call.enqueue(new Callback<ResponseUserData>() {
                                @Override
                                public void onResponse(Call<ResponseUserData> call, retrofit2.Response<ResponseUserData> response) {
                                    if (!response.isSuccessful()) {
                                        Log.d("Lak-NearbyViewModel", "Error: " + response.code());
                                        Toast.makeText(context, "Error activating", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    ResponseUserData result = response.body();

                                    if(result.died == true) {

                                        new MaterialAlertDialogBuilder(context)
                                                .setTitle("GAME OVER")
                                                .setMessage("You died in this fight, all the artifacts you had are lost...")
                                                .setNegativeButton("Ok", (dialog1, which1) -> {
                                                    dialog1.dismiss();
                                                })
                                                .show();

                                    }

                                    User user = userDBHelper.getUser();
                                    maxDistance = 100;
                                    sharedViewModel.setUser(new User(user.getSid(), user.getUid(), user.getName(), user.getLat(), user.getLon(), user.getTime(), result.life, result.experience, result.weapon, result.armor, result.amulet, user.getPicture(), user.getProfileversion(), user.isPositionshare()));
                                    userDBHelper.clearUsers();
                                    userDBHelper.insertUser(new User(user.getSid(), user.getUid(), user.getName(), user.getLat(), user.getLon(), user.getTime(), result.life, result.experience, result.weapon, result.armor, result.amulet, user.getPicture(), user.getProfileversion(), user.isPositionshare()));
                                    Toast.makeText(context, "Activated", Toast.LENGTH_SHORT).show();
                                    builder.dismiss();
                                }

                                @Override
                                public void onFailure(Call<ResponseUserData> call, Throwable t) {
                                    Log.d("Lak", "Error: " + t.getMessage());
                                    Toast.makeText(context, "Error activating", Toast.LENGTH_SHORT).show();
                                }
                            });

                        })
                        .setNegativeButton("Cancel", (dialog, which) -> {
                            builder.dismiss();
                        })
                        .show();


            } else {

                builder.dismiss();

                Call<ResponseUserData> call = retrofitProvider.getApiInterface().activateObject(virtualObj.getId(), sid);
                call.enqueue(new Callback<ResponseUserData>() {
                    @Override
                    public void onResponse(Call<ResponseUserData> call, retrofit2.Response<ResponseUserData> response) {
                        if (!response.isSuccessful()) {
                            Log.d("Lak-NearbyViewModel", "Error: " + response.code());
                            Toast.makeText(context, "Error activating", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        ResponseUserData result = response.body();

                        if(result.died == true) {

                            new MaterialAlertDialogBuilder(context)
                                    .setTitle("GAME OVER")
                                    .setMessage("You died in this fight, all the artifacts you had are lost...")
                                    .setNegativeButton("Ok", (dialog1, which1) -> {
                                        dialog1.dismiss();
                                    })
                                    .show();

                        }

                        User user = userDBHelper.getUser();
                        sharedViewModel.setUser(new User(user.getSid(), user.getUid(), user.getName(), user.getLat(), user.getLon(), user.getTime(), result.life, result.experience, result.weapon, result.armor, result.amulet, user.getPicture(), user.getProfileversion(), user.isPositionshare()));
                        userDBHelper.clearUsers();
                        userDBHelper.insertUser(new User(user.getSid(), user.getUid(), user.getName(), user.getLat(), user.getLon(), user.getTime(), result.life, result.experience, result.weapon, result.armor, result.amulet, user.getPicture(), user.getProfileversion(), user.isPositionshare()));
                        Toast.makeText(context, "Activated", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<ResponseUserData> call, Throwable t) {
                        Log.d("Lak", "Error: " + t.getMessage());
                        Toast.makeText(context, "Error activating", Toast.LENGTH_SHORT).show();
                    }
                });

            }

        });

        builder.show();

    }

    public void setMarkerPlayer(Player player, GoogleMap map, BitmapDrawable bitmapdraw) {

        int height = 100;
        int width = 100;
        Bitmap b = bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

        Marker marker = map.addMarker(new MarkerOptions()
                .position(new LatLng(player.getLat(), player.getLon()))
                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                .draggable(true)
        );

        marker.setTag(player);

    }

    public void setMarkerVirtualObj(VirtualObj virtualObj, GoogleMap map, BitmapDrawable bitmapdraw) {

        int height = 150;
        int width = 150;
        Bitmap b = bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

        Marker marker = map.addMarker(new MarkerOptions()
                .position(new LatLng(virtualObj.getLat(), virtualObj.getLon()))
                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                .draggable(true)
        );

        marker.setTag(virtualObj);

    }

    public void showDamage(TextView damageView, int level) {

        int weapon = sharedViewModelM.getUser().getValue().getWeapon();

        if(weapon != 0) {

            Call<ObjectsResponseId> call2 = retrofitProvider.getApiInterface().getObject(weapon, sid);
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
