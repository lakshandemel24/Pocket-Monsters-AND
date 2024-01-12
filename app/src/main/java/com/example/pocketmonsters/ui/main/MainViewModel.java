package com.example.pocketmonsters.ui.main;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.Base64;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.bumptech.glide.Glide;
import com.example.pocketmonsters.R;
import com.example.pocketmonsters.database.room.user.UserDBHelper;
import com.example.pocketmonsters.database.room.virtualObj.VirtualObjDBHelper;
import com.example.pocketmonsters.models.Player;
import com.example.pocketmonsters.models.User;
import com.example.pocketmonsters.models.VirtualObj;
import com.example.pocketmonsters.ui.SharedViewModel;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

public class MainViewModel extends ViewModel {

    private final MainModel mainModel;
    public MainViewModel() {
        super();
        mainModel = new MainModel();
    }

    public User getUser() {
        return mainModel.getUser();
    }

    public void addMarkers(GoogleMap map, double lat, double lon, VirtualObjDBHelper virtualObjDBHelper, SharedViewModel sharedViewModel, UserDBHelper userDBHelper, Context context) {

        map.clear();

        String sid = sharedViewModel.getUser().getValue().getSid();//NOT COMPLETE :(

        MainRepository mainRepository = new MainRepository(sid);

        mainRepository.getNearbyPlayers(sid, lat, lon, new MainPlayersListener() {

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


        mainRepository.getNearbyVirtualObjs(sid, lat, lon, virtualObjDBHelper, new MainVirtualObjsListener() {

            @Override
            public void onSuccess(List<VirtualObj> virtualObjList) {

                for (VirtualObj virtualObj : virtualObjList) {

                    Log.d("Lakko", virtualObj.getType());

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

            }
        });

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {

                if(marker.getTag().getClass() == VirtualObj.class) {

// TO DOOOOO        setBattleDialog();

                    return false;
                }

                Player p = (Player) marker.getTag();

                Dialog builder = new Dialog(context);
                builder.setContentView(R.layout.dialog_box);
                builder.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                builder.getWindow().setBackgroundDrawableResource(R.drawable.custom_dialog_bg);

                TextView name = builder.findViewById(R.id.nameD);
                TextView expPoints = builder.findViewById(R.id.expPointsD);
                TextView lifePoints = builder.findViewById(R.id.lifePointsD);
                ImageView profilePicture = builder.findViewById(R.id.imageView);
                Button close = builder.findViewById(R.id.close);

                name.setText(p.getName());
                expPoints.setText("Exp: " + p.getExpPoits());
                lifePoints.setText("Life: " + p.getLifePoints());
                if(p.getProfilePicture() != null) {

                    byte[] imageByteArray = Base64.decode(p.getProfilePicture(), Base64.DEFAULT);

                    Glide.with(context)
                            .asBitmap()
                            .load(imageByteArray)
                            .into(profilePicture)  ;


                }
                close.setOnClickListener(v1 -> builder.dismiss());

                builder.show();

                return false;
            }
        });

    }

    public void setMarkerPlayer(Player player, GoogleMap map, BitmapDrawable bitmapdraw) {

        int height = 150;
        int width = 150;
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

}
