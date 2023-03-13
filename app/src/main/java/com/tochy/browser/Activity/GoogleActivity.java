package com.tochy.browser.Activity;

import android.app.DownloadManager;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.WindowManager;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.tochy.browser.R;
import com.tochy.browser.databinding.ActivityGoogleBinding;

public class GoogleActivity extends AppCompatActivity {
    ActivityGoogleBinding binding;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = DataBindingUtil.setContentView(this, R.layout.activity_google);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }


        String url = getIntent().getStringExtra("url");


        binding.wvgoogle.loadUrl(url);
        binding.wvgoogle.setWebViewClient(new WebViewClient());
        binding.wvgoogle.getSettings().setSupportMultipleWindows(true);
        binding.wvgoogle.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        binding.wvgoogle.getSettings().setAllowFileAccess(true);
        binding.wvgoogle.getSettings().setJavaScriptEnabled(true);
        binding.wvgoogle.getSettings().setBuiltInZoomControls(true);
        binding.wvgoogle.getSettings().setDisplayZoomControls(false);
        binding.wvgoogle.getSettings().setLoadWithOverviewMode(true);
        binding.wvgoogle.getSettings().setUseWideViewPort(true);
        binding.wvgoogle.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));

                request.setDescription("Download file...");
                request.setTitle(URLUtil.guessFileName(url, contentDisposition, mimetype));
                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED); //Notify client once download is completed!
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(url, contentDisposition, mimetype));
                DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                dm.enqueue(request);
                Toast.makeText(getApplicationContext(), "Downloading File", Toast.LENGTH_LONG).show();

            }


        });


        SharedPreferences settings = getSharedPreferences("user-prif", MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong("chirag", 123456);
        editor.commit();
    }
}