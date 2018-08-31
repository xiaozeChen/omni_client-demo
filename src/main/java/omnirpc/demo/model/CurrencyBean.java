package omnirpc.demo.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CurrencyBean {
    private long id;
    private String name;
    private BigDecimal amount;

    public CurrencyBean() {
    }

    public CurrencyBean(long id, String name, BigDecimal amount) {
        this.id = id;
        this.name = name;
        this.amount = amount;
    }
}
