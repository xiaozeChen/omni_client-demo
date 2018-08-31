package omnirpc.demo;

import wf.bitcoin.javabitcoindrpcclient.BitcoinJSONRPCClient;
import wf.bitcoin.javabitcoindrpcclient.GenericRpcException;

import java.io.Serializable;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.util.*;

import static omnirpc.demo.MapWrapper.mapInt;
import static omnirpc.demo.MapWrapper.mapStr;


public class BitcoinClientExtend extends BitcoinJSONRPCClient {
    public BitcoinClientExtend(String rpcURL)
            throws MalformedURLException {
        super(rpcURL);
    }

    protected Transaction wrap(Map m) {
        System.out.println(m.toString());
        return new BtcTransaction(m);
    }

    public class BtcTransaction extends MapWrapper implements Transaction {


        public BtcTransaction(Map m) {
            super(m);
        }

        @Override
        public String account() {
            return mapStr("account");
        }

        @Override
        public String address() {
            return mapStr("address");
        }

        @Override
        public String category() {
            return mapStr("category");
        }

        @Override
        public double amount() {
            return mapDouble("amount");
        }

        @Override
        public double fee() {
            return mapDouble("fee");
        }

        @Override
        public int confirmations() {
            return mapInt("confirmations");
        }

        @Override
        public String blockHash() {
            return mapStr("blockHash");
        }

        @Override
        public int blockIndex() {
            return mapInt("blockIndex");
        }

        @Override
        public Date blockTime() {
            return new Date(mapLong("blockTime"));
        }

        @Override
        public String txId() {
            return mapStr("txId");
        }

        @Override
        public Date time() {
            return new Date(mapLong("time"));
        }

        @Override
        public Date timeReceived() {
            return new Date(mapLong("timeReceived"));
        }

        @Override
        public String comment() {
            return mapStr("comment");
        }

        @Override
        public String commentTo() {
            return mapStr("commentTo");
        }

        @Override
        public RawTransaction raw() {
            try {
                return getRawTransaction(mapStr("txid"));
            } catch (GenericRpcException ex) {
                throw new RuntimeException(ex);
            }
        }
    }


    public static class ExtendTxOutput extends BasicTxOutput {
        public String script;
        public int n;

        public ExtendTxOutput(String address, double amount, int n, String script) {
            super(address, amount);
            this.script = script;
            this.n = n;
        }
    }

    public String signRawTransactionExtend(String hex, List<TxInput> inputs, List<String> privateKeys)
            throws GenericRpcException {
        return signRawTransactionExtend(hex, inputs, privateKeys, "ALL");
    }

    public String signRawTransactionExtend(String hex, List<TxInput> inputs, List<String> privateKeys, String sigHashType) {
        List<Map> pInputs = null;
        if (inputs != null) {
            pInputs = new ArrayList();
            for (TxInput txInput : inputs) {
                pInputs.add(new LinkedHashMap() {
                    {
                        put("txid", txInput.txid());
                        put("vout", txInput.vout());
                        put("scriptPubKey", txInput.scriptPubKey());

                    }
                });
            }
        }
        Map result = (Map) query("signrawtransaction", new Object[]{hex, pInputs, privateKeys, sigHashType});
        if (((Boolean) result.get("complete")).booleanValue()) {
            return (String) result.get("hex");
        }
        throw new GenericRpcException("Incomplete");
    }

    public static class TxOutWrapper extends MapWrapper implements TxOut, Serializable {

        public TxOutWrapper(Map m) {
            super(m);
        }

        @Override
        public String bestBlock() {
            return mapStr("bestblock");
        }

        @Override
        public long confirmations() {
            return mapLong("confirmations");
        }

        @Override
        public BigDecimal value() {
            return mapBigDecimal("value");
        }

        @Override
        public String asm() {
            return mapStr("asm");
        }

        @Override
        public String hex() {
            return mapStr("hex");
        }

        @Override
        public long reqSigs() {
            return mapLong("reqSigs");
        }

        @Override
        public String type() {
            return mapStr("type");
        }

        @Override
        public List<String> addresses() {
            return (List<String>) m.get("addresses");
        }

        @Override
        public long version() {
            return mapLong("version");
        }

        @Override
        public boolean coinBase() {
            return mapBool("coinbase");
        }
    }

    public List<Unspent> listUnspent(int minConf, int maxConf, Object addresses, Boolean include_unsafe, Object option)
            throws GenericRpcException {
        return new BitcoinClientExtend.UnspentListWrapper((List) query("listunspent", new Object[]{Integer.valueOf(minConf), Integer.valueOf(maxConf), addresses, include_unsafe, option}));
    }

    private class UnspentListWrapper extends ListMapWrapper<Unspent> {

        public UnspentListWrapper(List<Map> list) {
            super(list);
        }

        @Override
        protected Unspent wrap(final Map m) {
            return new Unspent() {

                @Override
                public String txid() {
                    return mapStr(m, "txid");
                }

                @Override
                public int vout() {
                    return mapInt(m, "vout");
                }

                @Override
                public String address() {
                    return mapStr(m, "address");
                }

                @Override
                public String scriptPubKey() {
                    return mapStr(m, "scriptPubKey");
                }

                @Override
                public String account() {
                    return mapStr(m, "account");
                }

                @Override
                public double amount() {
                    return MapWrapper.mapDouble(m, "amount");
                }

                @Override
                public int confirmations() {
                    return mapInt(m, "confirmations");
                }

            };
        }
    }

}
