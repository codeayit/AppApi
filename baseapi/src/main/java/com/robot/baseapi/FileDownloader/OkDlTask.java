package com.robot.baseapi.FileDownloader;

import android.support.annotation.NonNull;

import org.litepal.crud.LitePalSupport;

import java.util.Objects;

public class OkDlTask extends LitePalSupport {

    public interface Status{
        public static final int STATUS_WAITING = 0;
        public static final int STATUS_DOWNLOADING = 1;
        public static final int STATUS_PAUSE = 2;
        public static final int STATUS_SUCCESS = 3;
        public static final int STATUS_ERROR = 4;
        public static final int STATUS_DISCARD = 5;
    }

    public interface Field{
        String url="url";
        String status="status";
        String fileName = "fileName";
        String totalLength="totalLength";
        String currentLength="currentLength";
        String flag = "flag";
    }




    private String url;
    private String dir;
    private int status;
    private String fileName;
    private long totalLength;
    private long currentLength;
    private int flag;

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }



    public OkDlTask(int flag, @NonNull String url, @NonNull String dir) {
        this.url = url;
        this.flag = flag;
        String[] split = url.split("/");
        this.fileName = split[split.length-1];
        this.dir = dir;
        init();
    }

    public OkDlTask(int flag, @NonNull String url, @NonNull String dir, @NonNull String fileName) {
        this.url = url;
        this.flag = flag;
        this.dir = dir;
        this.fileName = fileName;
        init();
    }

    private void init(){
        status = Status.STATUS_WAITING;
    }

    public String getUrl() {
        return url;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getTotalLength() {
        return totalLength;
    }

    public void setTotalLength(long totalLength) {
        this.totalLength = totalLength;
    }

    public long getCurrentLength() {
        return currentLength;
    }

    public void setCurrentLength(long currentLength) {
        this.currentLength = currentLength;
    }

    @Override
    public String toString() {
        return "OkDlTask{" +
                "url='" + url + '\'' +
                ", dir='" + dir + '\'' +
                ", status=" + status +
                ", fileName='" + fileName + '\'' +
                ", totalLength=" + totalLength +
                ", currentLength=" + currentLength +
                ", flag=" + flag +
                '}';
    }

    public String getLocalPath(){
        return dir+"/"+fileName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OkDlTask task = (OkDlTask) o;
        return Objects.equals(url, task.url);
    }

    @Override
    public int hashCode() {

        return Objects.hash(url);
    }
}
