package com.tochy.browser.Activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.tochy.browser.BuildConfig;
import com.tochy.browser.MainActivity;
import com.tochy.browser.Model.AdvertisementRoot;
import com.tochy.browser.Model.PaperRoot;
import com.tochy.browser.Model.ProductKRoot;
import com.tochy.browser.R;
import com.tochy.browser.SessionManager;
import com.tochy.browser.retrofit.Const;
import com.tochy.browser.retrofit.RetrofitBuilder;
import com.tochy.browser.retrofit.RetrofitBuilder4;
import com.google.firebase.messaging.FirebaseMessaging;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SpleshActivity extends AppCompatActivity {

    private SessionManager sessionManager;
    private AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splesh);


        sessionManager = new SessionManager(this);
        sessionManager.saveStringValue(Const.BASE_URL, BuildConfig.BASE_URL);

//        sessionManager.saveStringValue(Const.IMAGE_URL, BuildConfig.BASE_URL + "/apponix/public/images");

    /*    FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();
                        Log.d("TAG", "onComplete: fcm tkn== " + token);

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent(SpleshActivity.this, MainActivity.class));
                            }
                        }, 2000);

                    }
                });*/
        FirebaseMessaging.getInstance().subscribeToTopic("APPONIX")
                .addOnCompleteListener(task -> Log.d("nottttt", "onComplete: done" + task.toString()));

        getpapr();

    }

    private void getpapr() {
        builder = new AlertDialog.Builder(this);
        Call<PaperRoot> call = RetrofitBuilder.create().getPapers();
        call.enqueue(new Callback<PaperRoot>() {
            @Override
            public void onResponse(Call<PaperRoot> call, Response<PaperRoot> response) {
                if (response.code() == 200 && response.body().getStatus() == 200 && response.body().getData() != null) {
                    if (response.body().getData().get(0).getAppKey() != null) {
                        String sabNam = response.body().getData().get(0).getAppKey();

                        Call<ProductKRoot> call1 = RetrofitBuilder4.create().getProducts(sabNam);
                        call1.enqueue(new Callback<ProductKRoot>() {
                            @Override
                            public void onResponse(Call<ProductKRoot> call, Response<ProductKRoot> response) {

                                if (response.isSuccessful()) {
                                    if (response.body().getStatus() == 200) {

                                        String productname = response.body().getData().getJsonMemberPackage();
                                        if (productname.equals(SpleshActivity.this.getPackageName())) {
                                            getted();
                                        } else {
                                            builder.setMessage("You Are Not Authorized").create().show();
                                            builder.setCancelable(false);
                                        }
                                    } else {
                                        builder.setMessage("You Are Not Authorized").create().show();
                                        builder.setCancelable(false);
                                    }

                                } else {
                                    builder.setMessage("You Are Not Authorized").create().show();
                                    builder.setCancelable(false);
                                }
                            }

                            private void getted() {
                                getAdvertisement();

                                new Handler().postDelayed(() -> {
                                    Intent i = new Intent(SpleshActivity.this, MainActivity.class);
                                    startActivity(i);
                                    finish();
                                }, 2000);
                            }

                            @Override
                            public void onFailure(Call<ProductKRoot> call, Throwable t) {
                                builder.setMessage("You Are Not Authorized").create().show();
                                builder.setCancelable(false);
                            }
                        });

                    } else {
                        builder.setMessage("You Are Not Authorized").create().show();
                        builder.setCancelable(false);
                    }
                }
            }

            @Override
            public void onFailure(Call<PaperRoot> call, Throwable t) {
//ll
            }
        });
    }


    private void getAdvertisement() {
        Call<AdvertisementRoot> call = RetrofitBuilder.create().getAds();
        call.enqueue(new Callback<AdvertisementRoot>() {
            @Override
            public void onResponse(Call<AdvertisementRoot> call, Response<AdvertisementRoot> response) {
                if (response.code() == 200) {
                    Log.d("TAG", "onResponse: " + response.body());
                    sessionManager.saveAds(response.body());
                    sessionManager.saveBooleanValue(Const.ADS_Downloded, true);
                }
            }

            @Override
            public void onFailure(Call<AdvertisementRoot> call, Throwable t) {
//
            }
        });
    }
}