package com.bit.communityProperty.data.download;

import android.os.Handler;

import com.bit.communityProperty.service.UpdateApk;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by kezhangzhao on 2018/1/13.
 */

public class DownloadTask extends Thread{
    private static final int TIME_OUT = 60 * 1000; // 超时时间

    private int blockSize, downloadSizeMore;
    private int threadNum = 5;
    String urlStr, threadNo, fileName;
    Handler handler;
    public int fileSize, downloadedSize;

    public DownloadTask(String urlStr, int threadNum, String fileName,
                        Handler handler, int fileSize, int downloadedSize) {
        this.urlStr = urlStr;
        this.threadNum = threadNum;
        this.fileName = fileName;
        this.handler = handler;
        this.fileSize = fileSize;
        this.downloadedSize = downloadedSize;
    }

    @Override
    public void run() {
        FileDownloadThread[] fds = new FileDownloadThread[threadNum];
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(TIME_OUT);
            conn.setReadTimeout(TIME_OUT);
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                // 获取下载文件的总大小
                fileSize = conn.getContentLength();
                // 计算每个线程要下载的数据量
                blockSize = fileSize / threadNum;
                // 解决整除后百分比计算误差
                downloadSizeMore = (fileSize % threadNum);
                File file = new File(fileName);
                for (int i = 0; i < threadNum; i++) {
                    // 启动线程，分别下载自己需要下载的部分
                    FileDownloadThread fdt = new FileDownloadThread(url, file,
                            i * blockSize, (i + 1) * blockSize - 1);
                    fdt.setName("Thread" + i);
                    fdt.start();
                    fds[i] = fdt;
                }

                boolean finished = false;
                while (!finished) {
                    // 先把整除的余数搞定
                    downloadedSize = downloadSizeMore;
                    finished = true;
                    for (int i = 0; i < fds.length; i++) {
                        downloadedSize += fds[i].getDownloadSize();
                        if (!fds[i].isFinished()) {
                            finished = false;
                        }
                    }
                    // 通知handler去更新视图组件
                    handler.sendEmptyMessage(UpdateApk.PROGRESSING);
                    // 休息1秒后再读取下载进度
                    sleep(1000);
                }
            } else {
                handler.sendEmptyMessage(UpdateApk.UPDATERR);
            }
        } catch (Exception e) {

        }
    }
}
