package omnirpc.demo.model;

import lombok.Data;

/**
 * @author chenxz
 * @date 2018/8/27
 */
@Data
public class OmniResponse<T> {
    private T data;
    private String msg;
    private int code;

    public OmniResponse() {
    }

    public OmniResponse(int code, String msg, T data) {
        this.data = data;
        this.msg = msg;
        this.code = code;
    }

}
