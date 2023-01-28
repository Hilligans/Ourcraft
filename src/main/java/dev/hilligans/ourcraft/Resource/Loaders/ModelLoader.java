package dev.hilligans.ourcraft.Resource.Loaders;

import dev.hilligans.ourcraft.Client.Rendering.NewRenderer.IModel;

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
