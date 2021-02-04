package Hilligans.Entity;

import Hilligans.Blocks.Block;
import Hilligans.Blocks.Blocks;
import Hilligans.Client.MatrixStack;
import Hilligans.Data.Other.BoundingBox;
import Hilligans.Entity.Entities.ItemEntity;
import Hilligans.Entity.LivingEntities.PlayerEntity;
import Hilligans.Network.Packet.Server.SUpdateEntityPacket;
import Hilligans.Network.PacketData;
import Hilligans.Network.ServerNetworkHandler;
import Hilligans.ServerMain;
import Hilligans.World.BlockPos;
import Hilligans.World.World;
import org.joml.Matrix4f;
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
            Vector3f movement = getAllowedMovement(new Vector3f(velX, velY, velZ), ServerMain.world);

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



    public Vector3f getAllowedMovement(Vector3f motion, World world) {
        Vector3f newSpeed = new Vector3f(motion);
        BlockPos pos = new BlockPos(Math.round(x),Math.round(y),Math.round(z));
        //System.out.println((int)Math.ceil(motion.x));
        for(int x = 0; x < (motion.x > 0 ? Math.ceil(motion.x) : Math.floor(motion.x)); x++) {
            for(int y = 0; y < (motion.y > 0 ? Math.ceil(motion.y) : Math.floor(motion.y)); y++) {
                for(int z = 0; z < (motion.z > 0 ? Math.ceil(motion.z) : Math.floor(motion.z)); z++) {
                    Block block = world.getBlockState(pos.copy().add(x,y,z)).block;
                    if(block != Blocks.AIR) {
                     //   System.out.println(block.name);
                        Vector3f blockSpeed = block.getAllowedMovement(motion, new Vector3f(this.x, this.y, this.z), pos.copy().add(x, y, z), boundingBox);
                        if (blockSpeed.x != motion.x) {
                            if (motion.x > 0) {
                                newSpeed.x = Math.min(motion.x, blockSpeed.x);
                            } else {
                                newSpeed.x = Math.max(motion.x, blockSpeed.x);
                            }
                        }
                        if (blockSpeed.y != motion.y) {
                            if (motion.y > 0) {
                                newSpeed.y = Math.min(motion.y, blockSpeed.y);
                            } else {
                                newSpeed.y = Math.max(motion.y, blockSpeed.y);
                            }
                        }
                        if (blockSpeed.z != motion.z) {
                            if (motion.z > 0) {
                                newSpeed.z = Math.min(motion.z, blockSpeed.z);
                            } else {
                                newSpeed.z = Math.max(motion.z, blockSpeed.z);
                            }
                        }
                    }
                }
            }
        }
        return newSpeed;
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
