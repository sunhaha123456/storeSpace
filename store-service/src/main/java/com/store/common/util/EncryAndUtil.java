package com.store.common.util;

import java.nio.charset.Charset;

/**
 * 功能：与加密算法
 * 备注：只能对数字和字母组成的字符串起作用
 * @author sunpeng
 * @date 2018
 */
public class EncryAndUtil {
    private static final String key0 = IdUtil.getID();
    private static final Charset charset = Charset.forName("UTF-8");
    public static byte[] keyBytes = key0.getBytes(charset);

    // 加密
    public static String encode(String enc){
        byte[] b = enc.getBytes(charset);
        for(int i=0, size=b.length; i < size; i++){
            for(byte keyBytes0 : keyBytes){
                b[i] = (byte) (b[i]^keyBytes0);
            }
        }
        return new String(b);
    }

    // 解密
    public static String decode(String dec){
        byte[] e = dec.getBytes(charset);
        byte[] dee = e;
        for(int i=0, size=e.length; i < size; i++){
            for(byte keyBytes0 : keyBytes){
                e[i] = (byte) (dee[i]^keyBytes0);
            }
        }
        return new String(e);
    }
}