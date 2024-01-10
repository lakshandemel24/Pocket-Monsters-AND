package com.example.pocketmonsters.ui.nearby;

import com.example.pocketmonsters.models.Player;
import com.example.pocketmonsters.models.VirtualObj;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class NearbyModel {

    private final List<VirtualObj> virtualObjList = new ArrayList<>();

    public int getVirtualObjCount() {
        return virtualObjList.size();
    }

    public VirtualObj getVirtualObj(int position) {
        return virtualObjList.get(position);
    }

    public void appendPlayers(List<VirtualObj> p) {
        virtualObjList.addAll(p);
    }


}
