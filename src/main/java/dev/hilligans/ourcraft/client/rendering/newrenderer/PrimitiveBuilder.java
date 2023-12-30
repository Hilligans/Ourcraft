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

    public PrimitiveBuilder(ShaderSource shaderSource) {
        this(shaderSource.vertexFormat);
    }

    public PrimitiveBuilder setVertexFormat(VertexFormat vertexFormat) {
        this.vertexFormat = vertexFormat;
        return this;
    }

    public VertexMesh toVertexMesh() {
        VertexMesh vertexMesh = new VertexMesh(vertexFormat);
        vertexMesh.addData(indices.getElementDataBuffer(), vertices.getElementDataBuffer());
        return vertexMesh;
    }

    public void ensureCapacity(int vertexSize, int indexSize) {
        vertices.ensureCapacityInternal(vertices.size() + vertexSize);
        indices.ensureCapacityInternal(indices.size() + indexSize);
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

    public void add(int... indices) {
        int count = sizeVal;
        for(int index : indices) {
            this.indices.add(index + count);
        }
    }

    public int addIndex(float... vals) {
        vertices.add(vals);
        return this.size++;
    }

    public int sizeVal = 0;

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

    public void addBoundingBox(BoundingBox boundingBox, Consumer<FloatList> extraData) {
        float[] vertices = new float[]{boundingBox.minX, boundingBox.minY, boundingBox.minZ, boundingBox.maxX, boundingBox.minY, boundingBox.minZ, boundingBox.maxX, boundingBox.minY, boundingBox.maxZ, boundingBox.minX, boundingBox.minY, boundingBox.maxZ, boundingBox.minX, boundingBox.maxY, boundingBox.minZ, boundingBox.maxX, boundingBox.maxY, boundingBox.minZ, boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ, boundingBox.minX, boundingBox.maxY, boundingBox.maxZ,};
        int s = this.size;
        this.size += 12;
        for(int x = 0; x < vertices.length; x+=3) {
            this.vertices.add(vertices[x], vertices[x+1], vertices[x+2]);
            extraData.accept(this.vertices);
        }
        this.indices.add(s+0,s+1,s+1,s+2,s+2,s+3,s+3,s+0,s+0,s+4,s+1,s+5,s+2,s+6,s+3,s+7,s+4,s+5,s+5,s+6,s+6,s+7,s+7,s+4);
        //int[] indices = new int[]{0,1,1,2,2,3,3,0,0,4,1,5,2,6,3,7,4,5,5,6,6,7,7,4};
    }

    public void addQuadIndices() {
        int count = getVerticesCount();
        this.indices.add(count,count + 1, count + 2,count + 3, count + 2, count + 1);
    }

    public void addQuadIndicesInverse() {
        int count = getVerticesCount();
        this.indices.add(count,count + 2, count + 1,count + 3, count + 1, count + 2);
    }

    public void addQuadInverse(float... vertices) {
        int count = getVerticesCount();
        this.indices.add(count,count + 2, count + 1,count + 3, count + 1, count + 2);
        this.vertices.add(vertices);
        size+= 4;
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
