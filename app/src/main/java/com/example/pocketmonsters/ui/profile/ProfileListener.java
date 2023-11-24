package com.example.pocketmonsters.ui.profile;

import com.example.pocketmonsters.models.VirtualObj;
import java.util.List;

public interface ProfileListener {

    public void onSuccess(List<VirtualObj> list);

    public void onFailure();

}
