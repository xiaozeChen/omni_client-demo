package omnirpc.demo;

import foundation.omni.CurrencyID;
import foundation.omni.rpc.SmartPropertyListInfo;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;
import omnirpc.demo.model.*;
import org.bitcoinj.core.Address;
import org.bitcoinj.core.*;
import org.bitcoinj.params.TestNet3Params;
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
//    public static final HttpUrl OMNI_RPC_URL = HttpUrl.parse("http://54.183.219.188:8080");
    public static final HttpUrl OMNI_RPC_URL = HttpUrl.parse("http://127.0.0.1:8080");
    private static final int CONNECT_TIMEOUT_MILLIS = 15 * 1000; // 15s
    private static final int READ_TIMEOUT_MILLIS = 120 * 1000; // 120s (long enough to load USDT rich list)
    private static final int SUCCEED_CODE = 200;//成功返回码
    private static OmniRpcClient omniRpcClient;
    private static Retrofit restAdapter;
    private static OmniRpcApi service;
    private static Context context;

    public static void main(String[] args) {
        OmniRpcClient omniRpcClient = OmniRpcClient.getInstance();
        context = new Context(new TestNet3Params());
//        创建地址
//        privateKey= cVEvEN7WnZLntXZcy5vwpYBEHdFjois3F6MhNSczyacEwVVWRBio
//        publicKey=   02c7095cc0000dbe686fd33e672371e9e91362d5d515d4f95c09eb54604a798901
//        Base58=     n3JdaMLzENzbdrPFj82TCN93RuLZhfsFpN

//        privateKey=cPBsqYbo4TEU4rirU2G71M7GpRGMtzVv6ae7uaadBvugutRG9mV3
//        publicKey=036027e1df89aa706f4ec9e99e9b3bc0e7c1bf6e299b86c63fe7c7a31b7e6991d7
//        Base58=mrc3dG4L3nApvJ9R3DXd8Usijwu9rUhb3S

//        privateKey=cSzWNyqJftQHkpdwkqMUfPNR8ZgBUEMbaMEzd8TA5wU4Y7yW2vLv
//        publicKey=02a34f7d7efd3acc1480984321d94bffb88d94cc847cc3ce90b33e35be7bc4bd87
//        Base58=mxGC1ouHnf2sMkdaKRt85p7ZVawdYcjuXR

//        moneyqMan7uh8FqdCA2BV5yZ8qVrc9ikLP

//
//        ECKey ecKey = new ECKey();
//        String privateKey = ecKey.getPrivateKeyAsWiF(context.getParams());
//        String publicKey = ecKey.getPublicKeyAsHex();
//        System.out.println("privateKey=" + privateKey);
//        System.out.println("publicKey=" + publicKey);
//        Address address = ecKey.toAddress(context.getParams());
//        System.out.println("Base58=" + address.toBase58());
        Scanner sc = new Scanner(System.in);
        for (; ; ) {
            System.out.println("请输入要查询的地址");
//            String from = sc.nextLine().trim();

            String from = "n3JdaMLzENzbdrPFj82TCN93RuLZhfsFpN";
            System.out.println("当前usdt余额为：" + omniRpcClient.getBalance(from, 31L).doubleValue());
            System.out.println("当前omni余额为：" + omniRpcClient.getBalance(from, 1).doubleValue());
            System.out.println("当前btc余额为:" + omniRpcClient.getBtcBalance(from).doubleValue());
            System.out.println("请输入要转入的地址");
            String to = sc.nextLine().trim();
            System.out.println("请输入转账金额");
            BigDecimal amount = new BigDecimal(sc.nextLine().trim());
            System.out.println("请输入转出地址私钥");
//            String pk = sc.nextLine().trim();

            String pk ="cVEvEN7WnZLntXZcy5vwpYBEHdFjois3F6MhNSczyacEwVVWRBio";
            ECKey fromKey = DumpedPrivateKey.fromBase58(context.getParams(), pk).getKey();
//            ECKey fromKey = ECKey.fromPrivate(Base58.decode(pk));
            System.out.println("请输入要转账类型BTC/OMNI");
            String type = sc.nextLine().trim();
            String rawTxStr;
            PushSignRtxBean transaction;
            if ("BTC".equalsIgnoreCase(type)) {
                transaction = omniRpcClient.createBtcTransaction(fromKey, from, to, amount);
            } else if ("OMNI".equalsIgnoreCase(type)) {
                transaction = omniRpcClient.createUsdtTransaction(fromKey, from, to, CurrencyID.of(1), amount);
            } else {
                System.out.println("别这样让我无所适从。。。。");
                break;
            }
            rawTxStr = transaction.getRawTx();
            System.out.println("rawTxStr:" + rawTxStr);
            System.out.println("是否发送交易y/n)");
            String yn = sc.nextLine().trim();
            if ((!yn.equalsIgnoreCase("yes")) && (!yn.equalsIgnoreCase("y"))) {
                break;
            } else {
                String txId = omniRpcClient.pushTransaction(transaction);
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
     * @param signRtxBean 交易数据
     * @return 交易id
     */
    public String pushTransaction(PushSignRtxBean signRtxBean) {
        String txId = null;
        try {
            //发送一笔签名交易
            OmniResponse<String> response = service
                    .pushTransaction(signRtxBean)
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
    public PushSignRtxBean createUsdtTransaction(ECKey fromKey, String from, String to, CurrencyID currencyID, BigDecimal amount) {
        try {
            System.out.println("创建一笔OMNI交易");
            //从rpc服务器获取未签名的交易
            OmniResponse<RawTransaction> response = service.createUsdtTransaction(new RequestUnSignRtxBean(from,
                    to, currencyID.getValue(), amount.toPlainString())).get();
            if (response.getCode() == SUCCEED_CODE) {
                RawTransaction rawTransaction = response.getData();
                String oTxId = response.getMsg();
                System.out.println("签名前：" + rawTransaction.toString());
                String txHash = signTransactionByKey(fromKey, rawTransaction);
                System.out.println("\n\n哈希值：" + txHash);
                return new PushSignRtxBean(txHash, oTxId);
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
    public PushSignRtxBean createBtcTransaction(ECKey fromKey, String from, String to, BigDecimal amount) {
        try {
            System.out.println("创建一笔BTC交易");
            //从rpc服务器获取未签名的交易
            OmniResponse<RawTransaction> response = service.createBtcTransaction(new RequestUnSignRtxBean(from,
                    to, 0, amount.toPlainString())).get();
            if (response.getCode() == SUCCEED_CODE) {
                RawTransaction rawTransaction = response.getData();
                //原始交易id
                String oTxId = response.getMsg();
                System.out.println("签名前：" + rawTransaction.toString());
                //签名交易
                String txHash = signTransactionByKey(fromKey, rawTransaction);
                System.out.println("\n哈希值：" + txHash);
                return new PushSignRtxBean(txHash, oTxId);
            } else {
                log.info(response.getMsg());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String signTransactionByKey(ECKey fromKey, RawTransaction rawTransaction) throws Exception {
        Transaction usdt_tx = new Transaction(context.getParams());
        //签名交易
        List<RawTransaction.Vin> vin = rawTransaction.getVin();
        List<RawTransaction.Vout> vout = rawTransaction.getVout();
        for (RawTransaction.Vout out : vout) {
            if (out.getScriptPubKey().getAddresses() == null) {
                byte[] pubkeyBytes = Utils.HEX.decode(out.getScriptPubKey().getHex());
                Script script = new Script(pubkeyBytes);
                usdt_tx.addOutput(Coin.valueOf(new Double(out.getValue() * 1.0E8D).longValue()), script);
            } else {
                usdt_tx.addOutput(Coin.valueOf(new Double(out.getValue() * 1.0E8D).longValue()),
                        Address.fromBase58(context.getParams(), out.getScriptPubKey().getAddresses().get(0)));
            }
        }
        for (RawTransaction.Vin in : vin) {
            TxOutPut txOut = getTxOut(in.getTxid(), (long) in.getVout());
            byte[] pubkeyBytes = Utils.HEX.decode(txOut.getScriptPubKey().getHex());
            Script script = new Script(pubkeyBytes);
            TransactionOutPoint outPoint = new TransactionOutPoint(context.getParams(), in.getVout(), Sha256Hash.wrap(in.getTxid()));
            usdt_tx.addSignedInput(outPoint, script, fromKey, Transaction.SigHash.ALL, true);
        }
        usdt_tx.getConfidence().setSource(TransactionConfidence.Source.NETWORK);
        usdt_tx.setPurpose(Transaction.Purpose.USER_PAYMENT);
        return UsdtUtil.toHexString(usdt_tx.bitcoinSerialize());
    }

}
