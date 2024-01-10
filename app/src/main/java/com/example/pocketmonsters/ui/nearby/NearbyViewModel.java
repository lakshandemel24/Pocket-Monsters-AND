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

    public void loadNearbyVirtualObj(String sid, double lat, double lon, NearbyAdapter adapter, ProgressBar progressBar, TextView errorText){

        NearbyRepository nearbyRepository = new NearbyRepository(sid);

        nearbyRepository.getVirtualObj(sid, lat, lon, new NearbyListener() {
            @Override
            public void onSuccess(List<VirtualObj> virtualObjList) {
                nearbyModel.appendVirtualObjlist(virtualObjList);
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(ProgressBar.INVISIBLE);
            }

            @Override
            public void onFailure(){
                nearbyModel.appendVirtualObjlist(new ArrayList<>());
                progressBar.setVisibility(ProgressBar.INVISIBLE);
                errorText.setText("Error loading classification, try again later...");
            }

        });

    }

    public int getVirtualObjCount() {
        return  nearbyModel.getVirtualObjCount();
    }

    public VirtualObj getVirtualObj(int position) {
        return nearbyModel.getVirtualObj(position);
    }

}
