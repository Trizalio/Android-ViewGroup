package trizalio.viewgrouptest;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;
import android.widget.TextView;

/**
 * Created by trizalio on 13.11.14.
 */
public class CustomViewGroup extends ViewGroup {
    private static final String LOG_TAG = "CustomViewGroup";
    private static final String ACTION_TAG = "ACTION_TAG";
    private static final String ACTION_MOVE = "ACTION_MOVE";
    private static boolean mFling = false;

    private static float baseX = 0;
    private static float startX = 0;
    private static float shiftX = 0;
    private static float currentX = 0;
    private static float currentY = 0;
    private static Scroller mScroller;
    private static Handler mHandler;
    private static GestureDetector mDetector;
    private final GestureDetector.SimpleOnGestureListener mGestureListener
            = new GestureDetector.SimpleOnGestureListener() {

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2,
                               float velocityX, float velocityY) {
            Log.v(LOG_TAG, "GestureDetector.onFling");
            Log.v(LOG_TAG, String.valueOf(velocityX));
                if(velocityX > 7000 || velocityX < -7000){
                    Log.v(LOG_TAG, "DO IT, BITCH!!!");
                    mFling = true;
                    currentX = shiftX + baseX;
                    //int targetX = getChildAt(1).getMeasuredWidth();
                    int ScrollX = 0;
                    int flingVelocityX = 0;
                    if(velocityX > 7000) {
                        Log.v(LOG_TAG, "Right");
                        ScrollX = getChildAt(1).getMeasuredWidth();
                        flingVelocityX = 10000;
                        mScroller.fling((int)(currentX),(int)(currentY), (int)(flingVelocityX), 0, (int)(ScrollX), (int)(0), 0, (0));
                    }else{
                        Log.v(LOG_TAG, "Left");
                        ScrollX = 0;
                        flingVelocityX = -10000;
                        mScroller.fling((int)(currentX),(int)(currentY), (int)(flingVelocityX), 0, (int)(0), (int)(ScrollX), 0, (0));
                    }
                    //mScroller.startScroll((int)(currentX),(int)(currentY),(int)(ScrollX),(int)(0));
                    //mScroller.fling((int)(currentX),(int)(currentY), (int)(flingVelocityX), 0, (int)(0), (int)(ScrollX), 0, (0));
                    Message massage = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putString(ACTION_TAG, ACTION_MOVE);
                    massage.setData(bundle);
                    massage.setTarget(mHandler);
                    massage.sendToTarget();
                }
            return true;
        }
    };

    public CustomViewGroup(Context context) {
        super(context);
        Log.v(LOG_TAG, "CustomViewGroup(Context context)");
        addView(new View(this.getContext()));
        addView(new View(this.getContext()));
        getChildAt(0).setBackgroundColor(Color.BLUE);
        getChildAt(1).setBackgroundColor(Color.BLACK);
        mScroller = new Scroller(getContext());
        mHandler = new Handler() {
            public void handleMessage(android.os.Message msg) {
                if(msg.getData().getString(ACTION_TAG).equals(ACTION_MOVE)){
                    if(mScroller.computeScrollOffset()){
                        SetOffset(mScroller.getCurrX(), mScroller.getCurrY());
                        Message massage = new Message();
                        Bundle bundle = new Bundle();
                        bundle.putString(ACTION_TAG, ACTION_MOVE);
                        massage.setData(bundle);
                        massage.setTarget(mHandler);
                        massage.sendToTarget();
                    }
                }
                // обновляем TextView
            };
        };
        mDetector = new GestureDetector(mGestureListener);
    }

    public void SetOffset(int posX, int posY){
        baseX = posX;
        currentX = baseX;
        //baseX = posX;
        requestLayout();
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
        //Log.v(LOG_TAG, "onTouchEvent(MotionEvent event)");
        int action = event.getAction();
        //Log.v(LOG_TAG, "action: " + action);
        mDetector.onTouchEvent(event);
        if(action == 0){

            startX = event.getX();
            shiftX = 0;
            currentX = baseX;
        }else if(action == 1){
            if(mFling) {
                mFling = false;
            }else {
                Log.v(LOG_TAG, "onMouseUp");
                currentX = shiftX + baseX;
                int targetX = 0;
                if (shiftX + baseX > getChildAt(1).getMeasuredWidth() / 2) {
                    targetX = getChildAt(1).getMeasuredWidth();
                    //Log.v(LOG_TAG, String.valueOf(getChildAt(1).getMeasuredWidth()));
                } else if (shiftX + baseX < getChildAt(1).getMeasuredWidth() / 2) {
                    targetX = 0;
                }
                int ScrollX = targetX - (int) (currentX);
                mScroller.startScroll((int) (currentX), (int) (currentY), (int) (ScrollX), (int) (0));
                Message massage = new Message();
                Bundle bundle = new Bundle();
                bundle.putString(ACTION_TAG, ACTION_MOVE);
                massage.setData(bundle);
                massage.setTarget(mHandler);
                massage.sendToTarget();
                shiftX = 0;
            }
        }else if(action == 2){
            shiftX = event.getX() - startX;
            currentX = shiftX + baseX;
            if(currentX > getChildAt(1).getMeasuredWidth()){
                currentX = getChildAt(1).getMeasuredWidth();
                shiftX = currentX - baseX;
            }
            requestLayout();
        }
        //Log.v(LOG_TAG, "startX: " + startX);
        //Log.v(LOG_TAG, "shiftX: " + shiftX);
        return true;
        //return super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptHoverEvent(MotionEvent event) {
        //Log.v(LOG_TAG, "onInterceptHoverEvent(MotionEvent event)");
        return super.onInterceptHoverEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //Log.v(LOG_TAG, "onInterceptTouchEvent(MotionEvent ev)");
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
        //Log.v(LOG_TAG, "onLayout(boolean changed, int l, int t, int r, int b)");
        getChildAt(0).layout(0, 0, getChildAt(0).getMeasuredWidth(), getChildAt(0).getMeasuredHeight());
        getChildAt(1).layout(-getChildAt(1).getMeasuredWidth() + (int) (currentX), 0, (int) (currentX), getChildAt(1).getMeasuredHeight());

    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //Log.v(LOG_TAG, "onMeasure(int widthMeasureSpec, int heightMeasureSpec)");
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        int newWidth = MeasureSpec.makeMeasureSpec(width - 300, MeasureSpec.EXACTLY);
        //Log.v(LOG_TAG, String.valueOf(MeasureSpec.getSize(newWidth)));
        //MeasureSpec.makeMeasureSpec(widthMeasureSpec, );

        getChildAt(0).measure(widthMeasureSpec, heightMeasureSpec);
        getChildAt(1).measure(newWidth, heightMeasureSpec);

        setMeasuredDimension(width, height);

    }
}
