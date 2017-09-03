package com.example.android.popularmoviesapp.utils;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by dell on 23/08/2017.
 */

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {

    private final int mSpaces;

    public SpacesItemDecoration(int mSpaces) {
        this.mSpaces = mSpaces;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = mSpaces;
        outRect.right = mSpaces;
        outRect.bottom = mSpaces;

        if(parent.getChildAdapterPosition(view) == 0){
            outRect.top = mSpaces;
        }
    }
}
