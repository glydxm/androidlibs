package com.glyfly.librarys.rsa;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Date;

/**
 * Created by zhangfaming on 2017/9/6.
 */

public class RSAGenetateKey {

    private static BigInteger x; //存储临时的位置变量x，y 用于递归
    private static BigInteger y;

    //欧几里得扩展算法
    private static BigInteger ex_gcd(BigInteger a, BigInteger b) {
        if (b.intValue() == 0) {
            x = new BigInteger("1");
            y = new BigInteger("0");
            return a;
        }
        BigInteger ans = ex_gcd(b, a.mod(b));
        BigInteger temp = x;
        x = y;
        y = temp.subtract(a.divide(b).multiply(y));
        return ans;
    }

    //求反模
    private static BigInteger cal(BigInteger a, BigInteger k) {
        BigInteger gcd = ex_gcd(a, k);
        if (BigInteger.ONE.mod(gcd).intValue() != 0) {
            return new BigInteger("-1");
        }
        //由于我们只求乘法逆元 所以这里使用BigInteger.One,实际中如果需要更灵活可以多传递一个参数,表示模的值来代替这里
        x = x.multiply(BigInteger.ONE.divide(gcd));
        k = k.abs();
        BigInteger ans = x.mod(k);
        if (ans.compareTo(BigInteger.ZERO) < 0) ans = ans.add(k);
        return ans;
    }

    public static RSAKeyPair generateKey(int bitlength) {
        SecureRandom random = new SecureRandom();
        random.setSeed(new Date().getTime());
        BigInteger bigPrimep, bigPrimeq;
        while (!(bigPrimep = BigInteger.probablePrime(bitlength, random)).isProbablePrime(1)) {
            continue;
        }//生成大素数p

        while (!(bigPrimeq = BigInteger.probablePrime(bitlength, random)).isProbablePrime(1)) {
        }//生成大素数q

        BigInteger n = bigPrimep.multiply(bigPrimeq);//生成n
        //生成k
        BigInteger k = bigPrimep.subtract(BigInteger.ONE).multiply(bigPrimeq.subtract(BigInteger.ONE));
        //生成一个比k小的b,或者使用65537
        BigInteger b = BigInteger.probablePrime(bitlength - 1, random);
        //根据扩展欧几里得算法生成b
        BigInteger a = cal(b, k);
        //存储入 公钥与私钥中
        String priKey = a + "," + n;
        String pubKey = b + "," + n;
        String privateKey = encrypt(priKey);
        String publicKey = encrypt(pubKey);
        //生成秘钥对 返回密钥对
        return new RSAKeyPair(publicKey, privateKey);
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
