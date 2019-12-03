package entity.body;

import lombok.Data;

/**
 * @ClassName DT1001
 * @Description
 * @Date 2019/11/26
 * @Created by lizhanxu
 */
@Data
public class DT1001 implements Body {
    private int dataType;

    private byte flag;

    private String username;

    private byte[] password;
}
