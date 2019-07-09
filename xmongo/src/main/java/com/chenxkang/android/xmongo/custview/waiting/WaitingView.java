package com.chenxkang.android.xmongo.custview.waiting;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.chenxkang.android.xmongo.util.DisplayUtil;

/**
 * Desc:加载等待效果
 * Author: BG301395
 * Create: 2019/6/17 15:03
 */
public class WaitingView extends RelativeLayout {

    private ImageView mImageView;
    private WaitingDrawable waitingDrawable;

    public WaitingView(Context context) {
        super(context);

        waitingDrawable = new WaitingDrawable();
        waitingDrawable.setInset(DisplayUtil.dp2px(10));
        waitingDrawable.setStrokeWidth(DisplayUtil.dp2px(5));
        waitingDrawable.setColor(Color.parseColor("#f47e56"));

        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        this.setLayoutParams(lp);
        this.setClickable(true);

        mImageView = new ImageView(context);
        LayoutParams params = new LayoutParams(DisplayUtil.dp2px(60), DisplayUtil.dp2px(60));
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        mImageView.setLayoutParams(params);
//        mImageView.setBackgroundResource(R.drawable.view_waiting_bg);
        mImageView.setImageDrawable(waitingDrawable);
        this.addView(mImageView);
    }

    public synchronized void hide() {
        ViewGroup parentView = (ViewGroup) this.getParent();
        if (parentView != null) {
            parentView.removeView(this);
        }
        waitingDrawable.stop();
    }

    public synchronized void display() {
        if (this.getParent() != null) {
            return;
        }
        if (this.getContext() instanceof Activity) {
            Activity activity = (Activity) this.getContext();
            ((ViewGroup) activity.getWindow().getDecorView()).addView(this);
        } else {
            return;
        }
        waitingDrawable.start();

    }

    public boolean isDisplaying(){
        ViewGroup parentView = (ViewGroup) this.getParent();
        if (parentView != null) {
          return true;
        }
        return false;
    }
}
