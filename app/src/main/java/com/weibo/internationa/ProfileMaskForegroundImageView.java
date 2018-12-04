package com.weibo.internationa;

/**
 * Author：caokai on 2018/12/3 17:33
 * <p>
 * email：caokai@11td.com
 */
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.FloatRange;
import android.util.AttributeSet;

public class ProfileMaskForegroundImageView extends ForegroundImageView {
    private int lastAlpha;
    private Drawable mask;

    public ProfileMaskForegroundImageView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        if (this.mask != null) {
            this.mask.setBounds(0, 0, i, i2);
        }
    }

    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        if (this.mask != null) {
            this.mask.jumpToCurrentState();
        }
    }

    protected void drawableStateChanged() {
        super.drawableStateChanged();
        if (this.mask != null && this.mask.isStateful()) {
            this.mask.setState(getDrawableState());
        }
    }

    public void setMask(Drawable drawable) {
        if (this.mask != drawable) {
            if (this.mask != null) {
                this.mask.setCallback(null);
                unscheduleDrawable(this.mask);
            }
            this.mask = drawable;
            if (this.mask != null) {
                this.mask.setBounds(0, 0, getWidth(), getHeight());
                setWillNotDraw(false);
                this.mask.setCallback(this);
                if (this.mask.isStateful()) {
                    this.mask.setState(getDrawableState());
                }
            } else {
                setWillNotDraw(true);
            }
            invalidate();
        }
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (this.mask != null) {
            this.mask.draw(canvas);
        }
    }

    public void setForegroundAlpha(@FloatRange(from = 0.0d, to = 1.0d) float f) {
        if (this.foreground != null) {
            int i = (int) (20.0f * f);
            if (i != this.lastAlpha || this.lastAlpha == 0) {
                this.lastAlpha = i;
                this.foreground.setAlpha((int) (f * 255.0f));
            }
        }
    }
}
