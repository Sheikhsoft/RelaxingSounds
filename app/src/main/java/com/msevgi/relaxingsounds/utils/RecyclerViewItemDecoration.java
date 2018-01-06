package com.msevgi.relaxingsounds.utils;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.msevgi.relaxingsounds.R;

/**
 * Created by mustafasevgi on 7.01.2018.
 */

public class RecyclerViewItemDecoration extends RecyclerView.ItemDecoration {

    private int mSpacing;

    public RecyclerViewItemDecoration(Context context) {
        this.mSpacing = context.getResources().getDimensionPixelOffset(R.dimen.default_margin);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view); // item position
        if (position == 0) {
            outRect.top = mSpacing;
        }
        outRect.bottom = mSpacing;
        outRect.left = mSpacing;
        outRect.right = mSpacing;
    }
}

