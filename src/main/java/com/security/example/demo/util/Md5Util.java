//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.security.example.demo.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.charset.Charset;
import java.security.MessageDigest;

public class Md5Util {
    private static MessageDigest md5 = null;
    private static final char[] hexDigits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    private static final String UTF_8 = "UTF-8";

    public Md5Util() {
    }

    public static String getMd5(String str) {
        byte[] bs = md5.digest(str.getBytes(Charset.forName("UTF-8")));
        StringBuilder sb = new StringBuilder(40);
        byte[] var3 = bs;
        int var4 = bs.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            byte x = var3[var5];
            if ((x & 255) >> 4 == 0) {
                sb.append("0").append(Integer.toHexString(x & 255));
            } else {
                sb.append(Integer.toHexString(x & 255));
            }
        }

        return sb.toString();
    }

    public static String getFileMD5String(File file) throws IOException {
        FileInputStream in = new FileInputStream(file);
        FileChannel ch = in.getChannel();
        MappedByteBuffer byteBuffer = ch.map(MapMode.READ_ONLY, 0L, file.length());
        md5.update(byteBuffer);
        return bufferToHex(md5.digest());
    }

    public static String getMD5String(String s) {
        return getMD5String(s.getBytes(Charset.forName("UTF-8")));
    }

    public static String getMD5String(byte[] bytes) {
        md5.update(bytes);
        return bufferToHex(md5.digest());
    }

    private static String bufferToHex(byte[] bytes) {
        return bufferToHex(bytes, 0, bytes.length);
    }

    private static String bufferToHex(byte[] bytes, int m, int n) {
        StringBuffer stringbuffer = new StringBuffer(2 * n);
        int k = m + n;

        for(int l = m; l < k; ++l) {
            appendHexPair(bytes[l], stringbuffer);
        }

        return stringbuffer.toString();
    }

    private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
        char c0 = hexDigits[(bt & 240) >> 4];
        char c1 = hexDigits[bt & 15];
        stringbuffer.append(c0);
        stringbuffer.append(c1);
    }



    static {
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception var1) {
            System.out.println(var1.getMessage());
        }

    }
}
