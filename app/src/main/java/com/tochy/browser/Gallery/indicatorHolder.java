package com.tochy.browser.Gallery;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.tochy.browser.R;

/**
 * Author CodeBoy722
 * <p>
 * recyclerViewPagerImageIndicator's ViewHolder
 */
public class indicatorHolder extends RecyclerView.ViewHolder {

    public ImageView image;
    View positionController;
    private CardView card;

    indicatorHolder(@NonNull View itemView) {
        super(itemView);
        image = itemView.findViewById(R.id.imageIndicator);
        card = itemView.findViewById(R.id.indicatorCard);
        positionController = itemView.findViewById(R.id.activeImage);
    }
}
