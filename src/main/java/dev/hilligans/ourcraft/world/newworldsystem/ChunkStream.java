package dev.hilligans.ourcraft.world.newworldsystem;

import dev.hilligans.ourcraft.GameInstance;
import dev.hilligans.ourcraft.mod.handler.content.ModContainer;
import dev.hilligans.ourcraft.mod.handler.content.ModContent;
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
