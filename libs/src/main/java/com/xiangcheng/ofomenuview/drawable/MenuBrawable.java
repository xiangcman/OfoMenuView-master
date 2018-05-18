package com.xiangcheng.ofomenuview.drawable;

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
    private static final int START_OFFSET = 50;
    private int startOffset;

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
    //弧度样子是凹进去的
    public static final int CONCAVE = 1;
    //弧度是凸出来的
    public static final int CONVEX = 2;

    //当前的弧度是上面两个值
    private int mRadian = CONVEX;

    private Path convexPath;
    private Path concavePath;

    public MenuBrawable(Bitmap bitmap, Context context, View parent, int radian) {
        this.bitmap = bitmap;
        this.mParent = parent;
        this.mRadian = radian;
        arcY = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, HEIGHTEST_Y, context.getResources().getDisplayMetrics());
        bitmapXY = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, BITMAP_XY, context.getResources().getDisplayMetrics());
        bitmapOffset = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, context.getResources().getDisplayMetrics());
        startOffset = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, START_OFFSET, context.getResources().getDisplayMetrics());
        mPath = new Path();
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
        mBitmapRegion = new Region();
    }

    public MenuBrawable(Bitmap bitmap, Context context, View parent) {
        this(bitmap, context, parent, CONVEX);
    }

    public int getArcY() {
        return arcY;
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

    public void setRadian(int radian) {
        if (this.mRadian == radian) {
            return;
        }
        this.mRadian = radian;
        mPath.reset();
        if (mRadian == CONVEX) {
            mPath = createConvexPath(getBounds());
        } else {
            mPath = createConcavePath(getBounds());
        }
        invalidateSelf();
    }

    //bounds对象就是view占据的空间
    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        if (mRadian == CONVEX) {
            mPath = createConvexPath(bounds);
        } else {
            mPath = createConcavePath(bounds);
        }
        if (bitmap != null) {
            mBitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mBitmapPaint.setColor(Color.WHITE);
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

    private Path createConcavePath(Rect bounds) {
        if (concavePath == null) {
            float[] measurePoint = new float[2];
            Path measurePath = new Path();
            measurePath.moveTo(bounds.left, bounds.top);
            measurePath.quadTo(bounds.centerX(), bounds.top + arcY, bounds.right, bounds.top);
            measurePath.lineTo(bounds.left, bounds.top);
            PathMeasure pathMeasure = new PathMeasure();
            pathMeasure.setPath(measurePath, false);
            pathMeasure.getPosTan(bounds.centerX(), measurePoint, null);
            float startTop = bounds.top + arcY + -measurePoint[1];

            Path path = new Path();
            path.reset();
            path.moveTo(bounds.left, startTop);
            path.quadTo(bounds.centerX(), startTop + arcY, bounds.right, startTop);
            path.lineTo(bounds.right, bounds.bottom);
            path.lineTo(bounds.left, bounds.bottom);
            path.lineTo(bounds.left, startTop);
            concavePath = path;
        }
        return concavePath;
    }

    private Path createConvexPath(Rect bounds) {
        if (convexPath == null) {
            Path path = new Path();
            path.reset();
            path.moveTo(bounds.left, bounds.top + arcY);
            path.quadTo(bounds.centerX(), 0, bounds.right, bounds.top + arcY);
            path.lineTo(bounds.right, bounds.bottom);
            path.lineTo(bounds.left, bounds.bottom);
            path.lineTo(bounds.left, bounds.top + arcY);
            convexPath = path;
        }
        return convexPath;
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
