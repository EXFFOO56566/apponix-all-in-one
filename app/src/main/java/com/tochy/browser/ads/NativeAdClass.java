package com.tochy.browser.ads;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tochy.browser.Model.AdvertisementRoot;
import com.tochy.browser.R;
import com.tochy.browser.SessionManager;
import com.tochy.browser.retrofit.Const;
import com.facebook.ads.AdError;
import com.facebook.ads.AdOptionsView;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAdBase;
import com.facebook.ads.NativeAdLayout;
import com.facebook.ads.NativeAdsManager;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;

import java.util.ArrayList;
import java.util.List;

import static com.google.android.gms.ads.formats.NativeAdOptions.ADCHOICES_TOP_RIGHT;

/**
 * Created by Anurag Savaliya on 20-May-21.
 */
public class NativeAdClass {
    private static final String TAG = "nativeads";
    private boolean isEnabledAds = true;
    private Context context;

    private LinearLayout adsContainer;

    SessionManager sessionManager;
    private AdvertisementRoot.DataItem facebook;
    private AdvertisementRoot.DataItem google;


    public NativeAdClass(Context context, LinearLayout adsContainer) {
        this.context = context;
        this.adsContainer = adsContainer;
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
        AdLoader.Builder builder = null;
        builder = new AdLoader.Builder(context, (google != null) ? google.getJsonMemberNative() : "");
        AdLoader adLoader = builder.forNativeAd(
                unifiedNativeAd -> {
                    Log.d(TAG, "onUnifiedNativeAdLoaded: ");

                    showAdmobAds(unifiedNativeAd);


                }).withAdListener(
                new AdListener() {

                    @Override
                    public void onAdFailedToLoad(LoadAdError loadAdError) {
                        super.onAdFailedToLoad(loadAdError);
                        Log.e(TAG, "The previous native ad failed to load. Attempting to"
                                + " load another." + loadAdError.getCode());

                        loadFbNativeAds();
                    }
                })
                .withNativeAdOptions(new NativeAdOptions.Builder()
                        .setRequestCustomMuteThisAd(true)
                        .setAdChoicesPlacement(ADCHOICES_TOP_RIGHT)
                        .build()).build();


        adLoader.loadAds(new AdRequest.Builder().build(), 1);


    }



    private void loadFbNativeAds() {
        if (context != null && isEnabledAds) {
            NativeAdsManager mNativeAdsManager = new NativeAdsManager(context, (google != null) ? google.getBanner() : "", 1);
            mNativeAdsManager.setListener(new NativeAdsManager.Listener() {
                @Override
                public void onAdsLoaded() {
                    Log.d(TAG, "onAdsLoaded: fb ");
                    if (isEnabledAds) {
                        showFaceBookAds(mNativeAdsManager);
                    }
                }


                @Override
                public void onAdError(AdError adError) {
                    Log.e(TAG, "AdError: " + adError.getErrorMessage());
                }
            });

            mNativeAdsManager.loadAds(NativeAdBase.MediaCacheFlag.ALL);
        }

    }

    private void showFaceBookAds(NativeAdsManager mNativeAdsManager) {
        com.facebook.ads.NativeAd ad = mNativeAdsManager.nextNativeAd();
        NativeAdLayout nativeAdLayout = new NativeAdLayout(context);
        adsContainer.removeAllViews();
        if (ad != null) {
            View view = LayoutInflater.from(context).inflate(R.layout.native_facebook, null, false);
            nativeAdLayout.addView(view);
            NativeAdLayout linearLayout = view.findViewById(R.id.ad_choices_container);
            linearLayout.removeAllViews();
            MediaView mvAdMedia;
            MediaView ivAdIcon;
            TextView tvAdTitle;
            TextView tvAdBody;
            TextView btnAdCallToAction;
            mvAdMedia = (MediaView) view.findViewById(R.id.ad_media);
            tvAdTitle = (TextView) view.findViewById(R.id.ad_headline);
            tvAdBody = (TextView) view.findViewById(R.id.ad_body);
            btnAdCallToAction = (TextView) view.findViewById(R.id.ad_call_to_action);
            ivAdIcon = view.findViewById(R.id.ad_app_icon);
            tvAdTitle.setText(ad.getAdvertiserName());
            tvAdBody.setText(ad.getAdBodyText());
            btnAdCallToAction.setText("  " + ad.getAdCallToAction());
            btnAdCallToAction.setVisibility(
                    ad.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
            AdOptionsView adChoicesView = new AdOptionsView(context,
                    ad, nativeAdLayout);


            List<View> clickableViews = new ArrayList<>();
            clickableViews.add(ivAdIcon);
            clickableViews.add(mvAdMedia);
            clickableViews.add(btnAdCallToAction);
            ad.registerViewForInteraction(
                    view,
                    mvAdMedia,
                    ivAdIcon,
                    clickableViews);

            linearLayout.addView(adChoicesView, 0);
            adsContainer.addView(nativeAdLayout, 0);
        }

    }

    private void showAdmobAds(NativeAd unifiedNativeAd) {
        if (context != null) {
            View view = LayoutInflater.from(context).inflate(R.layout.google_native_big, null, false);
            NativeAdView adView = view.findViewById(R.id.ad_view);


            adView.setMediaView(adView.findViewById(R.id.ad_media));


            adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
            adView.setBodyView(adView.findViewById(R.id.ad_body));
            adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
            adView.setIconView(adView.findViewById(R.id.ad_app_icon));
            adsContainer.removeAllViews();
            adsContainer.addView(view, 0);
            populateNativeAdView(unifiedNativeAd, adView);
        }

    }

    private void populateNativeAdView(NativeAd nativeAd, NativeAdView adView) {


        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        ((TextView) adView.getCallToActionView()).setText(nativeAd.getCallToAction());


        NativeAd.Image icon = nativeAd.getIcon();

        if (icon == null) {
            adView.getIconView().setVisibility(View.INVISIBLE);
        } else if (context != null && isEnabledAds) {
            Glide.with(adView.getIconView())
                    .load(icon.getDrawable())
                    .circleCrop()
                    .into((ImageView) adView.getIconView());
            adView.getIconView().setVisibility(View.VISIBLE);
        }


        adView.setNativeAd(nativeAd);

    }
}
