package dev.hilligans.ourcraft.client.rendering.world.managers;

import dev.hilligans.ourcraft.client.rendering.newrenderer.PrimitiveBuilder;
import dev.hilligans.ourcraft.data.primitives.Tuple;

import java.util.HashMap;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.*;

public class  VAOManager {

    public static HashMap<Integer, Tuple<Integer,Integer>> buffers = new HashMap<>();

    public static void destroyBuffer(int id) {
        Tuple<Integer,Integer> tuple = buffers.get(id);
        buffers.remove(id);
        if(tuple != null) {
            glDeleteBuffers(tuple.getTypeA());
            glDeleteBuffers(tuple.getTypeB());
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
        buffers.put(VAO,new Tuple<>(VBO,EBO));

        return VAO;
    }

    public static int createVAO(PrimitiveBuilder primitiveBuilder) {
        return createVAO(primitiveBuilder,GL_STREAM_DRAW);
    }

    public static int createVAO(PrimitiveBuilder primitiveBuilder, int mode) {
        int[] vals = primitiveBuilder.createMesh(mode);
        buffers.put(vals[0],new Tuple<>(vals[1],vals[2]));
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

        buffers.put(VAO,new Tuple<>(VBO,EBO));

        return VAO;
    }
}
