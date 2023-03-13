package com.tochy.browser.Activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.tochy.browser.Adaptor.RingToneAdaptor;
import com.tochy.browser.Model.RingToneRoot;
import com.tochy.browser.R;
import com.tochy.browser.databinding.ActivityRingtoneselectBinding;
import com.tochy.browser.databinding.ItemRingtoneBinding;
import com.tochy.browser.retrofit.RetrofitBuilder;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RingtoneselectActivity extends AppCompatActivity {

    public ArrayList<String> drawables = new ArrayList<String>();
    ActivityRingtoneselectBinding binding;
    private MediaPlayer mediaPlayer;
    private ItemRingtoneBinding lastBainding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_ringtoneselect);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            boolean retVal = true;
            retVal = Settings.System.canWrite(this);
            if (retVal == false) {
                if (!Settings.System.canWrite(getApplicationContext())) {

                    Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS, Uri.parse("package:" + getPackageName()));
                    Toast.makeText(getApplicationContext(), "Please, allow system settings for automatic logout ", Toast.LENGTH_LONG).show();
                    startActivityForResult(intent, 200);
                }
            } else {
                Toast.makeText(getApplicationContext(), "You are not allowed to wright ", Toast.LENGTH_LONG).show();
            }
        }


        drawables.clear();
        drawables.add(0, "ring1");
        drawables.add(1, "ring2");
        drawables.add(2, "ring3");
        drawables.add(3, "ring4");
        drawables.add(4, "ring5");
        drawables.add(5, "ring6");
        drawables.add(6, "ring7");
        drawables.add(7, "ring8");
        drawables.add(8, "ring9");
        drawables.add(9, "ring10");
        drawables.add(10, "ring11");
        drawables.add(11, "ring12");
        drawables.add(12, "ring13");
        drawables.add(13, "ring14");
        drawables.add(14, "ring15");
        drawables.add(15, "ring16");
        drawables.add(16, "ring17");
        drawables.add(17, "ring18");
        drawables.add(18, "ring19");
        drawables.add(19, "ring20");

        initmain();

        binding.ivBack.setOnClickListener(v -> {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.release();
            }
            finish();

        });
    }

    private void initmain() {
        Call<RingToneRoot> call = RetrofitBuilder.create().getRingTone();
        call.enqueue(new Callback<RingToneRoot>() {
            @Override
            public void onResponse(Call<RingToneRoot> call, Response<RingToneRoot> response) {
                if (response.body().getStatus() == 200) {
                    Log.d("TAG", "onResponse: =============================");
                    binding.rvRingtone.setAdapter(new RingToneAdaptor(drawables, new RingToneAdaptor.OnClickListner() {
                        @Override
                        public void onClick(String ringtone, ItemRingtoneBinding itemRingtoneBinding) {

                            itemRingtoneBinding.stop.setVisibility(View.VISIBLE);
                            itemRingtoneBinding.play.setVisibility(View.GONE);

                            if (lastBainding != null) {
                                lastBainding.stop.setVisibility(View.GONE);
                                lastBainding.play.setVisibility(View.VISIBLE);
                            }
                            if (ringtone.equals("ring1")) {

                                if (mediaPlayer != null) {
                                    mediaPlayer.release();
                                }
                                mediaPlayer = MediaPlayer.create(RingtoneselectActivity.this, R.raw.ring1);
                                mediaPlayer.start();
                            } else if (ringtone.equals("ring2")) {

                                if (mediaPlayer != null) {
                                    mediaPlayer.release();
                                }
                                mediaPlayer = MediaPlayer.create(RingtoneselectActivity.this, R.raw.ring2);
                                mediaPlayer.start();
                            } else if (ringtone.equals("ring3")) {

                                if (mediaPlayer != null) {
                                    mediaPlayer.release();
                                }
                                mediaPlayer = MediaPlayer.create(RingtoneselectActivity.this, R.raw.ring3);
                                mediaPlayer.start();
                            } else if (ringtone.equals("ring4")) {

                                if (mediaPlayer != null) {
                                    mediaPlayer.release();
                                }
                                mediaPlayer = MediaPlayer.create(RingtoneselectActivity.this, R.raw.ring4);
                                mediaPlayer.start();
                            } else if (ringtone.equals("ring5")) {
                                if (mediaPlayer != null) {
                                    mediaPlayer.release();
                                }
                                mediaPlayer = MediaPlayer.create(RingtoneselectActivity.this, R.raw.ring5);
                                mediaPlayer.start();
                            } else if (ringtone.equals("ring6")) {
                                if (mediaPlayer != null) {
                                    mediaPlayer.release();
                                }
                                mediaPlayer = MediaPlayer.create(RingtoneselectActivity.this, R.raw.ring6);
                                mediaPlayer.start();
                            } else if (ringtone.equals("ring7")) {
                                if (mediaPlayer != null) {
                                    mediaPlayer.release();
                                }
                                mediaPlayer = MediaPlayer.create(RingtoneselectActivity.this, R.raw.ring7);
                                mediaPlayer.start();
                            } else if (ringtone.equals("ring8")) {
                                if (mediaPlayer != null) {
                                    mediaPlayer.release();
                                }
                                mediaPlayer = MediaPlayer.create(RingtoneselectActivity.this, R.raw.ring8);
                                mediaPlayer.start();
                            } else if (ringtone.equals("ring9")) {
                                if (mediaPlayer != null) {
                                    mediaPlayer.release();
                                }
                                mediaPlayer = MediaPlayer.create(RingtoneselectActivity.this, R.raw.ring9);
                                mediaPlayer.start();
                            } else if (ringtone.equals("ring10")) {
                                if (mediaPlayer != null) {
                                    mediaPlayer.release();
                                }
                                mediaPlayer = MediaPlayer.create(RingtoneselectActivity.this, R.raw.ring10);
                                mediaPlayer.start();
                            } else if (ringtone.equals("ring11")) {
                                if (mediaPlayer != null) {
                                    mediaPlayer.release();
                                }
                                mediaPlayer = MediaPlayer.create(RingtoneselectActivity.this, R.raw.ring11);
                                mediaPlayer.start();
                            } else if (ringtone.equals("ring12")) {
                                if (mediaPlayer != null) {
                                    mediaPlayer.release();
                                }
                                mediaPlayer = MediaPlayer.create(RingtoneselectActivity.this, R.raw.ring12);
                                mediaPlayer.start();
                            } else if (ringtone.equals("ring13")) {
                                if (mediaPlayer != null) {
                                    mediaPlayer.release();
                                }
                                mediaPlayer = MediaPlayer.create(RingtoneselectActivity.this, R.raw.ring13);
                                mediaPlayer.start();
                            } else if (ringtone.equals("ring14")) {
                                if (mediaPlayer != null) {
                                    mediaPlayer.release();
                                }
                                mediaPlayer = MediaPlayer.create(RingtoneselectActivity.this, R.raw.ring14);
                                mediaPlayer.start();
                            } else if (ringtone.equals("ring15")) {
                                if (mediaPlayer != null) {
                                    mediaPlayer.release();
                                }
                                mediaPlayer = MediaPlayer.create(RingtoneselectActivity.this, R.raw.ring15);
                                mediaPlayer.start();
                            } else if (ringtone.equals("ring16")) {
                                if (mediaPlayer != null) {
                                    mediaPlayer.release();
                                }
                                mediaPlayer = MediaPlayer.create(RingtoneselectActivity.this, R.raw.ring16);
                                mediaPlayer.start();
                            } else if (ringtone.equals("ring17")) {
                                if (mediaPlayer != null) {
                                    mediaPlayer.release();
                                }
                                mediaPlayer = MediaPlayer.create(RingtoneselectActivity.this, R.raw.ring17);
                                mediaPlayer.start();
                            } else if (ringtone.equals("ring18")) {
                                if (mediaPlayer != null) {
                                    mediaPlayer.release();
                                }
                                mediaPlayer = MediaPlayer.create(RingtoneselectActivity.this, R.raw.ring18);
                                mediaPlayer.start();
                            } else if (ringtone.equals("ring19")) {
                                if (mediaPlayer != null) {
                                    mediaPlayer.release();
                                }
                                mediaPlayer = MediaPlayer.create(RingtoneselectActivity.this, R.raw.ring19);
                                mediaPlayer.start();
                            } else if (ringtone.equals("ring20")) {
                                if (mediaPlayer != null) {
                                    mediaPlayer.release();
                                }
                                mediaPlayer = MediaPlayer.create(RingtoneselectActivity.this, R.raw.ring20);
                                mediaPlayer.start();
                            }


                            lastBainding = itemRingtoneBinding;
                        }

                        @Override
                        public void onClick2(String ringtone) {
                            Toast.makeText(RingtoneselectActivity.this, "Set Ringtone Successfully", Toast.LENGTH_SHORT).show();
                            Uri path = Uri.parse("android.resource://" + getPackageName() + "/raw/" + ringtone);
                            RingtoneManager.setActualDefaultRingtoneUri(getApplicationContext(), RingtoneManager.TYPE_RINGTONE, path);
                        }

                        @Override
                        public void onClick3() {
                            if (mediaPlayer == null) return;
                            if (mediaPlayer.isPlaying()) {
                                mediaPlayer.stop();
                                mediaPlayer.release();
                            }
                        }
                    }));


                }
            }

            @Override
            public void onFailure(Call<RingToneRoot> call, Throwable t) {
//
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }
}