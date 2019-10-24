package com.legend.liteim.common.adapter;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.listener.OnItemSwipeListener;

/**
 * @author Legend
 * @data by on 19-10-8.
 * @description
 */
public abstract class OnItemSwipeAdapter implements OnItemSwipeListener {

    @Override
    public void onItemSwipeStart(RecyclerView.ViewHolder viewHolder, int pos) {

    }

    @Override
    public void clearView(RecyclerView.ViewHolder viewHolder, int pos) {

    }

    @Override
    public void onItemSwipeMoving(Canvas canvas, RecyclerView.ViewHolder viewHolder, float dX, float dY, boolean isCurrentlyActive) {

    }
}
