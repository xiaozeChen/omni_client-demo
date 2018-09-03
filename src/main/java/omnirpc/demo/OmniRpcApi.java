package omnirpc.demo;

import foundation.omni.rpc.SmartPropertyListInfo;
import omnirpc.demo.model.*;
import org.bitcoinj.core.TransactionOutput;
import retrofit2.http.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author chenxz
 * @date 2018/8/28
 */
public interface OmniRpcApi {

    /**
     * 获取omni协议上的币种列表
     *
     * @return
     */
    @GET("/omni/getCurrencyList")
    CompletableFuture<OmniResponse<List<SmartPropertyListInfo>>> getChargeHistory();

    /**
     * 获取btc余额
     *
     * @param address
     * @return
     */
    @GET("/omni/getBtcBalance")
    CompletableFuture<OmniResponse<CurrencyBean>> getBtcBalance(@Query("address") String address);

    /**
     * 获取omni指定币种的余额，usdt id为31，名称为TetherUS
     *
     * @param address
     * @param currencyId
     * @return
     */
    @GET("/omni/getBalance")
    CompletableFuture<OmniResponse<CurrencyBean>> getBalance(@Query("address") String address,
                                                             @Query("currencyId") long currencyId);
     /**
     * 获取omni指定币种的余额，usdt id为31，名称为TetherUS
     *
     * @param txId
      * @param vOut
      * @return
     */
    @GET("/omni/getTxOut")
    CompletableFuture<OmniResponse<TxOutPut>> getTxOut(@Query("txId") String txId, @Query("vOut") long vOut);
    /**
     * 添加需要监听的地址
     *
     * @param userCoinAddress
     * @return
     */
    @POST("/omni/addAddress")
    CompletableFuture<OmniResponse<UserCoinAddress>> addAddress(@Body UserCoinAddress userCoinAddress);

    /**
     * 创建一笔未签名交易
     *
     * @param unSignRtxBean
     * @return
     */
    @POST("/omni/createTransaction")
    CompletableFuture<OmniResponse<OmniRawTransaction>> createTransaction(@Body RequestCreateUnSignRtxBean unSignRtxBean);

    /**
     * 发送一笔签名交易
     *
     * @param signRtxBean
     * @return
     */
    @POST("/omni/pushTransaction")
    CompletableFuture<OmniResponse<String>> pushTransaction(@Body RequestPushSignRtxBean signRtxBean);


}
