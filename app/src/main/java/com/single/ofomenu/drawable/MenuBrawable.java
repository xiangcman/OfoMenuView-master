package com.single.ofomenu.drawable;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;

/**
 * Created by xiangcheng on 17/9/19.
 */

public class MenuBrawable extends Drawable {
    private Path mPath;
    private Bitmap bitmap;
    private Paint paint;
    private Paint mBitmapPaint;

    private static final int HEIGHTEST_Y = 80;
    private static final int BITMAP_XY = 80;
    private int arcY;
    private int bitmapXY;
    private float[] bitmapCneter;
    private int bitmapOffset;

    public MenuBrawable(Bitmap bitmap, Context context) {
        this.bitmap = bitmap;
        arcY = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, HEIGHTEST_Y, context.getResources().getDisplayMetrics());
        bitmapXY = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, BITMAP_XY, context.getResources().getDisplayMetrics());
        bitmapOffset = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, context.getResources().getDisplayMetrics());
        mPath = new Path();
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);

    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawPath(mPath, paint);
        //启动一个新的图层
        canvas.saveLayer(getBounds().left, getBounds().top, getBounds().right, getBounds().bottom, null, Canvas.ALL_SAVE_FLAG);
        canvas.drawCircle(bitmapCneter[0], bitmapCneter[1], bitmapXY / 2, mBitmapPaint);
        mBitmapPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, bitmapCneter[0] - bitmapXY / 2, bitmapCneter[1] - bitmapXY / 2, mBitmapPaint);
        canvas.restore();
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        mPath.reset();
        mPath.moveTo(bounds.left, bounds.top + arcY);
        mPath.quadTo(bounds.centerX(), 0, bounds.right, bounds.top + arcY);
        mPath.lineTo(bounds.right, bounds.bottom);
        mPath.lineTo(bounds.left, bounds.bottom);
        mPath.lineTo(bounds.left, bounds.top + arcY);

        if (bitmap != null) {
            mBitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            int size = Math.min(bitmap.getWidth(), bitmap.getHeight());
            float scale = (float) (bitmapXY * 1.0 / size);
            Matrix matrix = new Matrix();
            matrix.setScale(scale, scale);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            PathMeasure pathMeasure = new PathMeasure();
            pathMeasure.setPath(mPath, false);
            bitmapCneter = new float[2];
            pathMeasure.getPosTan(bitmapOffset, bitmapCneter, null);
        }

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
}
