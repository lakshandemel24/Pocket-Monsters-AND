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

    RetrofitProvider retrofitProvider = new RetrofitProvider();

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

        sharedPreferences = getActivity().getPreferences(getContext().MODE_PRIVATE);
        sid = sharedPreferences.getString("sid", "default");
        viewModel = new ViewModelProvider(this).get(ClassificationViewModel.class);
        progressBar = view.findViewById(R.id.progressBar);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ClassificationAdapter(getContext(), viewModel);
        recyclerView.setAdapter(adapter);

        getAPi();

    }

    public void getAPi() {

        List<Player> players = new ArrayList<>();

        Call<List<ResponseUsersRanking>> call = retrofitProvider.getApiInterface().getRanking(sid);
        call.enqueue(new Callback<List<ResponseUsersRanking>>() {
            @Override
            public void onResponse(Call<List<ResponseUsersRanking>> call, retrofit2.Response<List<ResponseUsersRanking>> response) {
                if (!response.isSuccessful()) {
                    Log.d("Lak", "Error: " + response.code());
                    return;
                }
                List<ResponseUsersRanking> result = response.body();
                for (ResponseUsersRanking obj : result) {

                    Call<ResponseUsersId> call2 = retrofitProvider.getApiInterface().getUser(obj.uid, sid);
                    call2.enqueue(new Callback<ResponseUsersId>() {
                        @Override
                        public void onResponse(Call<ResponseUsersId> call, retrofit2.Response<ResponseUsersId> response) {
                            if (!response.isSuccessful()) {
                                Log.d("Lak", "Error: " + response.code());
                                return;
                            }
                            ResponseUsersId resultUs = response.body();
                            players.add(new Player(resultUs.name, resultUs.experience, resultUs.life));
                            if(players.size() == 20) {
                                progressBar.setVisibility(View.GONE);
                                viewModel.appendPlayers(players);
                                adapter.notifyDataSetChanged();
                            }
                        }
                        @Override
                        public void onFailure(Call<ResponseUsersId> call, Throwable t) {
                            Log.d("Lak", "Error: " + t.getMessage());
                        }
                    });
                }
            }
            @Override
            public void onFailure(Call<List<ResponseUsersRanking>> call, Throwable t) {
                Log.d("Api", "Error: " + t.getMessage());
            }
        });
    }

}