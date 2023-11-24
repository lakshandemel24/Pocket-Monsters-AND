package com.example.pocketmonsters.models;

public class VirtualObj {

    int id;
    String name;
    String type;
    int level;
    String image;

    public VirtualObj(int id, String name, String type, int level, String image) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.level = level;
        this.image = image;
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

}
