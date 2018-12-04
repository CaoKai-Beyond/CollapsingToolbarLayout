package com.weibo.internationa;

/**
 * Author：caokai on 2018/12/3 17:34
 * <p>
 * email：caokai@11td.com
 */
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.ViewOutlineProvider;

public class ForegroundImageView extends AppCompatImageView {
    protected Drawable foreground;

    public boolean hasOverlappingRendering() {
        return false;
    }

    public ForegroundImageView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.ForegroundView);
        Drawable drawable = obtainStyledAttributes.getDrawable(R.styleable.ForegroundView_drawable);
        if (drawable != null) {
            setForeground(drawable);
        }
        obtainStyledAttributes.recycle();
        if (VERSION.SDK_INT >= 21) {
            setOutlineProvider(ViewOutlineProvider.BOUNDS);
        }
    }

    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        if (this.foreground != null) {
            this.foreground.setBounds(0, 0, i, i2);
        }
    }

    protected boolean verifyDrawable(Drawable drawable) {
        return super.verifyDrawable(drawable) || drawable == this.foreground;
    }

    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        if (this.foreground != null) {
            this.foreground.jumpToCurrentState();
        }
    }

    protected void drawableStateChanged() {
        super.drawableStateChanged();
        if (this.foreground != null && this.foreground.isStateful()) {
            this.foreground.setState(getDrawableState());
        }
    }

    public Drawable getForeground() {
        return this.foreground;
    }

    public void setForeground(Drawable drawable) {
        if (this.foreground != drawable) {
            if (this.foreground != null) {
                this.foreground.setCallback(null);
                unscheduleDrawable(this.foreground);
            }
            this.foreground = drawable;
            if (this.foreground != null) {
                this.foreground.setBounds(0, 0, getWidth(), getHeight());
                setWillNotDraw(false);
                this.foreground.setCallback(this);
                if (this.foreground.isStateful()) {
                    this.foreground.setState(getDrawableState());
                }
            } else {
                setWillNotDraw(true);
            }
            invalidate();
        }
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (this.foreground != null) {
            this.foreground.draw(canvas);
        }
    }

    public void drawableHotspotChanged(float f, float f2) {
        super.drawableHotspotChanged(f, f2);
        if (this.foreground != null && VERSION.SDK_INT >= 21) {
            this.foreground.setHotspot(f, f2);
        }
    }
}
