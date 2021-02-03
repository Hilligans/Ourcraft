package Hilligans.Client;

import Hilligans.ClientMain;
import Hilligans.Network.ClientNetworkInit;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.Stack;

import static org.lwjgl.opengl.GL20.*;

public class MatrixStack {


    public Stack<Matrix4f> matrix4fStack = new Stack<>();
    public Stack<Vector4f> colorStack = new Stack<>();
    public Matrix4f matrix4f;
    public Vector4f color;

    public MatrixStack(Matrix4f matrix4f) {
        this.matrix4f = matrix4f;
        color = new Vector4f(1.0f,1.0f,1.0f,1.0f);
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
        applyColor();
    }

    public void applyTransformation(int shaderId) {
        int trans = glGetUniformLocation(shaderId, "transform");
        float[] floats = new float[16];
        glUniformMatrix4fv(trans,false,matrix4f.get(floats));
    }

    public void applyTransformation() {
        applyTransformation(ClientMain.shaderProgram);
    }

    public void applyColor(int shaderId) {
        int trans = glGetUniformLocation(shaderId, "color");
        glUniform4f(trans,color.x,color.y,color.z,color.w);
    }

    public void applyColor() {
        applyColor(ClientMain.shaderProgram);
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

    public void rotate(float rot, Vector3f vector) {
        matrix4f.rotate(rot,vector);
    }





}
