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
}
