package com.tochy.browser.Activity;


import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.tochy.browser.R;
import com.tochy.browser.databinding.ActivityWallpaperBinding;

public class WallpaperActivity extends AppCompatActivity {
    ActivityWallpaperBinding binding;
    Context context = WallpaperActivity.this;
    WallpaperManager myWallpaperManager;
    String[] options = new String[]{
            "Home Screen",
            "Lock Screen",
            "Both"
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_wallpaper);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }


        binding.selectWallpaper.setOnClickListener(v -> {
            startActivity(new Intent(this, SelectWallpaperActivity.class));
        });
    }



    public void onClickBack(View view) {
        finish();
    }
}