package dev.hilligans.ourcraft.util;

import dev.hilligans.ourcraft.util.interfaces.TriConsumer;
import org.joml.Vector3i;

public class Loops {

    public static void nearestToFurthestLoop(int xWidth, int yWidth, int zWidth, Vector3i playerChunkPos, TriConsumer<Integer, Integer, Integer> consumer) {
        for (int x = 0; x < xWidth; x++) {
            for (int y = 0; y < yWidth; y++) {
                for (int z = 0; z < zWidth; z++) {
                    int xx = x + playerChunkPos.x;
                    int yy = y + playerChunkPos.y;
                    int zz = z + playerChunkPos.z;
                    int nx = -x + playerChunkPos.x;
                    int ny = -y + playerChunkPos.y;
                    int nz = -z + playerChunkPos.z;

                    consumer.accept(xx, yy, zz);
                    if (x != 0 && y != 0 && z != 0) {
                        consumer.accept(nx, ny, nz);
                    }
                    if (x != 0 && y != 0) {
                        consumer.accept(nx, ny, zz);
                    }
                    if (x != 0 && z != 0) {
                        consumer.accept(nx, yy, nz);
                    }
                    if (y != 0 && z != 0) {
                        consumer.accept(xx, ny, nz);
                    }
                    if (x != 0) {
                        consumer.accept(nx, yy, zz);
                    }
                    if (y != 0) {
                        consumer.accept(xx, ny, zz);
                    }
                    if (z != 0) {
                        consumer.accept(xx, yy, nz);
                    }
                }
            }
        }
    }

    public static void furthestToNearestLoop(int xWidth, int yWidth, int zWidth, Vector3i playerChunkPos, TriConsumer<Integer, Integer, Integer> consumer) {
        for (int x = xWidth - 1; x >= 0; x--) {
            for (int y = yWidth - 1; y >= 0; y--) {
                for (int z = xWidth - 1; z >= 0; z--) {
                    int xx = x + playerChunkPos.x;
                    int yy = y + playerChunkPos.y;
                    int zz = z + playerChunkPos.z;
                    int nx = -x + playerChunkPos.x;
                    int ny = -y + playerChunkPos.y;
                    int nz = -z + playerChunkPos.z;

                    consumer.accept(xx, yy, zz);
                    if (x != 0 && y != 0 && z != 0) {
                        consumer.accept(nx, ny, nz);
                    }
                    if (x != 0 && y != 0) {
                        consumer.accept(nx, ny, zz);
                    }
                    if (x != 0 && z != 0) {
                        consumer.accept(nx, yy, nz);
                    }
                    if (y != 0 && z != 0) {
                        consumer.accept(xx, ny, nz);
                    }
                    if (x != 0) {
                        consumer.accept(nx, yy, zz);
                    }
                    if (y != 0) {
                        consumer.accept(xx, ny, zz);
                    }
                    if (z != 0) {
                        consumer.accept(xx, yy, nz);
                    }
                }
            }
        }
    }
}
