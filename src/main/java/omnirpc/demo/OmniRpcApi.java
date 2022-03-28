package omnirpc.demo;

import omnirpc.demo.model.*;
import retrofit2.http.*;

import java.util.concurrent.CompletableFuture;

/**
 * @author chenxz
 * @date 2018/8/28
 */
public interface OmniRpcApi {

    /**
     * 获取btc余额
     *
     * @param address
     * @return
     */
    @GET("/omni/get_btc_balance")
    CompletableFuture<OmniResponse<CurrencyBean>> getBtcBalance(@Query("address") String address);

    /**
     * 获取omni指定币种的余额，usdt id为31，名称为TetherUS
     *
     * @param address
     * @param currencyId
     * @return
     */
    @GET("/omni/get_balance")
    CompletableFuture<OmniResponse<CurrencyBean>> getBalance(@Query("address") String address,
                                                             @Query("currencyId") long currencyId);

    /**
     * 获取omni指定币种的余额，usdt id为31，名称为TetherUS
     *
     * @param txId
     * @param vOut
     * @return
     */
    @GET("/omni/get_tx_out")
    CompletableFuture<OmniResponse<TxOutPut>> getTxOut(@Query("txId") String txId, @Query("vOut") long vOut);

    /**
     * 添加需要监听的地址
     *
     * @param userCoinAddress
     * @return
     */
    @POST("/omni/add_address")
    CompletableFuture<OmniResponse<UserCoinAddress>> addAddress(@Body UserCoinAddress userCoinAddress);

    /**
     * 创建一笔未签名USDT交易
     *
     * @param unSignRtxBean
     * @return
     */
    @POST("/omni/create_usdt_transaction")
    CompletableFuture<OmniResponse<RawTransaction>> createUsdtTransaction(@Body RequestUnSignRtxBean unSignRtxBean);

    /**
     * 创建一笔未签名BTC交易
     *
     * @param unSignRtxBean
     * @return
     */
    @POST("/omni/create_btc_transaction")
    CompletableFuture<OmniResponse<RawTransaction>> createBtcTransaction(@Body RequestUnSignRtxBean unSignRtxBean);

    /**
     * 发送一笔签名交易
     *
     * @param signRtxBean
     * @return
     */
    @POST("/omni/push_transaction")
    CompletableFuture<OmniResponse<String>> pushTransaction(@Body PushSignRtxBean signRtxBean);


}
