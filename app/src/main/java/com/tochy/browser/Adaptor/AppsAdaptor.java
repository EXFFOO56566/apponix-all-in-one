package com.tochy.browser.Adaptor;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.tochy.browser.Activity.browser.WebActivity;
import com.tochy.browser.BuildConfig;
import com.tochy.browser.Model.AppItem;
import com.tochy.browser.R;
import com.tochy.browser.SessionManager;
import com.tochy.browser.databinding.ItemAppsMainBinding;

import java.util.List;

public class AppsAdaptor extends RecyclerView.Adapter<AppsAdaptor.Viewholder> {
    SessionManager sessionManager;
    private Context mContext;

    public AppsAdaptor(List<AppItem> dataModels) {
        this.dataModels = dataModels;

    }

    public MainAppsClickListner getMainAppsClickListner() {
        return mainAppsClickListner;
    }

    private MainAppsClickListner mainAppsClickListner;
    private List<AppItem> dataModels;


    public void setMainAppsClickListner(MainAppsClickListner mainAppsClickListner) {
        this.mainAppsClickListner = mainAppsClickListner;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        sessionManager = new SessionManager(mContext);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = (View) inflater.inflate(R.layout.item_apps_main, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        if (dataModels.get(position) == null) {
            holder.binding.addbtn.setVisibility(View.VISIBLE);
            holder.binding.appimage.setVisibility(View.GONE);
            holder.binding.tvname.setVisibility(View.GONE);
        } else {
            AppItem app = dataModels.get(position);
            holder.binding.tvname.setVisibility(View.VISIBLE);
            holder.binding.appimage.setVisibility(View.VISIBLE);
            holder.binding.addbtn.setVisibility(View.GONE);
            holder.binding.tvname.setText(app.getAppName());


            Log.d("TAG", "onBindViewHolder: " + app.getAppIcon());
            Glide.with(mContext)
                    .load(BuildConfig.BASE_URL + "public/img/" + app.getAppIcon())
                    .transform(new CenterCrop(), new RoundedCorners(20))
                    .into(holder.binding.appimage);
            holder.binding.appimage.setOnClickListener(v -> {

                Intent intent = new Intent(mContext, WebActivity.class);
                intent.putExtra("words", app.getAppUrl());
                mContext.startActivity(intent);


            });


            holder.binding.appimage.setOnLongClickListener(v -> {
                mainAppsClickListner.onAppLongclick(app, position);
                return true;
            });

        }
        holder.binding.addbtn.setOnClickListener(v -> mainAppsClickListner.onAddClick());
    }

    @Override
    public int getItemCount() {
        return dataModels.size();
    }


    public void removeOneItem(int position) {
        dataModels.remove(position);
        notifyDataSetChanged();
    }

    public interface MainAppsClickListner {
        void onAddClick();

        void onAppLongclick(AppItem s, int position);
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        ItemAppsMainBinding binding;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            binding = ItemAppsMainBinding.bind(itemView);
        }
    }
}
