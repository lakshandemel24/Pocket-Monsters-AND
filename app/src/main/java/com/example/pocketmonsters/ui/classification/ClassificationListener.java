package com.example.pocketmonsters.ui.classification;

import com.example.pocketmonsters.models.Player;

import java.util.List;

public interface ClassificationListener {

    public void onSuccess(List<Player> players);
    public void onFailure();

}
