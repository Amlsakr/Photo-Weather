package com.example.weatheramlsakrtask.view.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatheramlsakrtask.databinding.RecyclerItemViewBinding;

import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {
    List<Uri> pictureItems;
    RecyclerViewItemClickListener recyclerViewItemClickListener;

    public MainAdapter(List<Uri> pictureItems, RecyclerViewItemClickListener recyclerViewItemClickListener) {
        this.pictureItems = pictureItems;
        this.recyclerViewItemClickListener = recyclerViewItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerItemViewBinding recyclerItemViewBinding = RecyclerItemViewBinding.
                inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(recyclerItemViewBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.recyclerItemViewBinding.recyclerViewIV.setImageURI(pictureItems.get(position));
    }

    @Override
    public int getItemCount() {
        return pictureItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private RecyclerItemViewBinding recyclerItemViewBinding;

        public ViewHolder(RecyclerItemViewBinding recyclerItemViewBinding) {
            super(recyclerItemViewBinding.getRoot());
            this.recyclerItemViewBinding = recyclerItemViewBinding;
            recyclerItemViewBinding.recyclerViewIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recyclerViewItemClickListener.onClick(getAdapterPosition());
                }
            });

        }
    }
}
