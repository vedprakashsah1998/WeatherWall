package com.client.vpman.weatherwall.CustomeDesignViewPager;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MotionEventCompat;
import androidx.viewpager.widget.ViewPager;

public class VerticalViewPageAdapter extends ViewPager
{

    float x = 0;
    float mStartDragX = 0;
    private static final float SWIPE_X_MIN_THRESHOLD = 50;
    public VerticalViewPageAdapter(@NonNull Context context) {
        super(context);
        init();
    }

    public VerticalViewPageAdapter(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    private void init() {
        // The majority of the magic happens here
        setPageTransformer(true, new VerticalPageTransformer());
        // The easiest way to get rid of the overscroll drawing that happens on the left and right
        setOverScrollMode(OVER_SCROLL_NEVER);
    }

    private static class VerticalPageTransformer implements ViewPager.PageTransformer
    {

        private static final float MIN_SCALE = 0.75f;
        private static final float MIN_ALPHA = 0.75f;
        @Override
        public void transformPage(@NonNull View view, float position) {

            int pageWidth = view.getWidth();
            int pageHeight = view.getHeight();
            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0);

            } else if (position <= 1) { // [-1,1]
                view.setAlpha(0.95f);


                view.setTranslationX(view.getWidth() * -position);



                float yPosition = position * view.getHeight();
                view.setTranslationY(yPosition);
            }
            else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0);
            }
        }
    }

    private MotionEvent swapXY(MotionEvent ev) {
        float width = getWidth();
        float height = getHeight();

        float newX = (ev.getY() / height) * width;
        float newY = (ev.getX() / width) * height;

        ev.setLocation(newX, newY);

        return ev;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {

        boolean intercepted = super.onInterceptTouchEvent(swapXY(event));
        switch (event.getAction() & MotionEventCompat.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                x = event.getX();
                break;
        }
        swapXY(event); // return touch coordinates to original reference frame for any child views
        return intercepted;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        if (getAdapter() != null) {
            if (getCurrentItem() >= 0 || getCurrentItem() < getAdapter().getCount()) {
                swapXY(ev);
                final int action = ev.getAction();
                switch (action & MotionEventCompat.ACTION_MASK) {
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_UP:
                        mStartDragX = ev.getX();
                        if (x < mStartDragX
                                && (mStartDragX - x > SWIPE_X_MIN_THRESHOLD)
                                && getCurrentItem() > 0) {
                            Log.i("VerticalViewPager", "down " + x + " : " + mStartDragX + " : " + (mStartDragX - x));
                            setCurrentItem(getCurrentItem() - 1, true);
                            return true;
                        } else if (x > mStartDragX
                                && (x - mStartDragX > SWIPE_X_MIN_THRESHOLD)
                                && getCurrentItem() < getAdapter().getCount() - 1) {
                            Log.i("VerticalViewPager", "up " + x + " : " + mStartDragX + " : " + (x - mStartDragX));
                            mStartDragX = 0;
                            setCurrentItem(getCurrentItem() + 1, true);
                            return true;
                        }
                        break;
                }
            } else {
                mStartDragX = 0;
            }
            swapXY(ev);
            return super.onTouchEvent(swapXY(ev));
        }
        return false;
    }

    @Override
    public boolean canScrollHorizontally(int direction) {
        return false;
    }
}
