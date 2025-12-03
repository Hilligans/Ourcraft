package dev.hilligans.engine.client.graphics.assimp;

import org.lwjgl.assimp.AIScene;

public class AssimpScene {



    public AssimpScene(AIScene scene) {
        AssimpNode node = new AssimpNode(this, scene.mRootNode());


    }
}
