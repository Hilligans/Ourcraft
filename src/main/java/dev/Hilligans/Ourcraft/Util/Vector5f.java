package dev.Hilligans.Ourcraft.Util;

import org.joml.Vector4f;

import java.util.Arrays;

public class Vector5f {

    public float[] values;

    public Vector5f(float x, float y, float z, float a, float b) {
        values = new float[]{x,y,z,a,b};
    }

    public void addToList(float[] val, int offset) {
        System.arraycopy(values, 0, val, offset, values.length);
    }

    public Vector5f setColored() {
        if(values.length == 5) {
            values = new float[]{values[0], values[1], values[2], 1.0f, 1.0f, 1.0f, 1.0f, values[3], values[4]};
        }
        return this;
    }

    public Vector5f setColored(float r, float g, float b, float a) {
        if(values.length == 5) {
            values = new float[]{values[0], values[1], values[2], r, g, b, a, values[3], values[4]};
        } else {
            values[3] *= r;
            values[4] *= g;
            values[5] *= b;
            values[6] *= a;
        }
        return this;
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

    public static Vector5f[] color(Vector5f[] vector5fs, float r, float g, float b, float a) {
        for(Vector5f vector5f : vector5fs) {
            vector5f.setColored(r,g,b,a);
        }
        return vector5fs;
    }

    public static Vector5f[] color(Vector5f[] vector5fs, Vector4f color) {
        for(Vector5f vector5f : vector5fs) {
            vector5f.setColored(color.x,color.y,color.z,color.w);
        }
        return vector5fs;
    }

    @Override
    public String toString() {
        return "Vector5f{" +
                "values=" + Arrays.toString(values) +
                '}';
    }
}
