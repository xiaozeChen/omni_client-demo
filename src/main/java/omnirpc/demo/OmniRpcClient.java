package omnirpc.demo;

import foundation.omni.CurrencyID;
import foundation.omni.rpc.SmartPropertyListInfo;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;
import omnirpc.demo.model.*;
import org.bitcoinj.core.Address;
import org.bitcoinj.core.*;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.script.Script;
import retrofit2.Retrofit;
import retrofit2.adapter.java8.Java8CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author chenxz
 * @date 2018/8/28
 */
@Slf4j
public class OmniRpcClient {
    public static final HttpUrl OMNI_RPC_URL = HttpUrl.parse("http://192.168.31.50:8080");
    private static final int CONNECT_TIMEOUT_MILLIS = 15 * 1000; // 15s
    private static final int READ_TIMEOUT_MILLIS = 120 * 1000; // 120s (long enough to load USDT rich list)
    private static final int SUCCEED_CODE = 10000;//成功返回码
    private static OmniRpcClient omniRpcClient;
    private static MainNetParams netParams;
    private static Retrofit restAdapter;
    private static OmniRpcApi service;

    public static void main(String[] args) {
        OmniRpcClient omniRpcClient = OmniRpcClient.getInstance();
        new Context(netParams);
        Scanner sc = new Scanner(System.in);
        for (; ; ) {
            System.out.println("请输入要查询的地址");
            String from = sc.nextLine().trim();
            System.out.println("当前usdt余额为：" + omniRpcClient.getBalance(from, 31L).doubleValue());
            System.out.println("当前omni余额为：" + omniRpcClient.getBalance(from, 1).doubleValue());
            System.out.println("当前btc余额为:" + omniRpcClient.getBtcBalance(from).doubleValue());
            System.out.println("请输入要转入的地址");
            String to = sc.nextLine().trim();
            System.out.println("请输入转账金额");
            BigDecimal amount = new BigDecimal(sc.nextLine().trim());
            System.out.println("请输入转出地址私钥");
            String pk = sc.nextLine().trim();
            ECKey fromKey = DumpedPrivateKey.fromBase58(netParams, pk).getKey();
            System.out.println("请输入要转账类型BTC/OMNI");
            String type = sc.nextLine().trim();
            String rawTxStr;
            if ("BTC".equalsIgnoreCase(type)) {
                rawTxStr = omniRpcClient.createBtcTransaction(fromKey, from, to, amount);
            } else if ("USDT".equalsIgnoreCase(type)) {
                rawTxStr = omniRpcClient.createUsdtTransaction(fromKey, from, to, CurrencyID.of(31), amount);
            } else {
                System.out.println("别这样让我无所适从。。。。");
                break;
            }
            System.out.println("rawTxStr:" + rawTxStr);
            System.out.println("是否发送交易y/n)");
            String yn = sc.nextLine().trim();
            if ((!yn.equalsIgnoreCase("yes")) && (!yn.equalsIgnoreCase("y"))) {
                break;
            } else {
                String txId = omniRpcClient.pushTransaction(rawTxStr);
                System.out.println("txId=" + txId);
            }
            break;
        }
    }

    public static OmniRpcClient getInstance() {
        if (omniRpcClient == null) {
            omniRpcClient = new OmniRpcClient(OMNI_RPC_URL, true);
        }
        return omniRpcClient;
    }

    private OmniRpcClient(HttpUrl baseURL, boolean debug) {
        netParams = MainNetParams.get();
        OkHttpClient client = initClient(debug);
        restAdapter = new Retrofit.Builder()
                .client(client)
                .baseUrl(baseURL)
                .addConverterFactory(JacksonConverterFactory.create())
                .addCallAdapterFactory(Java8CallAdapterFactory.create())
                .build();
        service = restAdapter.create(OmniRpcApi.class);
    }

    private OkHttpClient initClient(boolean debug) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
                .readTimeout(READ_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
                .addInterceptor(new OmniInterceptor());
        if (debug) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(interceptor);
        }
        return builder.build();
    }

    public static class OmniInterceptor implements Interceptor {

        public OmniInterceptor() {
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request().newBuilder()
                    .addHeader("Content-Type", "application/json")
                    .build();
            return chain.proceed(request);
        }
    }

    /**
     * 获取币种列表
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public void getChargeHistory() throws ExecutionException, InterruptedException {
        try {
            OmniResponse<List<SmartPropertyListInfo>> response = service.getChargeHistory().get();
            if (response.getCode() == SUCCEED_CODE) {
                List<SmartPropertyListInfo> data = response.getData();
            } else {
                log.info(response.getMsg());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 获取btc余额
     *
     * @param address
     */
    public BigDecimal getBtcBalance(String address) {
        try {
            OmniResponse<CurrencyBean> response = service.getBtcBalance(address).get();
            if (response.getCode() == SUCCEED_CODE) {
                CurrencyBean data = response.getData();
                log.info("address=" + address + "  name=" + data.getName() + "  amount=" + data.getAmount());
                return data.getAmount();
            } else {
                log.info(response.getMsg());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return BigDecimal.ZERO;
    }

    /**
     * 获取指定币种余额
     *
     * @param address
     * @param currencyId
     */
    public BigDecimal getBalance(String address, long currencyId) {
        try {
            OmniResponse<CurrencyBean> response = service.getBalance(address, currencyId).get();
            if (response.getCode() == SUCCEED_CODE) {
                CurrencyBean data = response.getData();
                log.info("address=" + address + "  name=" + data.getName() + "  amount=" + data.getAmount());
                return data.getAmount();
            } else {
                log.info(response.getMsg());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return BigDecimal.ZERO;
    }

    /**
     * 添加监听地址
     *
     * @param userCoinAddress
     */
    public void addAddress(UserCoinAddress userCoinAddress) {
        try {
            OmniResponse<UserCoinAddress> response = service.addAddress(userCoinAddress).get();
            if (response.getCode() == SUCCEED_CODE) {
                UserCoinAddress data = response.getData();
                log.info("address=" + data.getAddress() + "  id=" + data.getUserId());
            } else {
                log.info(response.getMsg());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取交易输出
     *
     * @param txId
     * @param vOut
     */
    public TxOutPut getTxOut(String txId, long vOut) {
        TxOutPut data = null;
        try {
            OmniResponse<TxOutPut> response = service.getTxOut(txId, vOut).get();
            if (response.getCode() == SUCCEED_CODE) {
                data = response.getData();
                log.info("data=" + data.toString());
            } else {
                log.info(response.getMsg());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * 创建一笔交易-->签名-->获取hex值 后发送
     *
     * @param rawTransaction 交易哈希
     * @return 交易id
     */
    public String pushTransaction(String rawTransaction) {
        String txId = null;
        try {
            //发送一笔签名交易
            OmniResponse<String> response = service
                    .pushTransaction(new RequestPushSignRtxBean(rawTransaction))
                    .get();
            if (response.getCode() == SUCCEED_CODE) {
                txId = response.getData();
                log.info("txId=" + txId);
            }
        } catch (Exception e) {
            log.info(e.toString());
        }
        return txId;
    }


    /**
     * 创建一笔USDT交易-->签名-->获取hex值 后发送
     *
     * @param fromKey
     * @param from
     * @param to
     * @param currencyID
     * @param amount
     * @return 交易哈希
     * @throws Exception
     */
    public String createUsdtTransaction(ECKey fromKey, String from, String to, CurrencyID currencyID, BigDecimal amount) {
        try {
            //从rpc服务器获取未签名的交易
            OmniResponse<RawTransaction> response = service.createUsdtTransaction(new RequestCreateUnSignRtxBean(from,
                    to, currencyID.getValue(), amount.toPlainString())).get();
            if (response.getCode() == SUCCEED_CODE) {
                RawTransaction rawTransaction = response.getData();
                System.out.println("签名前：" + rawTransaction.toString());
                String txHash = signTransactionByKey(fromKey, rawTransaction);
                System.out.println("\n\n哈希值：" + txHash);
                return txHash;
            } else {
                log.info(response.getMsg());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 创建一笔BTC交易-->签名-->获取hex值 后发送
     *
     * @param fromKey
     * @param from
     * @param to
     * @param amount
     * @return 交易哈希
     * @throws Exception
     */
    public String createBtcTransaction(ECKey fromKey, String from, String to, BigDecimal amount) {
        try {
            //从rpc服务器获取未签名的交易
            OmniResponse<RawTransaction> response = service.createBtcTransaction(new RequestCreateUnSignRtxBean(from,
                    to, 0, amount.toPlainString())).get();
            if (response.getCode() == SUCCEED_CODE) {
                RawTransaction rawTransaction = response.getData();
                System.out.println("签名前：" + rawTransaction.toString());
                String txHash = signTransactionByKey(fromKey, rawTransaction);
                System.out.println("\n哈希值：" + txHash);
                return txHash;
            } else {
                log.info(response.getMsg());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String signTransactionByKey(ECKey fromKey, RawTransaction rawTransaction) throws Exception {
        Transaction usdt_tx = new Transaction(netParams);
        // 签名交易
        List<RawTransaction.Vin> vin = rawTransaction.getVin();
        List<RawTransaction.Vout> vout = rawTransaction.getVout();
        for (RawTransaction.Vout out : vout) {
            if (out.getScriptPubKey().getAddresses() == null) {
                byte[] pubkeyBytes = Utils.HEX.decode(out.getScriptPubKey().getHex());
                Script script = new Script(pubkeyBytes);
                usdt_tx.addOutput(Coin.valueOf(new Double(out.getValue() * 1.0E8D).longValue()), script);
            } else {
                usdt_tx.addOutput(Coin.valueOf(new Double(out.getValue() * 1.0E8D).longValue()),
                        Address.fromBase58(netParams, out.getScriptPubKey().getAddresses().get(0)));
            }
        }
        for (RawTransaction.Vin in : vin) {
            TxOutPut txOut = getTxOut(in.getTxid(), (long) in.getVout());
            byte[] pubkeyBytes = Utils.HEX.decode(txOut.getScriptPubKey().getHex());
            Script script = new Script(pubkeyBytes);
            TransactionOutPoint outPoint = new TransactionOutPoint(netParams, in.getVout(), Sha256Hash.wrap(in.getTxid()));
            usdt_tx.addSignedInput(outPoint, script, fromKey, Transaction.SigHash.ALL, true);
        }
        usdt_tx.getConfidence().setSource(TransactionConfidence.Source.NETWORK);
        usdt_tx.setPurpose(Transaction.Purpose.USER_PAYMENT);
        return UsdtUtil.toHexString(usdt_tx.bitcoinSerialize());
    }

}
