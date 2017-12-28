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
import android.graphics.Region;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by xiangcheng on 17/9/19.
 */

public class MenuBrawable extends Drawable {
    //外层弧形path
    private Path mPath;
    //图片对象
    private Bitmap bitmap;
    private Paint paint;
    //绘制图片时要用的画笔，主要为setXfermode做准备
    private Paint mBitmapPaint;
    //峰值常亮(80dp)
    private static final int HEIGHTEST_Y = 80;
    //图片宽度(80dp)
    private static final int BITMAP_XY = 80;
    //弧度的峰值，为后面绘制贝塞尔曲线做准备
    private int arcY;
    //图片边长
    private int bitmapXY;
    //图片的中心坐标
    private float[] bitmapCneter;
    //图片离左边的距离
    private int bitmapOffset;

    private Path circleBitmapPath;
    private View mParent;

    public MenuBrawable(Bitmap bitmap, Context context, View parent) {
        this.bitmap = bitmap;
        this.mParent = parent;
        arcY = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, HEIGHTEST_Y, context.getResources().getDisplayMetrics());
        bitmapXY = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, BITMAP_XY, context.getResources().getDisplayMetrics());
        bitmapOffset = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, context.getResources().getDisplayMetrics());
        mPath = new Path();
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
        mBitmapRegion = new Region();
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawPath(mPath, paint);

        //启动一个新的图层
        int layer = canvas.saveLayer(getBounds().left, getBounds().top, getBounds().right, getBounds().bottom, null, Canvas.ALL_SAVE_FLAG);
        //在xfmode之前画的是dst
        canvas.drawPath(circleBitmapPath, mBitmapPaint);
        //该mode下取两部分的交集部分
        mBitmapPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        //裁剪的方式也可以
//        canvas.save();
//        canvas.clipPath(circleBitmapPath);
        //在sfmode之后画的是src
        canvas.drawBitmap(bitmap, bitmapCneter[0] - bitmapXY / 2, bitmapCneter[1] - bitmapXY / 2, mBitmapPaint);
        mBitmapPaint.setXfermode(null);
        canvas.restoreToCount(layer);
    }

    public void setBitmap(Bitmap bitmap) {
        int size = Math.min(bitmap.getWidth(), bitmap.getHeight());
        float scale = (float) (bitmapXY * 1.0 / size);
        Matrix matrix = new Matrix();
        //需要对图片进行缩放
        matrix.setScale(scale, scale);
        this.bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        invalidateSelf();
    }

    //bounds对象就是view占据的空间
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
            //需要对图片进行缩放
            matrix.setScale(scale, scale);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            PathMeasure pathMeasure = new PathMeasure();
            pathMeasure.setPath(mPath, false);
            bitmapCneter = new float[2];
            //通过path的测量工具获取到bitmap的中心位置
            pathMeasure.getPosTan(bitmapOffset, bitmapCneter, null);
            circleBitmapPath = new Path();
            circleBitmapPath.addCircle(bitmapCneter[0], bitmapCneter[1], bitmapXY / 2, Path.Direction.CCW);
            mBitmapRegion.setPath(circleBitmapPath, new Region(bounds));
            mParent.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        if (mBitmapRegion.contains((int) event.getX(), (int) event.getY())) {
                            if (onBitmapClickListener != null) {
                                onBitmapClickListener.bitmapClick();
                            }
                        }
                    }
                    return true;
                }
            });
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

    private Region mBitmapRegion;

    public void setOnBitmapClickListener(OnBitmapClickListener onBitmapClickListener) {
        this.onBitmapClickListener = onBitmapClickListener;
    }

    private OnBitmapClickListener onBitmapClickListener;

    //添加对bitmap点击的回调
    public interface OnBitmapClickListener {
        void bitmapClick();
    }
}
