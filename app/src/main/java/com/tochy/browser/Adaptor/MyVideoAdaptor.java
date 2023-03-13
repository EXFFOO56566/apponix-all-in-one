package com.tochy.browser.Adaptor;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tochy.browser.Model.Videomodel;
import com.tochy.browser.R;
import com.tochy.browser.databinding.ItemVideoBinding;

import java.util.ArrayList;

public class MyVideoAdaptor extends RecyclerView.Adapter<MyVideoAdaptor.VideoViewHolder> {
    Context context;
    ArrayList<Videomodel> allvideos;

    OnVidClicklistnear onVidClicklistnear;



    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.item_video, parent, false);
        return new VideoViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        final Videomodel videofacer = allvideos.get(position);
        Log.d("iiiiiadapter", Videomodel.class.toString());

        if (videofacer.getThumbPath().equals("null")) {
            holder.binding.thumbimage.setImageResource(R.drawable.ic_launcher_foreground);
        } else {
            holder.binding.thumbimage.setImageURI(Uri.parse(videofacer.getThumbPath()));
        }

        holder.binding.thumbimagename.setText(videofacer.getVidName());

        holder.binding.thumbimage.setOnClickListener(view -> {
            if (holder.binding.imagechkbox.isChecked()) {
                videofacer.setSelected(false);
                onVidClicklistnear.onVidClick(videofacer, false);
                holder.binding.imagechkbox.setChecked(false);
            } else {
                onVidClicklistnear.onVidClick(videofacer, true);
                videofacer.setSelected(true);
                holder.binding.imagechkbox.setChecked(true);
            }

        });
        holder.binding.thumbimage.setOnLongClickListener(view -> {
            onVidClicklistnear.onVidLongClick(videofacer);
            return true;
        });

    }

    @Override
    public int getItemCount() {
        return allvideos.size();
    }

    public interface OnVidClicklistnear {
        void onVidClick(Videomodel picture, boolean b);

        void onVidLongClick(Videomodel picture);

    }

    public class VideoViewHolder extends RecyclerView.ViewHolder {
        ItemVideoBinding binding;

        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemVideoBinding.bind(itemView);
        }

    }
}
