package com.tochy.browser.Activity.browser;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.tochy.browser.R;
import com.tochy.browser.SessionManager;
import com.tochy.browser.databinding.ActivityBookamrksBinding;
import com.tochy.browser.retrofit.Const;

import java.util.ArrayList;

public class BookamrksActivity extends AppCompatActivity implements BookamrksAdapter.OnBookmarkClickListner {

    public static final int BOOKMAR_ACTIVITY_REQUEST_CODE = 1002;
    ActivityBookamrksBinding binding;
    private SessionManager sessonManager;
    private ArrayList<String> list = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_bookamrks);
        sessonManager = new SessionManager(this);
        initView();
        initListnear();


    }


    private void initListnear() {
        binding.tvClear.setOnClickListener(v -> clearBookmarks());
    }

    private void clearBookmarks() {
        ArrayList<String> b = sessonManager.getBookamrks();
        for (int i = 0; i <= b.size() - 1; i++) {
            sessonManager.toggleBookmark(b.get(i));
        }
        initView();
    }

    private void initView() {
        list.clear();
        list = sessonManager.getBookamrks();
        if (!list.isEmpty()) {
            BookamrksAdapter bookamrksAdapter = new BookamrksAdapter(list, this);
            binding.rvBookamrk.setAdapter(bookamrksAdapter);
        } else {
            Toast.makeText(this, "Bookmarks not found", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onBookmarkClick(String url, String work) {
        if (work.equals("DELETE")) {
            int i = sessonManager.toggleBookmark(url);
            if (i == 0) {
                Toast.makeText(this, "Bookmark Removed", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Bookmarked", Toast.LENGTH_SHORT).show();
            }
            initView();
        }
        if (work.equals("OPEN")) {
            if (sessonManager.getBooleanValue(Const.isMe)) {
                sessonManager.saveBooleanValue(Const.isMe, false);
                Intent intent = new Intent(BookamrksActivity.this, WebActivity.class);
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