package com.robot.baseapi.util;

import java.security.MessageDigest;

import static android.text.TextUtils.isEmpty;

/**
 * Created by lny on 2017/11/20.
 */

public class StringUtil {
    /**
     * 补齐不足长度
     *
     * @param length 长度
     * @param number 数字
     * @return
     */
    public static String strAutoFitLength(int length, int number) {
        String f = "%0" + length + "d";
        return String.format(f, number);
    }


    //生成MD5
    public static String encodeMD5(String message) {
        String md5 = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");  // 创建一个md5算法对象
            byte[] messageByte = message.getBytes("UTF-8");
            byte[] md5Byte = md.digest(messageByte);              // 获得MD5字节数组,16*8=128位
            md5 = bytesToHex(md5Byte);                            // 转换为16进制字符串
        } catch (Exception e) {
            e.printStackTrace();
        }
        return md5;
    }

    // 二进制转十六进制
    public static String bytesToHex(byte[] bytes) {
        StringBuffer hexStr = new StringBuffer();
        int num;
        for (int i = 0; i < bytes.length; i++) {
            num = bytes[i];
            if(num < 0) {
                num += 256;
            }
            if(num < 16){
                hexStr.append("0");
            }
            hexStr.append(Integer.toHexString(num));
        }
        return hexStr.toString().toUpperCase();
    }

    /**
     * 从http下载链接地址中获得文件名
     *
     * @param url
     * @return
     */
    public static String cutFileNameFromUrl(String url) {
        if (isEmpty(url)) {
            return null;
        }
        String[] parters = url.split("/");
        return parters[parters.length - 1];

    }


}
