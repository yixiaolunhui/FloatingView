package com.dalong.floatview;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;

import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.PropertyValuesHolder;
import com.nineoldandroids.view.ViewHelper;

/**
 * 首页浮球view
 * Created by zhouweilong on 16/9/19.
 */
public class FloatingView extends LinearLayout {

    //归位时间 向左或者向右的时间值
    public long  mHomingDuration=200L;

    //缩放值 大于1就是放大   小于1就是缩小
    public float mScale=1.4f;

    //显示的百分比
    public float mPercent=1.0f;

    //浮球的大小 宽高
    private int iconWidth=0,iconHeight=0;

    //触摸x,y值改变多少就移动浮球的位置
    private int mTouchSlop=3;

    // 边界点 其他滑动区域
    private int offsetX,offsetY;

    //浮球的活动范围
    private Rect mVisibityRect=new Rect();

    // 是否是第一次
    private boolean isFrist=false;

    //记录按下时的x坐标
    private int lastX;

    //记录按下时的y坐标
    private int lastY;

    //是否滑动中
    boolean isTouchMove=false;

    private Context mContext;

    public FloatingView(Context context) {
        this(context,null);
    }

    public FloatingView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public FloatingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext=context;
        TypedArray typedArray=context.obtainStyledAttributes(attrs, R.styleable.FloatingView);
        mHomingDuration=typedArray.getInt(R.styleable.FloatingView_dl_duration, (int) mHomingDuration);
        mPercent=typedArray.getFloat(R.styleable.FloatingView_dl_percent,mPercent);
        mScale=typedArray.getFloat(R.styleable.FloatingView_dl_scale,mScale);
        typedArray.recycle();
    }


    /**
     * 添加view
     * @param layoutId
     */
    public  FloatingView  addFloatingView(int layoutId){
        removeAllViews();
        addView(LayoutInflater.from(mContext).inflate(layoutId,null));
        return  this;
    }

    /**
     * 添加view
     * @param view
     */
    public  FloatingView  addFloatingView(View view){
        removeAllViews();
        addView(view);
        return  this;
    }

    /**
     * 设置左右归位的时间
     * @param duration
     * @return
     */
    public FloatingView setFloatingDuration(long duration){
        this.mHomingDuration=duration;
        return this;
    }

    /**
     * 设置按下悬浮view的缩放值
     * @param mScale
     * @return
     */
    public FloatingView setFloatingScale(float mScale){
        this.mScale=mScale;
        return this;
    }


    /**
     * 设置左右显示的百分比
     * @param mPercent
     * @return
     */
    public FloatingView setFloatingPercent(float mPercent){
        this.mPercent=mPercent;
        return this;
    }


    /**
     * 初始化全局变量值
     */
    public void init(){
        offsetX=0;
        offsetY= getStatusBarHeight();
        mVisibityRect.set(0,0,getScreenWidthPix(getContext()),getScreenHeightPix(getContext()));
        iconWidth=getMeasuredWidth();
        iconHeight=getMeasuredHeight();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        init();
        //第一次的位置 默认值
        if(!isFrist){
            ViewHelper.setTranslationX(this,getScreenWidthPix(getContext())-iconWidth*mPercent);
            ViewHelper.setTranslationY(this,(getScreenHeightPix(getContext())-offsetY)-iconHeight-dp2px(50));
            isFrist=true;
        }
    }


    /**
     * 触摸监听是否是移动浮标还是点击浮标
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        init();
        //偏移 状态栏
        int x= (int) event.getRawX()-offsetX;
        int y= (int) event.getRawY()-offsetY;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isTouchMove=false;
                lastX=x;
                lastY=y;
                scaleAnimation(true);
                break;
            case MotionEvent.ACTION_MOVE:
                float deltaX=x-lastX;
                float deltaY=y-lastY;

                if(Math.abs(deltaX)>mTouchSlop|| Math.abs(deltaY)>mTouchSlop){
                    transientXY(x,y);
                    isTouchMove=true;
                }

                lastX=x;
                lastY=y;

                if(isTouchMove)
                    return true;

                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                lastX=0;
                lastY=0;
                scaleAnimation(false);
                if(isTouchMove) {
                    isTouchMove=false;
                    moveToLeftOrRright(x);
                    return true;
                }else{
                    return super.onTouchEvent(event);
                }


            default:
                break;
        }

        return super.onTouchEvent(event);
    }

    /**
     * 手指松开时设置位置
     * @param x
     * @param y
     */
    public void transientXY(int x,int y){
        if(mVisibityRect.contains(x,y)){
            x-=iconWidth/2;
            y-=iconHeight/2;
            y=y<0?0:y;//当y为负值的时候设置为0
            ViewHelper.setTranslationX(this,x);
            ViewHelper.setTranslationY(this,y);
        }
    }

    /**
     * 设置缩放动画
     * @param isBig 是否缩放表大
     */
    public void scaleAnimation(boolean isBig){
        if(isBig){
            ObjectAnimator.ofPropertyValuesHolder(this, PropertyValuesHolder.ofFloat("scaleX",mScale),
                    PropertyValuesHolder.ofFloat("scaleY",mScale)).setDuration(150L).start();
        }else{
            ObjectAnimator.ofPropertyValuesHolder(this, PropertyValuesHolder.ofFloat("scaleX",1f),
                    PropertyValuesHolder.ofFloat("scaleY",1f)).setDuration(150L).start();
        }
    }


    /**
     * 根据是否是需要向左移动  如果是就向左 否则向右
     * @param x  x坐标
     */
    private void moveToLeftOrRright(int x){
        boolean isLeft=isLeft(x);
        ObjectAnimator animation= ObjectAnimator.ofFloat(this,"translationX",
                isLeft?iconWidth*(mPercent-1):mVisibityRect.width()-iconWidth*mPercent);
        animation.setDuration(mHomingDuration);
        animation.setInterpolator(new DecelerateInterpolator());
        animation.start();
    }

    /**
     * 是否滚动到左边
     * @param x
     * @return
     */
    private boolean isLeft(int x){
        int ceterX= mVisibityRect.width()/2;
        return x<=ceterX;
    }


    /**
     * 获取屏幕的高
     *
     * @param context 当前上下文
     * @return 屏幕高
     */
    public  int getScreenHeightPix(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    /**
     * 获取屏幕的宽
     * @param context
     * @return
     */
    public  int getScreenWidthPix(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    /**
     * 获取状态栏高度
     * @return
     */
    public  int getStatusBarHeight() {
        return Resources.getSystem().getDimensionPixelSize(Resources.getSystem()
                .getIdentifier("status_bar_height", "dimen", "android"));
    }
    /**
     * dp转px
     * @param dpValue dp
     * @return int px
     * @throws
     */
    public  int dp2px(float dpValue) {
        return (int) (dpValue * getDensity() + 0.5f);
    }

    /**
     * 获取屏幕密度
     * @return float
     * @throws
     */
    public  float getDensity() {
        DisplayMetrics dm = new DisplayMetrics();
        dm = mContext.getResources()
                .getDisplayMetrics();
        return dm.density;
    }
}
