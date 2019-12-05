package protocol;

import entity.Header;
import entity.Message;
import entity.body.Body;
import utils.ByteUtil;

/**
 * @ClassName MessageHandle
 * @Description
 * @Date 2019/11/26
 * @Created by lizhanxu
 */
public class MessageHandle {
    private byte[] packHeader(Header header) {
        byte[] data = new byte[header.getMsgLength()];
        byte[] msgLength = ByteUtil.intToBytes(header.getMsgLength());
        System.arraycopy(msgLength,0,data,0,msgLength.length);
        byte[] msgSn = ByteUtil.intToBytes(header.getMsgSn());
        System.arraycopy(msgSn,0,data,4,msgSn.length);
        byte[] crcCode = ByteUtil.intToBytes(header.getCrcCode());
        System.arraycopy(crcCode, 0, data, 8, crcCode.length);
        byte[] type = new byte[]{header.getType()};
        System.arraycopy(type,0,data,12,type.length);
        byte[] time = header.getTime();
        System.arraycopy(time, 0, data, 13, time.length);
        return data;
    }

    private Header unpackHeader(byte[] data) {
        Header header = new Header();
        int msgLength = ByteUtil.readInt(0, data);
        header.setMsgLength(msgLength);
        int msgSn = ByteUtil.readInt(4,data);
        header.setMsgSn(msgSn);
        int crcCode = ByteUtil.readInt(8,data);
        header.setCrcCode(crcCode);
        byte type = ByteUtil.readByte(12,data);
        header.setType(type);
        byte[] time = ByteUtil.readBytes(13, 22, data);
        header.setTime(time);
        return header;
    }

    private <T extends Body> Message<T> unpackMessage(T body) {
        return null;
    }
}
