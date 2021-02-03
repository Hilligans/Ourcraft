package Hilligans.Util;

public class Vector5f {

    float[] values;

    public Vector5f(float x, float y, float z, float a, float b) {
        values = new float[]{x,y,z,a,b};
    }

    public void addToList(float val[], int offset) {
        for(int x = 0; x < 5; x++) {
            val[x + offset] = values[x];
        }
    }

    public float[] get() {
        return values;
    }

    public Vector5f addX(float val) {
        values[0] += val;
        return this;
    }

    public Vector5f addY(float val) {
        values[1] += val;
        return this;
    }

    public Vector5f addZ(float val) {
        values[2] += val;
        return this;
    }

}
