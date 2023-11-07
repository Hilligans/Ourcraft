package dev.hilligans.ourcraft.client.rendering.minimap;

import dev.hilligans.ourcraft.client.MatrixStack;
import dev.hilligans.ourcraft.world.ClientWorld;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;

public class MiniMap {

    public ClientWorld clientWorld;
    public int lastX = 0;
    public int lastY = 0;
    public float microX;
    public float microZ;
    public int zoom = 500;

    public Long2ObjectOpenHashMap<GridChunk> chunks = new Long2ObjectOpenHashMap<>();

    int count = 0;

    public MiniMap(ClientWorld clientWorld) {
        this.clientWorld = clientWorld;
    }

    public GridChunk getGridChunk(int x, int z) {
        return chunks.get((x >> 4) & 4294967295L | ((z >> 4) & 4294967295L) << 32);
    }

    public void putGridChunk(int x, int z, GridChunk gridChunk) {
        chunks.put((x >> 4) & 4294967295L | ((z >> 4) & 4294967295L) << 32,gridChunk);
    }

    public void update(int x, int z) {
        GridChunk gridChunk = getGridChunk(x,z);
        if(gridChunk != null) {
            gridChunk.update(x,z);
            gridChunk.texture = -1;
        }
    }

    //TODO fix
    public void draw(MatrixStack matrixStack, int chunkX, int chunkZ, int x, int y, int windowWidth, int windowHeight) {
       /*
        GL11.glDisable(GL11.GL_DEPTH_TEST);

        int size = zoom;
        int width = windowWidth / size + 2;
        int height = windowHeight / size + 2;
        for (int X = -width / 2; X < width / 2 + 1; X++) {
            for (int Z = -height / 2; Z < height / 2 + 1; Z++) {
                GridChunk gridChunk = getGridChunk(chunkX - X * 16, chunkZ - Z * 16);
                if (gridChunk == null) {
                    gridChunk = new GridChunk(chunkX - (X * 16),chunkZ - (Z * 16),clientWorld);
                    putGridChunk(chunkX - (X * 16),chunkZ - (Z * 16),gridChunk);
                }
                if (gridChunk.texture == -1 || count == 500) {
                    gridChunk.makeTexture1();
                }
               // Renderer.drawTexture(matrixStack, gridChunk.texture, getX(chunkX + (X * 16), size,x,windowWidth), getZ(chunkX + (Z * 16), size,y,windowHeight), size, size);

            }
        }
        if(count == 500) {
            count = 0;
        }
        count++;

        Vector3d pos = Camera.renderPos;
        BlockPos blockPos = new BlockPos(pos);
        //Textures.SHORT_ICON.drawTexture(matrixStack,getX((int) blockPos.getChunkX(),size,x,windowWidth), getZ((int) blockPos.getChunkZ(),size,y,windowHeight),10,10);
        GL11.glEnable(GL11.GL_DEPTH_TEST);

        */
    }

    public int getX(int chunkX, int size, int x, int windowWidth) {
        return (int) (windowWidth / 2 - (chunkX / 16f) * size - size / 2 + microX * size / 256f + x);
    }

    public int getZ(int chunkZ, int size, int z, int windowHeight) {
        return (int) (windowHeight / 2 - (chunkZ / 16f) * size - size / 2 + microZ * size / 256f + z);
    }

    public void addX(float x) {
        microX += x;
        int extra = (int) (microX / 256f / 16);
        microX -= extra * 256f / 16;
        lastX -= extra;
    }

    public void addY(float y) {
        microZ += y;
        int extra = (int) (microZ / 256f / 16);
        microZ -= extra * 256f / 16;
        lastY -= extra;
    }


}
