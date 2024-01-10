package com.example.pocketmonsters.ui.nearby;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.pocketmonsters.R;

public class NearbyFragment extends Fragment {

    String TAG = "Lak-NEarbyFragment";
    SharedPreferences sharedPreferences;
    String sid;
    double lat;
    double lon;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View nearbyView = inflater.inflate(R.layout.fragment_nearby, container, false);

        ImageButton back = nearbyView.findViewById(R.id.buttonBack);

        back.setOnClickListener(v -> {
            getActivity().getSupportFragmentManager().popBackStack();
        });

        return nearbyView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedPreferences = getActivity().getPreferences(getContext().MODE_PRIVATE);
        sid = sharedPreferences.getString("sid", "default");

        lat = getArguments().getDouble("lat");
        lon = getArguments().getDouble("lon");



    }


}