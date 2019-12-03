package handle.bodyhandle;

import entity.body.DT2001;

/**
 * @ClassName DT2001_Handle
 * @Description
 * @Date 2019/11/26
 * @Created by lizhanxu
 */
public class DT2001_Handle implements BodyHandle<DT2001> {
    public byte[] packBody(DT2001 body) {
        return new byte[0];
    }

    public DT2001 unpackBody(byte[] data) {
        return null;
    }
}
