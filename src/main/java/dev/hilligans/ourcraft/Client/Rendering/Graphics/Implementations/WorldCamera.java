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
    public Quaternionf f = new Quaternionf();

    public Vector3f forwardVector = new Vector3f(1,0,0);
    public Vector3f leftVector = new Vector3f(0, 0, 1);

    public float pitch;
    public float yaw;
    public float roll;
    public float roller;

    public float maxPitch = 3.1415f / 2;
    public float minPitch = -3.1415f / 2;
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
            updateCameraForGravity(gravityVector);
        }

        ClientMain.getClient().sendPacket(new CUpdatePlayerPacket(pos.x,pos.y,pos.z,pitch,yaw,ClientMain.getClient().playerId));
    }

    public void updateCameraForGravity(Vector3fc newVector) {
        Vector3f grav = gravityVector.get(new Vector3f()).mul(moveMatrix);

        int comp = newVector.x() != 0 ? 0 : newVector.y() != 0 ? 1 : 2;
        int oldComp = grav.x() != 0 ? 0 : grav.y() != 0 ? 1 : 2;
        int sign = (newVector.get(comp) > 0 ? 1 : -1);
        int oldSign = (grav.get(oldComp) > 0 ? 1 : -1);

        int ss = sign * oldSign * (1 > 0 ? -1 : 1);
        int s = (comp + 1) * sign;
        //moveMatrix = moveMatrices[s + 3];


        float direction = ((float) (Math.PI/2) * getDirection((comp + 1) * sign, (oldComp + 1) * oldSign));
        //System.out.println(getDirection((comp + 1) * sign, (oldComp + 1) * oldSign));

        //System.out.println(direction);
        //new Matrix3f()
        Vector3f rotVector = new Vector3f(1, 1, 1);
        rotVector.setComponent(comp, 0);
        rotVector.setComponent(oldComp, 0);

        //gravityVector.rotateAxis(direction, rotVector.x, rotVector.y, rotVector.z);
        //forwardVector.rotateAxis(direction, rotVector.x, rotVector.y, rotVector.z);
        //leftVector.rotateAxis(direction, rotVector.x, rotVector.y, rotVector.z);

        if(comp == 0) {
            if(oldComp == 1) {
                //gravityVector.rotateZ(direction);
                //moveMatrix.rotateZ(direction);
                f.rotateLocalZ(direction);
            } else {
                //gravityVector.rotateY(direction);
                //moveMatrix.rotateY(direction);
                f.rotateLocalY(direction);
            }
        } else if (comp == 1) {
            if(oldComp == 0) {
                //gravityVector.rotateZ(direction);
                //moveMatrix.rotateZ(direction);
                f.rotateLocalZ(direction);
            } else {
                //gravityVector.rotateX(direction);
                //moveMatrix.rotateX(direction);
                f.rotateLocalX(direction);
            }
        } else {
            if(oldComp == 0) {
                //gravityVector.rotateY(direction);
                //moveMatrix.rotateY(direction);
                f.rotateLocalY(direction);
            } else {
                //gravityVector.rotateX(direction);
                //moveMatrix.rotateX(direction);
                f.rotateLocalX(direction);
            }
        }
        moveMatrix = new Matrix3f().rotate(f);
        //System.out.println(moveMatrix);
        for(int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                moveMatrix.set(x, y, Math.round(moveMatrix.get(x, y)));
            }
        }
        //System.out.println(moveMatrix);
        gravityVector.round();
        forwardVector.round();
        leftVector.round();

        //System.out.println(gravityVector.get(new Vector3f()).mul(moveMatrix));
        //System.out.println(newVector);

       // moveMatrix.rotateZ((float) -(Math.PI/2));

   //     Vector3f column1 = moveMatrix.getRow(comp, new Vector3f());
   //     Vector3f column2 = moveMatrix.getRow(oldComp, new Vector3f());

   //     moveMatrix.setRow(oldComp, column1.mul(ss, ss, ss));
   //     moveMatrix.setRow(comp, column2.mul(-ss, -ss, -ss));

      //  pitch += (Math.cos(yaw) > 0 ? 1f : -1f) * -direction;

        //pitch += -direction;
        //pitch += x > 0 ? (3.1415f / 2f) : -(3.1415f / 2f);
        //roll = (float) ((-Math.abs(yaw) * (x > 0 ? 1 : 0)));

        addRotation(0, 0);
    }

    public static Matrix3f[] moveMatrices = {
        new Matrix3f(1, 0, 0,
                    0, 1, 0,
                    0, 0, 1),

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

       new Matrix3f(1, 0, 0,
                    0, 1, 0,
                    0, 0, 1),
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
        this.yaw += yaw;

        if(this.pitch > maxPitch) {
            this.pitch = maxPitch;
        }

        if(this.pitch < minPitch) {
            this.pitch = minPitch;
        }

        //if(this.yaw > 6.283) {
        //    this.yaw -= 6.283f * 2;
        //} else if(this.yaw < -6.283) {
        //    this.yaw += 6.283f * 2;
        //}
        this.yaw = (this.yaw + 6.283f) % 6.283f;
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


    /**
     * x = 1
     * y = 2
     * z = 3
     */
    public static int getDirection(int current, int old) {
        String code = current + "" + old;
        return switch (code) {
            case "-1-2", "2-1", "12", "-21",    "3-2", "23", "-32", "-2-3",   "-13", "-3-1", "1-3", "31" -> -1;
            default -> 1;
        };
    }

}
