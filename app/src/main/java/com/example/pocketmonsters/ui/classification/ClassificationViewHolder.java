package com.example.pocketmonsters.ui.classification;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pocketmonsters.models.Player;
import com.example.pocketmonsters.R;

public class ClassificationViewHolder extends RecyclerView.ViewHolder {

    private TextView playerNameTextView;
    private TextView playerExpPointsTextView;
    private TextView playerLifePointsTextView;
    private TextView playerLevelTextView;
    private TextView playerPositionTextView;
    private ProgressBar playerProgressBar;
    private ImageView playerImageView;
    private CardView singleRowCardView;
    private boolean isPositionSharing;
    private double lat;
    private double lon;
    private String picture;

    public ClassificationViewHolder(@NonNull View itemView) {
        super(itemView);
        playerNameTextView = itemView.findViewById(R.id.name);
        playerExpPointsTextView = itemView.findViewById(R.id.expPoints);
        playerLifePointsTextView = itemView.findViewById(R.id.lifePoints);
        playerLevelTextView = itemView.findViewById(R.id.level);
        playerPositionTextView = itemView.findViewById(R.id.position);
        playerProgressBar = itemView.findViewById(R.id.progressExpBar);
        singleRowCardView = itemView.findViewById(R.id.singleRowCard);
        playerImageView = itemView.findViewById(R.id.profilePicture);


        itemView.setOnClickListener(v -> {

            Bundle bundle = new Bundle();

            bundle.putString("origin", "classification");
            bundle.putString("name", playerNameTextView.getText().toString());
            bundle.putString("expPoints", playerExpPointsTextView.getText().toString());
            bundle.putString("lifePoints", playerLifePointsTextView.getText().toString());
            bundle.putString("level", playerLevelTextView.getText().toString());
            bundle.putString("profilePicture", picture);
            bundle.putBoolean("isPositionSharing", isPositionSharing);
            if(isPositionSharing) {
                bundle.putDouble("lat", lat);
                bundle.putDouble("lon", lon);
            }

            NavController navController = Navigation.findNavController(itemView);
            navController.navigate(R.id.action_classificationFragment_to_playerDetails, bundle);

        });

    }


    public void bind(Player player) {

        playerNameTextView.setText(player.getName());
        playerExpPointsTextView.setText(String.format("%02d", player.getExpPoits()));
        playerLifePointsTextView.setText(String.format("%02d", player.getLifePoints()));
        playerLevelTextView.setText(String.format("%02d", player.getLevel()));
        playerPositionTextView.setText(String.format("%01d", getAdapterPosition()+1));
        playerProgressBar.setProgress(player.getExpPoits()%100);

        if(player.isPositionSharing()) {
            isPositionSharing = true;
            lat = player.getLat();
            lon = player.getLon();
        } else {
            isPositionSharing = false;
        }

        if(player.getProfilePicture() != null) {

            picture = player.getProfilePicture();

            byte[] imageByteArray = Base64.decode(player.getProfilePicture(), Base64.DEFAULT);

            Glide.with(itemView.getContext())
                    .asBitmap()
                    .load(imageByteArray)
                    .into(playerImageView);

        }

    }

}