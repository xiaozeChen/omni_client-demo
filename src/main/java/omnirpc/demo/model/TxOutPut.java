package omnirpc.demo.model;

import java.util.List;

/**
 * @author chenxz
 * @date 2018/9/3
 */
public class TxOutPut {

    /**
     * bestblock : hash
     * confirmations : 1
     * value : 1.2
     * scriptPubKey : {"asm":"code","hex":"hex","reqSigs":1,"type":"pubkeyhash","addresses":["bitcoinaddress"]}
     * version : 1
     * coinbase : true
     */

    private String bestblock;
    private int confirmations;
    private double value;
    private ScriptPubKey scriptPubKey;
    private int version;
    private boolean coinbase;

    public String getBestblock() {
        return bestblock;
    }

    public void setBestblock(String bestblock) {
        this.bestblock = bestblock;
    }

    public int getConfirmations() {
        return confirmations;
    }

    public void setConfirmations(int confirmations) {
        this.confirmations = confirmations;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public ScriptPubKey getScriptPubKey() {
        return scriptPubKey;
    }

    public void setScriptPubKey(ScriptPubKey scriptPubKey) {
        this.scriptPubKey = scriptPubKey;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public boolean isCoinbase() {
        return coinbase;
    }

    public void setCoinbase(boolean coinbase) {
        this.coinbase = coinbase;
    }

    @Override
    public String toString() {
        return "TxOutPut{" +
                "bestblock='" + bestblock + '\'' +
                ", confirmations=" + confirmations +
                ", value=" + value +
                ", scriptPubKey=" + scriptPubKey +
                ", version=" + version +
                ", coinbase=" + coinbase +
                '}';
    }

    public static class ScriptPubKey {
        /**
         * asm : code
         * hex : hex
         * reqSigs : 1
         * type : pubkeyhash
         * addresses : ["bitcoinaddress"]
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
