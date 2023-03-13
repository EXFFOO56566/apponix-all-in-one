package com.tochy.browser.Activity;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.tochy.browser.R;
import com.tochy.browser.databinding.ActivityUrldownloadBinding;

public class UrldownloadActivity extends AppCompatActivity {
    ActivityUrldownloadBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_urldownload);


    }


}