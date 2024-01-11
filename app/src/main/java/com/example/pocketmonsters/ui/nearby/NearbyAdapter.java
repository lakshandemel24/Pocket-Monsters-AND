package com.example.pocketmonsters.ui.nearby;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pocketmonsters.R;
import com.example.pocketmonsters.ui.classification.ClassificationViewHolder;

public class NearbyAdapter extends RecyclerView.Adapter<NearbyViewHolder> {

    private LayoutInflater mInflater;
    private NearbyViewModel viewModel;

    public NearbyAdapter(Context context, NearbyViewModel viewModel) {
        this.mInflater = LayoutInflater.from(context);
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public NearbyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.single_row_nearby, parent, false);

        return new NearbyViewHolder(view, viewModel);
    }

    @Override
    public void onBindViewHolder(@NonNull NearbyViewHolder holder, int position) {
        holder.bind(viewModel.getVirtualObj(position));
    }

    @Override
    public int getItemCount() {
        return viewModel.getVirtualObjCount();
    }
}
