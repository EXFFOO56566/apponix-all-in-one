package com.tochy.browser.Adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.tochy.browser.BuildConfig;
import com.tochy.browser.Model.AppItem;
import com.tochy.browser.R;
import com.tochy.browser.SessionManager;
import com.tochy.browser.databinding.AppsItemBinding;

import java.util.List;

public class AppsitemAdaptor extends RecyclerView.Adapter<AppsitemAdaptor.Viewholder> {
    private Context mContext;
    private List<AppItem> dataModels;
    private OnAppAddClickListner onAppAddClickListner;
    private SessionManager sessionManager;


    public AppsitemAdaptor(List<AppItem> dataModels, OnAppAddClickListner onAppAddClickListner) {
        this.dataModels = dataModels;
        this.onAppAddClickListner = onAppAddClickListner;

    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        sessionManager = new SessionManager(mContext);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = (View) inflater.inflate(R.layout.apps_item, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {

        AppItem app = dataModels.get(position);


        holder.binding.app.setVisibility(View.VISIBLE);

        holder.binding.tvappname.setText(app.getAppName());
        Glide.with(mContext)
                .load(BuildConfig.BASE_URL + "public/img/" + app.getAppIcon())
                .transform(new CenterCrop(), new RoundedCorners(10))
                .into(holder.binding.ivlogo);

        holder.binding.app.setOnClickListener(v -> {
            sessionManager.toogleFav(app);

            onAppAddClickListner.onAddAppClick(app.getAppIcon());

        });


    }


    @Override
    public int getItemCount() {
        return dataModels.size();
    }

    public interface OnAppAddClickListner {

        void onAddAppClick(String s);
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        AppsItemBinding binding;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            binding = AppsItemBinding.bind(itemView);
        }
    }


}
