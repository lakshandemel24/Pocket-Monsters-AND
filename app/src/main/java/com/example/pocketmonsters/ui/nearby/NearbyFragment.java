package com.example.pocketmonsters.ui.nearby;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.pocketmonsters.R;
import com.example.pocketmonsters.ui.SharedViewModel;

public class NearbyFragment extends Fragment {

    String TAG = "Lak-NearbyFragment";
    SharedPreferences sharedPreferences;
    String sid;
    double lat;
    double lon;
    ProgressBar progressBar;
    TextView errorText;
    NearbyAdapter adapter;
    NearbyViewModel viewModel;
    SharedViewModel sharedViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
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

        progressBar = view.findViewById(R.id.progressBar);
        errorText = view.findViewById(R.id.Error);

        sharedPreferences = getActivity().getPreferences(getContext().MODE_PRIVATE);
        sid = sharedPreferences.getString("sid", "ePzuGF55G6Z5ZRj6Vj7J");
        viewModel = new ViewModelProvider(this).get(NearbyViewModel.class);
        lat = getArguments().getDouble("lat");
        lon = getArguments().getDouble("lon");

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new NearbyAdapter(getContext(), viewModel);
        recyclerView.setAdapter(adapter);

        viewModel.loadNearbyVirtualObj(sid, lat, lon, adapter, progressBar, errorText, sharedViewModel, getContext());

    }


}