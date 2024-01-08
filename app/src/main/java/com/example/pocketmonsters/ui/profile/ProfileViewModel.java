package com.example.pocketmonsters.ui.profile;

import static android.opengl.ETC1.encodeImage;
import static com.google.common.io.ByteStreams.readBytes;

import android.app.Dialog;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.lifecycle.ViewModel;

import com.example.pocketmonsters.R;
import com.example.pocketmonsters.api.RetrofitProvider;
import com.example.pocketmonsters.database.room.UserDBHelper;
import com.example.pocketmonsters.models.User;
import com.example.pocketmonsters.models.VirtualObj;
import com.example.pocketmonsters.ui.SharedViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.JsonElement;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import kotlinx.coroutines.flow.SharingStarted;
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

        weaponProgress.setVisibility(ProgressBar.INVISIBLE);
        armorProgress.setVisibility(ProgressBar.INVISIBLE);
        amuletProgress.setVisibility(ProgressBar.INVISIBLE);

        profileRepository.getUserArtifacts(uidObj, sid, new ProfileListener() {
            @Override
            public void onSuccess(List<VirtualObj> list) {
                profileModel.setVirtualObj(list);

                for (VirtualObj virtualObj : list) {

                     if (virtualObj.getType().equals("weapon")) {
                         weaponProgress.setVisibility(ProgressBar.INVISIBLE);
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
                             weapon.setImageBitmap(null);
                         }

                         weapon.setOnClickListener(v -> {
                             showDialog(virtualObj, context);
                         });

                     } else if (virtualObj.getType().equals("armor")) {
                         armorProgress.setVisibility(ProgressBar.INVISIBLE);
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
                             armor.setImageBitmap(null);
                         }

                         armor.setOnClickListener(v -> {
                             showDialog(virtualObj, context);
                         });

                     } else if (virtualObj.getType().equals("amulet")) {
                         amuletProgress.setVisibility(ProgressBar.INVISIBLE);
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
                             amulet.setImageBitmap(null);
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

    public void setPosSharing(boolean posSharing, String sid, int uid, Context context) {

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

    public void changeUserName(String sid, int uid, String name, boolean posSharing) {

        Call<JsonElement> editUserCall = retrofitProvider.getApiInterface().editUSer(uid, sid, name, null, null);
        editUserCall.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, retrofit2.Response<JsonElement> response) {
                if (!response.isSuccessful()) {
                    Log.d("Lak-ProfileViewModel", "Error: " + response.code());
                    return;
                }
                Log.d("Lak-ProfileViewModel", "Name modified");

            }
            @Override
            public void onFailure(Call call, Throwable t) {
                Log.d("Lak-ProfileViewModel", "Error: " + t.getMessage());
            }
        });

    }

    public void changeUserImage(Uri o, SharedViewModel sharedViewModel, Context context) {

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

    private String encodeImage(Bitmap bm)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encImage;
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
