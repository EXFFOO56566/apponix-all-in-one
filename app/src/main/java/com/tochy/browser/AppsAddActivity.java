package com.tochy.browser;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tochy.browser.Adaptor.AppsListAdaptor;
import com.tochy.browser.Adaptor.CatogoryAdaptor;
import com.tochy.browser.Model.CatagoryAppModel;
import com.tochy.browser.databinding.ActivityAppsAddBinding;
import com.tochy.browser.retrofit.RetrofitBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppsAddActivity extends AppCompatActivity {

    ActivityAppsAddBinding binding;
    private List<CatagoryAppModel.DataItem> catagoriesDataModels = new ArrayList<>();
    private static final String TAG = "appsadd";


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_apps_add);

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAGS_CHANGED);
        window.setStatusBarColor(Color.WHITE);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark


        binding.shimmer.setVisibility(View.VISIBLE);
        binding.main.setVisibility(View.GONE);
        getdata();


        binding.rvappsitem.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int pos = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                    binding.rvCatagory.scrollToPosition(pos);
                    ((CatogoryAdaptor) binding.rvCatagory.getAdapter()).changePos(pos);
                    Log.d(TAG, "onScrollStateChanged: " + catagoriesDataModels.get(pos).getApp());
                }
            }
        });

        binding.back.setOnClickListener(v -> {
            finish();
        });


    }

    private void getdata() {
        Call<CatagoryAppModel> call = RetrofitBuilder.create().getCatagory();
        call.enqueue(new Callback<CatagoryAppModel>() {
            @Override
            public void onResponse(Call<CatagoryAppModel> call, Response<CatagoryAppModel> response) {

                if (response.body().getStatus() == 200) {


                    binding.rvCatagory.setAdapter(new CatogoryAdaptor(response.body().getData(), position -> binding.rvappsitem.scrollToPosition(position - 1)));

                    catagoriesDataModels = response.body().getData();

                    AppsListAdaptor appsListAdaptor = new AppsListAdaptor(catagoriesDataModels, s -> finish());
                    binding.shimmer.setVisibility(View.GONE);
                    binding.main.setVisibility(View.VISIBLE);
                    binding.rvappsitem.setAdapter(appsListAdaptor);


                }


            }

            @Override
            public void onFailure(Call<CatagoryAppModel> call, Throwable t) {
//ll
            }
        });
    }


}