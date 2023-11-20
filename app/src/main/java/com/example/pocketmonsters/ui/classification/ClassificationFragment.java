package com.example.pocketmonsters.ui.classification;

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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.pocketmonsters.R;
import com.example.pocketmonsters.api.ResponseUsersId;
import com.example.pocketmonsters.api.ResponseUsersRanking;
import com.example.pocketmonsters.api.RetrofitProvider;
import com.example.pocketmonsters.models.Player;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class ClassificationFragment extends Fragment {

    String sid;
    SharedPreferences sharedPreferences;
    ClassificationViewModel viewModel;
    ClassificationAdapter adapter;
    ProgressBar progressBar;
    TextView errorText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View classificationView = inflater.inflate(R.layout.fragment_classification, container, false);

        ImageButton back = classificationView.findViewById(R.id.buttonBack);

        back.setOnClickListener(v -> {
            getActivity().getSupportFragmentManager().popBackStack();
        });

        return classificationView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //sharedPreferences = getActivity().getPreferences(getContext().MODE_PRIVATE);
        //sid = sharedPreferences.getString("sid", "default");
        sid = "ZhyeEJ5lbtgFJ5BFBTvi";
        viewModel = new ViewModelProvider(this).get(ClassificationViewModel.class);
        progressBar = view.findViewById(R.id.progressBar);
        errorText = view.findViewById(R.id.Error);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ClassificationAdapter(getContext(), viewModel);
        recyclerView.setAdapter(adapter);

        viewModel.loadClassification(sid, adapter, progressBar, errorText);

    }


}