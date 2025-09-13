package dev.hilligans.ourcraft.entity;

import dev.hilligans.ourcraft.ServerMain;
import dev.hilligans.ourcraft.block.Block;
import dev.hilligans.ourcraft.block.Blocks;
import dev.hilligans.engine.client.graphics.MatrixStack;
import dev.hilligans.ourcraft.data.other.BlockPos;
import dev.hilligans.engine.data.BoundingBox;
import dev.hilligans.engine.network.IPacketByteArray;
import dev.hilligans.ourcraft.util.EntityPosition;
import dev.hilligans.ourcraft.world.newworldsystem.IWorld;
import org.joml.Vector2f;
import org.joml.Vector3d;
import org.joml.Vector3f;

public abstract class Entity {

    public float pitch,yaw,velX,velY,velZ;

    public EntityPosition position;

    public Vector3d vel = new Vector3d();
    public Vector2f rot = new Vector2f();

    public int dimension = 0;

    public BoundingBox boundingBox;

    public int id;
    public int type = 0;

    float slowAmount = 0.9f;
    public boolean onGround = true;
    public IWorld world;

    public Entity(int id) {
        this.id = id;
        position = new EntityPosition();
        boundingBox = new BoundingBox(0,0,0,0,0,0);
        setWorld();
    }

    public Entity(double x, double y, double z, int id) {
        position = new EntityPosition(x,y,z);
        this.id = id;
        boundingBox = new BoundingBox(0,0,0,0,0,0);
        setWorld();
    }

    public Entity(Vector3d pos, int id) {
        position = new EntityPosition(pos.x,pos.y,pos.z);
        this.id = id;
        boundingBox = new BoundingBox(0,0,0,0,0,0);
        setWorld();
    }

    public Entity(IPacketByteArray packetData) {
        position = new EntityPosition(packetData);
        pitch = packetData.readFloat();
        yaw = packetData.readFloat();
        id = packetData.readInt();
        boundingBox = new BoundingBox(0,0,0,0,0,0);
        setWorld();
    }

    public Entity setWorld(IWorld world) {
        this.world = world;
        return this;
    }

    private void setWorld() {
        //if(Settings.isServer) {
        //    world = ServerMain.getWorld(dimension);
        //} else {
        //    world = ClientMain.getClient().clientWorld;
        //}
    }

    public Entity setPos(double x, double y, double z) {
        position.set(x,y,z);
        return this;
    }

    public Entity setPos(Vector3d vector3d) {
        position.set(vector3d.x, vector3d.y, vector3d.z);
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

    public IWorld getWorld() {
        return world;
    }

    public void writeData(IPacketByteArray packetData) {
        packetData.writeInt(type);
        position.write(packetData);
        packetData.writeFloat(pitch);
        packetData.writeFloat(yaw);
        packetData.writeInt(id);
    }

    public void render(MatrixStack matrixStack) {}

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
            //ServerMain.getServer().sendPacket(new SUpdateEntityPacket(this));
        }
    }

    private void move(float velX, float velY, float velZ) {
        boolean couldMove = false;
        int x;
        for(x = 0; x < 7; x++) {
            if(!getAllowedMovement(tryMovement(x,velX,velY,velZ),ServerMain.getServer().getWorld(null))) {
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
        position.add(velX,velY,velZ);
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


    public boolean getAllowedMovement(Vector3f motion, IWorld world) {
        BlockPos pos = new BlockPos(position);
        int X = (int) Math.ceil(Math.abs(motion.x)) + 1;
        int Y = (int) Math.ceil(Math.abs(motion.y)) + 1;
        int Z = (int) Math.ceil(Math.abs(motion.z)) + 1;

        for(int x = -X; x < X; x++) {
            for(int y = -Y; y < Y; y++) {
                for(int z = -Z; z < Z; z++) {
                    Block block = world.getBlockState(pos.copy().add(x,y,z)).getBlock();
                    if(block != Blocks.AIR) {
                        boolean canMove = block.getAllowedMovement(new Vector3d(motion.x,motion.y,motion.z), new Vector3d(position.getX(), position.getY(), position.getZ()), pos.copy().add(x, y, z), boundingBox, world);
                        if(!canMove) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    public double getX() {
        return position.getX();
    }

    public double getY() {
        return position.getY();
    }

    public double getZ() {
        return position.getZ();
    }

    static int iD = 0;

    public static int getNewId() {
        return iD++;
    }

}
