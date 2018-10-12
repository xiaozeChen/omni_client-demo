package omnirpc.demo;

public class UsdtUtil {
    public static final String BTC = "BTC";
    public static final long BTC_TO_SATOSHI = 100000000L;
    static final byte[] HEX_TABLE = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
    static final char[] HEX_CHAR_TABLE = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    public static String toHexString(byte[] data) {
        if ((data == null) || (data.length == 0)) {
            return null;
        }
        byte[] hex = new byte[data.length * 2];
        int index = 0;
        for (byte b : data) {
            int v = b & 0xFF;
            hex[(index++)] = ((byte) HEX_CHAR_TABLE[(v >>> 4)]);
            hex[(index++)] = ((byte) HEX_CHAR_TABLE[(v & 0xF)]);
        }
        return new String(hex);
    }

    public static byte[] decodeHex(final String value) {
        // if string length is odd then throw exception
        if (value.length() % 2 != 0) {
            throw new NumberFormatException("odd number of characters in hex string");
        }
        byte[] bytes = new byte[value.length() / 2];
        for (int i = 0; i < value.length(); i += 2) {
            bytes[i / 2] = (byte) Integer.parseInt(value.substring(i, i + 2), 16);
        }
        return bytes;
    }

}
