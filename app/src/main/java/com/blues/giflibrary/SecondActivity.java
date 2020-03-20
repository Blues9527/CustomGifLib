package com.blues.giflibrary;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SecondActivity extends AppCompatActivity {

    String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "blues.gif";

    private ImageView iv;
    private GifHandle gifHandle;
    private int currentIndex = 0;
    private int maxIndex = 0;
    private Bitmap bitmap;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            long delay_time = gifHandle.renderFrame(bitmap, currentIndex);
            currentIndex++;
            if (currentIndex >= maxIndex) {
                currentIndex = 0;
            }
            iv.setImageBitmap(bitmap);
            sendEmptyMessageDelayed(1, delay_time);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_second);

        iv = findViewById(R.id.sample_image);

        showGif();
    }

    private void showGif() {
        File gifFile = new File(filePath);
        if (gifFile.exists()) {
            gifHandle = new GifHandle(filePath);

            //获取gif图片宽高
            int width = gifHandle.getWidth();
            int height = gifHandle.getHeight();

            //获取gif图片帧数

            //创建空的bitmap
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

            //渲染图片返回延迟
            long delay_time = gifHandle.renderFrame(bitmap, currentIndex);
            maxIndex = gifHandle.getLength();
            iv.setImageBitmap(bitmap);
            if (handler != null) {
                //根据延时时长发送空的消息
                handler.sendEmptyMessageDelayed(1, delay_time);
            }
        } else {
            Toast.makeText(SecondActivity.this, "gif not found!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bitmap != null) {
            bitmap.recycle();
        }
    }
}
