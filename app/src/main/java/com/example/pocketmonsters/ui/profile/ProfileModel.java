package com.example.pocketmonsters.ui.profile;

import com.example.pocketmonsters.models.User;
import com.example.pocketmonsters.models.VirtualObj;

import java.util.List;

public class ProfileModel {

    private User user;
    private List<VirtualObj> virtualObjList;

    public User getUser() {
        return user;
    }

    public List<VirtualObj> getVirtualObj() {
        return virtualObjList;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setVirtualObj(List<VirtualObj> virtualObjList) {
        this.virtualObjList = virtualObjList;
    }

}
