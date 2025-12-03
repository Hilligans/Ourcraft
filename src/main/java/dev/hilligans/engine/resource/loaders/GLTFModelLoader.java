package dev.hilligans.engine.resource.loaders;

import dev.hilligans.engine.client.graphics.api.IModel;
import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.AIColor4D;
import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.assimp.Assimp;

import java.nio.ByteBuffer;

import static org.lwjgl.assimp.Assimp.aiImportFileFromMemory;

public class GLTFModelLoader extends ResourceLoader<IModel> {
    public GLTFModelLoader() {
        super("gltf_model_loader", "model");
        withFileTypes("glb", "gltf");
        acceptsBuffer(BufferType.DIRECT);
    }

    @Override
    public IModel read(ByteBuffer buffer) {
        AIScene scene = aiImportFileFromMemory(buffer, Assimp.aiProcess_Triangulate | Assimp.aiProcess_ValidateDataStructure, "");
        System.out.println(scene.mNumTextures());

        PointerBuffer meshes = scene.mMeshes();

        for(int x = 0; x < meshes.remaining(); x++) {
            AIMesh mesh = AIMesh.createSafe(meshes.get(x));


            mesh.mVertices();

            PointerBuffer colors = mesh.mColors();
            for(int y = 0; y < colors.remaining(); y++) {

                AIColor4D color = AIColor4D.createSafe(colors.get(y));

                if(color == null) {
                    continue;
                }

                System.out.printf("%f, %f, %f, %f\n", color.r(), color.g(), color.b(), color.a());
            }
        }


        System.out.println(scene);

        return null;
    }

    @Override
    public ByteBuffer write(IModel iModel) {
        return null;
    }
}
