package omnirpc.demo.model;

public class PushSignRtxBean {
    private String rawTx;
    private String orTxId;

    public PushSignRtxBean() {
    }

    public String getRawTx() {
        return rawTx;
    }

    public void setRawTx(String rawTx) {
        this.rawTx = rawTx;
    }

    public String getOrTxId() {
        return orTxId;
    }

    public void setOrTxId(String orTxId) {
        this.orTxId = orTxId;
    }

    public PushSignRtxBean(String rawTx, String orTxId) {
        this.rawTx = rawTx;
        this.orTxId = orTxId;
    }
}
