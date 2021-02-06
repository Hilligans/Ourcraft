package Hilligans.Util;

import Hilligans.Data.Other.BlockPos;

public class Ray {

    double pitch;
    double yaw;

    public Ray(double pitch, double yaw) {
        this.pitch = pitch;
        this.yaw = yaw;
    }

    public BlockPos getNextBlock(int step) {
        float stepSize = 1.0f;

        int x = (int)(Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)) * step * stepSize);
        int y = (int)(Math.sin(Math.toRadians(pitch)) * step * stepSize);
        int z = (int)(Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)) * step * stepSize);
        return new BlockPos(x,y,z);
    }




}
