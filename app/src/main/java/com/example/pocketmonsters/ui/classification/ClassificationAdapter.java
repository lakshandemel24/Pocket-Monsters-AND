package com.example.pocketmonsters.ui.classification;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pocketmonsters.R;

public class ClassificationAdapter extends RecyclerView.Adapter<ClassificationViewHolder>{

    private LayoutInflater mInflater;
    private ClassificationViewModel viewModel;

    public ClassificationAdapter(Context context, ClassificationViewModel viewModel) {
        this.mInflater = LayoutInflater.from(context);
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public ClassificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.single_row, parent, false);

        return new ClassificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassificationViewHolder holder, int position) {

        if(position == 0)
            holder.itemView.setBackgroundColor(Color.parseColor("#D5B402"));
        else if(position == 1)
            holder.itemView.setBackgroundColor(Color.parseColor("#7C7A7A"));
        else if(position == 2)
            holder.itemView.setBackgroundColor(Color.parseColor("#B36E2A"));
        else
            holder.itemView.setBackgroundColor(Color.parseColor("#3D5AFE"));

        holder.bind(viewModel.getPlayers(position));
    }

    @Override
    public int getItemCount() {
        return viewModel.getPlayersCount();
    }

}
