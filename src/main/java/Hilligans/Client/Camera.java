package Hilligans.Client;

import Hilligans.Block.Block;
import Hilligans.Block.Blocks;
import Hilligans.Client.Key.KeyHandler;
import Hilligans.Client.Key.KeyPress;
import Hilligans.ClientMain;
import Hilligans.Data.Other.BlockPos;
import Hilligans.Data.Other.BoundingBox;
import Hilligans.Network.ClientNetworkHandler;
import Hilligans.Network.Packet.Client.CUpdatePlayerPacket;
import Hilligans.Network.Packet.Server.SUpdateEntityPacket;
import Hilligans.Network.ServerNetworkHandler;
import Hilligans.ServerMain;
import Hilligans.World.Chunk;
import Hilligans.World.World;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import java.nio.DoubleBuffer;

import static Hilligans.ClientMain.windowX;
import static Hilligans.ClientMain.windowY;
import static org.lwjgl.glfw.GLFW.*;

public class Camera {

    public static Vector3f pos = new Vector3f(0, Chunk.terrain,0);

    public static int fov = 70;

    private static final float fallSpeed = -0.0008f;
    private static final float terminalVel = -0.15f;

    public static double pitch;
    public static double yaw;

    public static float moveSpeed = 0.02f;

    public static boolean thirdPerson = false;

    public static boolean spectator = false;

    public static boolean isFlying = false;

    public static boolean isOnGround = false;

    public static boolean sprinting = false;
    public static int sprintTimeout = 1;
    public static int sprintDelay = 0;

    public static float sensitivity = 150;

    public static BoundingBox playerBoundingBox = new BoundingBox(-0.35f,-1.9f,-0.35f,0.35f,0.0f,0.35f);

    static {
        KeyHandler.register(new KeyPress() {
            @Override
            public void onPress() {
                thirdPerson = !thirdPerson;
            }
        },KeyHandler.GLFW_KEY_F5);
    }

    public static void moveForeWard() {
        if(KeyHandler.keyPressed[GLFW_KEY_LEFT_SHIFT]) {
            add(-(float) Math.cos(yaw) * moveSpeed / 3, 0, -(float) Math.sin(yaw) * moveSpeed / 3);
        } else {
            if (sprinting) {
                sprintTimeout = 1;
                add(-(float) Math.cos(yaw) * moveSpeed * 1.7f, 0, -(float) Math.sin(yaw) * moveSpeed * 1.7f);
            } else {
                add(-(float) Math.cos(yaw) * moveSpeed, 0, -(float) Math.sin(yaw) * moveSpeed);
            }
        }
    }

    public static void moveBackWard() {
        if (KeyHandler.keyPressed[GLFW_KEY_LEFT_SHIFT]) {
            add((float)Math.cos(yaw) * moveSpeed / 3,0, (float)Math.sin(yaw) * moveSpeed / 3);
        } else {
            add((float)Math.cos(yaw) * moveSpeed,0, (float)Math.sin(yaw) * moveSpeed);
        }
    }

    public static void strafeLeft() {
        if (KeyHandler.keyPressed[GLFW_KEY_LEFT_SHIFT]) {
            add(-(float) Math.sin(yaw) * moveSpeed / 3, 0, (float) Math.cos(yaw) * moveSpeed / 3);
        } else {
            add(-(float) Math.sin(yaw) * moveSpeed, 0, (float) Math.cos(yaw) * moveSpeed);
        }
    }

    public static void strafeRight() {
        if (KeyHandler.keyPressed[GLFW_KEY_LEFT_SHIFT]) {
            add((float) Math.sin(yaw) * moveSpeed / 3, 0, -(float) Math.cos(yaw) * moveSpeed / 3);
        } else {
            add((float) Math.sin(yaw) * moveSpeed, 0, -(float) Math.cos(yaw) * moveSpeed);
        }
    }

    public static void moveUp() {
        if(spectator || isFlying) {
            add(0, moveSpeed, 0);
        } else {
            if(velY == 0) {
                isOnGround = false;
                add(0, 0.045f, 0);
            }
        }
    }

    public static void moveDown() {
        if(spectator || isFlying) {
            add(0, -moveSpeed, 0);
        }
    }

    public static float maxX,maxZ;

    public static void add(float x, float y, float z) {

        if(spectator) {
            pos.add(x, y, z);
            ClientNetworkHandler.sendPacket(new CUpdatePlayerPacket(pos.x,pos.y,pos.z,(float)pitch,(float)yaw,ClientMain.playerId));
        } else {
            maxX += x;
            maxZ += z;

            //System.out.println(x + " : " + z);
            velX += x;
            //velX += x;
            velY += y;
            velZ += z;

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

            //velZ += z;
        }
    }

    public static void tick() {
        //velX = velX / 2;
        //velY = velY / 2;
        //velZ = velZ / 2;
        if(ClientMain.clientWorld.getChunk((int)pos.x >> 4, (int)pos.z >> 4) != null) {

            if (!spectator && !isFlying) {
                velY += fallSpeed;
                if (velY < terminalVel) {
                    velY = terminalVel;
                }
            }

            move();

            if (isOnGround) {
                velY = 0;
                velX = velX * 0.8f;
                velZ = velZ * 0.8f;
            }


            velX = velX * 0.995f;
            velZ = velZ * 0.995f;


            maxX = 0;
            maxZ = 0;
        }
    }

    public static void addPitch(double amount) {
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

        ClientNetworkHandler.sendPacket(new CUpdatePlayerPacket(pos.x,pos.y,pos.z,(float)pitch,(float)yaw,ClientMain.playerId));

    }

    public static void addYaw(double amount) {
        yaw += amount;
        ClientNetworkHandler.sendPacket(new CUpdatePlayerPacket(pos.x,pos.y,pos.z,(float)pitch,(float)yaw,ClientMain.playerId));
    }

    public static Vector3f duplicate() {
        return new Vector3f(Camera.pos.x,Camera.pos.y,Camera.pos.z);
    }

    static Vector3f cameraUp    = new Vector3f(0.0f, 1.0f, 0.0f);

    public static void applyTransformations(Matrix4f matrix4f) {
        Matrix4f projection = new Matrix4f();
        Matrix4f view = new Matrix4f();
        view.translate(0,0,1);
        if(thirdPerson) {
              view.translate(0,0,-3);
        }
        projection.perspective((float) Math.toRadians(fov), (float) windowX / windowY,0.1f,10000.0f);
        matrix4f.mul(projection).mul(view);
        matrix4f.lookAt(Camera.duplicate().add((float)(Math.cos(Camera.yaw) * Math.cos(Camera.pitch)),(float)(Math.sin(Camera.pitch)),(float)(Math.sin(Camera.yaw) * Math.cos(Camera.pitch))),Camera.duplicate(), cameraUp);
    }

    public static MatrixStack getWorldStack() {
        Matrix4f matrix4f = new Matrix4f();
        Matrix4f view = new Matrix4f();
        view.translate(0,0,1);
        if(thirdPerson) {
            view.translate(0,0,-3);
        }
        matrix4f.perspective((float) Math.toRadians(fov), (float) windowX / windowY,0.1f,10000.0f);
        matrix4f.mul(view);
        matrix4f.lookAt(Camera.duplicate().add((float)(Math.cos(Camera.yaw) * Math.cos(Camera.pitch)),(float)(Math.sin(Camera.pitch)),(float)(Math.sin(Camera.yaw) * Math.cos(Camera.pitch))),Camera.duplicate(), cameraUp);
        matrix4f.translate(0,0.15f,0);
        if(KeyHandler.keyPressed[GLFW_KEY_LEFT_SHIFT]) {
            matrix4f.translate(0,0.05f,0);
        }
        return new MatrixStack(matrix4f);
    }

    public static MatrixStack getScreenStack() {
        Matrix4f matrix4f = new Matrix4f();
        matrix4f.ortho(0,ClientMain.windowX,ClientMain.windowY,0,-1,1);
        return new MatrixStack(matrix4f);
    }

    static double newX = (float)windowX / 2;
    static double newY = (float)windowY / 2;


    public static void updateMouse() {
        DoubleBuffer x = BufferUtils.createDoubleBuffer(1);
        DoubleBuffer y = BufferUtils.createDoubleBuffer(1);

        glfwGetCursorPos(ClientMain.window, x, y);

        newX = x.get();
        newY = y.get();

        double halfWindowX = (double) windowX / 2;
        double halfWindowY = (double) windowY / 2;

        double deltaX = newX - halfWindowX;
        double deltaY = newY - halfWindowY;

        Camera.addPitch(deltaY / sensitivity);
        Camera.addYaw(deltaX / sensitivity);


        glfwSetCursorPos(ClientMain.window, halfWindowX, halfWindowY);
    }


    public static float sin(double angle) {
        return (float) Math.sin(angle);
    }

    public static float cos(double angle) {
        return (float) Math.cos(angle);
    }

    public static String getString() {
        return "x:" + pos.x + " y:" + pos.y + " z:" + pos.z;
    }

    public static boolean shouldRenderChunk(int chunkX, int chunkZ) {

        return true;

       /* chunkX -= (float)Math.cos(yaw) * 2;
        chunkZ -= (float)Math.sin(yaw) * 2;

        double dX = pos.x - chunkX * 16;
        double dZ = pos.z - chunkZ * 16;

        double yaw = Math.atan2(dZ, dX);

        double anglediff = (Math.toDegrees(Camera.yaw) - Math.toDegrees(yaw) + 180 + 360) % 360 - 180;


        return anglediff <= fov && anglediff>=-fov;

        */
    }

    public static float velX;
    public static float velY;
    public static float velZ;

    public static void move() {

        if (velX != 0 || velY != 0 || velZ != 0) {
            float count = 4;
            for (int a = 0; a < count; a++) {
                move(velX / count, velY / count, velZ / count);
            }
            ClientNetworkHandler.sendPacket(new CUpdatePlayerPacket(pos.x, pos.y, pos.z, (float) pitch, (float) yaw, ClientMain.playerId));

        }
    }

    private static void move(float velX, float velY, float velZ) {
        //System.out.println(velX + " : " + velZ);
        //isOnGround = false;
        int x;
        boolean couldMove = false;
        for(x = 0; x < 7; x++) {
            boolean movement = getAllowedMovement(tryMovement(x,velX,velY,velZ),pos, ClientMain.clientWorld);
            if(movement) {
                movement = canMove(tryMovement(x,velX,velY,velZ));
                //System.out.println(movement);
            }
            if(!movement) {
                continue;
            }
            couldMove = true;
            break;
        }
        isOnGround = false;
        if(!couldMove) {
            isOnGround = true;
            velX = 0;
            velY = 0;
            velZ = 0;
        }
        //System.out.println(x);
        if (x == 3 || x == 5 || x == 6) {
            //Camera.velX = 0;
            velX = 0;
        }
        if (x == 1 || x == 4 || x == 6) {
            //Camera.velZ = 0;
            isOnGround = true;
            velY = 0;
        }
        if (x == 2 || x == 4 || x == 5) {
            //Camera.velZ = 0;
            velZ = 0;
        }
        pos.x += velX;
        pos.y += velY;
        pos.z += velZ;

    }

    private static Vector3f tryMovement(int side, float velX, float velY, float velZ) {
        switch (side) {
            case 0:
                return new Vector3f(velX,velY,velZ);
            case 1:
                return new Vector3f(velX,0,velZ);
            case 2:
                sprintTimeout = 0;
                return new Vector3f(velX,velY,0);
            case 3:
                return new Vector3f(0,velY,velZ);
            case 4:
                return new Vector3f(velX,0,0);
            case 5:
                return new Vector3f(0,velY,0);
            case 6:
                //System.out.println("yes");
                return new Vector3f(0,0,velZ);
            default:
                //System.out.println("else");
                return new Vector3f();
        }
    }



    public static boolean getAllowedMovement(Vector3f motion, Vector3f cameraPos, World world) {
        BlockPos pos = new BlockPos((int)Math.floor(cameraPos.x),(int)Math.floor(cameraPos.y),(int)Math.floor(cameraPos.z));
        int X = (int) Math.ceil(Math.abs(motion.x)) + 2;
        int Y = (int) Math.ceil(Math.abs(motion.y)) + 4;
        int Z = (int) Math.ceil(Math.abs(motion.z)) + 2;

        for(int x = -X; x < X; x++) {
            for(int y = -Y; y < Y; y++) {
                for(int z = -Z; z < Z; z++) {
                    Block block = world.getBlockState(pos.copy().add(x,y,z)).block;
                    if(block != Blocks.AIR) {
                        boolean canMove = block.getAllowedMovement1(new Vector3f(motion.x,motion.y,motion.z), new Vector3f(cameraPos.x, cameraPos.y, cameraPos.z), pos.copy().add(x, y, z), playerBoundingBox);
                        if(!canMove) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    private static boolean canMove(Vector3f motion) {
        if(KeyHandler.keyPressed[GLFW_KEY_LEFT_SHIFT]) {
            if(isOnGround) {
                Vector3f newPos = new Vector3f(pos.x,pos.y,pos.z).add(motion);
                return !getAllowedMovement(new Vector3f(0,fallSpeed * 2,0), newPos, ClientMain.clientWorld);
                //System.out.println("yes");
                //return ClientMain.clientWorld.getBlockState((int)newPos.x,(int)newPos.y,(int)newPos.z).block != Blocks.AIR;
            } else {
                //System.out.println("ASD");
                return true;
            }
        } else {
           // System.out.println("else");
            return true;
        }
    }


}
