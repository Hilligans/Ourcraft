package dev.hilligans.engine.resource.loaders;

import dev.hilligans.engine.client.graphics.api.IModel;
import dev.hilligans.engine.client.graphics.assimp.AssimpAABB;
import dev.hilligans.engine.client.graphics.assimp.AssimpModel;
import dev.hilligans.engine.data.IBoundingBox;
import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.AIAABB;
import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.assimp.Assimp;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;

import static org.lwjgl.assimp.Assimp.*;

public class ModelLoader extends ResourceLoader<IModel> {

    public ModelLoader() {
        super("model_loader", "model");
        withFileTypes("obj");
    }

    @Override
    public IModel read(ByteBuffer buffer) {
        AIScene scene = aiImportFileFromMemory(buffer, Assimp.aiProcess_Triangulate | Assimp.aiProcess_ValidateDataStructure, "");
        PointerBuffer meshes = scene.mMeshes();
        for ( int i = 0; i < meshes.remaining(); i++ ) {


            AIMesh mesh = AIMesh.create(meshes.get(i));
            return new AssimpModel(mesh);

            //System.out.println(mesh.mNumVertices());
            //System.out.println(new AssimpAABB(mesh.mAABB()));
        }

        return null;
    }

    @Override
    public ByteBuffer write(IModel iModel) {
        return null;
    }
}
