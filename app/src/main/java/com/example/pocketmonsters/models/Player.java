package com.example.pocketmonsters.models;

import androidx.annotation.NonNull;

public class Player {

    private String name;
    private int expPoits;
    private int lifePoints;
    private int level;

    public Player(String name, int expPoits, int lifePoints) {
        this.name = name;
        this.expPoits = expPoits;
        this.lifePoints = lifePoints;
        this.level = expPoits / 100;
    }

    public String getName() {
        return name;
    }

    public int getExpPoits() {
        return expPoits;
    }

    public int getLifePoints() {
        return lifePoints;
    }

    public int getLevel() {
        return level;
    }

}
