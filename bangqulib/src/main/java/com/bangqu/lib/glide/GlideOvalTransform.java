package com.bangqu.lib.glide;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.security.MessageDigest;

/**
 * Created by Hello on 2015/8/21.
 * 图片转椭圆的方法
 * .transform(new GlideOvalTransform(context))
 */
public class GlideOvalTransform extends BitmapTransformation {

    private static Bitmap.Config config = Bitmap.Config.ARGB_8888; //透明背景，否则为黑色背景

    public GlideOvalTransform(Context context) {
        super(context);
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        return circleCrop(pool, toTransform, outWidth, outHeight);
    }

    private static Bitmap circleCrop(BitmapPool pool, Bitmap source, int outWidth, int outHeight) {
        if (source == null) return null;
        //计算图片缩放
        Matrix matrix = new Matrix();
        float scaleX = (float) outWidth / source.getWidth();
        float scaleY = (float) outHeight / source.getHeight();
        matrix.postScale(scaleX, scaleY);
        //绘制图形
        Bitmap result = pool.get(outWidth, outHeight, config);
        if (result == null) {
            result = Bitmap.createBitmap(outWidth, outHeight, config);
        }
        BitmapShader shader = new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
        shader.setLocalMatrix(matrix);
        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        paint.setShader(shader);
        paint.setAntiAlias(true);
//        Xfermode xFermode = new PorterDuffXfermode(PorterDuff.Mode.SRC);
//        paint.setXfermode(xFermode);
        RectF re = new RectF(0, 0, outWidth, outHeight);
        canvas.drawOval(re, paint);
        return result;
    }

    @Override
    public void updateDiskCacheKey(MessageDigest messageDigest) {

    }
}
