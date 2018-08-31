package omnirpc.demo.model;

import lombok.Data;

@Data
public class UserCoinAddress {
    private String userId;
    private String address;

    public UserCoinAddress() {
    }
}
