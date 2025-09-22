package dev.hilligans.engine.client.graphics.resource;

import org.joml.*;

import java.util.Stack;

import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;

public class MatrixStack {

    public Stack<Matrix4f> matrix4fStack = new Stack<>();
    public Stack<Vector4f> colorStack = new Stack<>();
    public Matrix4f matrix4f;
    public Vector4f color;
    public FrustumIntersection frustumIntersection = new FrustumIntersection();

    public MatrixStack(Matrix4f matrix4f) {
        this.matrix4f = matrix4f;
        color = new Vector4f(1.0f,1.0f,1.0f,1.0f);
    }

    public MatrixStack(Matrix4d matrix4d) {
        this.matrix4f = new Matrix4f(matrix4d);
        color = new Vector4f(1.0f,1.0f,1.0f,1.0f);
        frustumIntersection.set(matrix4f);
    }

    public void push() {
        Matrix4f matrix4f1 = new Matrix4f();
        matrix4f.get(matrix4f1);
        matrix4fStack.push(matrix4f);
        matrix4f = matrix4f1;

        Vector4f vector4f = new Vector4f();
        color.get(vector4f);
        colorStack.push(color);
        color = vector4f;
    }

    public void pop() {
        matrix4f = matrix4fStack.pop();
        color = colorStack.pop();
        //applyColor();
    }

    public void updateFrustum() {
        frustumIntersection.set(matrix4f);
    }

    public Matrix4f get() {
        return matrix4f;
    }

    public void applyTransformation(int shaderId) {
        int trans = glGetUniformLocation(shaderId, "transform");
        float[] floats = new float[16];
        matrix4f.get(floats);
        glUniformMatrix4fv(trans,false,matrix4f.get(floats));
    }

    public void setColor(float r, float g, float b, float a) {
        color.set(r,g,b,a);
    }

    public void setColor(int r, int g, int b, int a) {
        setColor(r / 255f,g / 255f, b / 255f, a / 255f);
    }

    public void setColor(float r, float g, float b) {
        color.set(r,g,b,1.0f);
    }

    public void setColor(int r, int g, int b) {
        setColor(r / 255f,g / 255f, b / 255f, 1.0f);
    }

    public void translate(float x, float y, float z) {
        matrix4f.translate(x,y,z);
    }

    public void translate(double x, double y, double z) {
        matrix4f.translate((float) x, (float) y, (float) z);
    }

    public void translateMinusOffset(float x, float y, float z) {
       // matrix4f.translate(x - (Camera.playerChunkPos.x << 4),y,z - (Camera.playerChunkPos.z << 4));
    }

    public void translate(Vector3f vector3f) {
        matrix4f.translate(vector3f);
    }

    public void translate(Vector3d vector) {
        matrix4f.translate((float)vector.x, (float)vector.y, (float)vector.z);
    }

    public void rotate(float rot, Vector3f vector) {
        matrix4f.rotate(rot,vector);
    }

    public void scale(float amount) {
        matrix4f.scale(amount);
    }
}
