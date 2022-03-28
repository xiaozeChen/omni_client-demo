package omnirpc.demo.model;


/**
 * @author chenxz
 * @date 2018/8/27
 */
public class OmniResponse<T> {
    private T data;
    private String msg;
    private int code;

    public OmniResponse() {
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public OmniResponse(int code, String msg, T data) {
        this.data = data;
        this.msg = msg;
        this.code = code;
    }

}
