package dev.hilligans.ourcraft.util;

import dev.hilligans.ourcraft.data.other.BlockPos;

public class Ray {

    double x;
    double y;
    double z;

    public Ray(double pitch, double yaw, float stepSize) {
        x = Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)) * stepSize;
        y = Math.sin(Math.toRadians(pitch)) * stepSize;
        z = Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)) * stepSize;
    }

    public Ray negate() {
        x *= -1;
        y *= -1;
        z *= -1;
        return this;
    }

    public BlockPos getNextBlock(int step) {
        return new BlockPos((int)(x * step),(int)(y * step),(int)(z * step));
    }

}
