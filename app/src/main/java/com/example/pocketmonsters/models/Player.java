package com.example.pocketmonsters.models;

import androidx.annotation.NonNull;

public class Player {

    private String name;
    private int expPoits;
    private int lifePoints;
    private int level;
    private String profilePicture;

    public Player(String name, int expPoits, int lifePoints, String profilePicture) {
        this.name = name;
        this.expPoits = expPoits;
        this.lifePoints = lifePoints;
        this.level = expPoits / 100;
        this.profilePicture = profilePicture;
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

    public String getProfilePicture() {
        return profilePicture;
    }

}
