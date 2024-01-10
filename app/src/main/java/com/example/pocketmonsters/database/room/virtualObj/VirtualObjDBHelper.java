package com.example.pocketmonsters.database.room.virtualObj;

import android.content.Context;

import androidx.room.Room;

import com.example.pocketmonsters.models.VirtualObj;

public class VirtualObjDBHelper {

    private final VirtualObjRepository appDatabase;

    public VirtualObjDBHelper(Context context) {

        appDatabase = Room.databaseBuilder(context, VirtualObjRepository.class, "VirtualObj.db")
                .allowMainThreadQueries()
                .build();

    }

    public int count() {
        return appDatabase.virtualObjDao().loadAll().size();
    }

    public void insertUser(VirtualObj virtualObj) {
        appDatabase.virtualObjDao().insert(virtualObj);
    }

    public VirtualObj loadById(int id) {
        return appDatabase.virtualObjDao().loadById(id);
    }

}
