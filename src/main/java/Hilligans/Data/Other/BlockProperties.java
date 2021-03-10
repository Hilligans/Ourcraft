package Hilligans.Data.Other;

import Hilligans.Block.Block;
import Hilligans.Client.Rendering.World.Managers.BlockTextureManager;

public class BlockProperties {

    public boolean serverSide = false;
    public boolean transparent = false;
    public boolean canWalkThrough = false;
    public BlockTextureManager blockTextureManager = new BlockTextureManager();

    public BlockProperties serverSide() {
        serverSide = true;
        return this;
    }

    public BlockProperties transparent() {
        transparent = true;
        return this;
    }

    public BlockProperties canWalkThrough() {
        canWalkThrough = true;
        return this;
    }

    public BlockProperties withTexture(String texture) {
        blockTextureManager.addString(texture);
        return this;
    }

    public BlockProperties withSidedTexture(String texture, int side) {
        blockTextureManager.addString(texture,side);
        return this;
    }







}
