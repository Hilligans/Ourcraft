package Hilligans.Client.Rendering.World.Managers;

import Hilligans.Block.Block;
import Hilligans.Client.Rendering.NewRenderer.PrimitiveBuilder;
import Hilligans.Data.Other.BlockPos;
import Hilligans.Data.Primitives.DoubleTypeWrapper;
import Hilligans.Util.Vector5f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.*;

public class  VAOManager {

    public static HashMap<Integer, DoubleTypeWrapper<Integer,Integer>> buffers = new HashMap<>();

    public static void destroyBuffer(int id) {
        DoubleTypeWrapper<Integer,Integer> doubleTypeWrapper = buffers.get(id);
        buffers.remove(id);
        if(doubleTypeWrapper != null) {
            glDeleteBuffers(doubleTypeWrapper.getTypeA());
            glDeleteBuffers(doubleTypeWrapper.getTypeB());
        }
        glDeleteVertexArrays(id);
    }

    public static int createVAO(float[] vertices, int[] indices) {
        int VAO = glGenVertexArrays();
        int VBO = glGenBuffers();
        int EBO = glGenBuffers();


        glBindVertexArray(VAO);

        glBindBuffer(GL_ARRAY_BUFFER, VBO);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EBO);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

        glVertexAttribPointer(0,3,GL_FLOAT,false,5 * 4,0);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 5 * 4, 3 * 4);
        glEnableVertexAttribArray(1);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        //glDeleteBuffers(VBO);
        // glDeleteBuffers(EBO);
        buffers.put(VAO,new DoubleTypeWrapper<>(VBO,EBO));

        return VAO;
    }

    public static int createVAO(PrimitiveBuilder primitiveBuilder) {
        int[] vals = primitiveBuilder.createMesh();
        buffers.put(vals[0],new DoubleTypeWrapper<>(vals[1],vals[2]));
        return vals[0];
    }

    public static int createLine(float[] vertices, int[] indices) {
        int VAO = glGenVertexArrays();
        int VBO = glGenBuffers();
        int EBO = glGenBuffers();

        glBindVertexArray(VAO);

        glBindBuffer(GL_ARRAY_BUFFER, VBO);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EBO);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

        glVertexAttribPointer(0,3,GL_FLOAT,false,3 * 4,0);
        glEnableVertexAttribArray(0);

        glBindBuffer(GL_ARRAY_BUFFER, 0);

        buffers.put(VAO,new DoubleTypeWrapper<>(VBO,EBO));

        return VAO;
    }

    public static int createDynamicVAO(float[] vertices, int[] indices) {
        int VAO = glGenVertexArrays();
        int VBO = glGenBuffers();
        int EBO = glGenBuffers();

        glBindVertexArray(VAO);

        glBindBuffer(GL_ARRAY_BUFFER, VBO);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_DYNAMIC_DRAW);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EBO);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_DYNAMIC_DRAW);

        glVertexAttribPointer(0,3,GL_FLOAT,false,5 * 4,0);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 5 * 4, 3 * 4);
        glEnableVertexAttribArray(1);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        return VAO;
    }

    public static float[] convertVertices(ArrayList<Vector5f> vector5fs, boolean coloured) {
        float[] floats;
        if(coloured) {
            floats = new float[vector5fs.size() * 9];
            int a = 0;
            for(Vector5f vector5f : vector5fs) {
                vector5f.addToList(floats,a * 9);
                a++;
            }
        } else {
            floats = new float[vector5fs.size() * 5];
            int a = 0;
            for(Vector5f vector5f : vector5fs) {
                vector5f.addToList(floats,a * 5);
                a++;
            }
        }
        return floats;
    }

    public static int[] convertIndices(ArrayList<Integer> indices) {
        int[] integers = new int[indices.size()];
        int a  = 0;
        for(Integer integer : indices) {
            integers[a] = integer;
            a++;
        }
        return integers;
    }

}
