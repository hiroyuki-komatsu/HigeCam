package com.github.hiroyuki_komatsu.higecam;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.github.hiroyuki_komatsu.higecam.R;

/**
 * Created by komatsu on 15/05/03.
 */
public class MustacheView extends View {
    // Mustache bitmap
    private Bitmap mMustacheBitmap;
    private Bitmap mMustacheScaledBitmap;

    private int mRatioWidth = 0;
    private int mRatioHeight = 0;

    public MustacheView(Context context) {
        this(context, null);
    }

    public MustacheView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MustacheView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        // Initialize Bitmaps.
        Resources resources = getResources();
        Drawable mustacheDrawable = resources.getDrawable(R.drawable.mustache, null);
        mMustacheBitmap = ((BitmapDrawable) mustacheDrawable).getBitmap();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Resize mMustacheScaledBitmap.
        int mustacheSize = canvas.getWidth() / 3;
        if (mMustacheScaledBitmap == null ||
                mMustacheScaledBitmap.getWidth() != mustacheSize) {
            // mMustacheBitmap should be square.
            mMustacheScaledBitmap = Bitmap.createScaledBitmap(
                    mMustacheBitmap, mustacheSize, mustacheSize, false /* filter */);
        }

        int mustacheOffset = mMustacheScaledBitmap.getWidth() / 2;
        canvas.drawBitmap(mMustacheScaledBitmap,
                canvas.getWidth() / 2 - mustacheOffset,
                canvas.getHeight() / 2 - mustacheOffset,
                null);
    }

    /**
     * Sets the aspect ratio for this view. The size of the view will be measured based on the ratio
     * calculated from the parameters. Note that the actual sizes of parameters don't matter, that
     * is, calling setAspectRatio(2, 3) and setAspectRatio(4, 6) make the same result.
     *
     * @param width  Relative horizontal size
     * @param height Relative vertical size
     */
    public void setAspectRatio(int width, int height) {
        if (width < 0 || height < 0) {
            throw new IllegalArgumentException("Size cannot be negative.");
        }
        mRatioWidth = width;
        mRatioHeight = height;
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (0 == mRatioWidth || 0 == mRatioHeight) {
            setMeasuredDimension(width, height);
        } else {
            if (width < height * mRatioWidth / mRatioHeight) {
                setMeasuredDimension(width, width * mRatioHeight / mRatioWidth);
            } else {
                setMeasuredDimension(height * mRatioWidth / mRatioHeight, height);
            }
        }
    }
}
