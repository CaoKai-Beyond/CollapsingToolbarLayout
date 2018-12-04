package com.weibo.internationa;

/**
 * Author：caokai on 2018/12/3 16:55
 * <p>
 * email：caokai@11td.com
 */
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.CoordinatorLayout.Behavior;
import android.support.design.widget.CoordinatorLayout.LayoutParams;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.util.AttributeSet;
import android.view.View;

import java.lang.reflect.Method;

public class MyProfileBtnBehavior extends Behavior<View> {
    int appBarLayoutMinHeight = -1;
    private final Context context;
    private boolean mIsHided;
    Rect rect = new Rect();

    public MyProfileBtnBehavior(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.context = context;
    }

    public boolean layoutDependsOn(CoordinatorLayout coordinatorLayout, View view, View view2) {
        ViewCompat.setElevation(view, 5.0f);
        return view2 instanceof AppBarLayout;
    }

    public boolean onDependentViewChanged(CoordinatorLayout coordinatorLayout, View view, View view2) {
        return view2 instanceof AppBarLayout ? updateVisibility(coordinatorLayout, (AppBarLayout) view2, view) : false;
    }

    private boolean updateVisibility(CoordinatorLayout coordinatorLayout, AppBarLayout appBarLayout, View view) {
        if (((LayoutParams) view.getLayoutParams()).getAnchorId() != appBarLayout.getId()) {
            return false;
        }
        view.getGlobalVisibleRect(this.rect);
        int centerY = this.rect.centerY();
        if (Build.MODEL.contains("MI 8") || Build.MODEL.contains("Nokia X6")) {
            centerY += 24;
        } else if (getStatesBarHeight(appBarLayout.getContext()) > 100) {
            centerY += getStatesBarHeight(appBarLayout.getContext()) / 2;
        }
        if (this.appBarLayoutMinHeight == -1) {

            Method method=ReflectUtils.getMethod(appBarLayout.getClass(),"getMinimumHeightForVisibleOverlappingContent");
            try {
                this.appBarLayoutMinHeight = (int) ReflectUtils.invokeMethod(method,appBarLayout.getClass());
            } catch (Exception e) {
                e.printStackTrace();
            }
            int[] iArr = new int[2];
            coordinatorLayout.getLocationOnScreen(iArr);
            this.appBarLayoutMinHeight += iArr[1];
        }
        if (centerY <= this.appBarLayoutMinHeight) {
            if (!this.mIsHided) {
                hide(view);
            }
        } else if (this.mIsHided) {
            show(view);
        }
        return true;
    }
    public  int getStatesBarHeight(Context context) {
        int identifier = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return identifier > 0 ? context.getResources().getDimensionPixelSize(identifier) : 0;
    }

    private void show(View view) {
        this.mIsHided = false;
        view.setVisibility(View.VISIBLE);
        view.animate().scaleX(1.0f).scaleY(1.0f).alpha(1.0f).setDuration(200).
                setInterpolator(new LinearOutSlowInInterpolator()).setListener(new AnimatorListenerAdapter() {
        }).start();
    }

    private void hide(final View view) {
        this.mIsHided = true;
        view.animate().cancel();
        view.animate().scaleX(0.0f).scaleY(0.0f).alpha(0.0f).setDuration(200).setInterpolator(new FastOutLinearInInterpolator()).setListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animator) {
                super.onAnimationEnd(animator);
                if (MyProfileBtnBehavior.this.mIsHided) {
                    view.setVisibility(View.INVISIBLE);
                }
            }
        }).start();
    }
}
