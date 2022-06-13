package com.example.db;

import java.util.UUID;

import static java.lang.System.arraycopy;

public class UUIDByteArrayConverter {

    public static UUID byteArrayToUUID(final byte[] value) {
        byte[] msb = new byte[8];
        byte[] lsb = new byte[8];

        arraycopy(value, 4, msb, 0, 4);
        arraycopy(value, 2, msb, 4, 2);
        arraycopy(value, 0, msb, 6, 2);
        arraycopy(value, 8, lsb, 0, 8);

        return new UUID(asLong(msb), asLong(lsb));
    }

    public static byte[] uuidToByteArray(final UUID uuid) {
        final byte[] out = new byte[16];
        final byte[] msbIn = fromLong(uuid.getMostSignificantBits());

        arraycopy(msbIn, 6, out, 0, 2);
        arraycopy(msbIn, 4, out, 2, 2);
        arraycopy(msbIn, 0, out, 4, 4);
        arraycopy(fromLong(uuid.getLeastSignificantBits()), 0, out, 8, 8);

        return out;
    }

    /**
     * Interpret the binary representation of a long.
     *
     * @param bytes The bytes to interpret.
     * @return The long
     */
    public static long asLong(byte[] bytes) {
        return asLong(bytes, 0);
    }

    /**
     * Interpret the binary representation of a long.
     *
     * @param bytes  The bytes to interpret.
     * @param srcPos starting position in the source array.
     * @return The long
     */
    public static long asLong(byte[] bytes, int srcPos) {
        if (bytes == null) {
            return 0;
        }
        final int size = srcPos + 8;
        if (bytes.length < size) {
            throw new IllegalArgumentException("Expecting 8 byte values to construct a long");
        }
        long value = 0;
        for (int i = srcPos; i < size; i++) {
            value = (value << 8) | (bytes[i] & 0xff);
        }
        return value;
    }

    /**
     * Interpret a long as its binary form
     *
     * @param longValue The long to interpret to binary
     * @return The binary
     */
    public static byte[] fromLong(long longValue) {
        byte[] bytes = new byte[8];
        fromLong(longValue, bytes, 0);
        return bytes;
    }

    /**
     * Interpret a long as its binary form
     *
     * @param longValue The long to interpret to binary
     * @param dest      the destination array.
     * @param destPos   starting position in the destination array.
     */
    public static void fromLong(long longValue, byte[] dest, int destPos) {

        dest[destPos] = (byte) (longValue >> 56);
        dest[destPos + 1] = (byte) ((longValue << 8) >> 56);
        dest[destPos + 2] = (byte) ((longValue << 16) >> 56);
        dest[destPos + 3] = (byte) ((longValue << 24) >> 56);
        dest[destPos + 4] = (byte) ((longValue << 32) >> 56);
        dest[destPos + 5] = (byte) ((longValue << 40) >> 56);
        dest[destPos + 6] = (byte) ((longValue << 48) >> 56);
        dest[destPos + 7] = (byte) ((longValue << 56) >> 56);
    }
}
