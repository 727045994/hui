package com.eim.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * base64 编码、解码util
 * 
 * @author luohui
 * @date 2015-3-4 上午09:23:33
 */
public class Base64Utils {
    /**
     * 将 String 进行 BASE64 编码
     *
     * @return String
     * @author luohui
     * @date 2015-3-4 上午09:24:02
     */
    public static String encode(String s) {
        if (s == null)
            return null;
        String res = "";
        try {
            res = new BASE64Encoder().encode(s.getBytes("GBK"));
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 将 BASE64 编码的字符串 S 进行解码
     *
     * @return String
     * @author luohui
     * @date 2015-3-4 上午09:24:26
     */
    public static String decode(String s) {
        if (s == null)
            return null;
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            byte[] b = decoder.decodeBuffer(s);
            return new String(b,"GBK");
        } catch (Exception e) {
            return null;
        }
    }
    /**
     * 
     * @return void
     * @author luohui
     * @date 2015-3-4 上午09:23:17
     */
    public static void main(String[] args) {
        System.out.println(Base64Utils.encode("123123123"));
       

    }

}