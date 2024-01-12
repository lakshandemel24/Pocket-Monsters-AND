package com.example.pocketmonsters.ui.main;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.example.pocketmonsters.R;
import com.example.pocketmonsters.database.room.user.UserDBHelper;
import com.example.pocketmonsters.database.room.virtualObj.VirtualObjDBHelper;
import com.example.pocketmonsters.databinding.FragmentMainBinding;
import com.example.pocketmonsters.ui.SharedViewModel;
import com.google.android.gms.location.CurrentLocationRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.ByteArrayOutputStream;
import java.util.Map;

public class MainFragment extends Fragment {

    private static final String TAG = "Lak-MainFragment";
    private GoogleMap mMap;
    private boolean locationPermissionGranted = true;
    private FusedLocationProviderClient fusedLocationClient;
    private static final float DEFAULT_ZOOM = 20f;
    private ImageButton btnMyLocation;

    private FragmentMainBinding binding;
    SharedViewModel sharedViewModel;
    MainViewModel viewModel;
    NavController navController;
    VirtualObjDBHelper virtualObjDBHelper;
    UserDBHelper userDBHelper;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        @Override
        public void onMapReady(GoogleMap googleMap) {

            //Toast.makeText(getContext(), "Map is Ready", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "onMapReady: map is ready");
            mMap = googleMap;

            if (locationPermissionGranted) {

                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getContext(), R.raw.map_dark));
                //mMap.setMinZoomPreference(15f);
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setCompassEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mMap.getUiSettings().setMapToolbarEnabled(false);

                btnMyLocation = getView().findViewById(R.id.buttonLocation);
                btnMyLocation.setOnClickListener( v -> {
                    checkLocationSettings();
                });


                getCurrPos(new MainLocationListener() {
                    @Override
                    public void onSuccess(double lat, double lon) {

                        viewModel.addMarkers(mMap, lat, lon, virtualObjDBHelper, sharedViewModel, userDBHelper, getContext());

                    }

                    @Override
                    public void onFailure() {

                        Log.d(TAG, "onFailure: Position");

                    }
                });

                //addMarkers(mMap);

            }

        }
    };
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        virtualObjDBHelper = new VirtualObjDBHelper(getContext());
        userDBHelper = new UserDBHelper(getContext());
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMainBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.navController = Navigation.findNavController(view);

        setNavBtn();

        getLocationPermission();

        sharedViewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                binding.userName.setText(user.getName());
                binding.userLife.setText(String.valueOf(user.getLife()));
                binding.userLevel.setText(String.valueOf(user.getExperience()/100));
                binding.progressExpBar.setProgress(user.getExperience() % 100);

                if(user.getPicture() != null) {
                    /*
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    byte[] imageBytes = baos.toByteArray();
                    imageBytes = Base64.decode(user.getPicture(), Base64.DEFAULT);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                    binding.buttonProfile.setImageBitmap(bitmap);
                     */

                    byte[] imageByteArray = Base64.decode(user.getPicture(), Base64.DEFAULT);

                    Glide.with(getContext())
                            .asBitmap()
                            .load(imageByteArray)
                            .into(binding.buttonProfile);

                }

            }
        });

    }

    public void setNavBtn() {

        Bundle bundle = new Bundle();

        binding.buttonProfile.setOnClickListener(v -> navController.navigate(R.id.action_mainFragment_to_profileFragment));
        binding.buttonClassification.setOnClickListener(v -> navController.navigate(R.id.action_mainFragment_to_classificationFragment));
        binding.buttonNearby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle.putDouble("lat", mMap.getMyLocation().getLatitude());
                bundle.putDouble("lon", mMap.getMyLocation().getLongitude());
                navController.navigate(R.id.action_mainFragment_to_nearbyFragment, bundle);
            }
        });

    }

    private void getLocationPermission() {

        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "onCreate: permission granted");
            initMap();

        } else {
            Log.d(TAG, "onCreate: permission not granted");
            requestPosition();
        }

    }

    public ActivityResultLauncher<String[]> requestPosition() {
        ActivityResultLauncher<String[]> locationPermissionRequest =
                registerForActivityResult(new ActivityResultContracts
                                .RequestMultiplePermissions(),
                        (result) -> onPermissionRequestResult(result)
                );
        locationPermissionRequest.launch(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
        });
        return locationPermissionRequest;
    }

    private void onPermissionRequestResult(Map<String, Boolean> result) {

        Log.d(TAG, "onPermissionRequestResult");
        Boolean fineLocationGranted = result.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false
        );
        Boolean coarseLocationGranted = result.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false
        );
        if (fineLocationGranted != null && fineLocationGranted) {
            // Precise location access granted
            Log.d(TAG, "onPermissionRequestResult: fineLocationGranted");
            initMap();
        } else if (coarseLocationGranted != null && coarseLocationGranted) { // Only approximate location access granted.
            Log.d(TAG, "onPermissionRequestResult: coarseLocationGranted");
            initMap();
        } else {
            // No location access granted.
            Log.d(TAG, "Permessi necessari per accedere alla posizione");
            showErrorText("Permessi necessari per accedere all'applicazione");
        }
    }

    private void moveCamera(LatLng latLng, float zoom) {

        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude);

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng) // Sets the center of the map to
                .zoom(zoom)                   // Sets the zoom
                .bearing(getView().getMeasuredWidthAndState()) // Sets the orientation of the camera to east
                .tilt(50)    // Sets the tilt of the camera to 30 degrees
                .build();    // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                cameraPosition));

    }

    private void initMap() {
        Log.d(TAG, "initMap: initializing map");
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    public void getCurrPos(MainLocationListener mainLocationListener) {

        checkLocationSettings();

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());

        LocationRequest locationRequest =
                new LocationRequest.Builder(10000)
                        .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                        .build();

        LocationCallback locationCallback = new LocationCallback() {
            @Override
            public void onLocationAvailability(@NonNull LocationAvailability locationAvailability) {
                super.onLocationAvailability(locationAvailability);
                Log.d(TAG, "onLocationAvailability " + locationAvailability.isLocationAvailable());
                if (!(locationAvailability.isLocationAvailable())) {
                    Log.d(TAG, "onLocationAvailability: location not available");
                    showErrorText("Attiva posizione!");
                } else {
                    Log.d(TAG, "onLocationAvailability: location available");
                }
            }

            @Override
            public void onLocationResult(com.google.android.gms.location.LocationResult locationResult) {
                super.onLocationResult(locationResult);

                Location currentLocation = (Location) locationResult.getLastLocation();
                //moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),DEFAULT_ZOOM);

                Log.d(TAG, "onLocationResult " + locationResult.getLastLocation().getLatitude());
                Log.d(TAG, "onLocationResult " + locationResult.getLastLocation().getLongitude());
                mainLocationListener.onSuccess(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude());

            }

        };

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
        );
    }

    private void checkLocationSettings() {

        CurrentLocationRequest clr = new CurrentLocationRequest.Builder().setPriority(Priority.PRIORITY_HIGH_ACCURACY).build();

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Task<Location> task = fusedLocationClient.getCurrentLocation(clr, null);
        task.addOnSuccessListener(
                location -> {
                    if (location != null) {
                        Log.d(TAG, "Location: " + location.getLatitude());
                        Log.d(TAG, "Location: " + location.getLongitude());

                        Location currentLocation = (Location) task.getResult();

                        moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                                DEFAULT_ZOOM);

                    } else {
                        Log.d(TAG, "Location: null");
                        showErrorText("Attiva posizione!");
                    }
                }
        );

    }

    private void showErrorText(String msg) {

        new MaterialAlertDialogBuilder(getContext())
                .setTitle("Errore")
                .setMessage("Attiva posizione!")
                .setPositiveButton("Ok", (dialog, which) -> {
                    dialog.dismiss();
                    getActivity().finishAffinity();
                })
                .show();

    }

    private void addMarkers(GoogleMap map) {

        int height = 150;
        int width = 150;
        BitmapDrawable bitmapdraw = (BitmapDrawable)getResources().getDrawable(R.drawable.cup);
        Bitmap b = bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

        map.addMarker(new MarkerOptions()
                .position(new LatLng(45.464211, 9.191383))
                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                .draggable(true)
        );

        map.setOnMarkerClickListener(marker -> {

            new MaterialAlertDialogBuilder(getContext())
                    .setTitle("Cattura")
                    .setMessage("Vuoi catturare questo pokemon?")
                    .setPositiveButton("Si", (dialog, which) -> {
                        dialog.dismiss();
                        Toast.makeText(getContext(), "Pokemon catturato!", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("No", (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .show();

            return false;
        });

    }

}