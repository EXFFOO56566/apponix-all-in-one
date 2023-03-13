package com.tochy.browser;

import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowInsets;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.databinding.DataBindingUtil;

import com.tochy.browser.Fragment.FilesFregment;
import com.tochy.browser.Fragment.HomeFregment;
import com.tochy.browser.Fragment.MeFregment;
import com.tochy.browser.Fragment.TabFregment;
import com.tochy.browser.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    public boolean tabOpen = false;
    public TabFregment tabfrag;
    ActivityMainBinding binding;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

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


        tabfrag = new TabFregment();
        getSupportFragmentManager().beginTransaction().replace(R.id.containerTab, tabfrag).commit();

        getSupportFragmentManager().beginTransaction().add(R.id.container, new HomeFregment()).commit();

        binding.bottomHome.setOnClickListener(v -> {
            setDefultBottomBar();
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new HomeFregment()).commit();
            binding.notSelect.setVisibility(View.GONE);
            binding.select.setVisibility(View.VISIBLE);
        });

        binding.bottomProfile.setOnClickListener(v -> {
            setDefultBottomBar();
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new MeFregment()).commit();
            binding.notSelect2.setVisibility(View.GONE);
            binding.select2.setVisibility(View.VISIBLE);
        });


        binding.bottomFile.setOnClickListener(v -> {
            setDefultBottomBar();
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new FilesFregment()).commit();
            binding.notSelect3.setVisibility(View.GONE);
            binding.select3.setVisibility(View.VISIBLE);
        });

        binding.bottomTab.setOnClickListener(v -> {



            if (tabOpen) {
                tabfrag.buttonClicked();
                return;
            }
            setDefultBottomBar();
            binding.select4.setVisibility(View.VISIBLE);
            binding.notSelect4.setVisibility(View.GONE);

            binding.containerTab.setVisibility(View.VISIBLE);
            binding.container.setVisibility(View.GONE);

            tabOpen = true;
        });
    }

    private void setDefultBottomBar() {
        binding.notSelect.setVisibility(View.VISIBLE);
        binding.notSelect2.setVisibility(View.VISIBLE);
        binding.notSelect3.setVisibility(View.VISIBLE);
        binding.notSelect4.setVisibility(View.VISIBLE);


        tabOpen = false;

        binding.select.setVisibility(View.GONE);
        binding.select2.setVisibility(View.GONE);
        binding.select3.setVisibility(View.GONE);
        binding.select4.setVisibility(View.GONE);


        binding.containerTab.setVisibility(View.GONE);
        binding.container.setVisibility(View.VISIBLE);
    }

    public int getStatusBarHeight() {
        int height;
        Resources myResources = getResources();
        int idStatusBarHeight = myResources.getIdentifier("status_bar_height", "dimen", "android");
        if (idStatusBarHeight > 0) {
            height = getResources().getDimensionPixelSize(idStatusBarHeight);
        } else {
            height = 0;
        }
        return height;
    }


    public interface ClickInterface {
        void buttonClicked();
    }
}