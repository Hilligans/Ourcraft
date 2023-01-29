package dev.hilligans.ourcraft.Client.Rendering.Graphics.Implementations;

import dev.hilligans.ourcraft.Block.Block;
import dev.hilligans.ourcraft.Client.MatrixStack;
import dev.hilligans.ourcraft.Client.Rendering.Graphics.API.ICamera;
import dev.hilligans.ourcraft.ClientMain;
import dev.hilligans.ourcraft.Network.Packet.Client.CUpdatePlayerPacket;
import dev.hilligans.ourcraft.Util.Ray;
import dev.hilligans.ourcraft.World.Chunk;
import dev.hilligans.ourcraft.World.NewWorldSystem.IWorld;
import dev.hilligans.ourcraft.World.World;
import org.joml.*;

import java.lang.Math;

public abstract class WorldCamera implements ICamera {

    public Vector3d pos = new Vector3d(1, Chunk.terrain + 5, 0);
   // public double x;
   // public double y = Chunk.terrain + 5;
  //  public double z;

    public Vector3f gravityVector = new Vector3f(0, -1, 0);
    public Matrix3f moveMatrix = new Matrix3f();
    public Vector3f moveVector = new Vector3f();

    public float pitch;
    public float yaw;
    public float roll;
    public float roller;

    public double maxPitch = 3.1415 / 2;
    public double minPitch = -3.1415 / 2;
    public double maxYaw = 6.283;
    public double minYaw = -6.283;

    public int pitchSign = 1;


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
        moveVector.set(x, y, z).mul(moveMatrix);
        pos.add(moveVector);

        IWorld world1 = ClientMain.getClient().newClientWorld;

        Vector3f gravityVector = (Vector3f) world1.getGravityVector(pos.get(new Vector3f()).get(new Vector3f()));
        if(!this.gravityVector.get(new Vector3f()).mul(moveMatrix).equals(gravityVector)) {
            updateCameraForGravity(gravityVector, x);
        }

        ClientMain.getClient().sendPacket(new CUpdatePlayerPacket(pos.x,pos.y,pos.z,pitch,yaw,ClientMain.getClient().playerId));
    }

    public void updateCameraForGravity(Vector3fc newVector, float x) {
        int comp = newVector.x() != 0 ? 0 : newVector.y() != 0 ? 1 : 2;
        int sign = (newVector.get(comp) > 0 ? 1 : -1);

        int s = (comp + 1) * sign;
        moveMatrix = moveMatrices[s + 3];
        System.out.println(s + 3);

        pitch += x > 0 ? (3.1415f / 2f) : -(3.1415f / 2f);

        //TODO fix this somehow
        roll = (float) ((-Math.abs(yaw) * (x > 0 ? 1 : 0)));

        addRotation(0, 0);
    }

    public static Matrix3f[] moveMatrices = {
        null,
        new Matrix3f(1, 0, 0,
                     0, 1, 0,
                     0, 0, 1),
        new Matrix3f(0, -1, 0,
                    1, 0, 0,
                     0, 0, 1),
        null,
        new Matrix3f(0, 1, 0,
                      -1,0, 0,
                      0, 0, 1),
        new Matrix3f(-1,0, 0,
                    0, -1, 0,
                     0, 0, 1),
        null
    };



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
        this.yaw += yaw * 1;

        if(this.pitch > maxPitch) {
            this.pitch = (float) maxPitch;
        }

        if(this.pitch < minPitch) {
            this.pitch = (float) minPitch;
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
        if(Math.abs(roll) < 0.05) {
            roll = 0;
        } else
        if(roll != 0) {
            //roll -= 0.008 * (roll > 0 ? 1 : -1);
        }
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
        return new Vector3d((Math.cos(yaw) * Math.cos(pitch)), (Math.sin(pitch)), (Math.sin(yaw) * Math.cos(pitch))).mul(moveMatrix);
    }

    @Override
    public MatrixStack getMatrixStack(int W, int H, int x, int y) {
        Matrix4d perspective = getPerspective(W, H, x, y,  fov, getWindow().getAspectRatio(), 0.01f, 100000f);
        perspective.mul(getView());
        Vector3d cameraPos = getPosition();
        savePosition(cameraPos);
        if(thirdPersonMode == 2) {
            perspective.lookAt(getSavedPosition(), cameraPos.add(getLookVector().negate()), cameraUp());
        } else {
            perspective.lookAt(getSavedPosition(), cameraPos.add(getLookVector()), cameraUp());
        }
        perspective.translate(0,0.15f,0);
        return new MatrixStack(perspective);
    }

    public Vector3d cameraUp() {
        return new Vector3d(1, 1, 1).mul(Math.sin(roll), Math.cos(roll), 0).mul(moveMatrix);
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
