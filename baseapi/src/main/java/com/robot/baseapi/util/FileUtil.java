package com.robot.baseapi.util;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by lny on 2017/10/20.
 */

public class FileUtil {
    /**
     * 复制asset文件到指定目录
     *
     * @param oldPath asset下的路径
     * @param newPath SD卡下保存路径
     */
    public static boolean CopyAssets(Context context, String oldPath, String newPath) {
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            is = context.getAssets().open(oldPath);
            File targetFile = new File(newPath);
            if (!targetFile.getParentFile().exists()) {
                targetFile.getParentFile().mkdirs();
            }
            fos = new FileOutputStream(targetFile);
            byte[] buffer = new byte[1024];
            int byteCount = 0;
            while ((byteCount = is.read(buffer)) != -1) {// 循环从输入流读取
                // buffer字节
                fos.write(buffer, 0, byteCount);// 将读取的输入流写入到输出流
            }
            fos.flush();// 刷新缓冲区

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (is != null)
                    is.close();
            } catch (IOException e) {
                is = null;
                e.printStackTrace();
            } finally {
                try {
                    if (fos != null)
                        fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    fos = null;
                }
            }

        }

    }


    public static boolean copyFile(String oldPath, String targetPath) {
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            File oldFile = new File(oldPath);
            if (!oldFile.exists()) {
                throw new Exception("oldPath is not exists ");
            }
            is = new FileInputStream(oldFile);
            File targetFile = new File(targetPath);
            if (!targetFile.getParentFile().exists()) {
                targetFile.getParentFile().mkdirs();
            }
            fos = new FileOutputStream(targetFile);
            byte[] buffer = new byte[1024];
            int byteCount = 0;
            while ((byteCount = is.read(buffer)) != -1) {// 循环从输入流读取
                // buffer字节
                fos.write(buffer, 0, byteCount);// 将读取的输入流写入到输出流
            }
            fos.flush();// 刷新缓冲区
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (is != null)
                    is.close();
            } catch (IOException e) {
                is = null;
                e.printStackTrace();
            } finally {
                try {
                    if (fos != null)
                        fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    fos = null;
                }
            }

        }
    }

    public static String readFile(String path){
        return readFile(path,1024);
    }

    public static String readFile(String path,int bufferSize){
        StringBuilder sb =  new StringBuilder();
        InputStream fis = null;
        try {
            fis = new FileInputStream(path);
            byte[] buffer = new byte[bufferSize];

            int len = 0;
            while ((len = fis.read(buffer))!=-1){
                sb.append(new String(buffer,0,len));
            }

        } catch (IOException e) {
            e.printStackTrace();
//            MyLog.JUN_KANG.d(" ***ERROR*** read file: " + e.getMessage());
        } finally {
            if(fis != null){
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    fis = null;
                }
            }
        }
        return sb.toString();
    }

    public static String readAssetsFile(Context context,String path){
        return readAssetsFile(context,path,1024);
    }

    public static String readAssetsFile(Context context,String path,int bufferSize){
        StringBuilder sb =  new StringBuilder();
        InputStream fis = null;
        try {
            fis = context.getAssets().open(path);
            byte[] buffer = new byte[bufferSize];
            int len = 0;
            while ((len = fis.read(buffer))!=-1){
                sb.append(new String(buffer,0,len));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(fis != null){
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    fis = null;
                }
            }
        }
        return sb.toString();
    }

}
