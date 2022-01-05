package dev.Hilligans.ourcraft.Client.Rendering;

import dev.Hilligans.ourcraft.Block.Block;
import dev.Hilligans.ourcraft.Block.Blocks;
import dev.Hilligans.ourcraft.Client.Camera;
import dev.Hilligans.ourcraft.Client.Key.KeyHandler;
import dev.Hilligans.ourcraft.Client.MatrixStack;
import dev.Hilligans.ourcraft.ClientMain;
import dev.Hilligans.ourcraft.Data.Other.BlockPos;
import dev.Hilligans.ourcraft.Data.Other.BoundingBox;
import dev.Hilligans.ourcraft.Entity.LivingEntities.PlayerEntity;
import dev.Hilligans.ourcraft.Network.Packet.Client.CUpdatePlayerPacket;
import dev.Hilligans.ourcraft.Util.Ray;
import dev.Hilligans.ourcraft.World.Chunk;
import dev.Hilligans.ourcraft.World.World;
import org.joml.Matrix4d;
import org.joml.Vector3d;
import org.joml.Vector3i;
import org.lwjgl.BufferUtils;

import java.nio.DoubleBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_SHIFT;

public class PlayerCamera {

    public int f5;

    public Vector3d pos = new Vector3d(0, Chunk.terrain,0);

    public int fov = 70;

    private final double fallSpeed = -0.0008f;
    private final double terminalVel = -1.15f;

    public double pitch;
    public double yaw;

    public double moveSpeed = 0.02f;

    public boolean thirdPerson = false;
    public int thirdPersonMode = -1;

    public Vector3i playerChunkPos = null;

    public float thirdPersonScroll = 2.0f;


    public PlayerEntity playerEntity = new PlayerEntity(0,0,0,Integer.MIN_VALUE);

    public boolean isOnGround = false;
    public boolean hitBlock = false;

    public boolean sprinting = false;
    public int sprintTimeout = 1;
    public int sprintDelay = 0;

    public final float accelTime = 80f;

    public float sensitivity = 150;

    public BoundingBox playerBoundingBox = new BoundingBox(-0.35f,-1.9f,-0.35f,0.35f,0.0f,0.35f, -0.15f);

    public double velX;
    public double velY;
    public double velZ;

    public double newX = (float)ClientMain.getWindowX() / 2;
    public double newY = (float)ClientMain.getWindowY() / 2;

    Vector3d cameraUp = new Vector3d(0.0f, 1.0f, 0.0f);

    public float maxX,maxZ;


    public void addToThirdPerson(float amount) {
        if(amount > 0) {
            thirdPersonScroll = Math.min(16,thirdPersonScroll + amount);
        } else {
            thirdPersonScroll = Math.max(2.0f,thirdPersonScroll + amount);
        }
    }

    public void toggleThirdPerson() {
        if(thirdPerson) {
            if (thirdPersonMode == 1) {
                thirdPersonMode = -1;
                thirdPerson = false;
            } else {
                thirdPersonMode = 1;
            }
        } else {
            thirdPerson = true;
        }
    }

    public void add(double x, double y, double z) {

        if(ClientMain.getClient().playerData.spectator) {
            pos.add(x * 4, y * 4, z * 4);
            ClientMain.getClient().sendPacket(new CUpdatePlayerPacket(pos.x,pos.y,pos.z,(float)pitch,(float)yaw,ClientMain.getClient().playerId));
        } else {

            maxX += x;
            maxZ += z;

            if(isOnGround) {
                velX += x / 4;
                velZ += z / 4;
            } else {
                velX += x / accelTime;
                velZ += z / accelTime;
            }
            velY += y;

        }
    }

    public void tick() {
        if(maxX != 0) {
            if (maxX > 0) {
                if (velX > maxX) {
                    velX = maxX;
                }
            } else {
                if (velX < maxX) {
                    velX = maxX;
                }
            }
        }
        if(maxZ != 0) {
            if (maxZ > 0) {
                if (velZ > maxZ) {
                    velZ = maxZ;
                }
            } else {
                if (velZ < maxZ) {
                    velZ = maxZ;
                }
            }
        }
        if(ClientMain.getClient().clientWorld.getChunk((int)pos.x >> 4, (int)pos.z >> 4) != null) {

            if (!ClientMain.getClient().playerData.spectator && !ClientMain.getClient().playerData.flying) {
                velY += fallSpeed;
                if (velY < terminalVel) {
                    velY = terminalVel;
                }
            }

            move();

            if (isOnGround) {
                velY = 0;
                if(maxX == 0) {
                    velX = velX * 0.95f;
                }
                if(maxZ == 0) {
                    velZ = velZ * 0.95f;
                }
            }


            velX = velX * 0.995f;
            velZ = velZ * 0.995f;

            if(hitBlock) {
                velY = 0;
                hitBlock = false;
            }


            maxX = 0;
            maxZ = 0;

            ClientMain.getClient().renderTime++;
        }
    }

    public void addPitch(double amount) {
        pitch += amount;
        if(pitch > 3.1415 / 2) {
            pitch = 3.1415 / 2;
        }

        if(pitch < - 3.1415 / 2) {
            pitch = -3.1415 / 2;
        }


        if(yaw > 6.283) {
            yaw = - 6.283;
        } else if(yaw < -6.283) {
            yaw = 6.283;
        }

        ClientMain.getClient().sendPacket(new CUpdatePlayerPacket(pos.x,pos.y,pos.z,(float)pitch,(float)yaw,ClientMain.getClient().playerId));

    }

    public void addYaw(double amount) {
        yaw += amount;
        ClientMain.getClient().sendPacket(new CUpdatePlayerPacket(pos.x,pos.y,pos.z,(float)pitch,(float)yaw,ClientMain.getClient().playerId));
    }

    public Vector3d duplicate() {
        return new Vector3d(Camera.pos.x,Camera.pos.y,Camera.pos.z);
    }

    public Vector3d duplicateAndAssign() {
        playerChunkPos = new Vector3i((int)Camera.pos.x >> 4, 0, (int)Camera.pos.z >> 4);
        renderPos = duplicate();
        return new Vector3d(Camera.pos.x - (playerChunkPos.x << 4), Camera.pos.y, Camera.pos.z - (playerChunkPos.z << 4));
    }

    public Vector3d renderPos = new Vector3d();

    public Vector3d duplicateExtra() {
        return new Vector3d(Camera.pos.x - (playerChunkPos.x << 4), Camera.pos.y, Camera.pos.z - (playerChunkPos.z << 4));
    }

    public void applyTransformations(Matrix4d matrix4d) {
        Matrix4d projection = new Matrix4d();
        Matrix4d view = new Matrix4d();
        view.translate(0,0,1);
        if(thirdPerson) {
            view.translate(0,0,-thirdPersonScroll);
        }
        projection.perspective((float) Math.toRadians(fov), (float) ClientMain.getWindowX() / ClientMain.getWindowY(),0.1f,1000000.0f);
        matrix4d.mul(projection).mul(view);
        matrix4d.lookAt(Camera.duplicate().add((float)(Math.cos(Camera.yaw) * Math.cos(Camera.pitch)),(float)(Math.sin(Camera.pitch)),(float)(Math.sin(Camera.yaw) * Math.cos(Camera.pitch))),Camera.duplicate(), cameraUp);
    }

    public MatrixStack getWorldStack() {
        Matrix4d matrix4d = getPerspective();
        return applyWorldStack(matrix4d);
    }

    public MatrixStack getWorldStack(int W, int H, int x, int y) {
        Matrix4d matrix4d = getPerspective(W,H,x,y);
        return applyWorldStack(matrix4d);
    }

    private MatrixStack applyWorldStack(Matrix4d matrix4d) {
        Matrix4d view = getViewStack();
        matrix4d.mul(view);
        if(thirdPerson && thirdPersonMode == 1) {
            matrix4d.lookAt(Camera.duplicateAndAssign().add(getLookVector().negate()), Camera.duplicateExtra(), cameraUp);
        } else {
            matrix4d.lookAt(Camera.duplicateAndAssign().add(getLookVector()), Camera.duplicateExtra(), cameraUp);
        }
        matrix4d.translate(0,0.15f,0);
        if(KeyHandler.keyPressed[GLFW_KEY_LEFT_SHIFT]) {
            matrix4d.translate(0,0.05f,0);
        }
        return new MatrixStack(matrix4d);
    }

    public Vector3d getLookVector() {
        return new Vector3d((Math.cos(Camera.yaw) * Math.cos(Camera.pitch)), (Math.sin(Camera.pitch)), (Math.sin(Camera.yaw) * Math.cos(Camera.pitch)));
    }

    public Matrix4d getViewStack() {
        Matrix4d view = new Matrix4d();
        view.translate(0,0, Math.abs(thirdPersonMode));
        if(thirdPerson) {
            view.translate(0,0,getViewLength() * -1);
        }
        return view;
    }

    public float getViewLength() {
        Ray ray = new Ray(pitch,yaw,0.1f);
        if(thirdPerson && thirdPersonMode == -1) {
            ray.negate();
        }
        int x;
        for(x = 0; x < thirdPersonScroll / 0.1; x++) {
            Block block = ClientMain.getClient().clientWorld.getBlockState(ray.getNextBlock(x).add(pos)).getBlock();
            if(!block.blockProperties.canWalkThrough) {
                x -= 1;
                break;
            }
        }
        return x * 0.1f;
    }

    public Matrix4d getPerspective() {
        return new Matrix4d().perspective((float) Math.toRadians(fov), (float) ClientMain.getWindowX() / ClientMain.getWindowY(),0.1f,10000.0f);
    }

    public Matrix4d getPerspective(int W, int H, int x, int y) {
        return new Matrix4d() .translate(W - 1 - 2*x, H - 1 - 2*y, 0).scale(W, H, 1).perspective((float) Math.toRadians(fov), (float) ClientMain.getWindowX() / ClientMain.getWindowY(),0.1f,10000.0f);
    }

    public MatrixStack getScreenStack() {
        Matrix4d matrix4d = new Matrix4d();
        matrix4d.ortho(0,ClientMain.getWindowX(),ClientMain.getWindowY(),0,-1,20000);
        return new MatrixStack(matrix4d);
    }


    public void updateMouse() {
        DoubleBuffer x = BufferUtils.createDoubleBuffer(1);
        DoubleBuffer y = BufferUtils.createDoubleBuffer(1);

        glfwGetCursorPos(ClientMain.getClient().window, x, y);

        newX = x.get();
        newY = y.get();

        if(ClientMain.getClient().mouseLocked) {
            double halfWindowX = (double) ClientMain.getWindowX() / 2;
            double halfWindowY = (double) ClientMain.getWindowY() / 2;

            double deltaX = newX - halfWindowX;
            double deltaY = newY - halfWindowY;

            Camera.addPitch(deltaY / sensitivity);
            Camera.addYaw(deltaX / sensitivity);


            glfwSetCursorPos(ClientMain.getClient().window, halfWindowX, halfWindowY);
        } else {

        }
    }

    public void renderPlayer(MatrixStack matrixStack) {
        if(thirdPerson) {
            playerEntity.setPos(pos);
            playerEntity.setRot((float) pitch, (float) yaw);
            playerEntity.render(matrixStack);
        }
    }

    public String getString() {
        return "x:" + pos.x + " y:" + pos.y + " z:" + pos.z;
    }

    public void move() {

        if (velX != 0 || velY != 0 || velZ != 0) {
            float count = 4;
            for (int a = 0; a < count; a++) {
                move(velX / count, velY / count, velZ / count);
            }
            ClientMain.getClient().sendPacket(new CUpdatePlayerPacket(pos.x, pos.y, pos.z, (float) pitch, (float) yaw, ClientMain.getClient().playerId));

        }
    }

    private void move(double velX, double velY, double velZ) {
        int x;
        boolean couldMove = false;
        for(x = 0; x < 7; x++) {
            boolean movement = getAllowedMovement(tryMovement(x,velX,velY,velZ),pos, ClientMain.getClient().clientWorld);

            if(!movement  && isOnGround && (x == 1 || x == 4 || x == 6)) {
                if(getAllowedMovement(tryMovement(x,velX,0,velZ),new Vector3d(pos.x,pos.y + 0.5f, pos.z), ClientMain.getClient().clientWorld)) {
                    pos.y += 0.5f;
                    addVel(x, velX, 0, velZ);
                    return;
                }
            }
            if(movement) {
                movement = canMove(tryMovement(x,velX,velY,velZ), Camera.pos);
                if(!movement) {
                    movement = canMove(tryMovement(x,velX,velY,velZ), new Vector3d(Camera.pos.x,Camera.pos.y - 0.5f, Camera.pos.z));
                }
            }

            if(!movement) {
                continue;
            }
            couldMove = true;
            break;
        }

        isOnGround = false;
        if(!couldMove) {
            hitBlock = true;
            if(velY < 0) {
                isOnGround = true;
            }
            velX = 0;
            velY = 0;
            velZ = 0;
        }
        addVel(x,velX,velY,velZ);

    }

    private void addVel(int x, double velX, double velY, double velZ) {
        if (x == 3 || x == 5 || x == 6) {
            velX = 0;
        }
        if (x == 1 || x == 4 || x == 6) {
            hitBlock = true;
            if(velY < 0) {
                isOnGround = true;
            }
            velY = 0;
        }
        if (x == 2 || x == 4 || x == 5) {
            velZ = 0;
        }
        pos.x += velX;
        pos.y += velY;
        pos.z += velZ;
    }

    private Vector3d tryMovement(int side, double velX, double velY, double velZ) {
        switch (side) {
            case 0:
                return new Vector3d(velX,velY,velZ);
            case 1:
                return new Vector3d(velX,0,velZ);
            case 2:
                sprintTimeout = 0;
                sprintDelay = 30;
                return new Vector3d(velX,velY,0);
            case 3:
                return new Vector3d(0,velY,velZ);
            case 4:
                return new Vector3d(velX,0,0);
            case 5:
                return new Vector3d(0,velY,0);
            case 6:
                return new Vector3d(0,0,velZ);
            default:
                return new Vector3d();
        }
    }

    public boolean getAllowedMovement(Vector3d motion, Vector3d cameraPos, World world) {
        BlockPos pos = new BlockPos((int)Math.floor(cameraPos.x),(int)Math.floor(cameraPos.y),(int)Math.floor(cameraPos.z));
        int X = (int) Math.ceil(Math.abs(motion.x)) + 2;
        int Y = (int) Math.ceil(Math.abs(motion.y)) + 4;
        int Z = (int) Math.ceil(Math.abs(motion.z)) + 2;

        for(int x = -X; x < X; x++) {
            for(int y = -Y; y < Y; y++) {
                for(int z = -Z; z < Z; z++) {
                    Block block = world.getBlockState(pos.copy().add(x,y,z)).getBlock();
                    if(block != Blocks.AIR) {
                        boolean canMove = block.getAllowedMovement(new Vector3d(motion.x,motion.y,motion.z), new Vector3d(cameraPos.x, cameraPos.y, cameraPos.z), pos.copy().add(x, y, z), playerBoundingBox, world);
                        if(!canMove) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    private boolean canMove(Vector3d motion, Vector3d pos) {
        if(KeyHandler.keyPressed[GLFW_KEY_LEFT_SHIFT]) {
            if(isOnGround) {
                Vector3d newPos = new Vector3d(pos.x,pos.y,pos.z).add(motion);
                return !getAllowedMovement(new Vector3d(0,fallSpeed * 2,0), newPos, ClientMain.getClient().clientWorld);
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

}
