package custom.entity;

import custom.entity.body.Body;
import lombok.Data;

/**
 * @ClassName Message
 * @Description
 * @Date 2019/11/26
 * @Created by lizhanxu
 */
@Data
public class Message<T extends Body> {
    private Header header;
    private T body;

    public Message(Header header, T body) {
        this.header = header;
        this.body = body;
    }
}
