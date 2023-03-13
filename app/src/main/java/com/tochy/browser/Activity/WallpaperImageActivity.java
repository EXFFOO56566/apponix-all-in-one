package com.tochy.browser.Activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.tochy.browser.R;
import com.tochy.browser.databinding.ActivityWallpaperImageBinding;
import com.github.wallpoo.Wallpo;

public class WallpaperImageActivity extends AppCompatActivity {
    ActivityWallpaperImageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_wallpaper_image);

        String url = getIntent().getStringExtra("url");


        Glide.with(this)
                .load(url)
                .centerCrop()
                .into(binding.ivMain);

        binding.homescreen.setOnClickListener(v -> {
            Wallpo.setMainScreenWallpaper(this, binding.ivMain, "Wallpaper Set");
        });

        binding.lockscreen.setOnClickListener(v -> {
            Wallpo.setLockScreenWallpaper(this, binding.ivMain, "LockWallpaper Set");
        });
    }
}