package com.tochy.browser.Gallery;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tochy.browser.R;


/**
 * Author CodeBoy722
 * <p>
 * picture_Adapter's ViewHolder
 */

public class PicHolder extends RecyclerView.ViewHolder {

    public ImageView picture;

    PicHolder(@NonNull View itemView) {
        super(itemView);

        picture = itemView.findViewById(R.id.image);
    }
}
