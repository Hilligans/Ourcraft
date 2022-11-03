package dev.Hilligans.ourcraft.World.NewWorldSystem;

import dev.Hilligans.ourcraft.GameInstance;
import dev.Hilligans.ourcraft.ModHandler.Content.ModContent;
import dev.Hilligans.ourcraft.Util.Registry.IRegistryElement;
import io.netty.buffer.ByteBuf;

import java.nio.ByteBuffer;

public abstract class ChunkStream implements IRegistryElement  {

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
