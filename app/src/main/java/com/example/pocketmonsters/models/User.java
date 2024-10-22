package com.example.pocketmonsters.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user")
public class User {

    @PrimaryKey
    private int uid;
    private String sid;
    private String name;
    private double lat;
    private double lon;
    private String time;
    private int life;
    private int experience;;
    private int weapon;
    private int armor;
    private int amulet;
    private String picture;
    private int profileversion;
    private boolean positionshare;

    public User(String sid, int uid, String name, double lat, double lon, String time, int life, int experience, int weapon, int armor, int amulet, String picture, int profileversion, boolean positionshare) {
        this.sid = sid;
        this.uid = uid;
        this.name = name;
        this.lat = lat;
        this.lon = lon;
        this.time = time;
        this.life = life;
        this.experience = experience;
        this.weapon = weapon;
        this.armor = armor;
        this.amulet = amulet;
        this.picture = picture;
        this.profileversion = profileversion;
        this.positionshare = positionshare;
    }

    public String getSid() {
        return sid;
    }

    public int getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public String getTime() {
        return time;
    }

    public int getLife() {
        return life;
    }

    public int getExperience() {
        return experience;
    }

    public int getWeapon() {
        return weapon;
    }

    public int getArmor() {
        return armor;
    }

    public int getAmulet() {
        return amulet;
    }

    public String getPicture() {
        return picture;
    }

    public int getProfileversion() {
        return profileversion;
    }

    public boolean isPositionshare() {
        return positionshare;
    }

    public int[] getArtifacts() {
        int[] artifacts = new int[3];
        artifacts[0] = weapon;
        artifacts[1] = armor;
        artifacts[2] = amulet;
        return artifacts;
    }

    public void setName(String name) {
        this.name = name;
    }
}
