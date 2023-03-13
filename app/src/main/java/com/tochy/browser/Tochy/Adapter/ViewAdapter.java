package com.tochy.browser.Tochy.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.tochy.browser.R;
import com.tochy.browser.Tochy.utils.Method;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;

public class ViewAdapter extends RecyclerView.Adapter<ViewAdapter.ViewHolder> {

    private Activity activity;
    private int columnWidth;
    private List<File> stringList;
    private String string;
    private Method method;


    public ViewAdapter(Activity activity, List<File> stringList, String string, Method method) {
        this.activity = activity;
        this.stringList = stringList;
        this.string = string;
        this.method = method;
        columnWidth = method.getScreenWidth();
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(activity).inflate(R.layout.view_adapter, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.imageView.setLayoutParams(new ConstraintLayout.LayoutParams(columnWidth / 2, columnWidth / 2));

        Glide.with(activity).load(stringList.get(position).toString())
                .placeholder(R.drawable.place_portrait).into(holder.imageView);

        holder.imageView.setOnClickListener(v -> method.click(position, string));

    }

    @Override
    public int getItemCount() {
        return stringList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView_view_adapter);

        }
    }
}
