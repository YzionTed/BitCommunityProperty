package com.bit.communityProperty.utils;

import android.text.TextUtils;

import java.util.regex.Pattern;

/**
 * KangTuUpperComputer-com.kangtu.uppercomputer.ble
 * 作者： YanwuTang
 * 时间： 2016/9/7.
 */
public class HexUtil {

    /**
     * int 转 byte[]
     * @param i
     * @return
     */
    public static byte[] intToByteArray(int i) {
        byte[] result = new byte[4];
        result[0] = (byte) ((i >> 24) & 0xFF);
        result[1] = (byte) ((i >> 16) & 0xFF);
        result[2] = (byte) ((i >> 8) & 0xFF);
        result[3] = (byte) (i & 0xFF);
        return result;
    }

    /**
     * byte[] 2 int
     * @param src
     * @param offset
     * @return
     */
    public static int bytesToInt(byte[] src, int offset) {
        int value = 0;
        if (src == null) return 0;
        for (int i = 0; i < src.length; i ++){
            value = ((src[offset + i] & 0xFF) << ((src.length - i - 1) * 8)) | value;
        }
        return value;
    }

    /**
     * 将16进制的字符串转换成byte[] 默认是双数
     *
     * @param str
     * @return
     */
    public static byte[] strTobyte(String str) {
        int length = str.length() / 2;
        byte[] value = new byte[length];
        char[] strChar = str.toCharArray();
        for (int i = 0, j = 0; i < length; i++) {
            value[i] = (byte) (toDigit(strChar[j], strChar[j + 1]) & 0xFF);
            j += 2;
//            value[i] = (byte) Integer.parseInt(str.substring(i, i + 2), 16);
        }
        return value;
    }

//    public static int toDigit(String str) {
//        int res = 0;
//        char[] sour = str.toCharArray();
//        for (int i = 0; i < sour.length - 1; i++) {
//            res += toDigit(sour[i]) * 16 ^ i;
//        }
//        res += toDigit(sour[sour.length - 1]);
//        return res;
//    }

    public static int toDigit(char h) {
        int hd = Character.digit(h, 16);
        if (hd == -1) {
            throw new RuntimeException("Illegal hexadecimal character " + h);
        }
        return hd;
    }

    public static int toDigit(char h, char l) {
        int hd = Character.digit(h, 16);
        int ld = Character.digit(l, 16);
        if (hd == -1 || ld == -1) {
            throw new RuntimeException("Illegal hexadecimal character " + h + " " + l);
        }
        return hd * 16 + ld;
    }


    /**
     * 获取校验码 XOR 异或
     *
     * @param data
     * @return
     */
    public static byte getCheckByte(byte[] data) {
        byte value = data[0];
        for (int i = 1; i < data.length; i++) {
            value = (byte) (value ^ data[i]);
        }
        return value;
    }

    /**
     *  校验和
     * SUM=(CMD+DN+DorA+DATA)&0x000000FF 最低位字节
     * @param cmd 参数都是16进制字符串
     * @return
     */
    public static String getCheckSUMByte(String cmd, String dn, String dorA, String data){
        int sum = 0;
        sum += Integer.valueOf(cmd, 16);
        sum += Integer.valueOf(dn.substring(0, 2), 16);
        sum += Integer.valueOf(dn.substring(2, 4), 16);

        for (int i = 0; i < dorA.length() / 2; i ++) {
            sum += Integer.valueOf(dorA.substring(i*2, (i + 1)*2), 16);
        }
        if (data != null) {
            for (int i = 0; i < data.length() / 2; i ++) {
                sum += Integer.valueOf(data.substring(i*2, (i + 1)*2), 16);
            }
        }
        LogUtil.d("getCheckSUMByte", "sum:" + sum + " HexString(sum):" + Integer.toHexString(sum) + " check:" + getLowestbyteStr(Integer.toHexString(sum)) + " ");

        return getLowestbyteStr(Integer.toHexString(sum));
    }

    /**
     * 获取校验
     * @param data
     * @return
     */
    public static String getCheckSUMByte(String data){
        int sum = 0;
        if (data != null) {
            if (data.length() % 2 > 0){
                data = "0" + data; // 补位
            }
            for (int i = 0; i < data.length() / 2; i ++) {
                sum += Integer.valueOf(data.substring(i*2, (i + 1)*2), 16);
            }
        }
        LogUtil.d("getCheckSUMByte", "sum:" + sum + " HexString(sum):" + Integer.toHexString(sum) + " check:" + getLowestbyteStr(Integer.toHexString(sum)) + " ");

        return getLowestbyteStr(Integer.toHexString(sum));
    }

    /**
     * 取 最低为字节
     * @param value
     * @return
     */
    private static String getLowestbyteStr(String value){
        if (!TextUtils.isEmpty(value)){
            return value.length() > 2 ? value.substring(value.length() - 2): value;
        }else {
            return "00";
        }
    }

    public static String byteToStr(byte check) {
        String result = Integer.toHexString(check & 0xFF);
        return result.length() == 1 ? "0" + result : result;
    }

    public static String bytesToStr(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            if (Integer.toHexString(bytes[i] & 0xFF).length() < 2) {
                sb.append("0").append(Integer.toHexString(bytes[i] & 0xFF));
                continue;
            }
            sb.append(Integer.toHexString(bytes[i] & 0xFF));
        }
        return sb.toString();
    }

    /**
     * 16进制转 2进制
     *
     * @param hexString
     * @return
     */
    public static String hexString2binaryString(String hexString) {
        if (hexString == null || hexString.length() % 2 != 0)
            return null;
        String tmp;
        StringBuffer bString = new StringBuffer();
        for (int i = 0; i < hexString.length(); i++) {
            tmp = "0000"
                    + Integer.toBinaryString(Integer.parseInt(hexString
                    .substring(i, i + 1), 16));
            bString.append(tmp.substring(tmp.length() - 4));
        }
        return bString.toString();
    }

    /**
     * 二进制字符串 转 boolean[]数组
     *
     * @param binary
     * @return
     */
    public static boolean[] binaryStr2booleanArr(String binary) {
        if(TextUtils.isEmpty(binary)){
            return null;
        }
        boolean[] result = new boolean[binary.length()];
        for (int i = 0; i < binary.length(); i++) {
            result[i] = (!binary.substring(i, i + 1).equals("0"));
        }
        LogUtil.d("binaryStr2booleanArr", binary);
        return result;
    }

    /**
     * boolean[] 转二进制
     * @param binary
     * @return
     */
    public static String booleanArr2binaryStr(boolean[] binary) {
        StringBuffer str = new StringBuffer();
        for (int i = 0; i< binary.length; i++){
            str.append(binary[i] ? 1 : 0);
        }
        return str.toString();
    }

    /**
     * 二进制转 16进制
     * @param binary
     * @return
     */
    public static String binary2HexString(String binary){
        //先补齐
        int zeroes = binary.length() % 4;
        for(int i = 0 ; zeroes >0 && i < 4 - zeroes ; i ++){
            binary  = "0" + binary ;
        }
        //然后每四位替换
        StringBuilder sb = new StringBuilder();
        for(int i = 0 ; i < binary.length() ; i += 4){
            String tempString = binary.substring(i, i + 4);
            sb.append(Integer.toHexString(Integer.valueOf(tempString, 2)));
        }
        return sb.toString();
    }

//    /**
//     * str 转换成4byte 的 str
//     * @param str
//     * @return
//     */
//    public static String str2FourHexStr(String str){
//        if (str.length() == 8){
//            return str;
//        }
//        char[] reslut = AddsParser.CMD_READ_DATA.toCharArray();
//        for (int i = str.length() - 1, j = reslut.length -1; i >= 0; i -- , j --){
//            reslut[j] = str.charAt(i);
//        }
//        return String.valueOf(reslut);
//    }
//    /**
//     * str 转换成2byte 的 str
//     * @param str
//     * @return
//     */
//    public static String str2TwoHexStr(String str){
//        if (str.length() == 4){
//            return str;
//        }
//        char[] reslut = AddsParser.FREQUENCY_CMD_READ_LOW_FOUR.toCharArray();
//        for (int i = str.length() - 1, j = reslut.length -1; i >= 0; i -- , j --){
//            reslut[j] = str.charAt(i);
//        }
//        return String.valueOf(reslut);
//    }

    /**
     * 16进制字符串 左边补0
     * @param length not byteLength ，is bytelength * 2
     * @param str
     * @return
     */
    public static String hexStrFormat(int length, String str){
        if (str.length() >= length) return str;
        int d = length - str.length();
        StringBuffer sb = new StringBuffer(String.format("%0" + d + "d", 0));
        return sb.append(str).toString();
    }

    public static String sumHex(String str1, String str2){
        if (str1.length() != str2.length()){          // 如果 长度不对 先补齐
            if (str1.length() < str2.length()){
                for(int i = str1.length() ; i < str2.length() ; i ++){
                    str1  = "0" + str1 ;
                }
            }else {
                for(int i = str2.length() ; i < str1.length() ; i ++){
                    str2  = "0" + str2 ;
                }
            }
        }

        char[] char1 = str1.toCharArray();
        char[] char2 = str2.toCharArray();
        StringBuffer stringBuffer = new StringBuffer();
        int addHigh = 0;       // 进位
        for (int i = char1.length - 1; i >= 0 ; i --){
            int a = toDigit(char1[i]);
            int b = toDigit(char2[i]);
            String hex = Integer.toHexString(a + b + addHigh);
            addHigh = 0;    // 先进位 再滞空
            if (hex.length() > 1){
                addHigh = toDigit(hex.charAt(0));
                stringBuffer.append(hex.charAt(1));
            } else {
                stringBuffer.append(hex);
            }

        }
        // 得到的是个倒序
        StringBuffer result = new StringBuffer();
        for (int i = stringBuffer.length() - 1; i >= 0 ; i --){
            result.append(stringBuffer.charAt(i));
        }
        return result.toString();
    }

    public static boolean checkHex(String aNumber) {
        String regString = "[0-9a-fA-F]";
        return Pattern.matches(regString, aNumber);
    }

    public byte [] getMacBytes(String mac){
        byte []macBytes = new byte[6];
        String [] strArr = mac.split(":");

        for(int i = 0;i < strArr.length; i++){
            int value = Integer.parseInt(strArr[i],16);
            macBytes[i] = (byte) value;
        }
        return macBytes;
    }

    /**
     * 合并两个byte数组
     * @param pByteA
     * @param pByteB
     * @return
     */
    public static byte[] getMergeBytes(byte[] pByteA, byte[] pByteB){
        int aCount = pByteA.length;
        int bCount = pByteB.length;
        byte[] b = new byte[aCount + bCount];
        for(int i=0;i<aCount;i++){
            b[i] = pByteA[i];
        }
        for(int i=0;i<bCount;i++){
            b[aCount + i] = pByteB[i];
        }
        return b;
    }

    /**
     * 字符串转换成十六进制字符串
     * @return String 每个Byte之间空格分隔，如: [61 6C 6B]
     */
    public static String str2HexStr(String str)
    {

        char[] chars = "0123456789ABCDEF".toCharArray();
        StringBuilder sb = new StringBuilder("");
        byte[] bs = str.getBytes();
        int bit;

        for (int i = 0; i < bs.length; i++)
        {
            bit = (bs[i] & 0x0f0) >> 4;
            sb.append(chars[bit]);
            bit = bs[i] & 0x0f;
            sb.append(chars[bit]);
            sb.append(' ');
        }
        return sb.toString().trim();
    }

    public static String byteArrayToHex(byte[] var0) {
        if(var0 != null && var0.length != 0) {
            StringBuilder var1 = new StringBuilder(var0.length);

            for(int var2 = 0; var2 < var0.length; ++var2) {
                var1.append(String.format("%02X", new Object[]{Byte.valueOf(var0[var2])}));
                if(var2 < var0.length - 1) {
                    var1.append(' ');
                }
            }

            return var1.toString();
        } else {
            return "";
        }
    }
}
