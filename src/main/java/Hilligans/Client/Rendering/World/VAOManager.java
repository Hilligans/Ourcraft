package Hilligans.Client.Rendering.World;

import Hilligans.Data.Primitives.DoubleTypeWrapper;

import java.util.ArrayList;
import java.util.HashMap;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.*;

public class VAOManager {

    public static HashMap<Integer, DoubleTypeWrapper<Integer,Integer>> buffers = new HashMap<>();

    public static void destroyBuffer(int id) {
        DoubleTypeWrapper<Integer,Integer> doubleTypeWrapper = buffers.get(id);
        buffers.remove(id);
        glDeleteBuffers(doubleTypeWrapper.getTypeA());
        glDeleteBuffers(doubleTypeWrapper.getTypeB());
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

        // note that this is allowed, the call to glVertexAttribPointer registered VBO as the vertex attribute's bound vertex buffer object so afterwards we can safely unbind
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        //glDeleteBuffers(VBO);
       // glDeleteBuffers(EBO);
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

        // note that this is allowed, the call to glVertexAttribPointer registered VBO as the vertex attribute's bound vertex buffer object so afterwards we can safely unbind
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        glDeleteBuffers(VBO);
        glDeleteBuffers(EBO);

        return VAO;
    }

    public static int createVAO(float[] vertices) {
        int size = vertices.length / 6;


        int[] indices = new int[vertices.length];

        ArrayList<Integer> vals = new ArrayList<>();


        for(int x = 0; x < size; x++) {
            vals.add(x * 6);
            vals.add(x * 6 + 1);
            vals.add(x * 6 + 2);

            vals.add(x * 6 + 4);
            vals.add(x * 6 + 3);
            vals.add(x * 6 + 5);
        }

        for(int x = 0; x < vertices.length; x++) {
            //indices[x] = vals.get(x);
            indices[x] = x;
        }

        return createVAO(vertices,indices);
    }



}
