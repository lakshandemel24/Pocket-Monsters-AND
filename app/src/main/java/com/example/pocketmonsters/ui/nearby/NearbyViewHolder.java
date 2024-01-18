package com.example.pocketmonsters.ui.nearby;

import android.app.Dialog;
import android.content.Context;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pocketmonsters.R;
import com.example.pocketmonsters.models.VirtualObj;

public class NearbyViewHolder extends RecyclerView.ViewHolder{

    private TextView name;
    private TextView levelView;
    private int level;
    private ImageView image;
    int id;
    String type;
    int levelObj;

    public NearbyViewHolder(@NonNull View itemView, NearbyViewModel viewModel) {
        super(itemView);

        name = itemView.findViewById(R.id.name);
        levelView = itemView.findViewById(R.id.level);
        image = itemView.findViewById(R.id.image);

        itemView.setOnClickListener(v -> {

            Dialog builder = new Dialog(itemView.getContext());
            builder.setContentView(R.layout.dialog_box);
            builder.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            builder.getWindow().setBackgroundDrawableResource(R.drawable.custom_dialog_bg);

            TextView name = builder.findViewById(R.id.nameD);
            TextView expPoints = builder.findViewById(R.id.expPointsD);
            TextView lifePoints = builder.findViewById(R.id.lifePointsD);
            ImageView profilePicture = builder.findViewById(R.id.imageView);
            TextView damage = builder.findViewById(R.id.damage);
            Button active = builder.findViewById(R.id.close);
            active.setText("activate");

            name.setText(name.getText());
            expPoints.setText(type);
            lifePoints.setText("" + levelView.getText());

            if(image.getDrawable() != null) {
                profilePicture.setImageDrawable(image.getDrawable());
            }

            if(type.equals("monster")) {
                active.setText("fight");
                viewModel.showDamage(damage, level);
            }

            active.setOnClickListener(v1 -> {
                builder.dismiss();
                viewModel.activateVirtualObj(id, levelObj, type, itemView, new NearbyActiteListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(itemView.getContext(), "Activated", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure() {
                        Toast.makeText(itemView.getContext(), "Error activating", Toast.LENGTH_SHORT).show();
                    }
                });

            });

            builder.show();

        });

    }

    public void bind(VirtualObj virtualObj) {

        Log.d("NearbyViewHolder", "bind: " + virtualObj.getName() + " " + virtualObj.getLevel() + " " + virtualObj.getType() + " " + virtualObj.getId() + " " + virtualObj.getLevel());

        name.setText(virtualObj.getName());
        levelView.setText("Lv: " + virtualObj.getLevel());
        type = virtualObj.getType();
        id = virtualObj.getId();
        levelObj = virtualObj.getLevel();
        level = virtualObj.getLevel();

        if(virtualObj.getImage() != null) {

            byte[] imageByteArray = Base64.decode(virtualObj.getImage(), Base64.DEFAULT);

            Glide.with(itemView.getContext())
                    .asBitmap()
                    .load(imageByteArray)
                    .into(image);
        } else {

            if(type.equals("monster")) {

                Glide.with(itemView.getContext())
                        .asBitmap()
                        .load(R.drawable.monster)
                        .into(image);

            } else if(type.equals("candy")) {

                Glide.with(itemView.getContext())
                        .asBitmap()
                        .load(R.drawable.candy)
                        .into(image);

            } else if (type.equals("weapon") || type.equals("armor") || type.equals("amulet")) {

                Glide.with(itemView.getContext())
                        .asBitmap()
                        .load(R.drawable.artifact)
                        .into(image);

            }


        }


    }


}
