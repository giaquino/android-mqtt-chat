package com.giaquino.chat.common.widget;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * @author Gian Darren Azriel Aquino
 * @since 5/26/16
 */
public class MarginDecoration extends RecyclerView.ItemDecoration {

    private final int vertical;
    private final int horizontal;

    public MarginDecoration(int vertical, int horizontal) {
        this.vertical = vertical / 2;
        this.horizontal = horizontal;
    }

    @Override public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
        RecyclerView.State state) {
        outRect.top = vertical;
        outRect.left = horizontal;
        outRect.right = horizontal;
        outRect.bottom = vertical;
    }
}
