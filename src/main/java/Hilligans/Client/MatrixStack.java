package Hilligans.Client;

import Hilligans.ClientMain;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.Stack;

import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;

public class MatrixStack {


    public Stack<Matrix4f> matrix4fStack = new Stack<>();
    public Matrix4f matrix4f;

    public MatrixStack(Matrix4f matrix4f) {
        this.matrix4f = matrix4f;
    }

    public void push() {
        Matrix4f matrix4f1 = new Matrix4f();
        matrix4f.get(matrix4f1);
        matrix4fStack.push(matrix4f);
        matrix4f = matrix4f1;
    }

    public void pop() {
        matrix4f = matrix4fStack.pop();
    }

    public void applyTransformation(int shaderId) {
        int trans = glGetUniformLocation(shaderId, "transform");
        float[] floats = new float[16];
        glUniformMatrix4fv(trans,false,matrix4f.get(floats));
    }

    public void applyTransformation() {
        applyTransformation(ClientMain.shaderProgram);
    }

    public void translate(float x, float y, float z) {
        matrix4f.translate(x,y,z);
    }

    public void rotate(float rot, Vector3f vector) {
        matrix4f.rotate(rot,vector);
    }





}
