package omnirpc.demo.model;

import lombok.Data;

@Data
public class RequestPushSignRtxBean {
    private String rawTx;

    public RequestPushSignRtxBean() {
    }

    public RequestPushSignRtxBean(String rawTx) {
        this.rawTx = rawTx;
    }
}
