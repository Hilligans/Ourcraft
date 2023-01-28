package dev.hilligans.ourcraft.World.NewWorldSystem;

import dev.hilligans.ourcraft.GameInstance;
import dev.hilligans.ourcraft.ModHandler.Content.ModContent;
import dev.hilligans.ourcraft.Util.Registry.IRegistryElement;
import io.netty.buffer.ByteBuf;

public abstract class ChunkStream implements IRegistryElement {

    public String name;
    public ModContent modContent;
    public GameInstance gameInstance;

    public ChunkStream(String name) {
        this.name = name;
    }

    public abstract IChunk fillChunk(ByteBuf buffer, int position, IChunk chunk);

    public abstract int fillBuffer(ByteBuf buffer, int position, IChunk chunk);

    @Override
    public String getResourceName() {
        return name;
    }

    @Override
    public String getIdentifierName() {
        return modContent.getModID() + ":" + name;
    }

    @Override
    public String getUniqueName() {
        return "chunk_stream." + modContent.getModID() + "." + name;
    }

    @Override
    public void assignModContent(ModContent modContent) {
        this.modContent = modContent;
        this.gameInstance = modContent.getGameInstance();
    }
}
