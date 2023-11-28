package dev.hilligans.ourcraft.util;

public class MathUtil {

    public static long ceil(double d) {
        return (long) (Math.ceil(Math.abs(d)) * Math.signum(d));
    }

    public static long floor(double d) {
        return (long) (Math.floor(Math.abs(d)) * Math.signum(d));
    }
}
