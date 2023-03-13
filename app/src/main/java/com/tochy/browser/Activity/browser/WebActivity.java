package com.tochy.browser.Activity.browser;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.view.WindowInsets;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LifecycleRegistry;
import androidx.viewpager2.widget.ViewPager2;

import com.tochy.browser.R;
import com.tochy.browser.SessionManager;
import com.tochy.browser.databinding.ActivityWebBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;


public class WebActivity extends AppCompatActivity {
    private static final String TAG = "web:activity";

    private static final int SPEECH_REQUEST_CODE = 1;
    private static final String GOOGLE_URL = "https://www.google.com";
    private static final boolean IS_PRIVATE = false;
    static List<WebsiteModel> websites = new ArrayList<>();
    private final boolean isIncogntigo = false;
    ActivityWebBinding binding;
    String baseGoogleSearch = "https://www.google.com/search?q=";
    private BottomSheetDialog bottomSheetDialog;
    private SessionManager sessonManager;


    private boolean showAds = false;
    private String adWebsite;
    private boolean ownloded = false;
    private ViewPager2Adapter viewPager2Adapter;
    private String fragmentUrl;
    private WebView fragmentWebview;
    private BrowserFragment fragment;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_web);
        sessonManager = new SessionManager(this);

        Intent intent = getIntent();


        View decorView = getWindow().getDecorView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            decorView.setOnApplyWindowInsetsListener((v, insets) -> {
                WindowInsets defaultInsets = v.onApplyWindowInsets(insets);
                return defaultInsets.replaceSystemWindowInsets(
                        defaultInsets.getSystemWindowInsetLeft(),
                        0,
                        defaultInsets.getSystemWindowInsetRight(),
                        defaultInsets.getSystemWindowInsetBottom());
            });
        }
        ViewCompat.requestApplyInsets(decorView);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, android.R.color.transparent));
        }

        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(binding.etSearch.getWindowToken(), 0);



        binding.refres.setOnClickListener(v -> {
            loadUrliNFragment(binding.etSearch.getText().toString());
        });

        viewPager2Adapter = new ViewPager2Adapter(getSupportFragmentManager(), new LifecycleRegistry(this));

        viewPager2Adapter.setViewPagerListnear(new ViewPager2Adapter.ViewPagerListnear() {
            @Override
            public void setUrl(String url) {
                fragmentUrl = url;
                binding.etSearch.setText(url);
            }

            @Override
            public void openHistory() {
                Intent intent = new Intent(WebActivity.this, HistoryActivity.class);
                startActivityForResult(intent, HistoryActivity.HISTORY_ACTIVITY_CODE);
            }

            @Override
            public void setWebObj(WebView webObj) {
                fragmentWebview = webObj;
            }

            @Override
            public void addTab() {
                viewPager2Adapter.addItem(new BrowserFragment(), false, "");
                binding.viewpager2.setCurrentItem(viewPager2Adapter.getItemCount() - 1);

            }

            @Override
            public void openBookmarks() {
                Intent intent = new Intent(WebActivity.this, BookamrksActivity.class);
                startActivityForResult(intent, BookamrksActivity.BOOKMAR_ACTIVITY_REQUEST_CODE);
            }

            @Override
            public void openIntigo() {
                viewPager2Adapter.addItem(new BrowserFragment(), true, "");
                binding.viewpager2.setCurrentItem(viewPager2Adapter.getItemCount() - 1);
                //viewPager2Adapter.getItemAt(binding.viewpager2.getCurrentItem()),true,"");
            }

            @Override
            public void openTabList(Bitmap bitmap, String title) {
                Log.d(TAG, "openTabList: " + title);
                viewPager2Adapter.getList().get(binding.viewpager2.getCurrentItem()).setTitle(title);
                viewPager2Adapter.getList().get(binding.viewpager2.getCurrentItem()).setBitmap(bitmap);
                TabListFragment listFragment = new TabListFragment(viewPager2Adapter.getList(), new TabListFragment.ListFragmentListner() {
                    @Override
                    public void onListClick(BrowserFragment browserFragment, int pos) {
                        binding.viewpager2.setCurrentItem(pos);
                    }

                    @Override
                    public void onClickRemove(BrowserFragment browserFragment, int pos) {
                        viewPager2Adapter.removePage(browserFragment, pos);
                        if (viewPager2Adapter.getList().size() == 0) {
                            viewPager2Adapter.addItem(new BrowserFragment(), false, "");
                            binding.viewpager2.setCurrentItem(viewPager2Adapter.getItemCount() - 1);
                            return;
                        }
                    }

                    @Override
                    public void onClickBookmark() {
                        onBackPressed();
                        Intent intent = new Intent(WebActivity.this, BookamrksActivity.class);
                        startActivityForResult(intent, BookamrksActivity.BOOKMAR_ACTIVITY_REQUEST_CODE);
                    }

                    @Override
                    public void onClickHistory() {
                        onBackPressed();
                        Intent intent = new Intent(WebActivity.this, HistoryActivity.class);
                        startActivityForResult(intent, HistoryActivity.HISTORY_ACTIVITY_CODE);
                    }


                    @Override
                    public void onClickAdd() {
                        viewPager2Adapter.addItem(new BrowserFragment(), false, "");
                        binding.viewpager2.setCurrentItem(viewPager2Adapter.getItemCount() - 1);
                    }

                    @Override
                    public void closeAll() {
                        for (int i = 0; i < viewPager2Adapter.getList().size(); i++) {
                            if (viewPager2Adapter.getItemAt(i) != null) {
                                viewPager2Adapter.removePage(viewPager2Adapter.getItemAt(i), i);
                            }
                        }
                        viewPager2Adapter.addItem(new BrowserFragment(), false, "");
                        binding.viewpager2.setCurrentItem(viewPager2Adapter.getItemCount() - 1);
                    }
                });
                getSupportFragmentManager().beginTransaction()
                        .addToBackStack(null)
                        .add(R.id.framelyt, listFragment).commit();


            }
        });
        binding.viewpager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                Log.d(TAG, "onPageSelected: " + position);
                if (viewPager2Adapter.getList().size() == 0) {
                    viewPager2Adapter.addItem(new BrowserFragment(), false, "");
                    binding.viewpager2.setCurrentItem(viewPager2Adapter.getItemCount() - 1);
                    return;
                }
                if (viewPager2Adapter.getItemAt(position) != null) {
                    fragment = viewPager2Adapter.getItemAt(position);


                    viewPager2Adapter.getItemAt(position).setBrowserFragmentListner(new BrowserFragment.BrowserFragmentListner() {
                        @Override
                        public void setUrl(String url) {
                            setUrlintoTextView(url);
                        }

                        @Override
                        public void setWebObj(WebView webObj) {
                            fragmentWebview = webObj;
                        }

                        @Override
                        public void addTab() {
                            viewPager2Adapter.addItem(new BrowserFragment(), false, "");
                            binding.viewpager2.setCurrentItem(viewPager2Adapter.getItemCount() - 1);
                        }

                        @Override
                        public void openIngontigo() {
                            viewPager2Adapter.addItem(new BrowserFragment(), true, "");
                            binding.viewpager2.setCurrentItem(viewPager2Adapter.getItemCount() - 1);
                        }

                        @Override
                        public void openHistory() {
                            Intent intent = new Intent(WebActivity.this, HistoryActivity.class);
                            startActivityForResult(intent, HistoryActivity.HISTORY_ACTIVITY_CODE);
                        }

                        @Override
                        public void openBookmarks() {
                            Intent intent = new Intent(WebActivity.this, BookamrksActivity.class);
                            startActivityForResult(intent, BookamrksActivity.BOOKMAR_ACTIVITY_REQUEST_CODE);
                        }

                        @Override
                        public void openTabList(Bitmap bitmap, String title) {
                            viewPager2Adapter.getList().get(position).setTitle(title);
                            viewPager2Adapter.getList().get(position).setBitmap(bitmap);


                            TabListFragment listFragment = new TabListFragment(viewPager2Adapter.getList(), new TabListFragment.ListFragmentListner() {
                                @Override
                                public void onListClick(BrowserFragment browserFragment, int pos) {
                                    binding.viewpager2.setCurrentItem(pos);
                                }

                                @Override
                                public void onClickBookmark() {
                                    Intent intent = new Intent(WebActivity.this, BookamrksActivity.class);
                                    startActivityForResult(intent, BookamrksActivity.BOOKMAR_ACTIVITY_REQUEST_CODE);
                                }

                                @Override
                                public void onClickHistory() {
                                    Intent intent = new Intent(WebActivity.this, HistoryActivity.class);
                                    startActivityForResult(intent, HistoryActivity.HISTORY_ACTIVITY_CODE);
                                }

                                @Override
                                public void onClickRemove(BrowserFragment browserFragment, int pos) {
                                    viewPager2Adapter.removePage(browserFragment, pos);
                                    if (viewPager2Adapter.getList().size() == 0) {
                                        viewPager2Adapter.addItem(new BrowserFragment(), false, "");
                                        binding.viewpager2.setCurrentItem(viewPager2Adapter.getItemCount() - 1);
                                        return;
                                    }
                                }

                                @Override
                                public void onClickAdd() {
                                    viewPager2Adapter.addItem(new BrowserFragment(), false, "");
                                    binding.viewpager2.setCurrentItem(viewPager2Adapter.getItemCount() - 1);
                                }

                                @Override
                                public void closeAll() {
                                    for (int i = 0; i < viewPager2Adapter.getList().size(); i++) {
                                        viewPager2Adapter.removePage(viewPager2Adapter.getItemAt(i), i);
                                    }
                                    viewPager2Adapter.addItem(new BrowserFragment(), false, "");
                                    binding.viewpager2.setCurrentItem(viewPager2Adapter.getItemCount() - 1);
                                }
                            });
                            getSupportFragmentManager().beginTransaction()
                                    .addToBackStack("null")
                                    .add(R.id.framelyt, listFragment).commit();


                        }
                    });
                }
            }

        });
        binding.viewpager2.setAdapter(viewPager2Adapter);
        binding.viewpager2.setUserInputEnabled(false);
        viewPager2Adapter.addItem(new BrowserFragment(), false, "");
        binding.viewpager2.setCurrentItem(viewPager2Adapter.getItemCount() - 1);


        getIntentData();

        binding.etSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String text = binding.etSearch.getText().toString();
                if (text.contains("https://") || text.contains("http://") || text.contains(".com")) {
                    loadUrliNFragment(binding.etSearch.getText().toString());
                } else {
                    loadUrliNFragment(baseGoogleSearch + text);

                }

                in.hideSoftInputFromWindow(binding.etSearch.getWindowToken(), 0);
                binding.etSearch.clearFocus();
                return true;
            }
            return false;
        });


        initListnear();


    }


    private void setUrlintoTextView(String url) {
        binding.etSearch.setText(url);
    }

    private void loadUrliNFragment(String toString) {
        viewPager2Adapter.addItem(new BrowserFragment(), IS_PRIVATE, toString);
        binding.viewpager2.setCurrentItem(viewPager2Adapter.getItemCount() - 1);
    }


    private void getIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            String str = intent.getStringExtra("words");
            if (str != null && !str.equals("")) {

                if (str.equals("about")) {
                    loadUrliNFragment("https://fennecbrowser.com/about-us");
                } else if (str.equals("terms")) {
                    loadUrliNFragment("https://fennecbrowser.com/terms-conditions");
                } else {
                    binding.etSearch.setText(str);
                    loadUrliNFragment(str);

//                    binding.etSearch.setText(baseGoogleSearch + str);
//                    loadUrliNFragment(baseGoogleSearch + str);

                }

            } else {
                loadUrliNFragment(intent.getStringExtra("url"));
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    private void initListnear() {

        binding.googleMic.setOnClickListener(v -> {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
// Start the activity, the intent will be populated with the speech text
            startActivityForResult(intent, SPEECH_REQUEST_CODE);
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: code" + requestCode);
        Log.d(TAG, "onActivityResult: code" + resultCode);
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> results = null;
            if (data != null) {
                results = data.getStringArrayListExtra(
                        RecognizerIntent.EXTRA_RESULTS);
            }
            String spokenText = null;
            if (results != null) {
                spokenText = results.get(0);
            }
            // Do something with spokenText
            binding.etSearch.setText(baseGoogleSearch + spokenText);
            loadUrliNFragment(baseGoogleSearch + spokenText);
        }

        if (requestCode == BookamrksActivity.BOOKMAR_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Log.d(TAG, "onActivityResult: requestcode " + requestCode);
                Log.d(TAG, "onActivityResult: result  " + resultCode);
                if (data != null) {
                    Log.d(TAG, "onActivityResult: datawork " + data.getStringExtra("url"));
                    loadUrliNFragment(data.getStringExtra("url"));
                }

            }


        }

        if (requestCode == HistoryActivity.HISTORY_ACTIVITY_CODE) {
            if (resultCode == RESULT_OK) {
                Log.d(TAG, "onActivityResult: requestcode " + requestCode);
                Log.d(TAG, "onActivityResult: result  " + resultCode);
                if (data != null) {
                    Log.d(TAG, "onActivityResult: datawork " + data.getStringExtra("url"));
                    loadUrliNFragment(data.getStringExtra("url"));
                }

            }


        }
    }


    public void onclickApp(View view) {
        finish();
    }
}