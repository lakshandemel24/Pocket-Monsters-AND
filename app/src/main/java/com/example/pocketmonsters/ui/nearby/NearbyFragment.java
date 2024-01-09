package com.example.pocketmonsters.ui.nearby;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.pocketmonsters.R;

public class NearbyFragment extends Fragment {

    double lat;
    double lon;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View viewMonsters = inflater.inflate(R.layout.fragment_nearby, container, false);

        ImageButton back = viewMonsters.findViewById(R.id.buttonBack);

        back.setOnClickListener(v -> {
            getActivity().getSupportFragmentManager().popBackStack();
        });

        lat = getArguments().getDouble("lat");
        lon = getArguments().getDouble("lon");

        return viewMonsters;
    }


}