package com.example.pocketmonsters.ui.main;

import com.example.pocketmonsters.models.VirtualObj;

import java.util.List;

public interface MainVirtualObjsListener {

    public void onSuccess(List<VirtualObj> virtualObjList);

    public void onFailure();

}
