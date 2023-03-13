package com.tochy.browser.Fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.WallpaperManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.tochy.browser.Activity.GallaryActivity;
import com.tochy.browser.Activity.MusicActivity;
import com.tochy.browser.Activity.MyVideoActivity;
import com.tochy.browser.Activity.RingToneActivity;
import com.tochy.browser.Activity.WallpaperActivity;
import com.tochy.browser.Activity.WsStatusActivity;
import com.tochy.browser.R;
import com.tochy.browser.ads.MyInterstitialAds;
import com.tochy.browser.databinding.FragmentFilesFregmentBinding;

import java.io.IOException;
import java.util.Arrays;

import static com.tochy.browser.Activity.WsStatusActivity.TAG;


public class FilesFregment extends Fragment implements MyInterstitialAds.InterAdClickListner {
    private static final int CHOOSE_IMAGE = 22;
    FragmentFilesFregmentBinding binding;
    WallpaperManager myWallpaperManager;
    String[] options = new String[]{
            "Home Screen",
            "Lock Screen",
            "Both"
    };
    private Uri mCropImageUri;
    private Dialog dialog;

    MyInterstitialAds myInterstitialAds;
    private boolean isRingtone = false;
    private boolean isGallary = false;
    private boolean isDownload = false;
    private boolean isWallpaper = false;
    private boolean isWsstory = false;
    private boolean isMusic = false;
    private boolean isVideo = false;

    public FilesFregment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_files_fregment, container, false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            int flags = getActivity().getWindow().getDecorView().getSystemUiVisibility();
            flags |=View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            getActivity().getWindow().getDecorView().setSystemUiVisibility(flags);
            getActivity().getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        myInterstitialAds = new MyInterstitialAds(getActivity(), this);
        dialog = new Dialog(getActivity());
        initmail();

    }

    @SuppressLint("ResourceType")
    private void initmail() {

        binding.gallery.setOnClickListener(v -> {
            isGallary = true;
            myInterstitialAds.showAds();
            myInterstitialAds = new MyInterstitialAds(getActivity(), this);

        });


        binding.download.setOnClickListener(v -> {
            isDownload = true;
            myInterstitialAds.showAds();
            myInterstitialAds = new MyInterstitialAds(getActivity(), this);

        });

        binding.wallpaper.setOnClickListener(v -> {
            isWallpaper = true;
            myInterstitialAds.showAds();
            myInterstitialAds = new MyInterstitialAds(getActivity(), this);
        });

        binding.ringtone.setOnClickListener(v -> {
            isRingtone = true;
            myInterstitialAds.showAds();
            myInterstitialAds = new MyInterstitialAds(getActivity(), this);
        });


        binding.music.setOnClickListener(v -> {
            isMusic = true;
            myInterstitialAds.showAds();
            myInterstitialAds = new MyInterstitialAds(getActivity(), this);

        });

        binding.video.setOnClickListener(v -> {
            isVideo = true;
            myInterstitialAds.showAds();
            myInterstitialAds = new MyInterstitialAds(getActivity(), this);

        });

        binding.wsstory.setOnClickListener(v -> {
            isWsstory = true;
            myInterstitialAds.showAds();
            myInterstitialAds = new MyInterstitialAds(getActivity(), this);
        });


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CHOOSE_IMAGE && data != null) {
            mCropImageUri = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), mCropImageUri);
                Log.e(TAG, "onActivityResult: BIT " + bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Choose from below");
            final Bitmap finalBitmap = bitmap;
            builder.setItems(options, new DialogInterface.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    String selectedItem = Arrays.asList(options).get(i);
                    if (selectedItem.equals(options[0])) {
                        try {
                            myWallpaperManager = WallpaperManager.getInstance(getActivity().getApplicationContext());
                            myWallpaperManager.setBitmap(finalBitmap, null, false, WallpaperManager.FLAG_SYSTEM);
                            Toast.makeText(getActivity(), "Wallpaper set successfully!", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        } catch (Exception e) {
                            Log.e(TAG, "onResourceReady: " + e.getMessage());
                        }
                    } else if (selectedItem.equals(options[1])) {
                        try {
                            myWallpaperManager = WallpaperManager.getInstance(getActivity().getApplicationContext());
                            myWallpaperManager.setBitmap(finalBitmap, null, false, WallpaperManager.FLAG_LOCK);
                            Toast.makeText(getActivity(), "Wallpaper set successfully!", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        } catch (Exception e) {
                            Log.e(TAG, "onResourceReady: " + e.getMessage());
                        }

                    } else if (selectedItem.equals(options[2])) {
                        try {
                            myWallpaperManager = WallpaperManager.getInstance(getActivity().getApplicationContext());
                            myWallpaperManager.setBitmap(finalBitmap, null, false, WallpaperManager.FLAG_LOCK | WallpaperManager.FLAG_SYSTEM);
                            Toast.makeText(getActivity(), "Wallpaper set successfully!", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        } catch (Exception e) {
                            Log.e(TAG, "onResourceReady: " + e.getMessage());
                        }
                    }
                }
            });
            dialog = builder.create();
            dialog.show();

        }
    }

    @Override
    public void onAdClosed() {

        doWork();
    }

    private void doWork() {
        if (isRingtone) {
            isRingtone = false;
            startActivity(new Intent(getActivity(), RingToneActivity.class));
        } else if (isGallary) {
            isGallary = false;
            startActivity(new Intent(getActivity(), GallaryActivity.class));
        } else if (isDownload) {
            isDownload = false;
            startActivity(new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS));
        } else if (isWallpaper) {
            isWallpaper = false;
            startActivity(new Intent(getActivity(), WallpaperActivity.class));
        } else if (isMusic) {
            isMusic = false;
            startActivity(new Intent(getActivity(), MusicActivity.class));
        } else if (isVideo) {
            isVideo = false;
            startActivity(new Intent(getActivity(), MyVideoActivity.class));
        } else if (isWsstory) {
            isWsstory = false;
            Intent intent = new Intent(getActivity(), WsStatusActivity.class);
            startActivity(intent);
        }

    }

    @Override
    public void onAdFail() {
        doWork();
    }

}