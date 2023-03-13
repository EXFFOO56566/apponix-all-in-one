package com.tochy.browser.Adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.tochy.browser.BuildConfig;
import com.tochy.browser.Model.CatagoryAppModel;
import com.tochy.browser.R;
import com.tochy.browser.databinding.AppsTabAppsBinding;

import java.util.List;

public class CatogoryAdaptor extends RecyclerView.Adapter<CatogoryAdaptor.CatagoryViewHolder> {
    OnClickLisner onClickLisner;
    Context context;
    List<CatagoryAppModel.DataItem> data;

    private int slelctedpos = 0;

    public CatogoryAdaptor(List<CatagoryAppModel.DataItem> data, OnClickLisner onClickLisner) {
        this.data = data;
        this.onClickLisner = onClickLisner;
    }

    @NonNull
    @Override
    public CatagoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.apps_tab_apps, parent, false);
        return new CatagoryViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull CatagoryViewHolder holder, int position) {
        CatagoryAppModel.DataItem item = data.get(holder.getAdapterPosition());

        holder.binding.tvappsname.setText(item.getCategoryName());
        Glide.with(context.getApplicationContext())
                .load(BuildConfig.BASE_URL + "public/img/" + item.getCategoryIcon())
                .circleCrop()
                .into(holder.binding.tvappslogo);

        holder.binding.bg.setOnClickListener(v -> {
            onClickLisner.onCatagoryClick(position);
            changePos(position);
            notifyDataSetChanged();
        });


        if (slelctedpos == position) {
            holder.binding.tvappsname.setTextColor(ContextCompat.getColor(context, R.color.white));
            holder.binding.bg.setBackground(ContextCompat.getDrawable(context, R.drawable.tab_layout_fg));
        } else {
            holder.binding.tvappsname.setTextColor(ContextCompat.getColor(context, R.color.black));
            holder.binding.bg.setBackground(ContextCompat.getDrawable(context, R.drawable.tab_layout_bg));
        }


    }

    public void changePos(int position) {
        slelctedpos = position;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public interface OnClickLisner {
        void onCatagoryClick(int position);
    }

    public class CatagoryViewHolder extends RecyclerView.ViewHolder {
        AppsTabAppsBinding binding;

        public CatagoryViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = AppsTabAppsBinding.bind(itemView);
        }
    }
}
