package dev.Hilligans.ourcraft.Entity;

import dev.Hilligans.ourcraft.ClientMain;
import dev.Hilligans.ourcraft.Block.Block;
import dev.Hilligans.ourcraft.Block.Blocks;
import dev.Hilligans.ourcraft.Client.MatrixStack;
import dev.Hilligans.ourcraft.Data.Other.BoundingBox;
import dev.Hilligans.ourcraft.Entity.Entities.ItemEntity;
import dev.Hilligans.ourcraft.Entity.LivingEntities.PlayerEntity;
import dev.Hilligans.ourcraft.Network.Packet.Server.SUpdateEntityPacket;
import dev.Hilligans.ourcraft.Network.PacketData;
import dev.Hilligans.ourcraft.Data.Other.BlockPos;
import dev.Hilligans.ourcraft.ServerMain;
import dev.Hilligans.ourcraft.World.World;
import dev.Hilligans.ourcraft.Util.Settings;
import org.joml.Vector2f;
import org.joml.Vector3d;
import org.joml.Vector3f;

import java.util.ArrayList;

public abstract class Entity {

    public float x,y,z,pitch,yaw,velX,velY,velZ;

    public Vector3d pos = new Vector3d();
    public Vector3d vel = new Vector3d();
    public Vector2f rot = new Vector2f();

    public int dimension = 0;

    public BoundingBox boundingBox;

    public int id;
    public int type = 0;

    float slowAmount = 0.9f;
    public boolean onGround = true;
    public World world;

    public Entity(int id) {
        this.id = id;
        x = 0;
        y = 0;
        z = 0;
        boundingBox = new BoundingBox(0,0,0,0,0,0);
        setWorld();
    }

    public Entity(float x, float y, float z, int id) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.id = id;
        boundingBox = new BoundingBox(0,0,0,0,0,0);
        setWorld();
    }

    public Entity(Vector3d pos, int id) {
        this.pos = pos;
        this.id = id;
        boundingBox = new BoundingBox(0,0,0,0,0,0);
        setWorld();
    }

    public Entity(PacketData packetData) {
        x = packetData.readFloat();
        y = packetData.readFloat();
        z = packetData.readFloat();
        pitch = packetData.readFloat();
        yaw = packetData.readFloat();
        id = packetData.readInt();
        boundingBox = new BoundingBox(0,0,0,0,0,0);
        setWorld();
    }

    private void setWorld() {
        if(Settings.isServer) {
            world = ServerMain.getWorld(dimension);
        } else {
            world = ClientMain.getClient().clientWorld;
        }
    }

    public Entity setPos(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    public Entity setPos(Vector3d vector3d) {
        this.x = (float) vector3d.x;
        this.y = (float) vector3d.y;
        this.z = (float) vector3d.z;
        this.pos = vector3d;
        return this;
    }

    public Entity setRot(float pitch, float yaw) {
       this.pitch = pitch;
       this.yaw = yaw;
       this.rot = new Vector2f(pitch,yaw);
        return this;
    }

    public Entity setRot(Vector2f rot) {
        this.rot = rot;
        return this;
    }

    public Entity setVel(float velX, float velY, float velZ) {
        this.vel = new Vector3d(velX,velY,velZ);
        return this;
    }

    public Entity setVel(Vector3d vel) {
        this.velX = (float) vel.x;
        this.velY = (float) vel.y;
        this.velZ = (float) vel.z;
        this.vel = vel;
        return this;
    }

    public World getWorld() {
        return world;
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
            int count = 4;
            for(int a = 0; a < count; a++) {
                move(velX / count, velY / count, velZ / count);
            }
            velX = velX * slowAmount;
            velY = velY * slowAmount;
            velZ = velZ * slowAmount;
            ServerMain.getServer().sendPacket(new SUpdateEntityPacket(this));
        }
    }

    private void move(float velX, float velY, float velZ) {
        boolean couldMove = false;
        int x;
        for(x = 0; x < 7; x++) {
            if(!getAllowedMovement(tryMovement(x,velX,velY,velZ),ServerMain.getWorld(dimension))) {
                continue;
            }
            couldMove = true;
            break;
        }
        onGround = false;
        if(!couldMove) {
            onGround = true;
            velX = 0;
            velY = 0;
            velZ = 0;
        }

        if (x == 3 || x == 5 || x == 6) {
            velX = 0;
        }
        if (x == 1 || x == 4 || x == 6) {
            onGround = true;
            velY = 0;
        }
        if (x == 2 || x == 4 || x == 5) {
            velZ = 0;
        }
        this.x += velX;
        this.y += velY;
        this.z += velZ;
    }

    private Vector3f tryMovement(int side, float velX, float velY, float velZ) {
        switch (side) {
            case 0:
                return new Vector3f(velX,velY,velZ);
            case 1:
                return new Vector3f(velX,0,velZ);
            case 2:
                return new Vector3f(velX,velY,0);
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



    public boolean getAllowedMovement(Vector3f motion, World world) {
        BlockPos pos = new BlockPos((int)Math.floor(x),(int)Math.floor(y),(int)Math.floor(z));
        int X = (int) Math.ceil(Math.abs(motion.x)) + 1;
        int Y = (int) Math.ceil(Math.abs(motion.y)) + 1;
        int Z = (int) Math.ceil(Math.abs(motion.z)) + 1;

        for(int x = -X; x < X; x++) {
            for(int y = -Y; y < Y; y++) {
                for(int z = -Z; z < Z; z++) {
                    Block block = world.getBlockState(pos.copy().add(x,y,z)).getBlock();
                    if(block != Blocks.AIR) {
                        boolean canMove = block.getAllowedMovement(new Vector3d(motion.x,motion.y,motion.z), new Vector3d(this.x, this.y, this.z), pos.copy().add(x, y, z), boundingBox, world);
                        if(!canMove) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
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
