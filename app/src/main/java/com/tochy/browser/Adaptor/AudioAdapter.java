package com.tochy.browser.Adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.tochy.browser.Model.ModelAudio;
import com.tochy.browser.R;

import java.util.ArrayList;

public class AudioAdapter extends RecyclerView.Adapter<AudioAdapter.viewHolder> {

    public OnItemClickListener onItemClickListener;
    Context context;
    ArrayList<ModelAudio> audioArrayList;

    public AudioAdapter(Context context, ArrayList<ModelAudio> audioArrayList) {
        this.context = context;
        this.audioArrayList = audioArrayList;
    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.audio_list, viewGroup, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(final viewHolder holder, final int i) {
        holder.title.setText(audioArrayList.get(i).getaudioTitle());
        holder.artist.setText(audioArrayList.get(i).getaudioArtist());
    }

    @Override
    public int getItemCount() {
        return audioArrayList.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int pos, View v);
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView title, artist;


        public viewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            artist = (TextView) itemView.findViewById(R.id.artist);

            itemView.setOnClickListener(v -> onItemClickListener.onItemClick(getAdapterPosition(), v));
        }
    }
}
