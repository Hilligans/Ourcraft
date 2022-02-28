package dev.Hilligans.ourcraft.Client.Rendering;

import dev.Hilligans.ourcraft.Client.Rendering.Graphics.VertexFormat;
import org.joml.Matrix4f;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

public class VertexMesh {

    /**
     * one of these have to be not null
     */
    public VertexFormat vertexFormat;
    public String vertexFormatName;

    public ArrayList<VertexComponent> vertexComponents = new ArrayList<>();
    public ArrayList<UniformComponent> uniformComponents = new ArrayList<>();

    public IntBuffer indices;


    public VertexMesh(String vertexFormatName) {
        this.vertexFormatName = vertexFormatName;
    }

    public VertexMesh(VertexFormat vertexFormat) {
        this.vertexFormat = vertexFormat;
    }

    public VertexMesh addUniform(Matrix4f matrix4f) {

        return this;
    }

    public VertexMesh addUniform(ByteBuffer byteBuffer) {

        return this;
    }

    static class VertexComponent {

        ByteBuffer data;

        public VertexComponent(ByteBuffer data) {
            this.data = data;
        }
    }

    static class UniformComponent {

        public String name;

        public UniformComponent(String name) {
            this.name = name;
        }

    }

}

