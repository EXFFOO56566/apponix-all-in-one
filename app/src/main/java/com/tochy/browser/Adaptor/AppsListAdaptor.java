package com.tochy.browser.Adaptor;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.tochy.browser.BuildConfig;
import com.tochy.browser.Model.AppsModel;
import com.tochy.browser.Model.CatagoryAppModel;
import com.tochy.browser.R;
import com.tochy.browser.databinding.AppsListBinding;
import com.tochy.browser.retrofit.RetrofitBuilder;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppsListAdaptor extends RecyclerView.Adapter<AppsListAdaptor.Viewholder> {
    private Context mContext;
    private CategoryAppsClickListner categoryAppsClickListner;
    private List<CatagoryAppModel.DataItem> catagoriesDataModels;



    public AppsListAdaptor(List<CatagoryAppModel.DataItem> catagoriesDataModels, CategoryAppsClickListner categoryAppsClickListner) {
        this.catagoriesDataModels = catagoriesDataModels;
        this.categoryAppsClickListner = categoryAppsClickListner;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = (View) inflater.inflate(R.layout.apps_list_, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {


        CatagoryAppModel.DataItem category = catagoriesDataModels.get(position);

        holder.binding.tvappname.setText(category.getCategoryName());
        Glide.with(mContext)
                .load(BuildConfig.BASE_URL + category.getCategoryIcon())
                .circleCrop()
                .into(holder.binding.ivlogo);


        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("category_id", category.getCategorieId());

        Call<AppsModel> call = RetrofitBuilder.create().getApps(String.valueOf(category.getCategorieId()));
        call.enqueue(new Callback<AppsModel>() {
            @Override
            public void onResponse(Call<AppsModel> call, Response<AppsModel> response) {
                if (response.code() == 200 && response.body().getStatus() == 200) {

                    Log.d("TAG", "onResponse: =" + response.body().getData().get(0).getCategoryName());
                    AppsitemAdaptor appAdapter = new AppsitemAdaptor(response.body().getData().get(0).getApp(), s -> {
                        categoryAppsClickListner.CategoryAddAppClick(s);
                    });
                    holder.binding.rvlist.setAdapter(appAdapter);
                }
            }

            @Override
            public void onFailure(Call<AppsModel> call, Throwable t) {
//
            }
        });



    }

    @Override
    public int getItemCount() {
        return catagoriesDataModels.size();
    }


    public interface CategoryAppsClickListner {
        void CategoryAddAppClick(String s);
    }


    public class Viewholder extends RecyclerView.ViewHolder {
        AppsListBinding binding;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            binding = AppsListBinding.bind(itemView);
        }
    }
}
