package com.example.spirit.threadpooldemo.domain;

import android.os.Environment;

import com.example.spirit.threadpooldemo.MainActivity;

import java.io.File;

public class DownloadInfo {

    public String id;
    public String name;
    public long size;
    public String downloadUrl;
    public String packageName;

    public long currentPos;//当前下载的位置
    public int currentState;

    public static final String GOOGLE_MARKET = "google_market";//sdcard根目录文件夹名称
    public static final String DOWNLOAD = "download";//子文件夹名称，存放下载的文件
    private final String url = "http://download.kugou.com/download/kugou_android";


    public String getUrl() {
        return url;
    }

    public void setCurrentPos(long currentPos) {
        this.currentPos = currentPos;
    }

    public float getProgress() {

        if (size == 0) {
            return 0;
        }

        return currentPos / (float) size;
    }

    public DownloadInfo() {}

    public DownloadInfo(String name) {
        this.name = name;
    }

    public String getFilePath() {
        StringBuffer sb = new StringBuffer();
        String sdCard = Environment.getExternalStorageDirectory().getAbsolutePath();
        sb.append(sdCard);
        sb.append(File.separator);
        sb.append(GOOGLE_MARKET);
        sb.append(File.separator);
        sb.append(DOWNLOAD);

        if (CreateDir(sb.toString())) {
            return sb.toString() + File.separator + name + ".apk";
        }

        return sb.toString();
    }

    /**
     * dirFile.exists() && dirFile.isDirectory() || dirFile.mkdirs()
     * <p>
     * ..................--文件是一个文件夹(true)    ***短路***
     * 文件存在(true)......................................................--创建失败(false)     false
     * ..................--文件不是一个文件夹(false)  创建文件夹返回执行结果
     * ...................................................................--创建成功(true)      true
     */
    private boolean CreateDir(String dir) {
        File dirFile = new File(dir);
        return dirFile.exists() && dirFile.isDirectory() || dirFile.mkdirs();
    }


    public String formartUnit() {
        return android.text.format.Formatter.formatFileSize(MainActivity.getContext(), size);
    }
}
