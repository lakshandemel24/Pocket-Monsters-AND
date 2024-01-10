package com.example.pocketmonsters.ui.classification;

import android.widget.Adapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.lifecycle.ViewModel;

import com.example.pocketmonsters.models.Player;

import java.util.ArrayList;
import java.util.List;

public class ClassificationViewModel extends ViewModel {

    private final ClassificationModel classificationModel;
    public ClassificationViewModel() {
        super();
        classificationModel = new ClassificationModel();
    }

    public void loadClassification(String sid, ClassificationAdapter adapter, ProgressBar progressBar, TextView errorText) {

        ClassificationRepository classificationRepository = new ClassificationRepository(sid);

        classificationRepository.getPlayers(sid, new ClassificationListener() {
            @Override
            public void onSuccess(List<Player> players) {
                classificationModel.appendPlayers(players);
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(ProgressBar.INVISIBLE);
            }

            @Override
            public void onFailure() {
                classificationModel.appendPlayers(new ArrayList<>());
                progressBar.setVisibility(ProgressBar.INVISIBLE);
                errorText.setText("Error loading classification, try again later...");
            }
        });

    }

    public int getPlayersCount() {
        return classificationModel.getPlayersCount();
    }
    public Player getPlayers(int position) {
        return classificationModel.getPlayers(position);
    }

}
