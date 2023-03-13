package com.tochy.browser.ads;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.tochy.browser.Model.AdvertisementRoot;
import com.tochy.browser.SessionManager;
import com.tochy.browser.retrofit.Const;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;


/**
 * Created by Anurag Savaliya on 20-May-21.
 */
public class MyInterstitialAds {

    InterAdClickListner interAdClickListner;
    private Context context;
    private InterstitialAd mInterstitialAd;
    private com.facebook.ads.InterstitialAd interstitialAdfb;
    SessionManager sessionManager;
    private AdvertisementRoot.DataItem facebook;
    private AdvertisementRoot.DataItem google;


    public MyInterstitialAds(Context context, InterAdClickListner interAdClickListner) {
        this.context = context;
        this.interAdClickListner = interAdClickListner;

        sessionManager = new SessionManager(context);
        if (sessionManager.getBooleanValue(Const.ADS_Downloded)) {

            if ((sessionManager.getAdsKeys().getData().get(0).getStatus()).equals("true")) {
                google = sessionManager.getAdsKeys().getData().get(0);
            }
            if ((sessionManager.getAdsKeys().getData().get(1).getStatus()).equals("true")) {
                facebook = sessionManager.getAdsKeys().getData().get(1);
            }
        }

        initAds();
    }


    private void initAds() {

        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(context, (google != null) ? google.getInter() : "", adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                initFacebook();
            }

            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                super.onAdLoaded(interstitialAd);
                mInterstitialAd = interstitialAd;

                mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        // Called when fullscreen content is dismissed.
                        Log.d("TAG", "The ad was dismissed.");
                        interAdClickListner.onAdClosed();
                    }


                    @Override
                    public void onAdShowedFullScreenContent() {
                        // Called when fullscreen content is shown.
                        // Make sure to set your reference to null so you don't
                        // show it a second time.
                        mInterstitialAd = null;
                        Log.d("TAG", "The ad was shown.");
                    }
                });

            }

        });


    }



    private void initFacebook() {
        interstitialAdfb = new com.facebook.ads.InterstitialAd(context, (facebook != null) ? facebook.getInter() : "");
        interstitialAdfb.loadAd(
                interstitialAdfb.buildLoadAdConfig().withAdListener(new InterstitialAdListener() {
                    @Override
                    public void onInterstitialDisplayed(Ad ad) {
//
                    }

                    @Override
                    public void onInterstitialDismissed(Ad ad) {
                        interAdClickListner.onAdClosed();
                    }

                    @Override
                    public void onError(Ad ad, AdError adError) {
                        Log.d("TAG", "onError: " + adError);
                        interAdClickListner.onAdFail();
                    }

                    @Override
                    public void onAdLoaded(Ad ad) {
//
                    }

                    @Override
                    public void onAdClicked(Ad ad) {
//
                    }

                    @Override
                    public void onLoggingImpression(Ad ad) {
//
                    }
                })
                        .build());


    }

    public void showAds() {
        if (mInterstitialAd != null) {
            mInterstitialAd.show((Activity) context);
        } else if (interstitialAdfb != null && interstitialAdfb.isAdLoaded()) {
            interstitialAdfb.show();
        }
    }

    public interface InterAdClickListner {
        void onAdClosed();

        void onAdFail();
    }
}

