package trizalio.viewgrouptest;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by trizalio on 13.11.14.
 */
public class CustomViewGroup extends ViewGroup {
    private static final String LOG_TAG = "CustomViewGroup";
    private static boolean pressed = false;

    private static float startX = 0;
    private static float currentX = 0;

    public CustomViewGroup(Context context) {
        super(context);
        Log.v(LOG_TAG, "CustomViewGroup(Context context)");
        addView(new View(this.getContext()));
        addView(new View(this.getContext()));
        getChildAt(0).setBackgroundColor(Color.BLUE);
        getChildAt(1).setBackgroundColor(Color.BLACK);
    }

    public CustomViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.v(LOG_TAG, "CustomViewGroup(Context context, AttributeSet attrs)");
    }

    public CustomViewGroup(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        Log.v(LOG_TAG, "CustomViewGroup(Context context, AttributeSet attrs, int defStyle)");
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.v(LOG_TAG, "onTouchEvent(MotionEvent event)");
        int action = event.getAction();
        Log.v(LOG_TAG, "action: " + action);
        if(action == 0){
            pressed = true;
            startX = event.getX();
        }else if(action == 1){
            pressed = false;
        }else if(action == 2){
            currentX = event.getX()-startX;
            requestLayout();
        }
        Log.v(LOG_TAG, "startX: " + startX);
        Log.v(LOG_TAG, "currentX: " + currentX);
        return true;
        //return super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptHoverEvent(MotionEvent event) {
        Log.v(LOG_TAG, "onInterceptHoverEvent(MotionEvent event)");
        return super.onInterceptHoverEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.v(LOG_TAG, "onInterceptTouchEvent(MotionEvent ev)");
        int action = ev.getAction();
        if(action == 0){
            return false;
        }else if(action == 1){
            return true;
        }else if(action == 2){
            return true;
        }
        return true;
        //return super.onInterceptTouchEvent(ev);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.v(LOG_TAG, "onLayout(boolean changed, int l, int t, int r, int b)");
        getChildAt(0).layout(0, 0, getChildAt(0).getMeasuredWidth(), getChildAt(0).getMeasuredHeight());
        getChildAt(1).layout(-getChildAt(0).getMeasuredWidth() + (int) (currentX) + 100, 0, (int) (currentX), getChildAt(0).getMeasuredHeight());

    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.v(LOG_TAG, "onMeasure(int widthMeasureSpec, int heightMeasureSpec)");
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        //MeasureSpec.makeMeasureSpec(widthMeasureSpec, );

        getChildAt(0).measure(widthMeasureSpec, heightMeasureSpec);
        getChildAt(1).measure(widthMeasureSpec, heightMeasureSpec);

        setMeasuredDimension(width, height);

    }
}
