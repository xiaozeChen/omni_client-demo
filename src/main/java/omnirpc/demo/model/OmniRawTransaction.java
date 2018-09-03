package omnirpc.demo.model;

import java.util.List;

/**
 * @author chenxz
 * @date 2018/9/3
 */
public class OmniRawTransaction {
    /**
     * txid : df10f9ca50897a43f2cdd1c54695176786aa0ea7050a31262d74a10077502037
     * hash : df10f9ca50897a43f2cdd1c54695176786aa0ea7050a31262d74a10077502037
     * size : 150
     * vsize : 150
     * version : 1
     * locktime : 0
     * vin : [{"txid":"e2fa18cd1c3a9eeb02de91bdc9bd34dd0c9a3fe9838400dbea7ea60eaa6cc62f","vout":8,"scriptSig":{"asm":"","hex":""},"sequence":4294967295}]
     * vout : [{"value":0.29947194,"n":0,"scriptPubKey":{"asm":"OP_DUP OP_HASH160 4e5e12b2553f56a7c39fee1278393b164e941e3e OP_EQUALVERIFY OP_CHECKSIG","hex":"76a9144e5e12b2553f56a7c39fee1278393b164e941e3e88ac","reqSigs":1,"type":"pubkeyhash","addresses":["189NQGm8fLTNmuBuY3pKpmJzx883fFwcJG"]}},{"value":0,"n":1,"scriptPubKey":{"asm":"OP_RETURN 6f6d6e69000000000000001f0000000005f5e100","hex":"6a146f6d6e69000000000000001f0000000005f5e100","type":"nulldata"}},{"value":5.46E-6,"n":2,"scriptPubKey":{"asm":"OP_DUP OP_HASH160 36c199f4fe8e70d422e426785b441d2d29ccb1c6 OP_EQUALVERIFY OP_CHECKSIG","hex":"76a91436c199f4fe8e70d422e426785b441d2d29ccb1c688ac","reqSigs":1,"type":"pubkeyhash","addresses":["15zXR28oWzpy3fuFxJQGNBK1FhfUa4tFAh"]}}]
     */

    private String txid;
    private String hash;
    private int size;
    private int vsize;
    private int version;
    private int locktime;
    private List<Vin> vin;
    private List<Vout> vout;

    public String getTxid() {
        return txid;
    }

    public void setTxid(String txid) {
        this.txid = txid;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getVsize() {
        return vsize;
    }

    public void setVsize(int vsize) {
        this.vsize = vsize;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getLocktime() {
        return locktime;
    }

    public void setLocktime(int locktime) {
        this.locktime = locktime;
    }

    public List<Vin> getVin() {
        return vin;
    }

    public void setVin(List<Vin> vin) {
        this.vin = vin;
    }

    public List<Vout> getVout() {
        return vout;
    }

    public void setVout(List<Vout> vout) {
        this.vout = vout;
    }

    @Override
    public String toString() {
        return "OmniRawTransaction{" +
                "txid='" + txid + '\'' +
                ", hash='" + hash + '\'' +
                ", size=" + size +
                ", vsize=" + vsize +
                ", version=" + version +
                ", locktime=" + locktime +
                ", vin=" + vin +
                ", vout=" + vout +
                '}';
    }

    public static class Vin {
        /**
         * txid : e2fa18cd1c3a9eeb02de91bdc9bd34dd0c9a3fe9838400dbea7ea60eaa6cc62f
         * vout : 8
         * scriptSig : {"asm":"","hex":""}
         * sequence : 4294967295
         */

        private String txid;
        private int vout;
        private ScriptSig scriptSig;
        private long sequence;

        public String getTxid() {
            return txid;
        }

        public void setTxid(String txid) {
            this.txid = txid;
        }

        public int getVout() {
            return vout;
        }

        public void setVout(int vout) {
            this.vout = vout;
        }

        public ScriptSig getScriptSig() {
            return scriptSig;
        }

        public void setScriptSig(ScriptSig scriptSig) {
            this.scriptSig = scriptSig;
        }

        public long getSequence() {
            return sequence;
        }

        public void setSequence(long sequence) {
            this.sequence = sequence;
        }

        public static class ScriptSig {
            /**
             * asm :
             * hex :
             */

            private String asm;
            private String hex;

            public String getAsm() {
                return asm;
            }

            public void setAsm(String asm) {
                this.asm = asm;
            }

            public String getHex() {
                return hex;
            }

            public void setHex(String hex) {
                this.hex = hex;
            }
        }
    }

    public static class Vout {
        /**
         * value : 0.29947194
         * n : 0
         * scriptPubKey : {"asm":"OP_DUP OP_HASH160 4e5e12b2553f56a7c39fee1278393b164e941e3e OP_EQUALVERIFY OP_CHECKSIG","hex":"76a9144e5e12b2553f56a7c39fee1278393b164e941e3e88ac","reqSigs":1,"type":"pubkeyhash","addresses":["189NQGm8fLTNmuBuY3pKpmJzx883fFwcJG"]}
         */

        private double value;
        private int n;
        private ScriptPubKey scriptPubKey;

        public double getValue() {
            return value;
        }

        public void setValue(double value) {
            this.value = value;
        }

        public int getN() {
            return n;
        }

        public void setN(int n) {
            this.n = n;
        }

        public ScriptPubKey getScriptPubKey() {
            return scriptPubKey;
        }

        public void setScriptPubKey(ScriptPubKey scriptPubKey) {
            this.scriptPubKey = scriptPubKey;
        }

        public static class ScriptPubKey {
            /**
             * asm : OP_DUP OP_HASH160 4e5e12b2553f56a7c39fee1278393b164e941e3e OP_EQUALVERIFY OP_CHECKSIG
             * hex : 76a9144e5e12b2553f56a7c39fee1278393b164e941e3e88ac
             * reqSigs : 1
             * type : pubkeyhash
             * addresses : ["189NQGm8fLTNmuBuY3pKpmJzx883fFwcJG"]
             */

            private String asm;
            private String hex;
            private int reqSigs;
            private String type;
            private List<String> addresses;

            public String getAsm() {
                return asm;
            }

            public void setAsm(String asm) {
                this.asm = asm;
            }

            public String getHex() {
                return hex;
            }

            public void setHex(String hex) {
                this.hex = hex;
            }

            public int getReqSigs() {
                return reqSigs;
            }

            public void setReqSigs(int reqSigs) {
                this.reqSigs = reqSigs;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public List<String> getAddresses() {
                return addresses;
            }

            public void setAddresses(List<String> addresses) {
                this.addresses = addresses;
            }
        }
    }
}
