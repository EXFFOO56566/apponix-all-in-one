package com.tochy.browser.Adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tochy.browser.R;
import com.tochy.browser.databinding.ItemRingtoneBinding;

import java.util.ArrayList;


public class RingToneAdaptor extends RecyclerView.Adapter<RingToneAdaptor.RingToneViewHolder> {
    Context context;
    OnClickListner onClickListner;
    ArrayList<String> data;

    public RingToneAdaptor(ArrayList<String> data, OnClickListner onClickListner) {
        this.data = data;
        this.onClickListner = onClickListner;
    }

    @NonNull
    @Override
    public RingToneViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_ringtone, parent, false);
        return new RingToneViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RingToneViewHolder holder, int position) {
        holder.binding.name.setText(data.get(position));


        holder.binding.play.setOnClickListener(v -> {
         /*   holder.binding.play.setVisibility(View.GONE);
            holder.binding.stop.setVisibility(View.VISIBLE);*/
            onClickListner.onClick(data.get(position), holder.binding);
        });

        holder.binding.stop.setOnClickListener(v -> {

            onClickListner.onClick3();
        });
        holder.binding.rlMain.setOnClickListener(v -> {
            onClickListner.onClick2(data.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public interface OnClickListner {
        void onClick(String ringtone, ItemRingtoneBinding binding);

        void onClick2(String ringtone);

        void onClick3();

    }

    public class RingToneViewHolder extends RecyclerView.ViewHolder {
        ItemRingtoneBinding binding;

        public RingToneViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemRingtoneBinding.bind(itemView);
        }
    }
}
