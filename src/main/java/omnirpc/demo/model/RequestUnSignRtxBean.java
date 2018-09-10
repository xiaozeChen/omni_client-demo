package omnirpc.demo.model;

import lombok.Data;

@Data
public class RequestUnSignRtxBean {
    private String from;
    private String to;
    private long currencyId;

    public RequestUnSignRtxBean() {
    }

    private String amount;

    public RequestUnSignRtxBean(String from, String to, long currencyId, String amount) {
        this.from = from;
        this.to = to;
        this.currencyId = currencyId;
        this.amount = amount;
    }
}
