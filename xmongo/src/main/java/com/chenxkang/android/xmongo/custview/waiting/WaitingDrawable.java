package com.chenxkang.android.xmongo.custview.waiting;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import com.chenxkang.android.xmongo.util.DisplayUtil;

public class WaitingDrawable extends Drawable implements Animatable {

    private final static long ANIMATOR_DURATION_DEFAULT = 1200l;

    private static final Interpolator LINEAR_INTERPOLATOR = new LinearInterpolator();
    private static final Interpolator MATERIAL_INTERPOLATOR = new FastOutSlowInInterpolator();
    private static final Interpolator ACCELERATE_INTERPOLATOR = new AccelerateInterpolator();
    private static final Interpolator DECELERATE_INTERPOLATOR = new DecelerateInterpolator();

    private float mRotationCount;
    private float mGroupRotation;
    private float mEndDegrees;
    private float mStartDegrees;
    private float mLevel1SwipeDegrees;
    private float mLevel2SwipeDegrees;
    private float mLevel3SwipeDegrees;
    //private float mRotationIncrement;
    private float mOriginEndDegrees;
    private float mOriginStartDegrees;
    //private float mOriginRotationIncrement;
    private static final int NUM_POINTS = 5;
    private static final int DEGREE_360 = 360;
    private static final float MIN_SWIPE_DEGREE = 0.1f;
    private static final float MAX_SWIPE_DEGREES = 0.8f * DEGREE_360;
    private static final float FULL_GROUP_ROTATION = 3.0f * DEGREE_360;
    private static final float MAX_ROTATION_INCREMENT = 0.25f * DEGREE_360;
    private static final float LEVEL2_SWEEP_ANGLE_OFFSET = 7.0f / 8.0f;
    private static final float LEVEL3_SWEEP_ANGLE_OFFSET = 5.0f / 8.0f;
    private static final float START_TRIM_DURATION_OFFSET = 0.5f;
    private static final float END_TRIM_DURATION_OFFSET = 1.0f;
    private Paint mPaint;
    private final RectF mTempBounds = new RectF();
    private ValueAnimator valueAnimator;
    private int inset;
    private Rect bounds;
    private int circleColor;

    private int mLevel1Color;
    private int mLevel2Color;
    private int mLevel3Color;


    public WaitingDrawable() {
        initValueAnimator();
        init();
    }


    private void initValueAnimator() {
        valueAnimator = ValueAnimator.ofFloat(0.0f, 1.0f);
        valueAnimator.setRepeatCount(Animation.INFINITE);
        //valueAnimator.setRepeatMode(Animation.RESTART);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                computeRender((float) animation.getAnimatedValue());
                invalidateSelf();
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationRepeat(Animator animator) {
                super.onAnimationRepeat(animator);
                storeOriginals();

                mStartDegrees = mEndDegrees;
                mRotationCount = (mRotationCount + 1) % (NUM_POINTS);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                mRotationCount = 0;
            }
        });
        valueAnimator.setDuration(ANIMATOR_DURATION_DEFAULT);
    }


    private void init() {
        setColor(Color.WHITE);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(DisplayUtil.dp2px(6));
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    public void setStrokeWidth(int strokeWidth) {
        mPaint.setStrokeWidth(strokeWidth);
    }

    public void setInset(int inset) {
        this.inset = inset;
    }

    public void setColor(@ColorInt int color) {
        circleColor = color;
        mLevel1Color = oneThirdAlphaColor(circleColor);
        mLevel2Color = twoThirdAlphaColor(circleColor);
        mLevel3Color = circleColor;
    }


    @Override
    public void draw(Canvas canvas) {
        bounds = getBounds();
        int saveCount = canvas.save();
        canvas.rotate(mGroupRotation, bounds.exactCenterX(), bounds.exactCenterY());
        RectF arcBounds = mTempBounds;
        arcBounds.set(bounds);
        arcBounds.inset(inset, inset);
        mPaint.setColor(mLevel1Color);
        canvas.drawArc(arcBounds, mEndDegrees, mLevel1SwipeDegrees, false, mPaint);
        mPaint.setColor(mLevel2Color);
        canvas.drawArc(arcBounds, mEndDegrees, mLevel2SwipeDegrees, false, mPaint);
        mPaint.setColor(mLevel3Color);
        canvas.drawArc(arcBounds, mEndDegrees, mLevel3SwipeDegrees, false, mPaint);
        canvas.restoreToCount(saveCount);
    }


    public void computeRender(float renderProgress) {
        // Moving the start trim only occurs in the first 50% of a
        // single ring animation
        if (renderProgress <= START_TRIM_DURATION_OFFSET) {
            float startTrimProgress = (renderProgress) / START_TRIM_DURATION_OFFSET;
            mStartDegrees = mOriginStartDegrees + MAX_SWIPE_DEGREES * MATERIAL_INTERPOLATOR.getInterpolation(startTrimProgress);

            float mSwipeDegrees = MIN_SWIPE_DEGREE;
            if (Math.abs(mEndDegrees - mStartDegrees) > MIN_SWIPE_DEGREE) {
                mSwipeDegrees = mEndDegrees - mStartDegrees;
            }
            float levelSwipeDegreesProgress = Math.abs(mSwipeDegrees) / MAX_SWIPE_DEGREES;

            float level1Increment = DECELERATE_INTERPOLATOR.getInterpolation(levelSwipeDegreesProgress) - LINEAR_INTERPOLATOR.getInterpolation(levelSwipeDegreesProgress);
            float level3Increment = ACCELERATE_INTERPOLATOR.getInterpolation(levelSwipeDegreesProgress) - LINEAR_INTERPOLATOR.getInterpolation(levelSwipeDegreesProgress);

            mLevel1SwipeDegrees = -mSwipeDegrees * (1 + level1Increment);
            mLevel2SwipeDegrees = -mSwipeDegrees * LEVEL2_SWEEP_ANGLE_OFFSET;
            mLevel3SwipeDegrees = -mSwipeDegrees * LEVEL3_SWEEP_ANGLE_OFFSET * (1 + level3Increment);
        }

        // Moving the end trim starts after 50% of a single ring
        // animation completes
        if (renderProgress > START_TRIM_DURATION_OFFSET) {
            float endTrimProgress = (renderProgress - START_TRIM_DURATION_OFFSET) / (END_TRIM_DURATION_OFFSET - START_TRIM_DURATION_OFFSET);
            mEndDegrees = mOriginEndDegrees + MAX_SWIPE_DEGREES * MATERIAL_INTERPOLATOR.getInterpolation(endTrimProgress);

            float mSwipeDegrees = MIN_SWIPE_DEGREE;
            if (Math.abs(mEndDegrees - mStartDegrees) > MIN_SWIPE_DEGREE) {
                mSwipeDegrees = mEndDegrees - mStartDegrees;
            }
            float levelSwipeDegreesProgress = Math.abs(mSwipeDegrees) / MAX_SWIPE_DEGREES;

            if (levelSwipeDegreesProgress > LEVEL2_SWEEP_ANGLE_OFFSET) {
                mLevel1SwipeDegrees = -mSwipeDegrees;
                mLevel2SwipeDegrees = MAX_SWIPE_DEGREES * LEVEL2_SWEEP_ANGLE_OFFSET;
                mLevel3SwipeDegrees = MAX_SWIPE_DEGREES * LEVEL3_SWEEP_ANGLE_OFFSET;
            } else if (levelSwipeDegreesProgress > LEVEL3_SWEEP_ANGLE_OFFSET) {
                mLevel1SwipeDegrees = MIN_SWIPE_DEGREE;
                mLevel2SwipeDegrees = -mSwipeDegrees;
                mLevel3SwipeDegrees = MAX_SWIPE_DEGREES * LEVEL3_SWEEP_ANGLE_OFFSET;
            } else {
                mLevel1SwipeDegrees = MIN_SWIPE_DEGREE;
                mLevel2SwipeDegrees = MIN_SWIPE_DEGREE;
                mLevel3SwipeDegrees = -mSwipeDegrees;
            }
        }

        mGroupRotation = ((FULL_GROUP_ROTATION / NUM_POINTS) * renderProgress) + (FULL_GROUP_ROTATION * (mRotationCount / NUM_POINTS));
        //L.i(TAG, " renderProgress " + renderProgress + "  mGroupRotation  " + mGroupRotation);
    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }


    @Override
    public void start() {
        resetOriginals();
        valueAnimator.start();
    }

    @Override
    public void stop() {
        valueAnimator.cancel();
    }

    @Override
    public boolean isRunning() {
        return valueAnimator.isRunning();
    }


    public void setDuration(long duration) {
        valueAnimator.setDuration(duration);
    }

    private void storeOriginals() {
        mOriginEndDegrees = mEndDegrees;
        mOriginStartDegrees = mStartDegrees;
    }

    private void resetOriginals() {
        mOriginEndDegrees = 0;
        mOriginStartDegrees = 0;
        //mOriginRotationIncrement = 0;

        mEndDegrees = 0;
        mStartDegrees = 0;
        // mRotationIncrement = 0;

        mLevel1SwipeDegrees = MIN_SWIPE_DEGREE;
        mLevel2SwipeDegrees = MIN_SWIPE_DEGREE;
        mLevel3SwipeDegrees = MIN_SWIPE_DEGREE;
    }

    private int oneThirdAlphaColor(int colorValue) {
        int startA = (colorValue >> 24) & 0xff;
        int startR = (colorValue >> 16) & 0xff;
        int startG = (colorValue >> 8) & 0xff;
        int startB = colorValue & 0xff;

        return (startA / 3 << 24)
                | (startR << 16)
                | (startG << 8)
                | startB;
    }

    private int twoThirdAlphaColor(int colorValue) {
        int startA = (colorValue >> 24) & 0xff;
        int startR = (colorValue >> 16) & 0xff;
        int startG = (colorValue >> 8) & 0xff;
        int startB = colorValue & 0xff;

        return (startA * 2 / 3 << 24)
                | (startR << 16)
                | (startG << 8)
                | startB;
    }
}