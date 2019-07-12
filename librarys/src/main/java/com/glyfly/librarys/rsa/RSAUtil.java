package com.glyfly.librarys.rsa;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;

/**
 * Created by zhangfaming on 2017/9/6.
 */

public class RSAUtil {

    public static String encrypt(String source, String key, String charset) {

        String realKey = decrypt(key);
        String[] nums = realKey.split(",");
        BigInteger b = new BigInteger(nums[0]);
        BigInteger n = new BigInteger(nums[1]);

        byte[] sourceByte = null;
        try {
            sourceByte = source.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        BigInteger temp = new BigInteger(1, sourceByte);
        BigInteger encrypted = temp.modPow(b, n);
        String s = Base64.encodeToString(encrypted.toByteArray(), Base64.DEFAULT);
        return s;
    }

    public static String decrypt(String cryptdata, String key, String charset) throws UnsupportedEncodingException {

        String realKey = decrypt(key);
        String[] nums = realKey.split(",");
        BigInteger a = new BigInteger(nums[0]);
        BigInteger n = new BigInteger(nums[1]);

        byte[] byteTmp = Base64.decode(cryptdata.getBytes(), Base64.DEFAULT);
        BigInteger cryptedBig = new BigInteger(byteTmp);
        byte[] cryptedData = cryptedBig.modPow(a, n).toByteArray();
        return new String(cryptedData, charset);
    }

    /**
     *  不固定key
     *  key值 0x01-0x7f（1-127）
     *  @param string 需要加密的字符串
     *  @return 加密后的字符串
     */
    public static String encrypt(String string){
        if (string == null || "".equals(string.trim())){
            return "";
        }
        try {
            byte[] bytes = string.getBytes("UTF8");
            int len = bytes.length;
            int key = 0x69;
            for (int i = 0; i < len; i++) {
                bytes[i] = (byte) (bytes[i] ^ key);
                key = bytes[i];
            }
            return new String(bytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     *  不固定key
     *  key值 0x01-0x7f（1-127）
     *  @param string 需要解密的字符串
     *  @return 解密后的字符串
     */
    public static String decrypt(String string){
        if (string == null || "".equals(string.trim())){
            return "";
        }
        try {
            byte[] bytes = string.getBytes("UTF8");
            int len = bytes.length;
            int key = 0x69;
            for (int i = len - 1; i > 0; i--) {
                bytes[i] = (byte) (bytes[i] ^ bytes[i - 1]);
            }
            bytes[0] = (byte) (bytes[0] ^ key);
            return new String(bytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }
}
