package com.tochy.browser.Tochy.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.RequiresApi;

import com.tochy.browser.R;
import com.tochy.browser.Tochy.interfaces.OnClick;

import java.io.File;

public class Method {

    private final String myPreference = "status";
    public Activity activity;
    public SharedPreferences.Editor editor;

    OnClick onClick;
    SharedPreferences pref;
    String isFirst = "is_first";

    public Method(Activity activity) {
        this.activity = activity;
        pref = activity.getSharedPreferences(myPreference, 0); // 0 - for private mode
        editor = pref.edit();
    }

    public Method(Activity activity, OnClick onClick) {
        this.activity = activity;
        this.onClick = onClick;
        pref = activity.getSharedPreferences(myPreference, 0); // 0 - for private mode
        editor = pref.edit();
    }

    //rtl
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void forceRTLIfSupported() {
        if (activity.getResources().getString(R.string.isRTL).equals("true")) {
            activity.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
    }

    public boolean isAppWA() {
        String packageName = "com.whatsapp";
        Intent mIntent = activity.getPackageManager().getLaunchIntentForPackage(packageName);
        return mIntent != null;
    }

    public boolean isAppWB() {
        String packageName = "com.whatsapp.w4b";
        Intent mIntent = activity.getPackageManager().getLaunchIntentForPackage(packageName);
        return mIntent != null;
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(isFirst, true);
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(isFirst, isFirstTime);
        editor.commit();
    }

    public int getScreenWidth() {
        int columnWidth;
        WindowManager wm = (WindowManager) activity
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        final Point point = new Point();

        point.x = display.getWidth();
        point.y = display.getHeight();

        columnWidth = point.x;
        return columnWidth;
    }

    public void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    public void setStatusBarGradiant() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    public void share(String link, String type) {

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);

        if (type.equals("image")) {
            shareIntent.setType("image/*");
        } else {
            shareIntent.setType("video/*");
        }
        shareIntent.putExtra(Intent.EXTRA_TEXT, activity.getResources().getString(R.string.play_more_app));
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(link)));
        activity.startActivity(Intent.createChooser(shareIntent, "Share to"));

    }

    public void click(final int position, final String type) {
        onClick.position(position, type);
    }

}
