package dev.hilligans.engine.client.graphics.assimp;

import dev.hilligans.engine.client.graphics.api.IDefaultEngineImpl;
import dev.hilligans.engine.client.graphics.api.IMeshBuilder;
import dev.hilligans.engine.client.graphics.api.IModel;
import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AIVector3D;
import org.lwjgl.system.MemoryUtil;

import static dev.hilligans.engine.Engine.position_color;

public class AssimpModel implements IModel {

    public AIMesh mesh;

    public AssimpModel(AIMesh mesh) {
        this.mesh = mesh;
    }

    @Override
    public IMeshBuilder build(IDefaultEngineImpl<?, ?, ?> impl) {
        IMeshBuilder builder = impl.getMeshBuilder(position_color);

        AIVector3D.Buffer buffer = mesh.mVertices();

        for(int x = 0; x < mesh.mNumVertices(); x++) {
            buffer.position(x);

            builder.addVertices(buffer.x(), buffer.y(), buffer.z(), 1, 1, 1, 1);
            builder.addIndices(x);
        }

        //for(int x = 0; x < mesh.mTextureCoordsNames().limit(); x++) {
        //    String s = MemoryUtil.memASCII(mesh.mTextureCoordsNames().get(x));

        //    System.out.println(s);
        //}



        return builder;
    }

    @Override
    public float[] getVertices(int side) {
        return new float[0];
    }

    @Override
    public int[] getIndices(int side) {
        return new int[0];
    }

    @Override
    public String getModel() {
        return "";
    }

    @Override
    public String getPath() {
        return "";
    }
}
