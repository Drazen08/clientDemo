package com.hema.client;


import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

/**
 * @author sunjx
 */
public class HexStringUtil {

    /**
     * 字符串转换成十六进制字符串
     */
    public static String str2HexStr(String str) {
        char[] chars = "0123456789ABCDEF".toCharArray();
        StringBuilder sb = new StringBuilder("");
        byte[] bs = str.getBytes();
        int bit;
        for (byte b : bs) {
            bit = (b & 0x0f0) >> 4;
            sb.append(chars[bit]);
            bit = b & 0x0f;
            sb.append(chars[bit]);
        }
        return sb.toString();
    }


    /**
     * 把16进制字符串转换成字节数组
     *
     * @param hex hex
     * @return byte[]
     */
    public static byte[] hexStringToByte(String hex) {
        int len = (hex.length() / 2);
        byte[] result = new byte[len];
        char[] achar = hex.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
        }
        return result;
    }

    private static int toByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }


    /**
     * 数组转换成十六进制字符串
     *
     * @param bArray
     * @return HexString
     */
    public static String bytesToHexString(byte[] bArray) {
        StringBuilder sb = new StringBuilder(bArray.length);
        String sTemp;
        for (byte aBArray : bArray) {
            sTemp = Integer.toHexString(0xFF & aBArray);
            if (sTemp.length() < 2) {
                sb.append(0);
            }
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }


    /**
     * 数组转成十六进制字符串
     *
     * @param b byte
     * @return HexString
     */
    public static String toHexString1(byte[] b) {
        StringBuilder buffer = new StringBuilder();
        for (byte aB : b) {
            buffer.append(toHexString1(aB));
        }
        return buffer.toString();
    }

    private static String toHexString1(byte b) {
        String s = Integer.toHexString(b & 0xFF);
        if (s.length() == 1) {
            return "0" + s;
        } else {
            return s;
        }
    }


    /**
     * 十六进制字符串转换成字符串
     *
     * @param hexStr hexStr2Str
     * @return String
     */
    public static String hexStr2Str(String hexStr) {

        String str = "0123456789ABCDEF";
        char[] hexs = hexStr.toCharArray();
        byte[] bytes = new byte[hexStr.length() / 2];
        int n;
        for (int i = 0; i < bytes.length; i++) {
            n = str.indexOf(hexs[2 * i]) * 16;
            n += str.indexOf(hexs[2 * i + 1]);
            bytes[i] = (byte) (n & 0xff);
        }
        return new String(bytes);
    }


    /**
     * 十六进制字符串转换字符串
     *
     * @param s
     * @return String
     */
    public static String toStringHex(String s) {
        byte[] baKeyword = new byte[s.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(
                        i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            // UTF-16le:Not
            s = new String(baKeyword, StandardCharsets.UTF_8);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return s;
    }


    /**
     * string转ASCII
     *
     * @param value
     * @return
     */
    public static String stringToAscii(String value) {
        StringBuilder sbu = new StringBuilder();
        char[] chars = value.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (i != chars.length - 1) {
                sbu.append((int) chars[i]).append(",");
            } else {
                sbu.append((int) chars[i]);
            }
        }
        return sbu.toString();
    }


    /**
     * 在数字前面自动补零
     *
     * @param sourceDate
     * @param formatLength
     * @return
     */
    public static String frontCompWithZore(int sourceDate, int formatLength) {
        return String.format("%0" + formatLength + "d", sourceDate);
    }

    /**
     * 在字符串前补零
     *
     * @param s
     * @param length
     * @return
     */
    public static String leftZeroShift(String s, int length) {
        if (s == null || s.length() > length) {
            return s;
        }
        String str = getZero(length) + s;
        str = str.substring(str.length() - length);
        return str;
    }

    //获取0的字符串
    private static String getZero(int length) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < length; i++) {
            str.append("0");
        }
        return str.toString();
    }


    public static String asciiToString(String value) {
        StringBuilder sbu = new StringBuilder();
        String[] chars = value.split(",");
        for (String aChar : chars) {
            sbu.append((char) Integer.parseInt(aChar));
        }
        return sbu.toString();
    }


    /**
     * 中文转16进制字符串
     *
     * @param s
     * @return
     */
    public static String toChineseHex(String s) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
        String[] saa = new String[s.length()];

        for (int i = 0; i < saa.length; i++) {
            saa[i] = s.substring(i, (i + 1));
        }
        for (String sa : saa) {
            byte[] bytes = sa.getBytes("GBK");
            for (byte b : bytes) {
                int temp = (int) b;
                String me = Integer.toHexString((temp & 0x000000FF) | 0xFFFFFF00).substring(6);
                sb.append(me);
            }
        }
        return sb.toString().toUpperCase();
    }


}
