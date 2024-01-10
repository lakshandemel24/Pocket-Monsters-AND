package com.example.pocketmonsters.ui.nearby;

import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.lifecycle.ViewModel;

import com.example.pocketmonsters.models.VirtualObj;
import java.util.ArrayList;
import java.util.List;

public class NearbyViewModel extends ViewModel {

    public List<VirtualObj> virtualObjList;
    private final NearbyModel nearbyModel;

    public NearbyViewModel() {
        super();
        nearbyModel = new NearbyModel();
    }

    //public void loadNearbyVirtualObj(String sid, NearbyObjAdapter adapter, ProgressBar progressBar, TextView errorText){}

}
