package dev.hilligans.engine.network.debug;

import dev.hilligans.engine.data.Tuple;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class PacketTrace {

    public static final AtomicLong PACKET_IDS = new AtomicLong();
    public static final ConcurrentHashMap<Long, PacketTrace> PACKET_TRACE = new ConcurrentHashMap<>();



    public Queue<Tuple<ByteArrayDataType, Object>> data = new ArrayDeque<>();
    public int totalLength;

    public<T> T put(ByteArrayDataType dataType, T t) {
        data.add(new Tuple<>(dataType, t));
        return t;
    }

    public byte put(byte val) {
        data.add(new Tuple<>(ByteArrayDataType.BYTE, val));
        return val;
    }

    public boolean put(boolean val) {
        data.add(new Tuple<>(ByteArrayDataType.BOOLEAN, val));
        return val;
    }

    public short put(short val) {
        data.add(new Tuple<>(ByteArrayDataType.SHORT, val));
        return val;
    }

    public int put(int val) {
        data.add(new Tuple<>(ByteArrayDataType.INT, val));
        return val;
    }

    public long put(long val) {
        data.add(new Tuple<>(ByteArrayDataType.LONG, val));
        return val;
    }

    public float put(float val) {
        data.add(new Tuple<>(ByteArrayDataType.FLOAT, val));
        return val;
    }

    public double put(double val) {
        data.add(new Tuple<>(ByteArrayDataType.DOUBLE, val));
        return val;
    }

    public byte get(byte val) {
        check(val, "byte", ByteArrayDataType.BYTE);
        return val;
    }

    public boolean get(boolean val) {
        check(val, "boolean", ByteArrayDataType.BOOLEAN);
        return val;
    }

    public short get(short val) {
        check(val, "short", ByteArrayDataType.SHORT);
        return val;
    }

    public int get(int val) {
        check(val, "int", ByteArrayDataType.INT);
        return val;
    }

    public long get(long val) {
        check(val, "long", ByteArrayDataType.LONG);
        return val;
    }

    public float get(float val) {
        check(val, "float", ByteArrayDataType.FLOAT);
        return val;
    }

    public double get(double val) {
        check(val, "double", ByteArrayDataType.DOUBLE);
        return val;
    }

    public void check(Object val, String type, ByteArrayDataType dataType) {
        Tuple<ByteArrayDataType, Object> tuple = data.remove();
        if(tuple.typeA != dataType) {

            throw new RuntimeException("Failed to decode packet, expected a " + type + " but got " + tuple.typeA + "\nremaining data:\n" + data.toString());
        }
        if(!tuple.getTypeB().equals(val)) {
            throw new RuntimeException("Failed to decode packet, expected a value of " + tuple.typeB + " but got " + val + "\nremaining data:\n" + data.toString());
        }
    }
}
