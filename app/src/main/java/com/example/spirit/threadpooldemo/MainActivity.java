package com.example.spirit.threadpooldemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.spirit.threadpooldemo.domain.DownloadInfo;
import com.example.spirit.threadpooldemo.manager.DownloadManager;

public class MainActivity extends AppCompatActivity {

    private static MainActivity mainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainActivity = MainActivity.this;

        DownloadInfo info = new DownloadInfo("kuGo");
        DownloadManager downloadManager = DownloadManager.getDownloadManager();
        downloadManager.setInfo(info);
        //System.out.println(info.getUrl() + ", " + info.getFilePath() + "," + info.getProgress());

        downloadManager.registerObserver(new DownloadManager.DownloadObserver() {
            @Override
            public void onDownloadStateChanged() {

            }

            @Override
            public void onDownloadProgressChanged(float progress) {
                System.out.println("registerObserver" + progress);
            }
        });

        //downloadManager.download();
        //System.out.println(info.size);
    }

    public static MainActivity getContext() {
        return mainActivity;
    }
}
