package com.tochy.browser.api;

import android.app.Activity;

import com.tochy.browser.util.Utils;

import io.reactivex.observers.DisposableObserver;

public class CommonClassForAPI {
    private static Activity mActivity;
    private static CommonClassForAPI CommonClassForAPI;

    public static CommonClassForAPI getInstance(Activity activity) {
        if (CommonClassForAPI == null) {
            CommonClassForAPI = new CommonClassForAPI();
        }
        mActivity = activity;
        return CommonClassForAPI;
    }

    public void callResult(final DisposableObserver observer, String URL, String Cookie) {
        if (Utils.isNullOrEmpty(Cookie)) {
            Cookie = "";
        }
        RestClient.getInstance(mActivity).getService().callResult(URL, Cookie,
                "Instagram 9.5.2 (iPhone7,2; iPhone OS 9_3_3; en_US; en-US; scale=2.00; 750x1334) AppleWebKit/420+");
    }



    public void getStories(final DisposableObserver observer, String Cookie) {
        if (Utils.isNullOrEmpty(Cookie)) {
            Cookie = "";
        }
        RestClient.getInstance(mActivity).getService().getStoriesApi("https://i.instagram.com/api/v1/feed/reels_tray/", Cookie,
                "\"Instagram 9.5.2 (iPhone7,2; iPhone OS 9_3_3; en_US; en-US; scale=2.00; 750x1334) AppleWebKit/420+\"")
        ;
    }

    public void getFullDetailFeed(final DisposableObserver observer, String UserId, String Cookie) {
        RestClient.getInstance(mActivity).getService().getFullDetailInfoApi(
                "https://i.instagram.com/api/v1/users/" + UserId + "/full_detail_info?max_id="
                , Cookie, "\"Instagram 9.5.2 (iPhone7,2; iPhone OS 9_3_3; en_US; en-US; scale=2.00; 750x1334) AppleWebKit/420+\"")
        ;
    }



}