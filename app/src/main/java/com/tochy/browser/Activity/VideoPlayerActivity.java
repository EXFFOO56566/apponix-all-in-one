package com.tochy.browser.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.tochy.browser.R;
import com.tochy.browser.databinding.ActivityVideoPlayerBinding;

public class VideoPlayerActivity extends AppCompatActivity {
    ActivityVideoPlayerBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_video_player);


        Intent intent = getIntent();


        String videoPath = intent.getStringExtra("PathVideo");
        Log.d("TAG", "onCreate: " + videoPath);



        try {
            MediaController mediaController = new MediaController(VideoPlayerActivity.this);
            mediaController.setAnchorView(binding.playerView);
            Uri video = Uri.parse(videoPath);
            binding.playerView.setMediaController(mediaController);
            binding.playerView.setVideoURI(video);
            binding.playerView.start();
            binding.playerView.setOnPreparedListener(mp -> {
//
            });
            binding.playerView.setOnCompletionListener(mediaPlayer -> {
//
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}