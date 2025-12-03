package dev.hilligans.engine.client.graphics.assimp;

import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.AINode;

import java.nio.IntBuffer;

public class AssimpNode {

    AssimpScene root;
    AssimpNode[] children;
    int[] meshes;

    public AssimpNode(AssimpScene root, AINode node) {
        this.root = root;

        if(node == null) {
            return;
        }

        PointerBuffer children = node.mChildren();
        if(children != null) {
            this.children = new AssimpNode[node.mNumChildren()];
            for (int x = 0; x < node.mNumChildren(); x++) {
                AINode child = AINode.createSafe(children.get(x));

                if (child == null) {
                    continue;
                }

                this.children[x] = new AssimpNode(root, child);
            }
        }

        IntBuffer meshes = node.mMeshes();

        if(meshes != null) {
            this.meshes = new int[node.mNumMeshes()];

            for (int x = 0; x < node.mNumMeshes(); x++) {
                this.meshes[x] = meshes.get(x);
            }
        }
    }
}
