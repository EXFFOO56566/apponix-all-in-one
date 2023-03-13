package com.tochy.browser.Activity;

import android.app.Dialog;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.tochy.browser.Adaptor.WallpaperAdaptor;
import com.tochy.browser.Model.WallpaperRoot;
import com.tochy.browser.R;
import com.tochy.browser.databinding.ActivitySelectWallpaperBinding;
import com.tochy.browser.retrofit.RetrofitBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectWallpaperActivity extends AppCompatActivity {
    private static final int CHOOSE_IMAGE = 22;
    ActivitySelectWallpaperBinding bindingl;
    Context context = SelectWallpaperActivity.this;
    WallpaperManager myWallpaperManager;
    String[] options = new String[]{
            "Home Screen",
            "Lock Screen",
            "Both"
    };
    private String mCropImageUri;
    private Dialog dialog;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindingl = DataBindingUtil.setContentView(this, R.layout.activity_select_wallpaper);

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAGS_CHANGED);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.my_statusbar_color));

        initmain();

        bindingl.ivBack.setOnClickListener(v -> finish());

    }

    private void initmain() {
        Call<WallpaperRoot> call = RetrofitBuilder.create().getWallpaper();
        call.enqueue(new Callback<WallpaperRoot>() {
            @Override
            public void onResponse(Call<WallpaperRoot> call, Response<WallpaperRoot> response) {
                if (response.body().getStatus() == 200) {
                    Log.d("TAG", "onResponse: =============================");
                    bindingl.rvWallpaper.setAdapter(new WallpaperAdaptor(response.body().getData(), new WallpaperAdaptor.OnClickListner() {
                        @Override
                        public void onClick() {
//
                        }

                        @Override
                        public void onClickUrl(String s) {
                            mCropImageUri = s;
                            Intent intent = new Intent(context, WallpaperImageActivity.class);
                            intent.putExtra("url", s);
                            startActivity(intent);
                            //imageview is the view where image is loading
                           /* Intent intent=new Intent();
                            intent.putExtra("url",mCropImageUri);
                            setResult(Activity.RESULT_OK, intent);*/


                        }
                    }));

                }
            }

            @Override
            public void onFailure(Call<WallpaperRoot> call, Throwable t) {
//
            }
        });
    }


}