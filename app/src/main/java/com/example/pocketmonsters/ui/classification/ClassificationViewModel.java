package com.example.pocketmonsters.ui.classification;

import androidx.lifecycle.ViewModel;

import com.example.pocketmonsters.models.Player;

public class ClassificationViewModel extends ViewModel {

    private ClassificationModel classificationModel;
    public ClassificationViewModel() {
        super();
        classificationModel = new ClassificationModel();
        classificationModel.simulateLoadDataFromDB();
    }
    public int getPlayersCount() {
        return classificationModel.getPlayersCount();
    }
    public Player getPlayers(int position) {
        return classificationModel.getPlayers(position);
    }

}
