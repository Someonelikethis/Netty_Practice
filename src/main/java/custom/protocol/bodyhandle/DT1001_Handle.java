package custom.protocol.bodyhandle;

import custom.entity.body.DT1001;

/**
 * @ClassName DT1001_Handle
 * @Description
 * @Date 2019/11/26
 * @Created by lizhanxu
 */
public class DT1001_Handle implements BodyHandle<DT1001> {
    public byte[] packBody(DT1001 body) {
        return new byte[0];
    }

    public DT1001 unpackBody(byte[] data) {
        return null;
    }
}
