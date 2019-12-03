package utils;

import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;

/**
 * @ClassName ByteUtil
 * @Description
 * @Date 2019/11/26
 * @Created by lizhanxu
 */
@Slf4j
public class ByteUtil {
    /**
     * long 转 byte[]
     */
    public static byte[] longToBytes(long value) {
        byte[] bytes = new byte[8];
        bytes[0] = (byte) ((value >> 56) & 0xFF);
        bytes[1] = (byte) ((value >> 48) & 0xFF);
        bytes[2] = (byte) ((value >> 40) & 0xFF);
        bytes[3] = (byte) ((value >> 32) & 0xFF);
        bytes[4] = (byte) ((value >> 24) & 0xFF);
        bytes[5] = (byte) ((value >> 16) & 0xFF);
        bytes[6] = (byte) ((value >> 8) & 0xFF);
        bytes[7] = (byte) (value & 0xFF);
        return bytes;
    }

    /**
     * int 转 byte[]
     */
    public static byte[] intToBytes(int value) {
        byte[] bytes = new byte[4];
        bytes[0] = (byte) ((value >> 24) & 0xFF);
        bytes[1] = (byte) ((value >> 16) & 0xFF);
        bytes[2] = (byte) ((value >> 8) & 0xFF);
        bytes[3] = (byte) (value & 0xFF);
        return bytes;
    }

    /**
     * int 转 byte[]
     */
    public static byte[] intTo2Bytes(int value) {
        byte[] bytes = new byte[2];
        bytes[0] = (byte) ((value >> 8) & 0xFF);
        bytes[1] = (byte) (value & 0xFF);
        return bytes;
    }

    /**
     * int 转 byte
     */
    public static byte intToByte(int value) {
        return (byte) (value & 0xFF);
    }

    /**
     * short 转 byte[]
     */
    public static byte[] shortToBytes(short value) {
        byte[] bytes = new byte[2];
        bytes[0] = (byte) ((value >> 8) & 0xFF);
        bytes[1] = (byte) (value & 0xFF);
        return bytes;
    }

    /**
     * byte[] 转 long
     */
    public static long bytesToLong(byte[] bytes) {
        return (long) ((bytes[0] & 0xFF) << 56) |
                (long) ((bytes[1] & 0xFF) << 48) |
                (long) ((bytes[2] & 0xFF) << 40) |
                (long) ((bytes[3] & 0xFF) << 32) |
                (long) ((bytes[4] & 0xFF) << 24) |
                (long) ((bytes[5] & 0xFF) << 16) |
                (long) ((bytes[6] & 0xFF) << 8) |
                (long) ((bytes[7] & 0xFF));
    }

    /**
     * byte[] 转 int
     */
    public static int bytesToInt(byte[] bytes) {
        return (bytes[0] & 0xFF) << 24 |
                (bytes[1] & 0xFF) << 16 |
                (bytes[2] & 0xFF) << 8 |
                (bytes[3] & 0xFF);
    }

    /**
     * byte[] 转 无符号 int
     */
    public static int bytes2ToUInt(byte[] bytes) {
        return bytesToShort(bytes) & 0xFFFF;
    }

    /**
     * byte[] 转 short
     */
    public static short bytesToShort(byte[] bytes) {
        return (short) ((bytes[0] & 0xFF) << 8 | (bytes[1] & 0xFF));
    }

    /**
     * 字节转十六进制
     *
     * @param b 需要进行转换的byte字节
     * @return 转换后的Hex字符串
     */
    public static String byteToHex(byte b) {
        String hex = Integer.toHexString(b & 0xFF);
        if (hex.length() < 2) {
            hex = "0" + hex;
        }
        return hex;
    }

    /**
     * @return java.lang.String
     * @Description 字节数组转Hex进制字符串
     * @Param [bytes]
     * @Date 13:52 2019/6/26
     **/
    public static String bytesToHex(byte[] bytes) {
        StringBuffer sb = new StringBuffer(bytes.length);
        String temp = null;
        int size = bytes.length;
        for (int i = 0; i < size; i++) {
            temp = Integer.toHexString(0xFF & bytes[i]);
            if (temp.length() < 2)
                sb.append(0);
            sb.append(temp);
        }
        return sb.toString();
    }

    public static String intToHex(int value) {
        return bytesToHex(intTo2Bytes(value));
    }

    /**
     * Hex字符串转byte
     *
     * @param hex 待转换的Hex字符串
     * @return 转换后的byte
     */
    public static byte hexToByte(String hex) {
        return (byte) (Integer.parseInt(hex, 16) & 0xFF);
    }

    /**
     * hex字符串转byte数组
     *
     * @param hex 待转换的Hex字符串
     * @return 转换后的byte数组结果
     */
    public static byte[] hexToBytes(String hex) {
        int length = hex.length() / 2;
        byte[] bytes = new byte[length];
        for (int i = 0; i < length; i++) {
            String subStr = hex.substring(i * 2, i * 2 + 2);
            bytes[i] = (byte) (Integer.parseInt(subStr, 16) & 0xFF);
        }
        return bytes;
    }

    /**
     * 读字节数组
     *
     * @param index  从字节数组第几位开始读取
     * @param length 读取长度
     * @param source 源数组
     * @return byte[] 目标数组
     */
    public static byte[] readBytes(int index, int length, byte[] source) {
        byte[] data = new byte[length];
        System.arraycopy(source, index, data, 0, length);
        return data;
    }

    /**
     * 读字节数组返回去掉前后空格的GBK字符串
     *
     * @param index  从字节数组第几位开始读取
     * @param length 读取长度
     * @param source 源数组
     * @return String 去掉前后空格的GBK字符串
     */
    public static String readGbkStr(int index, int length, byte[] source) {
        String str = null;
        try {
            str = new String(readBytes(index, length, source), "GBK").trim();
        } catch (UnsupportedEncodingException e) {
//            log.info(e.getMessage(), e);

        }
        return str;
    }

    /**
     * 读字节数组返回字符串
     *
     * @param index  从字节数组第几位开始读取
     * @param length 读取长度
     * @param source 源数组
     * @return String 字符串
     */
    public static String readStr(int index, int length, byte[] source) {
        return new String(readBytes(index, length, source));
    }

    /**
     * 读取short
     *
     * @param index  从字节数组第几位开始读取
     * @param source 源数组
     * @return short
     */
    public static short readShort(int index, byte[] source) {
        return bytesToShort(readBytes(index, 2, source));
    }

    /**
     * 读取无符号short
     *
     * @param index  从字节数组第几位开始读取
     * @param source 源数组
     * @return short
     */
    public static int readUShort(int index, byte[] source) {
        return bytesToShort(readBytes(index, 2, source)) & 0xFFFF;
    }

    /**
     * 读取int
     *
     * @param index  从字节数组第几位开始读取
     * @param source 源数组
     * @return int
     */
    public static int readInt(int index, byte[] source) {
        return bytesToInt(readBytes(index, 4, source));
    }

    /**
     * @param :byteIndex 字节数字引索
     * @param :bytes     字节数组
     * @return
     * @throws Exception
     * @Description:获取某一位的值
     */
    public static byte readByte(int byteIndex, byte[] bytes) {
        return bytes[byteIndex];
    }
}
