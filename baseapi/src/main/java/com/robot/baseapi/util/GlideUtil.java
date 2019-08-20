package com.robot.baseapi.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.request.RequestFutureTarget;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.robot.baseapi.widget.BlurTransformation;


import java.io.File;
import java.security.MessageDigest;

import static com.bumptech.glide.load.resource.bitmap.VideoDecoder.FRAME_OPTION;

/**
 * glide 工具类
 */
public class GlideUtil {


    public static void into(@NonNull RequestOptions requestOptions, Context context, RequestListener<Drawable> listener, ImageView imageView, int width, int height, Object path, int holder, int error, float blurRadius) {
        if (width != 0 && height != 0) {
            requestOptions.overrideOf(width, height);
        }
        if (holder != 0) {
            requestOptions.placeholder(holder);
        }
        if (error != 0) {
            requestOptions.error(error);
        }

        if (blurRadius > 0 && blurRadius <= 25) {
            requestOptions.transforms(new BlurTransformation(context, blurRadius));
        }

        RequestBuilder<Drawable> load = Glide.with(context)
                .load(path);
        if (listener != null) {
            load.listener(listener);
        }
        load.apply(requestOptions)
                .into(imageView);
    }

    public static void into(@NonNull RequestOptions requestOptions, Context context, ImageView imageView, int width, int height, Object path, int holder, int error) {
        into(requestOptions, context,null, imageView, width, height, path, holder, error, 0);
    }

    public static void into(@NonNull RequestOptions requestOptions, Context context, RequestListener<Drawable> listener,  ImageView imageView, int width, int height, Object path, int holder, int error) {
        into(requestOptions, context,listener, imageView, width, height, path, holder, error, 0);
    }



    /**
     * 居中切割
     *
     * @param context
     * @param imageView
     * @param width
     * @param height
     * @param path
     * @param holder
     * @param error
     */
    public static void centerCropTransform(Context context, ImageView imageView, int width, int height, Object path, int holder, int error) {
        into(RequestOptions.centerCropTransform(), context,null,imageView, width, height, path, holder, error, 0);
    }

    public static void centerCropTransform(Context context, RequestListener<Drawable> listener, ImageView imageView, int width, int height, Object path, int holder, int error) {
        into(RequestOptions.centerCropTransform(), context,listener, imageView, width, height, path, holder, error, 0);
    }


    public static void centerCropTransform(Context context,  RequestListener<Drawable> listener,ImageView imageView, int width, int height, Object path, int holder, int error, float blurRadius) {
        into(RequestOptions.centerCropTransform(), context,listener, imageView, width, height, path, holder, error, blurRadius);
    }

    public static void centerCropTransform(Context context, ImageView imageView, int width, int height, Object path, int holder, int error, float blurRadius) {
        into(RequestOptions.centerCropTransform(), context, null,imageView, width, height, path, holder, error, blurRadius);
    }

    public static void centerCropTransform(Context context, ImageView imageView, Object path) {
        centerCropTransform(context, imageView, 0, 0, path, 0, 0, 0);
    }

    public static void centerCropTransform(Context context,RequestListener<Drawable> listener, ImageView imageView, Object path) {
        centerCropTransform(context,listener, imageView, 0, 0, path, 0, 0, 0);
    }

    public static void centerCropTransform(Context context, ImageView imageView, Object path, float blurRadius) {
        centerCropTransform(context, imageView, 0, 0, path, 0, 0, blurRadius);
    }

    public static void centerCropTransform(Context context,RequestListener<Drawable> listener, ImageView imageView, Object path, float blurRadius) {
        centerCropTransform(context,listener, imageView, 0, 0, path, 0, 0, blurRadius);
    }

    public static void centerCropTransform(Context context, ImageView imageView, int width, int height, Object path) {
        centerCropTransform(context, imageView, width, height, path, 0, 0, 0);
    }

    public static void centerCropTransform(Context context,RequestListener<Drawable> listener, ImageView imageView, int width, int height, Object path) {
        centerCropTransform(context,listener, imageView, width, height, path, 0, 0, 0);
    }


    public static void centerCropTransform(Context context, ImageView imageView, int width, int height, Object path, float blurRadius) {
        centerCropTransform(context, imageView, width, height, path, 0, 0, 0);
    }

    public static void centerCropTransform(Context context,RequestListener<Drawable> listener, ImageView imageView, int width, int height, Object path, float blurRadius) {
        centerCropTransform(context,listener ,imageView, width, height, path, 0, 0, 0);
    }

    public static void centerCropTransform(Context context, ImageView imageView, Object path, int holder, int error) {
        centerCropTransform(context, imageView, 0, 0, path, holder, error, 0);
    }

    public static void centerCropTransform(Context context, RequestListener<Drawable> listener,ImageView imageView, Object path, int holder, int error) {
        centerCropTransform(context,listener, imageView, 0, 0, path, holder, error, 0);
    }

    public static void centerCropTransform(Context context, ImageView imageView, Object path, int holder, int error, float blurRadius) {
        centerCropTransform(context, imageView, 0, 0, path, holder, error, blurRadius);
    }

    public static void centerCropTransform(Context context,RequestListener<Drawable> listener, ImageView imageView, Object path, int holder, int error, float blurRadius) {
        centerCropTransform(context,listener, imageView, 0, 0, path, holder, error, blurRadius);
    }


    /**
     * 圆形
     *
     * @param context
     * @param imageView
     * @param width
     * @param height
     * @param path
     * @param holder
     * @param error
     */
    public static void circleCropTransform(Context context, ImageView imageView, int width, int height, Object path, int holder, int error) {
        into(RequestOptions.circleCropTransform(), context,null, imageView, width, height, path, holder, error, 0);
    }

    public static void circleCropTransform(Context context,RequestListener<Drawable> listener, ImageView imageView, int width, int height, Object path, int holder, int error) {
        into(RequestOptions.circleCropTransform(), context,listener, imageView, width, height, path, holder, error, 0);
    }

    public static void circleCropTransform(Context context, ImageView imageView, int width, int height, Object path, int holder, int error, float blurRadius) {
        into(RequestOptions.circleCropTransform(), context, null,imageView, width, height, path, holder, error, blurRadius);
    }

    public static void circleCropTransform(Context context, RequestListener<Drawable> listener,ImageView imageView, int width, int height, Object path, int holder, int error, float blurRadius) {
        into(RequestOptions.circleCropTransform(), context,listener, imageView, width, height, path, holder, error, blurRadius);
    }



    public static void circleCropTransform(Context context, RequestListener<Drawable> listener, ImageView imageView, Object path) {
        circleCropTransform(context,listener, imageView, 0, 0, path, 0, 0);
    }
    public static void circleCropTransform(Context context, ImageView imageView, Object path) {
        circleCropTransform(context, imageView, 0, 0, path, 0, 0);
    }

    public static void circleCropTransform(Context context, ImageView imageView, Object path, float blurRadius) {
        circleCropTransform(context, imageView, 0, 0, path, 0, 0, blurRadius);
    }
    public static void circleCropTransform(Context context, RequestListener<Drawable> listener,ImageView imageView, Object path, float blurRadius) {
        circleCropTransform(context,listener, imageView, 0, 0, path, 0, 0, blurRadius);
    }


    public static void circleCropTransform(Context context, ImageView imageView, int width, int height, Object path) {
        circleCropTransform(context, imageView, width, height, path, 0, 0);
    }

    public static void circleCropTransform(Context context, RequestListener<Drawable> listener,ImageView imageView, int width, int height, Object path) {
        circleCropTransform(context,listener, imageView, width, height, path, 0, 0);
    }

    public static void circleCropTransform(Context context, ImageView imageView, int width, int height, Object path, float blurRadius) {
        circleCropTransform(context, imageView, width, height, path, 0, 0, blurRadius);
    }
    public static void circleCropTransform(Context context, RequestListener<Drawable> listener,ImageView imageView, int width, int height, Object path, float blurRadius) {
        circleCropTransform(context,listener, imageView, width, height, path, 0, 0, blurRadius);
    }


    public static void circleCropTransform(Context context, ImageView imageView, Object path, int holder, int error) {
        circleCropTransform(context, imageView, 0, 0, path, holder, error);
    }
    public static void circleCropTransform(Context context,  RequestListener<Drawable> listener,ImageView imageView, Object path, int holder, int error) {
        circleCropTransform(context,listener, imageView, 0, 0, path, holder, error);
    }


    public static void circleCropTransform(Context context,RequestListener<Drawable> listener, ImageView imageView, Object path, int holder, int error, float blurRadius) {
        circleCropTransform(context,listener, imageView, 0, 0, path, holder, error, blurRadius);
    }
    public static void circleCropTransform(Context context, ImageView imageView, Object path, int holder, int error, float blurRadius) {
        circleCropTransform(context, imageView, 0, 0, path, holder, error, blurRadius);
    }


    /**
     * 居中填充
     *
     * @param context
     * @param imageView
     * @param width
     * @param height
     * @param path
     * @param holder
     * @param error
     */

    public static void fitCenterTransform(Context context,RequestListener<Drawable> listener, ImageView imageView, int width, int height, Object path, int holder, int error) {
        into(RequestOptions.fitCenterTransform(), context,listener, imageView, width, height, path, holder, error);
    }
    public static void fitCenterTransform(Context context, ImageView imageView, int width, int height, Object path, int holder, int error) {
        into(RequestOptions.fitCenterTransform(), context, imageView, width, height, path, holder, error);
    }

    public static void fitCenterTransform(Context context,RequestListener<Drawable> listener, ImageView imageView, int width, int height, Object path, int holder, int error, float blurRadius) {
        into(RequestOptions.fitCenterTransform(), context,listener, imageView, width, height, path, holder, error, blurRadius);
    }
    public static void fitCenterTransform(Context context, ImageView imageView, int width, int height, Object path, int holder, int error, float blurRadius) {
        into(RequestOptions.fitCenterTransform(), context,null ,imageView, width, height, path, holder, error, blurRadius);
    }

    public static void fitCenterTransform(Context context, ImageView imageView, Object path) {
        fitCenterTransform(context, imageView, 0, 0, path, 0, 0);
    }
    public static void fitCenterTransform(Context context,RequestListener<Drawable> listener, ImageView imageView, Object path) {
        fitCenterTransform(context,listener, imageView, 0, 0, path, 0, 0);
    }

    public static void fitCenterTransform(Context context, ImageView imageView, Object path, float blurRadius) {
        fitCenterTransform(context, imageView, 0, 0, path, 0, 0, blurRadius);
    }
    public static void fitCenterTransform(Context context,RequestListener<Drawable> listener, ImageView imageView, Object path, float blurRadius) {
        fitCenterTransform(context,listener, imageView, 0, 0, path, 0, 0, blurRadius);
    }

    public static void fitCenterTransform(Context context, ImageView imageView, int width, int height, Object path) {
        fitCenterTransform(context, imageView, width, height, path, 0, 0);
    }
    public static void fitCenterTransform(Context context,RequestListener<Drawable> listener, ImageView imageView, int width, int height, Object path) {
        fitCenterTransform(context,listener, imageView, width, height, path, 0, 0);
    }

    public static void fitCenterTransform(Context context, ImageView imageView, int width, int height, Object path, float blurRadius) {
        fitCenterTransform(context, imageView, width, height, path, 0, 0, blurRadius);
    }
    public static void fitCenterTransform(Context context,RequestListener<Drawable> listener, ImageView imageView, int width, int height, Object path, float blurRadius) {
        fitCenterTransform(context,listener, imageView, width, height, path, 0, 0, blurRadius);
    }

    public static void fitCenterTransform(Context context, ImageView imageView, Object path, int holder, int error) {
        fitCenterTransform(context, imageView, 0, 0, path, holder, error);
    }
    public static void fitCenterTransform(Context context,RequestListener<Drawable> listener, ImageView imageView, Object path, int holder, int error) {
        fitCenterTransform(context,listener, imageView, 0, 0, path, holder, error);
    }


    public static void fitCenterTransform(Context context, ImageView imageView, Object path, int holder, int error, float blurRadius) {
        fitCenterTransform(context, imageView, 0, 0, path, holder, error, blurRadius);
    }
    public static void fitCenterTransform(Context context,RequestListener<Drawable> listener, ImageView imageView, Object path, int holder, int error, float blurRadius) {
        fitCenterTransform(context,listener, imageView, 0, 0, path, holder, error, blurRadius);
    }


    /**
     * 居中显示
     *
     * @param context
     * @param imageView
     * @param width
     * @param height
     * @param path
     * @param holder
     * @param error
     */
    public static void centerInsideTransform(Context context, ImageView imageView, int width, int height, Object path, int holder, int error) {
        into(RequestOptions.centerInsideTransform(), context,null,imageView, width, height, path, holder, error, 0);
    }
    public static void centerInsideTransform(Context context, RequestListener<Drawable> listener,ImageView imageView, int width, int height, Object path, int holder, int error) {
        into(RequestOptions.centerInsideTransform(), context, listener,imageView, width, height, path, holder, error, 0);
    }


    public static void centerInsideTransform(Context context, ImageView imageView, int width, int height, Object path, int holder, int error, float blurRadius) {
        into(RequestOptions.centerInsideTransform(), context, null,imageView, width, height, path, holder, error, blurRadius);
    }
    public static void centerInsideTransform(Context context,  RequestListener<Drawable> listener,ImageView imageView, int width, int height, Object path, int holder, int error, float blurRadius) {
        into(RequestOptions.centerInsideTransform(), context,listener, imageView, width, height, path, holder, error, blurRadius);
    }

    public static void centerInsideTransform(Context context, ImageView imageView, Object path) {
        centerInsideTransform(context, imageView, 0, 0, path, 0, 0);
    }
    public static void centerInsideTransform(Context context,RequestListener<Drawable> listener, ImageView imageView, Object path) {
        centerInsideTransform(context,listener, imageView, 0, 0, path, 0, 0);
    }

    public static void centerInsideTransform(Context context, ImageView imageView, Object path, float blurRadius) {
        centerInsideTransform(context, imageView, 0, 0, path, 0, 0, blurRadius);
    }
    public static void centerInsideTransform(Context context, RequestListener<Drawable> listener,ImageView imageView, Object path, float blurRadius) {
        centerInsideTransform(context,listener, imageView, 0, 0, path, 0, 0, blurRadius);
    }

    public static void centerInsideTransform(Context context, ImageView imageView, int width, int height, Object path) {
        centerInsideTransform(context, imageView, width, height, path, 0, 0);
    }
    public static void centerInsideTransform(Context context,  RequestListener<Drawable> listener,ImageView imageView, int width, int height, Object path) {
        centerInsideTransform(context, listener,imageView, width, height, path, 0, 0);
    }

    public static void centerInsideTransform(Context context, ImageView imageView, int width, int height, Object path, float blurRadius) {
        centerInsideTransform(context, imageView, width, height, path, 0, 0, blurRadius);
    }
    public static void centerInsideTransform(Context context,  RequestListener<Drawable> listener,ImageView imageView, int width, int height, Object path, float blurRadius) {
        centerInsideTransform(context,listener, imageView, width, height, path, 0, 0, blurRadius);
    }

    public static void centerInsideTransform(Context context, ImageView imageView, Object path, int holder, int error) {
        centerInsideTransform(context, imageView, 0, 0, path, holder, error);
    }
    public static void centerInsideTransform(Context context, RequestListener<Drawable> listener, ImageView imageView, Object path, int holder, int error) {
        centerInsideTransform(context,listener, imageView, 0, 0, path, holder, error);
    }

    public static void centerInsideTransform(Context context, ImageView imageView, Object path, int holder, int error, float blurRadius) {
        centerInsideTransform(context, imageView, 0, 0, path, holder, error, blurRadius);
    }
    public static void centerInsideTransform(Context context, RequestListener<Drawable> listener, ImageView imageView, Object path, int holder, int error, float blurRadius) {
        centerInsideTransform(context,listener, imageView, 0, 0, path, holder, error, blurRadius);
    }

    /**
     * 有个0- 到1秒的延迟
     *
     * @param context         上下文
     * @param videoFile       视频文件
     * @param imageView       设置image
     * @param frameTimeMicros 获取某一时间帧
     */
    public static void loadVideoScreenshot(final Context context, File videoFile, ImageView imageView, long frameTimeMicros, int holder, int error) {
        RequestOptions requestOptions = RequestOptions.frameOf(frameTimeMicros);
        requestOptions.set(FRAME_OPTION, MediaMetadataRetriever.OPTION_CLOSEST);
        requestOptions.placeholder(holder)
                .error(error);
        requestOptions.transform(new BitmapTransformation() {
            @Override
            protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
                return toTransform;
            }

            @Override
            public void updateDiskCacheKey(MessageDigest messageDigest) {
                try {
                    messageDigest.update((context.getPackageName() + "RotateTransform").getBytes("utf-8"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        Glide.with(context).load(Uri.fromFile(videoFile)).apply(requestOptions).into(imageView);
    }


}
