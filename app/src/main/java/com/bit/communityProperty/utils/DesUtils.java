package com.bit.communityProperty.utils;

/**
 * 加密、解密工具类
 * Created by kezhangzhao on 2018/1/9.
 */

public class DesUtils {

//    public static String decryptDES(String decryptString, String decryptKey) throws Exception {
//        sun.miscc.BASE64Decoder decoder = new sun.miscc.BASE64Decoder();
//        byte[] byteMi = decoder.decodeBuffer(decryptString);
//        IvParameterSpec zeroIv = new IvParameterSpec(iv);
//        SecretKeySpec key = new SecretKeySpec(decryptKey.getBytes(), "DES");
//        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
//        cipher.init(Cipher.DECRYPT_MODE, key, zeroIv);
//        byte decryptedData[] = cipher.doFinal(byteMi);
//
//        return new String(decryptedData);
//    }
}
