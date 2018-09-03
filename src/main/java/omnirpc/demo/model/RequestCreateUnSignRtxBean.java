package omnirpc.demo.model;

import lombok.Data;

@Data
public class RequestCreateUnSignRtxBean {
    private String from;
    private String to;
    private long currencyId;

    public RequestCreateUnSignRtxBean() {
    }

    private String amount;

    public RequestCreateUnSignRtxBean(String from, String to, long currencyId, String amount) {
        this.from = from;
        this.to = to;
        this.currencyId = currencyId;
        this.amount = amount;
    }
}
