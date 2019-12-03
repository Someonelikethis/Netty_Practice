package entity;

import lombok.Data;

/**
 * @ClassName Header
 * @Description
 * @Date 2019/11/26
 * @Created by lizhanxu
 */
@Data
public class Header {
    //消息总长
    private int msgLength;
    //报文序列号
    private int msgSn;
    //crc校验码
    private int crcCode = 0xabef0101;
    //消息类型
    private byte type;
    //时间   22字节
    private byte[] time;
}
