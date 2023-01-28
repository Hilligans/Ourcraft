package dev.hilligans.ourcraft.Client.Rendering.Graphics.Implementations;

import dev.hilligans.ourcraft.Block.Block;
import dev.hilligans.ourcraft.Client.MatrixStack;
import dev.hilligans.ourcraft.Client.Rendering.Graphics.API.ICamera;
import dev.hilligans.ourcraft.ClientMain;
import dev.hilligans.ourcraft.Network.Packet.Client.CUpdatePlayerPacket;
import dev.hilligans.ourcraft.Util.Ray;
import dev.hilligans.ourcraft.World.Chunk;
import dev.hilligans.ourcraft.World.World;
import org.joml.Matrix4d;
import org.joml.Vector2f;
import org.joml.Vector3d;
import org.joml.Vector3f;

public abstract class WorldCamera implements ICamera {

    public Vector3d pos = new Vector3d(1, Chunk.terrain + 5, 0);
   // public double x;
   // public double y = Chunk.terrain + 5;
  //  public double z;

    public float pitch;
    public float yaw;

    public float velX;
    public float velY;
    public float velZ;

    public World world;

    public float fov = 90;

    public Vector3d savedPosition;

    /**
     0 = normal camera
     1 = camera behind player
     2 = camera looking at player
     */
    public int thirdPersonMode = 0;
    public float cameraZoom = 0;

    @Override
    public void move(float x, float y, float z) {
        pos.add(x,y,z);
        ClientMain.getClient().sendPacket(new CUpdatePlayerPacket(pos.x,pos.y,pos.z,pitch,yaw,ClientMain.getClient().playerId));
    }

    @Override
    public void setPosition(double x, double y, double z) {
        pos.set(x,y,z);
    }

    @Override
    public Vector3d getPosition() {
        return pos.get(new Vector3d());
    }

    @Override
    public void setRotation(float pitch, float yaw) {
        this.pitch = pitch;
        this.yaw = yaw;
    }

    @Override
    public Vector2f getRotation() {
        return new Vector2f(pitch,yaw);
    }

    //TODO fix packet not being sent
    @Override
    public void addRotation(float pitch, float yaw) {
        this.pitch += pitch;
        this.yaw += yaw;

        if(this.pitch > 3.1415 / 2) {
            this.pitch = 3.1415f / 2;
        }

        if(this.pitch < - 3.1415 / 2) {
            this.pitch = -3.1415f / 2;
        }

        if(this.yaw > 6.283) {
            this.yaw = - 6.283f;
        } else if(this.yaw < -6.283) {
            this.yaw = 6.283f;
        }
    }

    @Override
    public float getPitch() {
        return pitch;
    }

    @Override
    public float getYaw() {
        return yaw;
    }

    @Override
    public void tick() {

    }

    @Override
    public float getSensitivity() {
        return 1;
    }

    @Override
    public void setMotion(float velX, float velY, float velZ) {
        this.velX = velX;
        this.velY = velY;
        this.velZ = velZ;
    }

    @Override
    public void addMotion(float velX, float velY, float velZ) {

    }

    @Override
    public Vector3f getMotion() {
        return new Vector3f(velX,velY,velZ);
    }

    public Vector3d getLookVector() {
        return new Vector3d((Math.cos(yaw) * Math.cos(pitch)), (Math.sin(pitch)), (Math.sin(yaw) * Math.cos(pitch)));
    }

    @Override
    public MatrixStack getMatrixStack(int W, int H, int x, int y) {
        Matrix4d perspective = getPerspective(W, H, x, y,  fov, getWindow().getAspectRatio(), 0.01f, 100000f);
        perspective.mul(getView());
        Vector3d cameraPos = getPosition();
        savePosition(cameraPos);
        if(thirdPersonMode == 2) {
            perspective.lookAt(cameraPos.add(getLookVector().negate()), getSavedPosition(), cameraUp());
        } else {
            perspective.lookAt(cameraPos.add(getLookVector()), getSavedPosition(), cameraUp());
        }
        perspective.translate(0,0.15f,0);
        return new MatrixStack(perspective);
    }

    @Override
    public Matrix4d getView() {
        Matrix4d view = new Matrix4d();
        if(thirdPersonMode != 0) {
            view.translate(0,0,1 + getViewLength() * -1);
        }
        return view;
    }

    public float getViewLength() {
        if(world == null) {
            return cameraZoom;
        }
        Ray ray = new Ray(pitch,yaw,0.1f);
        if(thirdPersonMode == 2) {
            ray.negate();
        }
        int x;
        for(x = 0; x < cameraZoom / 0.1; x++) {
            Block block = world.getBlockState(ray.getNextBlock(x).add(getCameraPos())).getBlock();
            if(!block.blockProperties.canWalkThrough) {
                x -= 1;
                break;
            }
        }
        return x * 0.1f;
    }


    @Override
    public void savePosition(Vector3d vector3d) {
        this.savedPosition = vector3d.get(new Vector3d());
    }

    @Override
    public Vector3d getSavedPosition() {
        return savedPosition;
    }
}
