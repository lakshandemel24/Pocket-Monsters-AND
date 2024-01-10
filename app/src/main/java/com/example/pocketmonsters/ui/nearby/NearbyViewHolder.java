package com.example.pocketmonsters.ui.nearby;

import android.app.Dialog;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pocketmonsters.R;
import com.example.pocketmonsters.databinding.FragmentNearbyBinding;
import com.example.pocketmonsters.databinding.SingleRowBinding;
import com.example.pocketmonsters.databinding.SingleRowNearbyBinding;
import com.example.pocketmonsters.models.VirtualObj;

public class NearbyViewHolder extends RecyclerView.ViewHolder{

    private SingleRowNearbyBinding binding;
    String type;

    public NearbyViewHolder(@NonNull View itemView) {
        super(itemView);

        itemView.setOnClickListener(v -> {

            Dialog builder = new Dialog(itemView.getContext());
            builder.setContentView(R.layout.dialog_box);
            builder.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            builder.getWindow().setBackgroundDrawableResource(R.drawable.custom_dialog_bg);

            TextView name = builder.findViewById(R.id.nameD);
            TextView expPoints = builder.findViewById(R.id.expPointsD);
            TextView lifePoints = builder.findViewById(R.id.lifePointsD);
            ImageView profilePicture = builder.findViewById(R.id.imageView);
            Button close = builder.findViewById(R.id.close);

            name.setText(binding.name.getText());
            expPoints.setText(type);
            lifePoints.setText("Lv: " +binding.level.getText());

            if(binding.image.getDrawable() != null) {
                profilePicture.setImageDrawable(binding.image.getDrawable());
            }

            close.setOnClickListener(v1 -> builder.dismiss());

            builder.show();

        });

    }

    public void bind(VirtualObj virtualObj) {

        binding.name.setText(virtualObj.getName());
        binding.level.setText("Level: " + virtualObj.getLevel());
        type = virtualObj.getType();

        if(virtualObj.getImage() != null) {
            byte[] imageByteArray = Base64.decode(virtualObj.getImage(), Base64.DEFAULT);

            Glide.with(itemView.getContext())
                    .asBitmap()
                    .load(imageByteArray)
                    .into(binding.image);
        }


    }


}
