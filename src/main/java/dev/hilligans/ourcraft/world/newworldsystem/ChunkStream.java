package dev.hilligans.ourcraft.world.newworldsystem;

import dev.hilligans.engine.GameInstance;
import dev.hilligans.engine.mod.handler.content.ModContainer;
import dev.hilligans.engine.util.IByteArray;
import dev.hilligans.ourcraft.util.registry.IRegistryElement;
import io.netty.buffer.ByteBuf;

public abstract class ChunkStream implements IRegistryElement {

    public String name;
    public ModContainer owner;
    public GameInstance gameInstance;

    public ChunkStream(String name) {
        this.name = name;
    }

    public abstract IChunk fillChunk(ByteBuf buffer, int position, IChunk chunk);

    public abstract int fillBuffer(ByteBuf buffer, int position, IChunk chunk);

    public abstract void fillBuffer(IByteArray array, IChunk chunk);

    public abstract void fillChunk(IByteArray array, IChunk chunk);

    @Override
    public String getResourceName() {
        return name;
    }

    @Override
    public String getResourceOwner() {
        return owner.getModID();
    }

    @Override
    public String getResourceType() {
        return "chunk_stream";
    }

    @Override
    public void assignOwner(ModContainer owner) {
        this.owner = owner;
        this.gameInstance = owner.getGameInstance();
    }
}
