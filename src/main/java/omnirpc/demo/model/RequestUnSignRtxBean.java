package omnirpc.demo.model;


public class RequestUnSignRtxBean {
    private String from;
    private String to;
    private long currencyId;

    public RequestUnSignRtxBean() {
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public long getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(long currencyId) {
        this.currencyId = currencyId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    private String amount;

    public RequestUnSignRtxBean(String from, String to, long currencyId, String amount) {
        this.from = from;
        this.to = to;
        this.currencyId = currencyId;
        this.amount = amount;
    }
}
