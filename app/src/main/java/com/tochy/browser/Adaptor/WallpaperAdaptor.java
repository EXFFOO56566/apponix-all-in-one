package com.tochy.browser.Adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.tochy.browser.BuildConfig;
import com.tochy.browser.Model.WallpaperRoot;
import com.tochy.browser.R;
import com.tochy.browser.databinding.ItemImageBinding;

import java.util.ArrayList;
import java.util.List;

public class WallpaperAdaptor extends RecyclerView.Adapter<WallpaperAdaptor.WallpaperViewHolder> {

    Context context;
    private OnClickListner onClickListner;
    private List<WallpaperRoot.DataItem> data = new ArrayList<>();

    public WallpaperAdaptor(List<WallpaperRoot.DataItem> data, OnClickListner onClickListner) {
        this.data = data;
        this.onClickListner = onClickListner;
    }

    @NonNull
    @Override
    public WallpaperViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_image, parent, false);
        return new WallpaperViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WallpaperViewHolder holder, int position) {
        Glide.with(context)
                .load(BuildConfig.BASE_URL + "public/img/" + data.get(position).getImage())
                .centerCrop()
                .into(holder.binding.ivWallpaper);
        holder.binding.ivWallpaper.setOnClickListener(v -> {
            onClickListner.onClick();
            onClickListner.onClickUrl(BuildConfig.BASE_URL + "public/img/" + data.get(position).getImage());
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public interface OnClickListner {
        void onClick();

        void onClickUrl(String s);

    }

    public class WallpaperViewHolder extends RecyclerView.ViewHolder {
        ItemImageBinding binding;

        public WallpaperViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemImageBinding.bind(itemView);
        }
    }
}
