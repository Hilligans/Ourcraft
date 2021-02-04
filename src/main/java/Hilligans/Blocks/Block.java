package Hilligans.Blocks;

import Hilligans.Client.Rendering.World.CubeManager;
import Hilligans.Client.Rendering.World.TextureManager;
import Hilligans.Data.Other.BoundingBox;
import Hilligans.Util.Vector5f;
import Hilligans.Client.Rendering.World.BlockTextureManager;
import Hilligans.World.BlockPos;
import org.joml.Vector3f;
import org.joml.Vector3i;

public class Block {

    public String name;
    public short id;

    public BlockTextureManager blockTextureManager = new BlockTextureManager();

    public Block(String name) {
        this.name = name;
        id = Blocks.getNextId();
        Blocks.BLOCKS.add(this);
        Blocks.MAPPED_BLOCKS.put(name,id);
    }

    public Vector3f getAllowedMovement(Vector3f motion, Vector3f pos, BlockPos blockPos, BoundingBox boundingBox) {
        float x = getMomentum(blockPos.x - 0.5f, blockPos.x + 0.5f, boundingBox.minX + pos.x,boundingBox.maxX + pos.x, motion.x);
        float y = getMomentum(blockPos.y - 0.5f, blockPos.y + 0.5f, boundingBox.minY + pos.y,boundingBox.maxY + pos.y, motion.y);
        float z = getMomentum(blockPos.z - 0.5f, blockPos.z + 0.5f, boundingBox.minZ + pos.z,boundingBox.maxZ + pos.z, motion.z);
        return new Vector3f(x,y,z);
    }

    private float getMomentum(float blockMin, float blockMax, float entityMin, float entityMax, float speed) {
        if(speed > 0) {
            if(entityMax < blockMin && entityMax + speed < blockMin) {
                return speed;
            }
            return blockMin - entityMax;
        } else {
            if(entityMin > blockMax && entityMin + speed > blockMax) {
                return speed;
            }
            return blockMax - entityMin;
        }
    }



    public Block withTexture(String texture) {
        blockTextureManager.addString(texture);
        return this;
    }

    public Block withSidedTexture(String texture, int side) {
        blockTextureManager.addString(texture,side);
        return this;
    }

    public void generateTextures() {
        blockTextureManager.generate();
    }

    public Vector5f[] getVertices(int side) {
        return CubeManager.getVertices(blockTextureManager,side,0.5f);
    }

    public Integer[] getIndices(int side, int spot) {
        return CubeManager.getIndices(side,spot);
    }

    public static BlockPos getBlockPos(int side) {
        switch (side) {
            case 0:
                return new BlockPos(0,0,-1);
            case 1:
                return new BlockPos(0,0,1);
            case 2:
                return new BlockPos(-1,0,0);
            case 3:
                return new BlockPos(1,0,0);
            case 4:
                return new BlockPos(0,-1,0);
            default:
                return new BlockPos(0,1,0);
        }
    }

    public static final int UP = 5;
    public static final int DOWN = 4;

}
