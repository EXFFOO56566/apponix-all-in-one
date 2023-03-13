package com.tochy.browser.Tochy.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tochy.browser.R;
import com.tochy.browser.Tochy.Adapter.ViewAdapter;
import com.tochy.browser.Tochy.ShowItem;
import com.tochy.browser.Tochy.interfaces.OnClick;
import com.tochy.browser.Tochy.utils.Constant;
import com.tochy.browser.Tochy.utils.Method;
import com.google.android.material.textview.MaterialTextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class VideoFragment extends Fragment {

    Method method;
    OnClick onClick;
    String root;
    ProgressBar progressBar;
    RecyclerView recyclerView;
    ViewAdapter viewAdapter;
    MaterialTextView textviewNodata;
    LayoutAnimationController animation;
    Object url = "/WhatsApp/Media/.Statuses/";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment, container, false);

        onClick = (position, type) -> startActivity(new Intent(getActivity(), ShowItem.class)
                .putExtra("position", position)
                .putExtra("type", type));
        method = new Method(getActivity(), onClick);

        int resId = R.anim.layout_animation_from_bottom;
        animation = AnimationUtils.loadLayoutAnimation(getActivity(), resId);

        progressBar = view.findViewById(R.id.progressbar_fragment);
        recyclerView = view.findViewById(R.id.recyclerView_fragment);
        textviewNodata = view.findViewById(R.id.textView_noData_fragment);
        textviewNodata.setVisibility(View.GONE);

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(layoutManager);

        new DataVideo().execute();

        return view;

    }


    @SuppressLint("StaticFieldLeak")
    @Deprecated
    public class DataVideo extends AsyncTask<String, String, String> {
        private List<File> getListFiles(File parentDir) {
            List<File> inFiles = new ArrayList<>();
            try {
                Queue<File> files = new LinkedList<>(Arrays.asList(parentDir.listFiles()));
                while (!files.isEmpty()) {
                    File file = files.remove();
                    if (file.isDirectory()) {
                        files.addAll(Arrays.asList(file.listFiles()));
                    } else if (file.getName().endsWith(".mp4")) {
                        inFiles.add(file);
                    }
                }
            } catch (Exception e) {
                Log.d("error", e.toString());
            }
            return inFiles;
        }

        boolean isVlaue = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressBar.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(String... strings) {

            root = Environment.getExternalStorageDirectory().toString() + url;
            File file = new File(root);
            Constant.videoArray.clear();
            Constant.videoArray = getListFiles(file);

            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            if (!isVlaue) {
                if (Constant.videoArray.isEmpty()) {
                    textviewNodata.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                } else {
                    textviewNodata.setVisibility(View.GONE);
                    viewAdapter = new ViewAdapter(getActivity(), Constant.videoArray, "video", method);
                    recyclerView.setAdapter(viewAdapter);
                    recyclerView.setLayoutAnimation(animation);
                }
            } else {
                textviewNodata.setVisibility(View.VISIBLE);
            }
            progressBar.setVisibility(View.GONE);

            super.onPostExecute(s);
        }
    }

}
