package com.tochy.browser.Activity;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tochy.browser.Adaptor.StoriesListAdapter;
import com.tochy.browser.Adaptor.UserListAdapter;
import com.tochy.browser.Model.Download.Edge;
import com.tochy.browser.Model.Download.EdgeSidecarToChildren;
import com.tochy.browser.Model.Download.ResponseModel;
import com.tochy.browser.Model.FBStoryModel.NodeModel;
import com.tochy.browser.Model.story.FullDetailModel;
import com.tochy.browser.Model.story.StoryModel;
import com.tochy.browser.Model.story.TrayModel;
import com.tochy.browser.R;
import com.tochy.browser.SessionManager;
import com.tochy.browser.api.CommonClassForAPI;
import com.tochy.browser.databinding.ActivityInstagramBinding;
import com.tochy.browser.interfaces.UserListInterface;
import com.tochy.browser.util.SharePrefs;
import com.tochy.browser.util.Utils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.reactivex.observers.DisposableObserver;

import static android.content.ClipDescription.MIMETYPE_TEXT_PLAIN;
import static com.tochy.browser.util.Utils.RootDirectoryInsta;
import static com.tochy.browser.util.Utils.createFileFolder;
import static com.tochy.browser.util.Utils.startDownload;

public class InstagramActivity extends AppCompatActivity implements UserListInterface {

    ActivityInstagramBinding binding;

    Context context;
    CommonClassForAPI commonClassForAPI;
    SessionManager appLangSessionManager;
    UserListAdapter userListAdapter;
    StoriesListAdapter storiesListAdapter;
    private InstagramActivity activity;
    private ClipboardManager clipBoard;
    private String PhotoUrl;
    private String VideoUrl;
    private DisposableObserver<JsonObject> instaObserver = new DisposableObserver<JsonObject>() {
        @Override
        public void onNext(JsonObject versionList) {
            Utils.hideProgressDialog(activity);
            try {
                Log.e("onNext: ", versionList.toString());
                Type listType = new TypeToken<ResponseModel>() {
                }.getType();
                ResponseModel responseModel = new Gson().fromJson(versionList.toString(), listType);
                EdgeSidecarToChildren edgeSidecarToChildren = responseModel.getGraphql().getShortcode_media().getEdge_sidecar_to_children();
                if (edgeSidecarToChildren != null) {
                    List<Edge> edgeArrayList = edgeSidecarToChildren.getEdges();
                    for (int i = 0; i < edgeArrayList.size(); i++) {
                        if (edgeArrayList.get(i).getNode().isIs_video()) {
                            VideoUrl = edgeArrayList.get(i).getNode().getVideo_url();
                            startDownload(VideoUrl, RootDirectoryInsta, activity, getVideoFilenameFromURL(VideoUrl));
                            binding.etText.setText("");
                            VideoUrl = "";

                        } else {
                            PhotoUrl = edgeArrayList.get(i).getNode().getDisplay_resources().get(edgeArrayList.get(i).getNode().getDisplay_resources().size() - 1).getSrc();
                            startDownload(PhotoUrl, RootDirectoryInsta, activity, getImageFilenameFromURL(PhotoUrl));
                            PhotoUrl = "";
                            binding.etText.setText("");
                        }
                    }
                } else {
                    boolean isVideo = responseModel.getGraphql().getShortcode_media().isIs_video();
                    if (isVideo) {
                        VideoUrl = responseModel.getGraphql().getShortcode_media().getVideo_url();
                        //new DownloadFileFromURL().execute(VideoUrl,getFilenameFromURL(VideoUrl));
                        startDownload(VideoUrl, RootDirectoryInsta, activity, getVideoFilenameFromURL(VideoUrl));
                        VideoUrl = "";
                        binding.etText.setText("");
                    } else {
                        PhotoUrl = responseModel.getGraphql().getShortcode_media().getDisplay_resources()
                                .get(responseModel.getGraphql().getShortcode_media().getDisplay_resources().size() - 1).getSrc();

                        startDownload(PhotoUrl, RootDirectoryInsta, activity, getImageFilenameFromURL(PhotoUrl));
                        PhotoUrl = "";
                        binding.etText.setText("");
                        // new DownloadFileFromURL().execute(PhotoUrl,getFilenameFromURL(PhotoUrl));
                    }
                }

                showRewardedVideoAds();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(Throwable e) {
            Utils.hideProgressDialog(activity);
            e.printStackTrace();
        }

        @Override
        public void onComplete() {
            Utils.hideProgressDialog(activity);
        }
    };
    private DisposableObserver<StoryModel> storyObserver = new
            DisposableObserver<StoryModel>() {
                @Override
                public void onNext(StoryModel response) {
                    binding.RVUserList.setVisibility(View.VISIBLE);
                    binding.prLoadingBar.setVisibility(View.GONE);
                    try {
                        ArrayList<TrayModel> arrayListTrayModel = new ArrayList<>();
                        for (int i = 0; i < response.getTray().size(); i++) {
                            try {
                                if (response.getTray().get(i).getUser().getFull_name() != null) {
                                    arrayListTrayModel.add(response.getTray().get(i));
                                }
                            } catch (Exception ex) {
                            }
                        }
                        userListAdapter = new UserListAdapter(activity, arrayListTrayModel,
                                activity);
                        binding.RVUserList.setAdapter(userListAdapter);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(Throwable e) {
                    binding.prLoadingBar.setVisibility(View.GONE);
                    e.printStackTrace();
                }

                @Override
                public void onComplete() {
                    binding.prLoadingBar.setVisibility(View.GONE);
                }
            };
    private DisposableObserver<FullDetailModel> storyDetailObserver = new DisposableObserver<FullDetailModel>() {
        @Override
        public void onNext(FullDetailModel response) {
            binding.RVUserList.setVisibility(View.VISIBLE);
            binding.prLoadingBar.setVisibility(View.GONE);
            try {
                storiesListAdapter = new StoriesListAdapter(activity, response.getReel_feed().getItems());
                binding.RVStories.setAdapter(storiesListAdapter);
                storiesListAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(Throwable e) {
            binding.prLoadingBar.setVisibility(View.GONE);
            e.printStackTrace();
        }

        @Override
        public void onComplete() {
            binding.prLoadingBar.setVisibility(View.GONE);
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_instagram);


        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAGS_CHANGED);
        window.setStatusBarColor(Color.parseColor("#FF3700B3"));

        context = activity = this;

        appLangSessionManager = new SessionManager(activity);
//        setLocale(appLangSessionManager.getLanguage());


        commonClassForAPI = CommonClassForAPI.getInstance(activity);
        createFileFolder();

//        AdsUtils.showGoogleBannerAd(InstagramActivity.this,binding.adView);
//        rewardedVideoAdsINIT();

        initViews();

    }

    @Override
    protected void onResume() {
        super.onResume();
        context = activity = this;
        assert activity != null;
        clipBoard = (ClipboardManager) activity.getSystemService(CLIPBOARD_SERVICE);
        PasteText();
    }

    private void initViews() {
        clipBoard = (ClipboardManager) activity.getSystemService(CLIPBOARD_SERVICE);

        binding.imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        if (!SharePrefs.getInstance(activity).getBoolean(SharePrefs.ISSHOWHOWTOINSTA)) {
            SharePrefs.getInstance(activity).putBoolean(SharePrefs.ISSHOWHOWTOINSTA, true);

        }


        binding.loginBtn1.setOnClickListener(v -> {

            String LL = binding.etText.getText().toString();
            if (LL.equals("")) {
                Utils.setToast(activity, getResources().getString(R.string.enter_url));
            } else if (!Patterns.WEB_URL.matcher(LL).matches()) {
                Utils.setToast(activity, getResources().getString(R.string.enter_valid_url));
            } else {
                GetInstagramData();
                showRewardedVideoAds();
            }
            showRewardedVideoAds();

        });

        binding.tvPaste.setOnClickListener(v -> {
            PasteText();
        });
        binding.LLOpenInstagram.setOnClickListener(v -> {
            Utils.OpenApp(activity, "com.instagram.android");
        });

        GridLayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        binding.RVUserList.setLayoutManager(mLayoutManager);
        binding.RVUserList.setNestedScrollingEnabled(false);
        mLayoutManager.setOrientation(RecyclerView.HORIZONTAL);


        if (SharePrefs.getInstance(activity).getBoolean(SharePrefs.ISINSTALOGIN)) {
            layoutCondition();
            callStoriesApi();
            binding.SwitchLogin.setChecked(true);
        } else {
            binding.SwitchLogin.setChecked(false);
        }

        binding.tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity,
                        LoginActivity.class);
                startActivityForResult(intent, 100);
            }
        });

        binding.RLLoginInstagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!SharePrefs.getInstance(activity).getBoolean(SharePrefs.ISINSTALOGIN)) {
                    Intent intent = new Intent(activity,
                            LoginActivity.class);
                    startActivityForResult(intent, 100);
                } else {
                    AlertDialog.Builder ab = new AlertDialog.Builder(activity);
                    ab.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            SharePrefs.getInstance(activity).putBoolean(SharePrefs.ISINSTALOGIN, false);
                            SharePrefs.getInstance(activity).putString(SharePrefs.COOKIES, "");
                            SharePrefs.getInstance(activity).putString(SharePrefs.CSRF, "");
                            SharePrefs.getInstance(activity).putString(SharePrefs.SESSIONID, "");
                            SharePrefs.getInstance(activity).putString(SharePrefs.USERID, "");

                            if (SharePrefs.getInstance(activity).getBoolean(SharePrefs.ISINSTALOGIN)) {
                                binding.SwitchLogin.setChecked(true);
                            } else {
                                binding.SwitchLogin.setChecked(false);
                                binding.RVUserList.setVisibility(View.GONE);
                                binding.RVStories.setVisibility(View.GONE);
                                binding.tvViewStories.setText(activity.getResources().getText(R.string.view_stories));
                                binding.tvLogin.setVisibility(View.VISIBLE);
                            }
                            dialog.cancel();

                        }
                    });
                    ab.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog alert = ab.create();
                    alert.setTitle(getResources().getString(R.string.do_u_want_to_download_media_from_pvt));
                    alert.show();
                }

            }
        });

        GridLayoutManager mLayoutManager1 = new GridLayoutManager(getApplicationContext(), 3);
        binding.RVStories.setLayoutManager(mLayoutManager1);
        binding.RVStories.setNestedScrollingEnabled(false);
        mLayoutManager1.setOrientation(RecyclerView.VERTICAL);

    }

    public void layoutCondition() {
        binding.tvViewStories.setText(activity.getResources().getString(R.string.stories));
        binding.tvLogin.setVisibility(View.GONE);

    }

    private void GetInstagramData() {
        try {
            createFileFolder();
            URL url = new URL(binding.etText.getText().toString());
            String host = url.getHost();
            Log.e("initViews: ", host);
            if (host.equals("www.instagram.com")) {
                callDownload(binding.etText.getText().toString());
                showRewardedVideoAds();
            } else {
                Utils.setToast(activity, getResources().getString(R.string.enter_valid_url));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void PasteText() {
        try {
            binding.etText.setText("");
            String CopyIntent = getIntent().getStringExtra("CopyIntent");
            if (CopyIntent.equals("")) {
                if (!(clipBoard.hasPrimaryClip())) {

                } else if (!(clipBoard.getPrimaryClipDescription().hasMimeType(MIMETYPE_TEXT_PLAIN))) {
                    if (clipBoard.getPrimaryClip().getItemAt(0).getText().toString().contains("instagram.com")) {
                        binding.etText.setText(clipBoard.getPrimaryClip().getItemAt(0).getText().toString());
                    }

                } else {
                    ClipData.Item item = clipBoard.getPrimaryClip().getItemAt(0);
                    if (item.getText().toString().contains("instagram.com")) {
                        binding.etText.setText(item.getText().toString());
                    }

                }
            } else {
                if (CopyIntent.contains("instagram.com")) {
                    binding.etText.setText(CopyIntent);
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getUrlWithoutParameters(String url) {
        try {
            URI uri = new URI(url);
            return new URI(uri.getScheme(),
                    uri.getAuthority(),
                    uri.getPath(),
                    null, // Ignore the query part of the input url
                    uri.getFragment()).toString();
        } catch (Exception e) {
            e.printStackTrace();
            Utils.setToast(activity, getResources().getString(R.string.enter_valid_url));
            return "";
        }
    }

    private void callDownload(String Url) {
        String UrlWithoutQP = getUrlWithoutParameters(Url);
        UrlWithoutQP = UrlWithoutQP + "?__a=1";
        try {
            Utils utils = new Utils(activity);
            if (utils.isNetworkAvailable()) {
                if (commonClassForAPI != null) {
                    Utils.showProgressDialog(activity);
                    commonClassForAPI.callResult(instaObserver, UrlWithoutQP,
                            "ds_user_id=" + SharePrefs.getInstance(activity).getString(SharePrefs.USERID)
                                    + "; sessionid=" + SharePrefs.getInstance(activity).getString(SharePrefs.SESSIONID));
                }
            } else {
                Utils.setToast(activity, getResources().getString(R.string.no_net_conn));
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public String getImageFilenameFromURL(String url) {
        try {
            return new File(new URL(url).getPath().toString()).getName();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return System.currentTimeMillis() + ".png";
        }
    }

    public String getVideoFilenameFromURL(String url) {
        try {
            return new File(new URL(url).getPath().toString()).getName();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return System.currentTimeMillis() + ".mp4";
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        instaObserver.dispose();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == 100 && resultCode == RESULT_OK) {
                String requiredValue = data.getStringExtra("key");
                if (SharePrefs.getInstance(activity).getBoolean(SharePrefs.ISINSTALOGIN)) {
                    binding.SwitchLogin.setChecked(true);
                    layoutCondition();
                    callStoriesApi();
                } else {
                    binding.SwitchLogin.setChecked(false);
                }

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);


    }

    private void callStoriesApi() {
        try {
            Utils utils = new Utils(activity);
            if (utils.isNetworkAvailable()) {
                if (commonClassForAPI != null) {
                    binding.prLoadingBar.setVisibility(View.VISIBLE);
                    commonClassForAPI.getStories(storyObserver, "ds_user_id=" + SharePrefs.getInstance(activity).getString(SharePrefs.USERID)
                            + "; sessionid=" + SharePrefs.getInstance(activity).getString(SharePrefs.SESSIONID));
                }
            } else {
                Utils.setToast(activity, activity
                        .getResources().getString(R.string.no_net_conn));
            }
        } catch (Exception e) {
            e.printStackTrace();

        }


    }

    @Override
    public void userListClick(int position, TrayModel trayModel) {
        callStoriesDetailApi(String.valueOf(trayModel.getUser().getPk()));
    }

    @Override
    public void fbUserListClick(int position, NodeModel trayModel) {

    }

    private void callStoriesDetailApi(String UserId) {
        try {
            Utils utils = new Utils(activity);
            if (utils.isNetworkAvailable()) {
                if (commonClassForAPI != null) {
                    binding.prLoadingBar.setVisibility(View.VISIBLE);
                    commonClassForAPI.getFullDetailFeed(storyDetailObserver, UserId, "ds_user_id=" + SharePrefs.getInstance(activity).getString(SharePrefs.USERID)
                            + "; sessionid=" + SharePrefs.getInstance(activity).getString(SharePrefs.SESSIONID));
                }
            } else {
                Utils.setToast(activity, activity
                        .getResources().getString(R.string.no_net_conn));
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    //RewardedAds : Start
    public void rewardedVideoAdsINIT() {
     /*   rewardedAd = new RewardedAd(this,
                getResources().getString(R.string.admob_rewareded_ad));

        RewardedAdLoadCallback adLoadCallback = new RewardedAdLoadCallback() {
            @Override
            public void onRewardedAdLoaded() {
                // Ad successfully loaded.

                Log.d(TAG, "onRewardedAdLoaded: ");

            }

            @Override
            public void onRewardedAdFailedToLoad(int i) {
                Log.d(TAG, "onRewardedAdFailedToLoad: "+i);
            }
        };
        rewardedAd.loadAd(new AdRequest.Builder().build(), adLoadCallback);*/
    }

    private void showRewardedVideoAds() {
      /*  if (rewardedAd.isLoaded()) {

            RewardedAdCallback adCallback = new RewardedAdCallback() {
                @Override
                public void onRewardedAdOpened() {
                    // Ad opened.
                }

                @Override
                public void onRewardedAdClosed() {
                    // Ad closed.
                }

                @Override
                public void onUserEarnedReward(@NonNull RewardItem reward) {
                    // User earned reward.
                }

            };
            rewardedAd.show(activity, adCallback);
        } else {
            Log.d("TAG", "The rewarded ad wasn't loaded yet.");
        }*/
    }
}
