package com.example.pocketmonsters.database.room.virtualObj;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.pocketmonsters.models.VirtualObj;

@Database(entities = {VirtualObj.class}, version = 1, exportSchema = false)
public abstract class VirtualObjRepository extends RoomDatabase {
    public abstract VirtualObjDao virtualObjDao();
}
