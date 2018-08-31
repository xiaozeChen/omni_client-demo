package omnirpc.demo;

import foundation.omni.tx.FeeCalculator;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.Transaction;

public class MyFeeCalculator implements FeeCalculator {
    private long fee;
    public MyFeeCalculator(long f) {
        fee = f;
    }

    @Override
    public Coin calculateFee(Transaction transaction) {
        return Coin.valueOf(fee);
    }
}
