package omnirpc.demo.model;

import lombok.Data;

@Data
public class PushSignRtxBean {
    private String rawTx;
    private String orTxId;

    public PushSignRtxBean() {
    }

    public PushSignRtxBean(String rawTx, String orTxId) {
        this.rawTx = rawTx;
        this.orTxId = orTxId;
    }
}
