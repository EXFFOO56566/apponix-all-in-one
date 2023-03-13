package com.tochy.browser.Tochy.fragment;

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

public class ImageFragment extends Fragment {

    Method method;
    OnClick onClick;
    String root;
    ProgressBar progressBar;
    RecyclerView recyclerView;
    ViewAdapter viewAdapter;
    MaterialTextView textViewNoData;
    LayoutAnimationController animation;
    String url = "/WhatsApp/Media/.Statuses/";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment, container, false);

        onClick = (position, type) -> startActivity(new Intent(getActivity(), ShowItem.class)
                .putExtra("position", position)
                .putExtra("type", type));
        method = new Method(getActivity(), onClick);

        progressBar = view.findViewById(R.id.progressbar_fragment);
        recyclerView = view.findViewById(R.id.recyclerView_fragment);
        textViewNoData = view.findViewById(R.id.textView_noData_fragment);
        textViewNoData.setVisibility(View.GONE);

        int resId = R.anim.layout_animation_from_bottom;
        animation = AnimationUtils.loadLayoutAnimation(getActivity(), resId);

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(layoutManager);

        new DataImage().execute();

        return view;

    }

    private List<File> getListFiles(File parentDir) {
        List<File> inFiles = new ArrayList<>();
        try {
            Queue<File> files = new LinkedList<>(Arrays.asList(parentDir.listFiles()));
            while (!files.isEmpty()) {
                File file = files.remove();
                if (file.isDirectory()) {
                    files.addAll(Arrays.asList(file.listFiles()));
                } else if (file.getName().endsWith(".jpg") || file.getName().endsWith(".gif")) {
                    inFiles.add(file);
                }
            }
        } catch (Exception e) {
            Log.d("error", e.toString());
        }
        return inFiles;
    }

    public class DataImage extends AsyncTask<String, String, String> {

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
            Log.d("TAG", "doInBackground:=========1 " + file);
            Constant.imageArray.clear();
            Constant.imageArray = getListFiles(file);


            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            if (!isVlaue) {
                Log.d("TAG", "onPostExecute: ============2121");
                if (Constant.imageArray.size() == 0) {
                    textViewNoData.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    Log.d("TAG", "onPostExecute: ============2122");
                } else {
                    textViewNoData.setVisibility(View.GONE);
                    viewAdapter = new ViewAdapter(getActivity(), Constant.imageArray, "image", method);
                    recyclerView.setAdapter(viewAdapter);
                    recyclerView.setLayoutAnimation(animation);
                    Log.d("TAG", "onPostExecute: ============211");
                }
            } else {
                textViewNoData.setVisibility(View.VISIBLE);
            }
            progressBar.setVisibility(View.GONE);

            super.onPostExecute(s);
        }
    }

}
