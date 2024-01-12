package com.example.pocketmonsters.models;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "virtualObj")
public class VirtualObj {

    @PrimaryKey
    int id;
    String name;
    String type;
    int level;
    String image;
    public double lat;
    public double lon;

    public VirtualObj(int id, String name, String type, int level, String image, double lat, double lon) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.level = level;
        this.image = image;
        this.lat = lat;
        this.lon = lon;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public int getLevel() {
        return level;
    }

    public String getImage() {
        return image;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

}
