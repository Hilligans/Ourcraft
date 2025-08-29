package dev.hilligans.ourcraft.client.rendering.newrenderer;

import dev.hilligans.ourcraft.client.rendering.graphics.ShaderSource;
import dev.hilligans.ourcraft.client.rendering.graphics.VertexFormat;
import dev.hilligans.ourcraft.client.rendering.VertexMesh;
import dev.hilligans.ourcraft.data.other.BoundingBox;
import dev.hilligans.ourcraft.data.primitives.FloatList;
import dev.hilligans.ourcraft.data.primitives.IntList;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.function.Consumer;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class PrimitiveBuilder {

    public FloatList vertices = new FloatList();
    public IntList indices = new IntList();

    public int type;
    public int size = 0;
    public int count = 0;

    public VertexFormat vertexFormat;

    public PrimitiveBuilder(VertexFormat vertexFormat) {
        this.vertexFormat = vertexFormat;
    }

    public VertexMesh toVertexMesh() {
        VertexMesh vertexMesh = new VertexMesh(vertexFormat);
        vertexMesh.addData(indices.getElementDataBuffer(), vertices.getElementDataBuffer());
        vertexMesh.setAllocator(vertexMesh);
        return vertexMesh;
    }

    public void ensureCapacity(int vertexSize, int indexSize) {
        vertices.ensureCapacityInternal(vertices.size() + vertexSize);
        indices.ensureCapacityInternal(indices.size() + indexSize);
    }


    public void addQuad(float... vertices) {
        int count = getVerticesCount();
        this.indices.add(count,count + 1, count + 2,count + 3, count + 2, count + 1);
        this.vertices.add(vertices);
        size+= 4;
    }

    public void addSolidQuad(float minX, float minY, float maxX, float maxY, float... data) {
        int count = getVerticesCount();
        float z = 0.95f;
        this.indices.add(count,count + 1, count + 2,count + 3, count + 2, count + 1);
        this.vertices.ensureCapacity(this.vertices.size() + (3 + data.length) * 4);
        this.vertices.add(minX,minY,z).add(data).add(minX,maxY,z).add(data).add(maxX,minY,z).add(data).add(maxX,maxY,z).add(data);
        size += 4;
    }

    public void buildQuad(float minX, float minY, float z, float minTexX, float minTexY, float maxX, float maxY, float maxTexX, float maxTexY) {
        addQuad(minX,minY,z,minTexX,minTexY,
                minX,maxY,z,minTexX,maxTexY,
                maxX,minY,z,maxTexX,minTexY,
                maxX,maxY,z,maxTexX,maxTexY);
    }

    protected void translate(int startX, float x, float y, float z) {
        for(int i = startX; i < vertices.size(); i+= getCount()) {
            vertices.elementData[i] += x;
            vertices.elementData[i + 1] += y;
            vertices.elementData[i + 2] += z;
        }
    }

    public void translate(float x, float y, float z) {
        translate(0,x,y,z);
    }

    public int getVerticesCount() {
        return vertices.size() / getCount();
    }

    public int getCount() {
        return (vertexFormat.getStride() / 4);
    }

    public void rotate(float degrees, Vector3f vector) {
        rotate(degrees,vector,0);
    }

    public void rotate(float degrees, Vector3f vector, int startPos) {
        Matrix3f matrix3f = new Matrix3f(1.0f,1.0f,1.0f,1.0f,1.0f,1.0f,1.0f,1.0f,1.0f);
        matrix3f.rotate(degrees,vector);
        for(int x = startPos; x < vertices.size(); x+=vertexFormat.getStride()) {
            Vector3f vector3f = new Vector3f(vertices.elementData[x],vertices.elementData[x] + 1, vertices.elementData[x] + 2).mul(matrix3f);
            vertices.elementData[x] = vector3f.x;
            vertices.elementData[x + 1] = vector3f.y;
            vertices.elementData[x + 2] = vector3f.z;
        }
    }
}
