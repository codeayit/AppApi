package com.robot.baseapi.FileDownloader;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

import okhttp3.Response;
import okhttp3.ResponseBody;

public abstract class OkFileCallBack extends Callback<File> {

    /**
     * 目标文件存储的文件夹路径
     */
    private String destFileDir;
    /**
     * 目标文件存储的文件名
     */
    private String destFileName;

    /**
     * 状态
     */
    private int status;

    /**
     * 开始位置
     */
    private long startsPoint;

    private long progressDuration;


    public OkFileCallBack(String destFileDir, String destFileName) {
        status = 0;
        this.destFileDir = destFileDir;
        this.destFileName = destFileName;
        this.progressDuration = 1000;
    }

    public OkFileCallBack(String destFileDir, String destFileName, long startsPoint) {
        this.destFileDir = destFileDir;
        this.destFileName = destFileName;
        this.startsPoint = startsPoint;
        status = 0;
        this.progressDuration = 1000;
    }

    public void setProgressDuration(long progressDuration) {
        this.progressDuration = progressDuration;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public boolean isCancle() {
        return status != 0;
    }


    @Override
    public File parseNetworkResponse(Response response, int id) throws Exception {
//        return saveFile(response, id);
        return saveFile(response,startsPoint,id);
    }

    /**
     * 子线程中更新数据库
     *
     * @param currentLenght
     * @param totalLength
     */
    public abstract void inProgressSubThread(long currentLenght, long totalLength);

    /**
     * 主线程中 提示
     *
     * @param totalLength
     */
    public abstract void onStart(long totalLength);


//    public File saveFile(Response response, final int id) throws IOException {
//        InputStream is = null;
//        byte[] buf = new byte[2048];
//        int len = 0;
//        FileOutputStream fos = null;
//        try {
//            is = response.body().byteStream();
//            final long total = response.body().contentLength();
//
//            long sum = 0;
//
//            File dir = new File(destFileDir);
//            if (!dir.exists()) {
//                dir.mkdirs();
//            }
//            File file = new File(dir, destFileName);
//            OkHttpUtils.getInstance().getDelivery().execute(new Runnable() {
//                @Override
//                public void run() {
//                    onStart(total);
//                }
//            });
//
//            fos = new FileOutputStream(file);
//            long startTime = System.currentTimeMillis();
//            while ((len = is.read(buf)) != -1 && !isCancle()) {
//                sum += len;
//                fos.write(buf, 0, len);
//                final long finalSum = sum;
//                inProgressSubThread(finalSum, total);
//                if (System.currentTimeMillis() - startTime > 1000) {
//                    OkHttpUtils.getInstance().getDelivery().execute(new Runnable() {
//                        @Override
//                        public void run() {
//                            inProgress(finalSum * 1.0f / total, total, id);
//                        }
//                    });
//                    startTime = System.currentTimeMillis();
//                }
//            }
//            fos.flush();
//            if (isCancle()) {
//                return null;
//            }
//            return file;
//
//        } finally {
//            try {
//                response.body().close();
//                if (is != null) is.close();
//            } catch (IOException e) {
//            }
//            try {
//                if (fos != null) fos.close();
//            } catch (IOException e) {
//            }
//        }
//    }

    public File saveFile(Response response, final long startsPoint, final int id) throws IOException {
        final ResponseBody body = response.body();
        final long total = body.contentLength()+startsPoint;
        InputStream in = body.byteStream();
        FileChannel channelOut = null;
        // 随机访问文件，可以指定断点续传的起始位置
        RandomAccessFile randomAccessFile = null;
        File file = new File(destFileDir,destFileName);
        if (file.getParentFile().exists()){
            file.getParentFile().mkdirs();
        }else if (startsPoint == 0){
            if (file.exists()){
                file.delete();
            }
        }
        OkHttpUtils.getInstance().getDelivery().execute(new Runnable() {
            @Override
            public void run() {
                onStart(total);
            }
        });
        try {
            randomAccessFile = new RandomAccessFile(file, "rwd");
            //Chanel NIO中的用法，由于RandomAccessFile没有使用缓存策略，直接使用会使得下载速度变慢，亲测缓存下载3.3秒的文件，用普通的RandomAccessFile需要20多秒。
            channelOut = randomAccessFile.getChannel();
            // 内存映射，直接使用RandomAccessFile，是用其seek方法指定下载的起始位置，使用缓存下载，在这里指定下载位置。
            MappedByteBuffer mappedBuffer = channelOut.map(FileChannel.MapMode.READ_WRITE, startsPoint, total-startsPoint);
            byte[] buffer = new byte[2048];
            int len;
            long sum = startsPoint;
            long startTime = System.currentTimeMillis();
            while ((len = in.read(buffer)) != -1 && !isCancle()) {
                sum += len;
                final long finalSum = sum;
                mappedBuffer.put(buffer, 0, len);
                inProgressSubThread(finalSum, total);
                if (System.currentTimeMillis() - startTime > progressDuration) {
                    OkHttpUtils.getInstance().getDelivery().execute(new Runnable() {
                        @Override
                        public void run() {
                            inProgress(finalSum * 1.0f / total, total, id);
                        }
                    });
                    startTime = System.currentTimeMillis();
                }
            }
            if (isCancle()) {
                return null;
            }
            return file;
        }
//        catch (IOException e) {
//            e.printStackTrace();
//        }
        finally {
            try {
                in.close();
                if (channelOut != null) {
                    channelOut.close();
                }
                if (randomAccessFile != null) {
                    randomAccessFile.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
