package com.example.pocketmonsters.ui.profile;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.lifecycle.ViewModel;

import com.example.pocketmonsters.R;
import com.example.pocketmonsters.models.User;
import com.example.pocketmonsters.models.VirtualObj;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class ProfileViewModel extends ViewModel {

    private final ProfileModel profileModel;

    public ProfileViewModel() {
        super();
        profileModel = new ProfileModel();
    }

    public User getUser() {
        return profileModel.getUser();
    }

    public List<VirtualObj> getVirtualObj() {
        return profileModel.getVirtualObj();
    }

    public void setUserArtifacts(int[] uidObj, String sid, ImageButton weapon, ImageButton armor, ImageButton amulet, ProgressBar weaponProgress, ProgressBar armorProgress, ProgressBar amuletProgress, TextView weaponName, TextView armorName, TextView amuletName, Context context) {

        ProfileRepository profileRepository = new ProfileRepository();

        profileRepository.getUserArtifacts(uidObj, sid, new ProfileListener() {
            @Override
            public void onSuccess(List<VirtualObj> list) {
                profileModel.setVirtualObj(list);

                for (VirtualObj virtualObj : list) {
                     if (virtualObj.getType().equals("weapon")) {
                         if(virtualObj.getImage() != null) {
                             weaponProgress.setVisibility(ProgressBar.INVISIBLE);
                             ByteArrayOutputStream baos = new ByteArrayOutputStream();
                             byte[] imageBytes = baos.toByteArray();
                             imageBytes = Base64.decode(virtualObj.getImage(), Base64.DEFAULT);
                             Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                             weapon.setImageBitmap(bitmap);
                         } else {
                                weaponProgress.setVisibility(ProgressBar.INVISIBLE);
                                weaponName.setText(virtualObj.getName());
                         }

                         weapon.setOnClickListener(v -> {
                             showDialog(virtualObj, context);
                         });

                     } else if (virtualObj.getType().equals("armor")) {
                         if(virtualObj.getImage() != null) {
                             armorProgress.setVisibility(ProgressBar.INVISIBLE);
                             ByteArrayOutputStream baos = new ByteArrayOutputStream();
                             byte[] imageBytes = baos.toByteArray();
                             imageBytes = Base64.decode(virtualObj.getImage(), Base64.DEFAULT);
                             Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                             armor.setImageBitmap(bitmap);
                         } else {
                             armorProgress.setVisibility(ProgressBar.INVISIBLE);
                             armorName.setText(virtualObj.getName());
                         }

                         armor.setOnClickListener(v -> {
                             showDialog(virtualObj, context);
                         });

                     } else if (virtualObj.getType().equals("amulet")) {
                         if(virtualObj.getImage() != null) {
                             amuletProgress.setVisibility(ProgressBar.INVISIBLE);
                             ByteArrayOutputStream baos = new ByteArrayOutputStream();
                             byte[] imageBytes = baos.toByteArray();
                             imageBytes = Base64.decode(virtualObj.getImage(), Base64.DEFAULT);
                             Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                             amulet.setImageBitmap(bitmap);
                         } else {
                             amuletProgress.setVisibility(ProgressBar.INVISIBLE);
                             amuletName.setText(virtualObj.getName());
                         }

                         amulet.setOnClickListener(v -> {
                             showDialog(virtualObj, context);
                         });

                     }

                }

            }

            @Override
            public void onFailure() {
                profileModel.setVirtualObj(null);
            }
        });

    }

    private void showDialog(VirtualObj virtualObj, Context context) {

        new MaterialAlertDialogBuilder(context)
                .setTitle(virtualObj.getName())
                .setMessage("Livelo: " + String.valueOf(virtualObj.getLevel()))
                .setPositiveButton("Ok", (dialog, which) -> {
                    dialog.dismiss();
                })
                .show();
    }

}
