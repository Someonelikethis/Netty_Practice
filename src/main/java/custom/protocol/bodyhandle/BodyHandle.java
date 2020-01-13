package custom.protocol.bodyhandle;

import custom.entity.body.Body;

/**
 * @ClassName Handle
 * @Description
 * @Date 2019/11/26
 * @Created by lizhanxu
 */
public interface BodyHandle<B extends Body> {
    byte[] packBody(B body);

    B unpackBody(byte[] data);
}
