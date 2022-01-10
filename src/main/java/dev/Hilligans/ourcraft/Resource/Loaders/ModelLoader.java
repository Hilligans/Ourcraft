package dev.Hilligans.ourcraft.Resource.Loaders;

import dev.Hilligans.ourcraft.Client.Rendering.NewRenderer.IModel;
import dev.Hilligans.ourcraft.Resource.ResourceLoader;

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
