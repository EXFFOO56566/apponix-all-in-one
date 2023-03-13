package com.tochy.browser.Adaptor;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.tochy.browser.Activity.browser.WebActivity;
import com.tochy.browser.Model.NewsApiRoot;
import com.tochy.browser.R;
import com.tochy.browser.databinding.ItemNewsBinding;

import java.util.ArrayList;
import java.util.List;

public class NewsAdaptor extends RecyclerView.Adapter<NewsAdaptor.NewsViewHolder> {
    Context context;
    List<Object> body = new ArrayList<>();


    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_news, parent, false);
        return new NewsViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        NewsApiRoot.ArticlesItem item = (NewsApiRoot.ArticlesItem) body.get(position);

        if (item.getAuthor() != null) {
            holder.binding.tvChName.setVisibility(View.VISIBLE);
            holder.binding.newsChName.setText(item.getAuthor());
        }
        holder.binding.tvTite.setText(item.getTitle());
        holder.binding.tvDescreption.setText(item.getSummary());
        holder.binding.tvTime.setText(item.getPublishedDate());


        Glide.with(context)
                .load(item.getMedia())
                .transform(new CenterCrop(), new RoundedCorners(25))
                .into(holder.binding.ivNews);

        holder.binding.ivNews.setOnClickListener(v -> {
            Intent intent = new Intent(context, WebActivity.class);
            intent.putExtra("words", item.getLink());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return body.size();
    }

    public void addata(List<NewsApiRoot.ArticlesItem> articles) {
        body.addAll(articles);
        notifyItemRangeInserted(body.size(), articles.size());
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder {
        ItemNewsBinding binding;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemNewsBinding.bind(itemView);
        }
    }
}
