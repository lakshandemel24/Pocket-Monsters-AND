package com.example.pocketmonsters.ui.nearby;

import com.example.pocketmonsters.models.VirtualObj;

import java.util.List;

public interface NearbyListener {

    public void onSuccess(List<VirtualObj> virtualObjList);
    public void onFailure();

}
