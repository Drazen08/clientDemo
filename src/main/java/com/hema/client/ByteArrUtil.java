package com.hema.client;



import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author sunjx
 * @Date 2018/8/7 18:00
 */
public class ByteArrUtil {

    /**
     * int转byte
     *
     * @param i
     * @return
     */
    public static byte intToByteArray1(int i) {
        if (i > 255) {
            return 0;
        }
        // 由高位到低位
        return (byte) (i & 0xFF);
    }

    /**
     * byte[2]转为 int
     *
     * @param bytes
     * @return
     */
    public static int bytes2ToInt(byte[] bytes) {
        int num = bytes[1] & 0xFF;
        num |= ((bytes[0] << 8) & 0xFF00);
        return num;
    }

    /**
     * int转byte
     *
     * @param b
     * @return
     */
    public static Integer byteToInt(byte b) {
        return (b & 0xFF);
    }


    /**
     * int转byte[]
     *
     * @param i
     * @return
     */
    public static byte[] intToByteArray2(int i) {
        if (i > 65535) {
           return null;
        }
        byte[] result = new byte[2];
        // 由高位到低位
        result[0] = (byte) ((i >> 8) & 0xFF);
        result[1] = (byte) (i & 0xFF);
        return result;
    }

    /**
     * byte[]转int
     *
     * @param bytes
     * @return
     */
    public static Integer byteArrayToInt2pri(byte[] bytes) {
        if (bytes == null || bytes.length > 2) {
            return 0;
        }
        int value = 0;
        // 由高位到低位
        for (int i = 0; i < 2; i++) {
            int shift = (2 - 1 - i) * 8;
            value += (bytes[i] & 0x000000FF) << shift;// 往高位游
        }
        return value;
    }

    /**
     * Byte[]转int
     *
     * @param bytes
     * @return
     */
    public static Integer byteArrayToInt2(Byte[] bytes) {
        if (bytes == null || bytes.length > 2) {
            return 0;
        }
        int value = 0;
        // 由高位到低位
        for (int i = 0; i < 2; i++) {
            int shift = (2 - 1 - i) * 8;
            value += (bytes[i] & 0x000000FF) << shift;// 往高位游
        }
        return value;
    }

    /**
     * int 转 byte [4]
     *
     * @param s
     * @param asc
     * @return
     */
    public static byte[] intToByte4(int s, boolean asc) {
        byte[] buf = new byte[4];
        if (asc) {
            for (int i = buf.length - 1; i >= 0; i--) {
                buf[i] = (byte) (s & 0x000000ff);
                s >>= 8;
            }
        } else {
            for (int i = 0; i < buf.length; i++) {
                buf[i] = (byte) (s & 0x000000ff);
                s >>= 8;
            }
        }
        return buf;
    }

    /**
     * byte [4] 转 int
     *
     * @param buf
     * @param asc
     * @return
     */
    public final static int byteToInt4(byte[] buf, boolean asc) {
        if (buf == null) {
            throw new IllegalArgumentException("byte array is null!");
        }
        if (buf.length > 4) {
            throw new IllegalArgumentException("byte array size > 4 !");
        }
        int r = 0;
        if (asc) {
            for (int i = buf.length - 1; i >= 0; i--) {
                r <<= 8;
                r |= (buf[i] & 0xff);
            }
        } else {
            for (int i = 0; i < buf.length; i++) {
                r <<= 8;
                r |= (buf[i] & 0xff);
            }
        }
        return r;
    }

    /**
     * byte[] 转 Byte[]
     *
     * @param bytesPrim
     * @return
     */
    public static Byte[] toObjects(byte[] bytesPrim) {
        Byte[] bytes = new Byte[bytesPrim.length];
        int i = 0;
        for (byte b : bytesPrim) {
            // Autoboxing
            bytes[i++] = b;
        }
        return bytes;
    }


    /**
     * @param oBytes
     * @return
     */
    public static byte[] toPrimitives(Byte[] oBytes) {
        byte[] bytes = new byte[oBytes.length];

        for (int i = 0; i < oBytes.length; i++) {
            bytes[i] = oBytes[i];
        }

        return bytes;
    }


    /**
     * double 转 byte[]
     *
     * @param d
     * @return
     */
    public static byte[] double2Bytes(double d) {
        long value = Double.doubleToRawLongBits(d);
        byte[] byteRet = new byte[8];
        for (int i = 0; i < 8; i++) {
            byteRet[i] = (byte) ((value >> 8 * i) & 0xff);
        }
        return byteRet;
    }

    /**
     * byte [] 转 double
     *
     * @param arr
     * @return
     */
    public static double bytes2Double(byte[] arr) {
        long value = 0;
        for (int i = 0; i < 8; i++) {
            value |= ((long) (arr[i] & 0xff)) << (8 * i);
        }
        return Double.longBitsToDouble(value);
    }

    /**
     * 截取byte 数组并返回
     *
     * @param result
     * @param length
     * @param offset
     * @return
     */
    public static byte[] getBytes(byte[] result, int length, int offset) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(length);
        byteBuffer.put(result, offset, length);
        byte[] array = byteBuffer.array();
        return array;
    }


    public static ArrayList<Byte> bytesToList(byte[] bytes) {
        ArrayList<Byte> list = new ArrayList<>();
        for (int i = 0; i < bytes.length; i++) {
            list.add(bytes[i]);
        }
        return list;
    }


    public static byte[] listTobyte(ArrayList<Byte> list) {
        if (list == null || list.size() == 0) {
            return null;
        }
        byte[] bytes = new byte[list.size()];
        int i = 0;
        Iterator<Byte> iterator = list.iterator();
        while (iterator.hasNext()) {
            bytes[i] = iterator.next();
            i++;
        }
        return bytes;
    }

}
