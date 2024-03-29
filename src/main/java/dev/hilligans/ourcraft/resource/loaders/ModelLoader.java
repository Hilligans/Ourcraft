package dev.hilligans.ourcraft.resource.loaders;

import dev.hilligans.ourcraft.client.rendering.newrenderer.IModel;

import java.nio.ByteBuffer;

public class ModelLoader extends ResourceLoader<IModel> {

    public ModelLoader() {
        super("model_loader", "model");
    }

    @Override
    public IModel read(ByteBuffer buffer) {
        return null;
    }

    @Override
    public ByteBuffer write(IModel iModel) {
        return null;
    }
}
