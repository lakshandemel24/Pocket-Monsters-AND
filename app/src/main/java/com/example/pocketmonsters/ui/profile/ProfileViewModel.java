package com.example.pocketmonsters.ui.profile;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.lifecycle.ViewModel;

import com.example.pocketmonsters.api.RetrofitProvider;
import com.example.pocketmonsters.database.room.user.UserDBHelper;
import com.example.pocketmonsters.models.User;
import com.example.pocketmonsters.models.VirtualObj;
import com.example.pocketmonsters.ui.SharedViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.JsonElement;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class ProfileViewModel extends ViewModel {

    private final ProfileModel profileModel;

    RetrofitProvider retrofitProvider = new RetrofitProvider();

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

        amuletProgress.setVisibility(ProgressBar.INVISIBLE);
        armorProgress.setVisibility(ProgressBar.INVISIBLE);
        weaponProgress.setVisibility(ProgressBar.INVISIBLE);

        profileRepository.getUserArtifacts(uidObj, sid, new ProfileListener() {
            @Override
            public void onSuccess(List<VirtualObj> list) {
                profileModel.setVirtualObj(list);

                for (VirtualObj virtualObj : list) {

                     if (virtualObj.getType().equals("weapon")) {
                         if(virtualObj.getImage() != null) {
                             ByteArrayOutputStream baos = new ByteArrayOutputStream();
                             byte[] imageBytes = baos.toByteArray();
                             imageBytes = Base64.decode(virtualObj.getImage(), Base64.DEFAULT);
                             Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                             weapon.setImageBitmap(bitmap);
                         } else {
                                weaponName.setText(virtualObj.getName());
                             weapon.setImageBitmap(null);
                         }

                         weapon.setOnClickListener(v -> {
                             showDialog(virtualObj, context, "The damage suffered by the monsters will be reduced by " + String.valueOf(virtualObj.getLevel()) + "%");
                         });

                     } else if (virtualObj.getType().equals("armor")) {
                         if(virtualObj.getImage() != null) {
                             ByteArrayOutputStream baos = new ByteArrayOutputStream();
                             byte[] imageBytes = baos.toByteArray();
                             imageBytes = Base64.decode(virtualObj.getImage(), Base64.DEFAULT);
                             Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                             armor.setImageBitmap(bitmap);
                         } else {
                             armorName.setText(virtualObj.getName());
                             armor.setImageBitmap(null);
                         }

                         armor.setOnClickListener(v -> {
                             showDialog(virtualObj, context, "The maximum number of life points has been increased to " + String.valueOf(virtualObj.getLevel() + 100));
                         });

                     } else if (virtualObj.getType().equals("amulet")) {
                         if(virtualObj.getImage() != null) {
                             ByteArrayOutputStream baos = new ByteArrayOutputStream();
                             byte[] imageBytes = baos.toByteArray();
                             imageBytes = Base64.decode(virtualObj.getImage(), Base64.DEFAULT);
                             Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                             amulet.setImageBitmap(bitmap);
                         } else {
                             amuletName.setText(virtualObj.getName());
                             amulet.setImageBitmap(null);
                         }

                         amulet.setOnClickListener(v -> {
                             showDialog(virtualObj, context, "The virtual objects are visible up to " + String.valueOf(virtualObj.getLevel() + 100) + " meters");
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

    public void setPosSharing(boolean posSharing, String sid, int uid) {

        Call<JsonElement> editUserCall = retrofitProvider.getApiInterface().editUSer(uid, sid, null, null, posSharing);
        editUserCall.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, retrofit2.Response<JsonElement> response) {
                if (!response.isSuccessful()) {
                    Log.d("Lak-ProfileViewModel", "Error: " + response.code());
                    return;
                }
                Log.d("Lak-ProfileViewModel", "Position Sharing: " + posSharing);
            }
            @Override
            public void onFailure(Call call, Throwable t) {
                Log.d("Lak-ProfileViewModel", "Error: " + t.getMessage());
            }
        });

    }

    public void changeUserName(String sid, int uid, String name, SharedViewModel sharedViewModel, UserDBHelper userDBHelper) {

        Call<JsonElement> editUserCall = retrofitProvider.getApiInterface().editUSer(uid, sid, name, null, null);
        editUserCall.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, retrofit2.Response<JsonElement> response) {
                if (!response.isSuccessful()) {
                    Log.d("Lak-ProfileViewModel", "Error: " + response.code());
                    return;
                }
                Log.d("Lak-ProfileViewModel", "Name modified");
                sharedViewModel.setUser(new User(sharedViewModel.getUser().getValue().getSid(), sharedViewModel.getUser().getValue().getUid(), name, sharedViewModel.getUser().getValue().getLat(), sharedViewModel.getUser().getValue().getLon(), sharedViewModel.getUser().getValue().getTime(), sharedViewModel.getUser().getValue().getLife(), sharedViewModel.getUser().getValue().getExperience(), sharedViewModel.getUser().getValue().getWeapon(), sharedViewModel.getUser().getValue().getArmor(), sharedViewModel.getUser().getValue().getAmulet(), sharedViewModel.getUser().getValue().getPicture(), sharedViewModel.getUser().getValue().getProfileversion(), sharedViewModel.getUser().getValue().isPositionshare()));
                userDBHelper.clearUsers();
                userDBHelper.insertUser(new User(sharedViewModel.getUser().getValue().getSid(), sharedViewModel.getUser().getValue().getUid(), name, sharedViewModel.getUser().getValue().getLat(), sharedViewModel.getUser().getValue().getLon(), sharedViewModel.getUser().getValue().getTime(), sharedViewModel.getUser().getValue().getLife(), sharedViewModel.getUser().getValue().getExperience(), sharedViewModel.getUser().getValue().getWeapon(), sharedViewModel.getUser().getValue().getArmor(), sharedViewModel.getUser().getValue().getAmulet(), sharedViewModel.getUser().getValue().getPicture(), sharedViewModel.getUser().getValue().getProfileversion(), sharedViewModel.getUser().getValue().isPositionshare()));
            }
            @Override
            public void onFailure(Call call, Throwable t) {
                Log.d("Lak-ProfileViewModel", "Error: " + t.getMessage());
            }
        });

    }

    public void changeUserImage(Uri o, SharedViewModel sharedViewModel, Context context, UserDBHelper userDBHelper) {

        String img = uriToBase64(context, o);

        Call<JsonElement> editUserCall = retrofitProvider.getApiInterface().editUSer(sharedViewModel.getUser().getValue().getUid(), sharedViewModel.getUser().getValue().getSid(), null, img, null);
        editUserCall.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, retrofit2.Response<JsonElement> response) {
                if (!response.isSuccessful()) {
                    Log.d("Lak-ProfileViewModel", "Error: " + response.code());
                    return;
                }
                Log.d("Lak-ProfileViewModel", "Profile picture modified");
                sharedViewModel.setUser(new User(sharedViewModel.getUser().getValue().getSid(), sharedViewModel.getUser().getValue().getUid(), sharedViewModel.getUser().getValue().getName(), sharedViewModel.getUser().getValue().getLat(), sharedViewModel.getUser().getValue().getLon(), sharedViewModel.getUser().getValue().getTime(), sharedViewModel.getUser().getValue().getLife(), sharedViewModel.getUser().getValue().getExperience(), sharedViewModel.getUser().getValue().getWeapon(), sharedViewModel.getUser().getValue().getArmor(), sharedViewModel.getUser().getValue().getAmulet(), img, sharedViewModel.getUser().getValue().getProfileversion(), sharedViewModel.getUser().getValue().isPositionshare()));
                userDBHelper.clearUsers();
                userDBHelper.insertUser(new User(sharedViewModel.getUser().getValue().getSid(), sharedViewModel.getUser().getValue().getUid(), sharedViewModel.getUser().getValue().getName(), sharedViewModel.getUser().getValue().getLat(), sharedViewModel.getUser().getValue().getLon(), sharedViewModel.getUser().getValue().getTime(), sharedViewModel.getUser().getValue().getLife(), sharedViewModel.getUser().getValue().getExperience(), sharedViewModel.getUser().getValue().getWeapon(), sharedViewModel.getUser().getValue().getArmor(), sharedViewModel.getUser().getValue().getAmulet(), img, sharedViewModel.getUser().getValue().getProfileversion(), sharedViewModel.getUser().getValue().isPositionshare()));
            }
            @Override
            public void onFailure(Call call, Throwable t) {
                Log.d("Lak-ProfileViewModel", "Error: " + t.getMessage());
            }
        });

    }

    public static String uriToBase64(Context context, Uri uri) {
        InputStream inputStream = null;
        try {
            inputStream = context.getContentResolver().openInputStream(uri);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }

            byte[] byteArray = byteArrayOutputStream.toByteArray();
            return Base64.encodeToString(byteArray, Base64.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private void showDialog(VirtualObj virtualObj, Context context, String message) {

        new MaterialAlertDialogBuilder(context)
                .setTitle(virtualObj.getName())
                .setMessage("Livelo: " + String.valueOf(virtualObj.getLevel()) + "\n" + message)
                .setPositiveButton("Ok", (dialog, which) -> {
                    dialog.dismiss();
                })
                .show();
    }

}
