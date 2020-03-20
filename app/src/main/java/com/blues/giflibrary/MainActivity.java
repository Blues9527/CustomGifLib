package com.blues.giflibrary;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.PermissionChecker;

public class MainActivity extends AppCompatActivity {

    final String path = "https://n.sinaimg.cn/tech/transform/748/w358h390/20200319/d437-iqyrykv3316800.gif";
    String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "blues.gif";

    private String[] denied;
    private String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private Handler mMainHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }


    @Override
    protected void onResume() {

        //权限申请
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> list = new ArrayList<>();
            for (int i = 0; i < permissions.length; i++) {
                if (PermissionChecker.checkSelfPermission(this, permissions[i]) == PermissionChecker.PERMISSION_DENIED) {
                    list.add(permissions[i]);
                }
            }
            if (list.size() != 0) {
                denied = new String[list.size()];
                for (int i = 0; i < list.size(); i++) {
                    denied[i] = list.get(i);
                    ActivityCompat.requestPermissions(this, denied, 5);
                }
            }
        }
        super.onResume();
    }

    private void downloadGif(String imageUrl) {
        File gifFile = new File(filePath);
        if (gifFile.exists()) {
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainActivity.this, "file exits!", Toast.LENGTH_SHORT).show();
                }
            });
            return;
        }

        FileOutputStream fos = null;
        InputStream is = null;
        try {
            URL url = new URL(imageUrl);
            URLConnection connection = url.openConnection();
            connection.setReadTimeout(30_000);
            connection.setConnectTimeout(30_000);
            connection.connect();

            is = connection.getInputStream();

            int len = 0;
            File file = new File(Environment.getExternalStorageDirectory(), "blues.gif");
            byte[] buf = new byte[128];
            fos = new FileOutputStream(file);
            while ((len = is.read(buf)) != -1) {
                fos.write(buf, 0, len);
            }
            fos.flush();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 5) {
            if (denied.length != 0) {
                boolean isDenied = false;
                for (int i = 0; i < denied.length; i++) {
                    String permission = denied[i];
                    for (int j = 0; j < permissions.length; j++) {
                        if (permissions[j].equals(permission)) {
                            if (grantResults[j] != PackageManager.PERMISSION_GRANTED) {
                                isDenied = true;
                                break;
                            }
                        }
                    }
                }
                if (isDenied) {
                    Toast.makeText(this, "请开启权限", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Settings.ACTION_SETTINGS);
                    startActivity(intent);
                } else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            downloadGif(path);
                        }
                    }).start();
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    public void downloadGif(View view) {
        //开启一个线程去下载gif
        new Thread(new Runnable() {
            @Override
            public void run() {
                downloadGif(path);
            }
        }).start();
    }

    public void showGif(View view) {
        startActivity(new Intent(MainActivity.this, SecondActivity.class));
    }
}
