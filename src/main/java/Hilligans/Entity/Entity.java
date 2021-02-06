package Hilligans.Entity;

import Hilligans.Block.Block;
import Hilligans.Block.Blocks;
import Hilligans.Client.MatrixStack;
import Hilligans.Data.Other.BoundingBox;
import Hilligans.Entity.Entities.ItemEntity;
import Hilligans.Entity.LivingEntities.PlayerEntity;
import Hilligans.Network.Packet.Server.SUpdateEntityPacket;
import Hilligans.Network.PacketData;
import Hilligans.Network.ServerNetworkHandler;
import Hilligans.ServerMain;
import Hilligans.Data.Other.BlockPos;
import Hilligans.World.World;
import org.joml.Vector3f;

import java.util.ArrayList;

public abstract class Entity {

    public float x,y,z,pitch,yaw,velX,velY,velZ;

    public BoundingBox boundingBox;

    public int id;
    public int type = 0;

    public Entity(int id) {
        this.id = id;
        x = 0;
        y = 0;
        z = 0;
    }

    public Entity(float x, float y, float z, int id) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.id = id;
    }

    public Entity(PacketData packetData) {
        x = packetData.readFloat();
        y = packetData.readFloat();
        z = packetData.readFloat();
        pitch = packetData.readFloat();
        yaw = packetData.readFloat();
        id = packetData.readInt();
    }

    public Entity setPos(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    public Entity setRot(float pitch, float yaw) {
       this.pitch = pitch;
       this.yaw = yaw;
        return this;
    }

    public void writeData(PacketData packetData) {
        packetData.writeInt(type);
        packetData.writeFloat(x);
        packetData.writeFloat(y);
        packetData.writeFloat(z);
        packetData.writeFloat(pitch);
        packetData.writeFloat(yaw);
        packetData.writeInt(id);
    }

    public abstract void render(MatrixStack matrixStack);

    public void tick() {}

    public void destroy() {}

    int stepCount = 10;

    public void move() {
        //System.out.println("moving");
        if(velX != 0 || velY != 0 || velZ != 0) {
            Vector3f movement = new Vector3f();
            /*Vector3f movement = getAllowedMovement(new Vector3f(velX, velY, velZ), ServerMain.world);

             */
            for(int x = 0; x < 7; x++) {
                movement = getAllowedMovement(tryMovement(x,velX,velY,velZ),ServerMain.world);
                if(movement.x != velX && movement.y != velY && movement.z != velZ) {
                    continue;
                }
                break;
            }


            if (movement.x != velX) {
                velX = 0;
            }
            if (movement.y != velY) {
                velZ = 0;
            }
            if (movement.z != velY) {
                velZ = 0;
            }
            x += movement.x;
            y += movement.y;
            z += movement.z;
            ServerNetworkHandler.sendPacket(new SUpdateEntityPacket(this));
        }
    }

    private Vector3f tryMovement(int side, float velX, float velY, float velZ) {
        switch (side) {
            case 0:
                return new Vector3f(velX,velY,velZ);
            case 1:
                return new Vector3f(velX,velY,0);
            case 2:
                return new Vector3f(velX,0,velZ);
            case 3:
                return new Vector3f(0,velY,velZ);
            case 4:
                return new Vector3f(velX,0,0);
            case 5:
                return new Vector3f(0,velY,0);
            case 6:
                return new Vector3f(0,0,velZ);
            default:
                return new Vector3f();
        }
    }



    public Vector3f getAllowedMovement(Vector3f motion, World world) {
        Vector3f newSpeed = new Vector3f(motion);
        //BlockPos pos = new BlockPos(Math.round(x),Math.round(y),Math.round(z));
        BlockPos pos = new BlockPos((int)Math.floor(x),(int)Math.floor(y),(int)Math.floor(z));
        //System.out.println((int)Math.ceil(motion.x));
        int X = (int) Math.ceil(Math.abs(motion.x)) + 1;
        int Y = (int) Math.ceil(Math.abs(motion.y)) + 1;
        int Z = (int) Math.ceil(Math.abs(motion.z)) + 1;

        for(int x = -X; x < X; x++) {
            for(int y = -Y; y < Y; y++) {
                for(int z = -Z; z < Z; z++) {
                    Block block = world.getBlockState(pos.copy().add(x,y,z)).block;
                    if(block != Blocks.AIR) {
                     //   System.out.println(block.name);
                        Vector3f blockSpeed = block.getAllowedMovement(new Vector3f(motion.x,motion.y,motion.z), new Vector3f(this.x, this.y, this.z), pos.copy().add(x, y, z), boundingBox);
                        if(blockSpeed.x != motion.x || blockSpeed.y != motion.y || blockSpeed.z != motion.z) {
                            return new Vector3f(0,0,0);
                        }
                       /* if(motion.x > 0) {
                            newSpeed.x = Math.min(newSpeed.x, blockSpeed.x);
                        } else {
                            newSpeed.x = Math.max(newSpeed.x, blockSpeed.x);
                        }
                        if(motion.y > 0) {
                            newSpeed.y = Math.min(newSpeed.y, blockSpeed.y);
                        } else {
                            newSpeed.y = Math.max(newSpeed.y, blockSpeed.y);
                        }
                        if(motion.z > 0) {
                            newSpeed.z = Math.min(newSpeed.z, blockSpeed.z);
                        } else {
                            newSpeed.z = Math.max(newSpeed.z, blockSpeed.z);
                        }

                        */

                    }
                }
            }
        }
        return motion;
    }


    static int iD = 0;

    public static int getNewId() {
        return iD++;
    }

    public static ArrayList<EntityFetcher> entities = new ArrayList<>();

    public static void register() {
        entities.add(PlayerEntity::new);
        entities.add(ItemEntity::new);
    }



}
