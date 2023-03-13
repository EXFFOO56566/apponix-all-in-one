package com.tochy.browser.Activity;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowInsets;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.databinding.DataBindingUtil;

import com.tochy.browser.R;
import com.tochy.browser.databinding.ActivityWsStatusBinding;
import com.tochy.browser.Tochy.Adapter.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

public class WsStatusActivity extends AppCompatActivity {


    public static final String TAG = "Home";
    ActivityWsStatusBinding binding;
    private ViewPagerAdapter pagerAdapter;
    private String[] pageTitle;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_ws_status);

        pageTitle = new String[]{getResources().getString(R.string.image),
                getResources().getString(R.string.video),
                getResources().getString(R.string.download)};

        View decorView = getWindow().getDecorView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            decorView.setOnApplyWindowInsetsListener((v, insets) -> {
                WindowInsets defaultInsets = v.onApplyWindowInsets(insets);
                return defaultInsets.replaceSystemWindowInsets(
                        defaultInsets.getSystemWindowInsetLeft(),
                        0,
                        defaultInsets.getSystemWindowInsetRight(),
                        defaultInsets.getSystemWindowInsetBottom());
            });
        }
        ViewCompat.requestApplyInsets(decorView);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, android.R.color.transparent));
        }


        getPermissionsForAndroid23AndBeyond(this);

        for (int i = 0; i < 3; i++) {
            binding.tabLayout.addTab(binding.tabLayout.newTab().setText(pageTitle[i]));
        }

        binding.tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), binding.tabLayout.getTabCount(), WsStatusActivity.this);
        binding.viewPager.setAdapter(pagerAdapter);

        //change Tab selection when swipe ViewPager
        binding.viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(binding.tabLayout));

        //change ViewPager page when tab selected
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
//
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
//
            }
        });


    }

    private void getPermissionsForAndroid23AndBeyond(WsStatusActivity wsStatusActivity) {
        Dexter.withActivity(wsStatusActivity)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.MANAGE_EXTERNAL_STORAGE
                ).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {/* ... */}

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {/* ... */}
        }).withErrorListener(error -> Log.e("Dexter", "There was an error: " + error.toString())).onSameThread().check();
    }

}