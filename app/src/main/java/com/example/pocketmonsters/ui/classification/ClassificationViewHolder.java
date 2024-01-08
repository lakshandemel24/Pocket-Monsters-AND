package com.example.pocketmonsters.ui.classification;

import android.app.Dialog;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
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

            Dialog builder = new Dialog(itemView.getContext());
            builder.setContentView(R.layout.dialog_box);
            builder.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            builder.getWindow().setBackgroundDrawableResource(R.drawable.custom_dialog_bg);

            TextView name = builder.findViewById(R.id.nameD);
            TextView expPoints = builder.findViewById(R.id.expPointsD);
            TextView lifePoints = builder.findViewById(R.id.lifePointsD);
            ImageView profilePicture = builder.findViewById(R.id.imageView);
            Button close = builder.findViewById(R.id.close);

            name.setText(playerNameTextView.getText());
            expPoints.setText("Exp: " + playerExpPointsTextView.getText());
            lifePoints.setText("Life: " + playerLifePointsTextView.getText());
            if(playerImageView.getDrawable() != null) {
                profilePicture.setImageDrawable(playerImageView.getDrawable());
            }
            close.setOnClickListener(v1 -> builder.dismiss());

            builder.show();

        });

    }


    public void bind(Player player) {

        playerNameTextView.setText(player.getName());
        playerExpPointsTextView.setText(String.format("%02d", player.getExpPoits()));
        playerLifePointsTextView.setText(String.format("%02d", player.getLifePoints()));
        playerLevelTextView.setText(String.format("%02d", player.getLevel()));
        playerPositionTextView.setText(String.format("%01d", getAdapterPosition()+1));
        playerProgressBar.setProgress(player.getExpPoits()%100);

        if(player.getProfilePicture() != null) {

            byte[] imageByteArray = Base64.decode(player.getProfilePicture(), Base64.DEFAULT);

            Glide.with(itemView.getContext())
                    .asBitmap()
                    .load(imageByteArray)
                    .into(playerImageView);

        }

    }

}