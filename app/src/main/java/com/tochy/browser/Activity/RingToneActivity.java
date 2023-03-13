package com.tochy.browser.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.tochy.browser.R;
import com.tochy.browser.databinding.ActivityRingToneBinding;

public class RingToneActivity extends AppCompatActivity {
    ActivityRingToneBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_ring_tone);
        initmain();
    }

    private void initmain() {
        binding.selectRingtone.setOnClickListener(v -> {
            startActivity(new Intent(this, RingtoneselectActivity.class));
        });
    }

    public void onClickBack(View view) {
        finish();
    }
}