package com.zinoviev.flickrbrowser;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener
{
    public static interface OnItemClickListener
    {
        public void onItemClick(View view, int position);
        public void onItemLongClick(View view, int position);
    }

    private OnItemClickListener itemClickListener;
    private GestureDetector gestureDetector;

    public RecyclerItemClickListener(Context context, final RecyclerView recyclerView, OnItemClickListener clickListener)
    {
        itemClickListener = clickListener;
        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener()
        {
            public boolean onSingleTapUp(MotionEvent e)
            {
                return true;
            }

            public void onLongPress(MotionEvent e)
            {
                View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());
                if (childView != null && itemClickListener != null)
                {
                    itemClickListener.onItemLongClick(childView, recyclerView.getChildPosition(childView));
                }
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e)
    {
        View childView = rv.findChildViewUnder(e.getX(), e.getY());
        if (childView != null && itemClickListener != null && gestureDetector.onTouchEvent(e))
        {
            itemClickListener.onItemClick(childView, rv.getChildPosition(childView));
        }

        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e)
    {

    }
}
