package dev.hilligans.ourcraft.client.rendering.culling;

import dev.hilligans.ourcraft.client.rendering.graphics.api.ICamera;
import dev.hilligans.ourcraft.data.other.BoundingBox;
import dev.hilligans.ourcraft.world.newworldsystem.IChunk;
import dev.hilligans.ourcraft.world.newworldsystem.IWorld;
import dev.hilligans.ourcraft.world.SubChunk;
import org.joml.Vector3d;

import java.util.ArrayList;

public class PlaneTestCullingEngine extends CullingEngine {

    public boolean building;

    public ArrayList<BoundingBox> boxes = new ArrayList<>();

    public PlaneTestCullingEngine(IWorld world) {
        super(world);
    }

    @Override
    public boolean shouldRenderChunk(IChunk chunk, ICamera camera) {
        if(building) {
            return true;
        }


        Vector3d pos = camera.getCameraPos();
        for(BoundingBox b : boxes) {
            //if(Intersectiond.intersectRayAab(chunk.x * 16,0,chunk.z * 16))
        }

       // boolean a = Intersectiond.intersectRayPlane(chunk.x,0,chunk.z,pos.x,pos.y,pos.z,)


        return false;
    }

    public void build(ICamera pos) {
        building = true;


    }

    public static int getSolidChunkFaces(SubChunk chunk) {
        int result = 0;
        short[][] vals = new short[16][16];
        for(int x = 0; x < 16; x++) {
            for(int y = 0; y < 16; y++) {
                for(int z = 0; z < 16; z++) {
                    if(chunk.getBlock(x,y,z).getBlock().blockProperties.transparent) {
                        vals[x][y] |= 1 << z;
                        recursivelyCheck(chunk,vals,x,y,z);
                        if(checkFace(vals,0,true,false,false) && checkFace(vals,15,true,false,false)) {
                            result = 1;
                        }
                        if(checkFace(vals,0,false,true,false) && checkFace(vals,15,false,true,false)) {
                            result |= 2;
                        }
                        if(checkFace(vals,0,false,false,true) && checkFace(vals,15,false,false,true)) {
                            result |= 4;
                        }
                        return result;
                    }
                }
            }
        }
        return 0;
    }

    public static boolean checkFace(short[][] vals, int pos, boolean addX, boolean addY, boolean addZ) {
        if(addX) {
            for(int y = 0; y < 16; y++) {
               for(int z = 0; z < 16; z++) {
                   if((vals[pos][y] & 1 << z) != 0) {
                       return true;
                   }
               }
            }
        } else if(addY) {
            for(int x = 0; x < 16; x++) {
                for(int z = 0; z < 16; z++) {
                    if((vals[x][pos] & 1 << z) != 0) {
                        return true;
                    }
                }
            }
        } else if(addZ) {
            for(int x = 0; x < 16; x++) {
                for(int y = 0; y < 16; y++) {
                    if((vals[x][y] & 1 << pos) != 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static void recursivelyCheck(SubChunk chunk, short[][] vals, int x, int y, int z) {
        boolean posZ = false, negZ = false, posX = false, negX = false, posY = false, negY = false;
        if(z != 15 && chunk.getBlock(x,y,z + 1).getBlock().blockProperties.transparent) {
            if ((vals[x][y] & (1 << z + 1)) == 0) {
                vals[x][y] |= 1 << z + 1;
                posZ = true;
            }
        }
        if(z != 0 && chunk.getBlock(x,y,z - 1).getBlock().blockProperties.transparent) {
            if ((vals[x][y] & (1 << z - 1)) == 0) {
                vals[x][y] |= 1 << z - 1;
                negZ = true;
            }
        }
        if(x != 15 && chunk.getBlock(x + 1,y,z).getBlock().blockProperties.transparent) {
            if ((vals[x + 1][y] & (1 << z)) == 0) {
                vals[x + 1][y] |= 1 << z;
                posX = true;
            }
        }
        if(x != 0 && chunk.getBlock(x - 1,y,z).getBlock().blockProperties.transparent) {
            if ((vals[x - 1][y] & (1 << z)) == 0) {
                vals[x - 1][y] |= 1 << z;
                negX = true;
            }
        }
        if(y != 15 && chunk.getBlock(x,y + 1,z).getBlock().blockProperties.transparent) {
            if ((vals[x][y + 1] & (1 << z)) == 0) {
                vals[x][y + 1] |= 1 << z;
                posY = true;
            }
        }
        if(y != 0 && chunk.getBlock(x,y - 1,z).getBlock().blockProperties.transparent) {
            if ((vals[x][y - 1] & (1 << z)) == 0) {
                vals[x][y - 1] |= 1 << z;
                negY = true;
            }
        }
        if(posZ) {
            recursivelyCheck(chunk,vals,x,y,z + 1);
        }
        if(negZ) {
            recursivelyCheck(chunk,vals,x,y,z - 1);
        }
        if(posX) {
            recursivelyCheck(chunk,vals,x + 1,y,z);
        }
        if(negX) {
            recursivelyCheck(chunk,vals,x - 1,y,z);
        }
        if(posY) {
            recursivelyCheck(chunk,vals,x,y + 1,z);
        }
        if(negY) {
            recursivelyCheck(chunk,vals,x,y - 1,z);
        }
    }
}
