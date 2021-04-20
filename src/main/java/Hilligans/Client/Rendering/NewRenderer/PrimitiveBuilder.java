package Hilligans.Client.Rendering.NewRenderer;

import Hilligans.Client.Camera;
import Hilligans.Client.MatrixStack;
import Hilligans.ClientMain;
import Hilligans.Data.Primitives.DoubleTypeWrapper;
import Hilligans.Data.Primitives.FloatList;
import Hilligans.Data.Primitives.IntList;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL30;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class PrimitiveBuilder {

    public FloatList vertices = new FloatList();
    public IntList indices = new IntList();

    public int type;
    public int size = 0;
    public int count = 0;

    Shader shader;

    public PrimitiveBuilder(int type, Shader shader) {
        this.type = type;
        this.shader = shader;
    }

    public void add(float... floats) {
        count += floats.length;
        vertices.add(floats);
        indices.add(vertices.size());
        size++;
    }

    public void add(float[] vertices, int[] indices) {
        count += vertices.length;
        this.vertices.add(vertices);
        this.indices.add(indices);
        size++;
    }

    public int getVerticesCount() {
        return vertices.size() / shader.shaderElementCount;
    }

    public int[] createMesh() {
        float[] vertices = this.vertices.getElementData();
        int[] indices = this.indices.getElementData();

        int VAO = glGenVertexArrays();
        int VBO = glGenBuffers();
        int EBO = glGenBuffers();
        glBindVertexArray(VAO);
        glBindBuffer(GL_ARRAY_BUFFER, VBO);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EBO);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);
        int x = 0;
        int pointer = 0;
        for(Shader.ShaderElement shaderElement : shader.shaderElements) {
            glVertexAttribPointer(x,shaderElement.count,shaderElement.type,shaderElement.normalised,shader.shaderElementCount * 4,pointer * 4);
            glEnableVertexAttribArray(x);
            x++;
            pointer += shaderElement.count;
        }
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        return new int[]{VAO,VBO,EBO};
    }

    public void draw(MatrixStack matrixStack, int drawMode) {
        float[] vals = new float[count];
        System.arraycopy(vertices.elementData,0,vals,0,count);
        //System.out.println(Arrays.toString(vals));
        //System.out.println(glGetError());
        int VAO = glGenVertexArrays();
        int VBO = glGenBuffers();
        //int EBO = glGenBuffers();

        glBindVertexArray(VAO);


        glBindBuffer(GL_ARRAY_BUFFER, VBO);
        glBufferData(GL_ARRAY_BUFFER, vals, GL_STATIC_DRAW);

        //glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EBO);
        //glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices.elementData, GL_STATIC_DRAW);

        int x = 0;
        int pointer = 0;
        for(Shader.ShaderElement shaderElement : shader.shaderElements) {
            //System.out.println(x + ":" + shaderElement.count + ":" + shaderElement.type + ":" + shaderElement.normalised +":" + shader.shaderElementCount * 4 + ":" + pointer * 4);
            glVertexAttribPointer(x,shaderElement.count,shaderElement.type,shaderElement.normalised,shader.shaderElementCount * 4,pointer * 4);
            //System.out.println(glGetVertexAttribPointer(0, GL_VERTEX_ATTRIB_ARRAY_BUFFER_BINDING));
            //System.out.println(glGetError());

            glEnableVertexAttribArray(x);
            x++;
            pointer += shaderElement.count;
        }
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        matrixStack.push();
        //System.out.println(glGetError());
        glUseProgram(shader.shader);
        //System.out.println(glGetError());

        //System.out.println(glGetError());
        //matrixStack.applyTransformation(shader.shader);

        //int trans = glGetUniformLocation(shader.shader, "pos");
        //glUniformMatrix4fv(trans,false,matrix4f.get(floats));
        //glUniform3fv(trans, new float[]{Camera.pos.x,Camera.pos.y,Camera.pos.z});



        GL30.glBindVertexArray(VAO);
        //System.out.println(glGetError());

        glDrawArrays(type, 0,size);
        //glDrawElements(type, size,GL_UNSIGNED_INT,0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        //System.out.println(glGetError());


        matrixStack.pop();
        glDeleteBuffers(VBO);
       // glDeleteBuffers(EBO);
        glDeleteVertexArrays(VAO);
    }

    public void drawTris(MatrixStack matrixStack) {
        float[] vals = new float[count];
        System.arraycopy(vertices.elementData,0,vals,0,count);

        int VAO = glGenVertexArrays();
        int VBO = glGenBuffers();
        //int EBO = glGenBuffers();

        glBindVertexArray(VAO);
        glBindBuffer(GL_ARRAY_BUFFER, VBO);
        glBufferData(GL_ARRAY_BUFFER, vals, GL_STREAM_DRAW);

        //glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EBO);
        //glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices.elementData, GL_STATIC_DRAW);

        int x = 0;
        int pointer = 0;
        for(Shader.ShaderElement shaderElement : shader.shaderElements) {
            glVertexAttribPointer(x,shaderElement.count,shaderElement.type,shaderElement.normalised,shader.shaderElementCount * 4,pointer * 4);
            glEnableVertexAttribArray(x);
            x++;
            pointer += shaderElement.count;
        }
        //glBindBuffer(GL_ARRAY_BUFFER, 0);

        matrixStack.push();
        glUseProgram(shader.shader);
        matrixStack.applyTransformation(shader.shader);
        GL30.glBindVertexArray(VAO);
        glDrawArrays(type,0,vertices.size());
        //glDrawElements(type, size + 8,GL_UNSIGNED_INT,0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDeleteBuffers(VBO);
        //glDeleteBuffers(EBO);
        matrixStack.pop();
        glDeleteVertexArrays(VAO);
    }

    public void applyTransformation(Matrix4f matrix4f, int shaderId, String name) {
        int trans = glGetUniformLocation(shaderId, name);
        float[] floats = new float[16];;
        glUniformMatrix4fv(trans,false,matrix4f.get(floats));
    }

    public void applyTransformation(float windowX, float windowY, int shaderId, String name) {
        int trans = glGetUniformLocation(shaderId, name);
        glUniform2fv(trans,new float[]{windowX,windowY});
    }

    public static final int
            POINTS         = 0x0,
            LINES          = 0x1,
            LINE_LOOP      = 0x2,
            LINE_STRIP     = 0x3,
            TRIANGLES      = 0x4,
            TRIANGLE_STRIP = 0x5,
            TRIANGLE_FAN   = 0x6,
            QUADS          = 0x7,
            QUAD_STRIP     = 0x8,
            POLYGON        = 0x9;
}
