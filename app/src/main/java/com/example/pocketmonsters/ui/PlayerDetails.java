package com.example.pocketmonsters.ui;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.pocketmonsters.R;
import com.example.pocketmonsters.databinding.FragmentPlayerDetailsBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

public class PlayerDetails extends Fragment {

    String TAG = "Lak-PlayerDetails";
    private FragmentPlayerDetailsBinding binding;
    private GoogleMap mMap;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            Log.d(TAG, "onMapReady: map is ready");

            mMap = googleMap;

            mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getContext(), R.raw.map_dark));
            mMap.getUiSettings().setCompassEnabled(false);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            mMap.getUiSettings().setMapToolbarEnabled(false);

            BitmapDrawable bitmapdraw = (BitmapDrawable)getContext().getResources().getDrawable(R.drawable.player);
            Bitmap b = bitmapdraw.getBitmap();
            Bitmap smallMarker = Bitmap.createScaledBitmap(b, 100, 100, false);

            moveCamera(new LatLng(45.4761217, 9.2318783), 15f);
            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(45.4761217, 9.2318783))
                    .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                    .draggable(true)
            );

        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentPlayerDetailsBinding.inflate(inflater, container, false);

        setView(binding);

        return binding.getRoot();
    }

    public void setView(FragmentPlayerDetailsBinding binding) {

        binding.buttonLocation.setOnClickListener(v -> {
            moveCamera(new LatLng(45.4761217, 9.2318783), 15f);
        });

        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);

        binding.buttonBack.setOnClickListener(v -> {

            if(getArguments().getString("origin").equals("classification"))
                navController.navigate(R.id.action_playerDetails_to_classificationFragment);
            else if(getArguments().getString("origin").equals("main"))
                navController.navigate(R.id.action_playerDetails_to_mainFragment);
        });

        binding.userName.setText(getArguments().getString("name"));
        binding.userExp.setText(Integer.parseInt(getArguments().getString("expPoints")) % 100 + "/100");
        binding.userLevel.setText("LIVELLO " + getArguments().getString("level"));
        binding.userLife.setText(getArguments().getString("lifePoints"));
        binding.progressExpBar.setProgress(Integer.parseInt(getArguments().getString("expPoints")) % 100);

        String pic = getArguments().getString("profilePicture");

        if(pic != null) {

            try {
                byte[] imageByteArray = Base64.decode(pic, Base64.DEFAULT);

                Glide.with(getContext())
                        .asBitmap()
                        .load(imageByteArray)
                        .into(binding.userImage);
            } catch (Exception e) {
                Toast.makeText(getContext(), "Errore nel caricamento dell'immagine", Toast.LENGTH_SHORT).show();
            }

        }


        if(getArguments().getBoolean("isPositionSharing")) {

            initMap();

        } else {

            binding.map.setVisibility(View.GONE);

            binding.positionSharingOff.setVisibility(View.VISIBLE);

        }

        Log.d(TAG, "setView: " + getArguments().getBoolean("isPositionSharing"));
        Log.d(TAG, "setView: " + getArguments().getDouble("lat"));
        Log.d(TAG, "setView: " + getArguments().getDouble("lon"));
        Log.d(TAG, "setView: " + getArguments().getString("profilePicture"));

    }
    private void initMap() {
        Log.d(TAG, "initMap: initializing map");
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapView);

        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    private void moveCamera(LatLng latLng, float zoom) {

        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude);

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng) // Sets the center of the map to
                .zoom(zoom)                   // Sets the zoom
                .bearing(getView().getMeasuredWidthAndState()) // Sets the orientation of the camera to east
                .tilt(50)    // Sets the tilt of the camera to 50 degrees
                .build();    // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                cameraPosition));

    }

}