package dev.Hilligans.ourcraft.Client.Rendering.Graphics.Implementations;

import dev.Hilligans.ourcraft.Block.Block;
import dev.Hilligans.ourcraft.Client.Camera;
import dev.Hilligans.ourcraft.Client.MatrixStack;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.API.ICamera;
import dev.Hilligans.ourcraft.ClientMain;
import dev.Hilligans.ourcraft.Util.Ray;
import dev.Hilligans.ourcraft.World.World;
import org.joml.Matrix4d;
import org.joml.Vector2f;
import org.joml.Vector3d;
import org.joml.Vector3f;

public abstract class WorldCamera implements ICamera {

    public double x;
    public double y;
    public double z;

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
        this.x += x;
        this.y += y;
        this.z += z;
    }

    @Override
    public void setPosition(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public Vector3d getPosition() {
        return new Vector3d(x,y,z);
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
        return new Vector3d((Math.cos(Camera.yaw) * Math.cos(Camera.pitch)), (Math.sin(Camera.pitch)), (Math.sin(Camera.yaw) * Math.cos(Camera.pitch)));
    }

    @Override
    public MatrixStack getMatrixStack(int W, int H, int x, int y) {
        Matrix4d perspective = getPerspective(W, H, x, y, fov, getWindow().getAspectRatio(), 0.001f, 1000000000f);
        perspective.mul(getView());
        Vector3d cameraPos = getLookVector();
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
        this.savedPosition = vector3d;
    }

    @Override
    public Vector3d getSavedPosition() {
        return savedPosition;
    }
}
