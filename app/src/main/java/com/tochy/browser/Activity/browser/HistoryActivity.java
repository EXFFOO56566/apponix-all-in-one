package com.tochy.browser.Activity.browser;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.tochy.browser.R;
import com.tochy.browser.SessionManager;
import com.tochy.browser.databinding.ActivityHistoryBinding;
import com.tochy.browser.retrofit.Const;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity implements HistoryAdapter.OnHistoryClickListnear {
    public static final int HISTORY_ACTIVITY_CODE = 1001;
    private static final String TAG = "historyact";
    ActivityHistoryBinding binding;
    private SessionManager sessonManager;
    private ArrayList<String> list = new ArrayList<>();
    private boolean showAds = false;
    private String adWebsite;
    private boolean ownloded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_history);

        sessonManager = new SessionManager(this);
        initView();
        initListnear();


    }


    private void initListnear() {
        binding.tvClear.setOnClickListener(v -> clearBookmarks());
    }

    private void clearBookmarks() {
        ArrayList<String> b = sessonManager.getHistory();
        Log.d(TAG, "clearBookmarks: " + b.size());
        for (int i = 0; i < b.size(); i++) {
            sessonManager.removefromHistory(b.get(i));
        }
        Log.d(TAG, "clearBookmarks:size2  " + sessonManager.getHistory().size());
        initView();
    }

    private void initView() {
        list.clear();
        list = sessonManager.getHistory();
        Log.d(TAG, "clearBookmarks:size3  " + sessonManager.getHistory().size());
        Log.d(TAG, "clearBookmarks:size34  " + list.size());


        HistoryAdapter historyAdapter = new HistoryAdapter(list, this);
        binding.rvHistory.setAdapter(historyAdapter);


    }

    @Override
    public void onHistoryClick(String url, String work) {
        if (work.equals("DELETE")) {
            sessonManager.removefromHistory(url);
            initView();
        }
        if (work.equals("OPEN")) {
            if (sessonManager.getBooleanValue(Const.isMe)) {
                sessonManager.saveBooleanValue(Const.isMe, false);
                Intent intent = new Intent(HistoryActivity.this, WebActivity.class);
                intent.putExtra("url", url);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent();
                intent.putExtra("url", url);
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    }

    public void onclickBack(View view) {

        finish();

    }

    @Override
    public void onBackPressed() {

        finish();

    }

}