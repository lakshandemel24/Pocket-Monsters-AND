package com.example.pocketmonsters.ui.classification;

import androidx.lifecycle.ViewModel;

import com.example.pocketmonsters.models.Player;

import java.util.List;

public class ClassificationViewModel extends ViewModel {

    private final ClassificationModel classificationModel;
    public ClassificationViewModel() {
        super();
        classificationModel = new ClassificationModel();
    }

    public void appendPlayers(List<Player> pls) {
        classificationModel.appendPlayers(pls);
    }

    public void getRenkingFromModel () {
        classificationModel.getRankingUsers();
    }

    public int getPlayersCount() {
        return classificationModel.getPlayersCount();
    }
    public Player getPlayers(int position) {
        return classificationModel.getPlayers(position);
    }

}
