package com.example.pocketmonsters.ui.main;

public interface MainLocationListener {

    public void onSuccess(double lat, double lon);

    public void onFailure();

}
