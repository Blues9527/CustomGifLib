package com.blues.giflibrary;

import android.graphics.Bitmap;

public class GifHandle {

    private volatile long gifInfo;

    static {
        System.loadLibrary("native-lib");
    }


    public GifHandle(String path) {
        gifInfo = openFile(path);
    }

    //获取宽
    public synchronized int getWidth() {
        return getWidthNative(gifInfo);
    }

    private native int getWidthNative(long gifInfo);

    //获取高
    public synchronized int getHeight() {
        return getHeightNative(gifInfo);
    }

    private native int getHeightNative(long gifInfo);

    //获取帧数
    public synchronized int getLength() {
        return getLengthNative(gifInfo);
    }

    private native int getLengthNative(long gifInfo);

    //c拿对象只拿地址
    private native long openFile(String msg);

    //渲染
    public long renderFrame(Bitmap bitmap, int index) {
        return renderFrameNative(gifInfo, bitmap, index);
    }

    private native long renderFrameNative(long gifInfo, Bitmap bitmap, int index);
}
