package dev.hilligans.ourcraft.util;

import dev.hilligans.ourcraft.data.other.BlockPos;

public class CurvedRay {

    float fac1,fac2,fac3,fac4,fac5,fac6;

    public CurvedRay(float fac1, float fac2, float fac3, float fac4, float fac5, float fac6) {
        this.fac1 = fac1;
        this.fac2 = fac2;
        this.fac3 = fac3;
        this.fac4 = fac4;
        this.fac5 = fac5;
        this.fac6 = fac6;
    }

    public CurvedRay() {
        this(2,1.1f,30,1,1.5f,55);
    }

    public BlockPos eval(int step) {
        double y1 = getY1(step/20f);
        double y2 = getY2(step/20f);


        if(y1 < y2) {
            if (step % 2 == 0) {
                return new BlockPos(step/20f,y1,0);
            } else {
                return new BlockPos(step/20f,y2,0);
            }
        }
        return null;
    }

    public double getY1(float step) {
        return -Math.log10(step / fac1)/Math.log10(fac2) + fac3;
    }

    public double getY2(float step) {
        return Math.log10(step / fac4)/Math.log10(fac5) + fac6;
    }




}
