package entity;

/**
 * @ClassName Data
 * @Description
 * @Date 2019/11/26
 * @Created by lizhanxu
 */
@lombok.Data
public class Data {
    private byte[] bytes;

    public Data(byte[] bytes) {
        this.bytes = bytes;
    }
}
