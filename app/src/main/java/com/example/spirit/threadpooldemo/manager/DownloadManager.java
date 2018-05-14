package com.example.spirit.threadpooldemo.manager;

import android.content.Intent;
import android.net.Uri;

import com.example.spirit.threadpooldemo.MainActivity;
import com.example.spirit.threadpooldemo.domain.DownloadInfo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class DownloadManager {

    private static DownloadManager manager;
    public static final int STATE_UNDO = 1;
    public static final int STATE_WAITING = 2;
    public static final int STATE_DOWNLOADING = 3;
    public static final int STATE_PAUSE = 4;
    public static final int STATE_ERROR = 5;
    public static final int STATE_SUCCESS = 6;
    private ArrayList<DownloadObserver> observers = new ArrayList<>();
    private DownloadTask task;
    private DownloadInfo info = null;

    private DownloadManager() {}

    public void setInfo(DownloadInfo info) {
        this.info = info;
    }

    public static DownloadManager getDownloadManager() {
        if (manager == null) {
            synchronized (DownloadManager.class) {
                if (manager == null) {
                    manager = new DownloadManager();
                }
            }
        }

        return manager;
    }

    public interface DownloadObserver {

        //下载状态发生变化
        void onDownloadStateChanged();

        //下载进度发生变化
        void onDownloadProgressChanged(float progress);
    }

    public void registerObserver(DownloadObserver observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void unRegisterObserver(DownloadObserver observer) {
        if (observer != null && observers.contains(observer)) {
            observers.remove(observer);
        }
    }

    public void notifyDownloadStateChanged() {
        for (DownloadObserver observer : observers) {
            observer.onDownloadStateChanged();
        }
    }

    public void notifyDownlaodProgressChanged(float progress) {
        for (DownloadObserver observer : observers) {
            observer.onDownloadProgressChanged(progress);
        }
    }


    public void download() {

        task = new DownloadTask();
        ThreadManager.getThreadPool().execute(task);
    }

    public void pause() {
        if (task != null) {
            ThreadManager.getThreadPool().cancel(task);
        }
    }

    public void install() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.parse("file://" + info.getFilePath()),
                "application/vnd.android" + ".package-archive");
        MainActivity.getContext().startActivity(intent);
    }

    class DownloadTask implements Runnable {

        private InputStream inputStream;
        private FileOutputStream fos;

        @Override
        public void run() {
            System.out.println(info.name + "开始下载啦");
            File file = new File(info.getFilePath());
            if (!file.exists() || file.length() == info.currentPos || info.currentPos == 0) {
                file.delete();//文件不存在也可以删除，只不过是没有效果而已

                try {
                    URL url = new URL(info.getUrl());
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    info.size = connection.getContentLength();
                    connection.setReadTimeout(2000);
                    int responseCode = connection.getResponseCode();
                    if (responseCode == 200) {
                        inputStream = connection.getInputStream();
                        fos = new FileOutputStream(info.getFilePath(), true);
                        int len = -1;
                        byte[] buffer = new byte[1024];
                        while ((len = inputStream.read(buffer)) != -1) {
                            fos.write(buffer, 0, len);
                            fos.flush();//把剩余数据刷入本地
                            info.currentPos += len;
                            notifyDownlaodProgressChanged(info.getProgress());
                            //System.out.println(info.getUrl() + ", "
                            // + info.getFilePath() + "," + info.getProgress());
                            System.out.println(info.getProgress()+", "+info.formartUnit());
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        inputStream.close();
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else {

            }
        }
    }
}
