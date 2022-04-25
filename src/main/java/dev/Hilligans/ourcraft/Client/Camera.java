package dev.Hilligans.ourcraft.Client;

import dev.Hilligans.ourcraft.Block.Block;
import dev.Hilligans.ourcraft.Block.Blocks;
import dev.Hilligans.ourcraft.Client.Input.Key.KeyHandler;
import dev.Hilligans.ourcraft.Client.Input.Key.KeyPress;
import dev.Hilligans.ourcraft.ClientMain;
import dev.Hilligans.ourcraft.Data.Other.BlockPos;
import dev.Hilligans.ourcraft.Data.Other.BoundingBox;
import dev.Hilligans.ourcraft.Entity.LivingEntities.PlayerEntity;
import dev.Hilligans.ourcraft.Network.Packet.Client.CUpdatePlayerPacket;
import dev.Hilligans.ourcraft.Util.Ray;
import dev.Hilligans.ourcraft.World.Chunk;
import dev.Hilligans.ourcraft.World.World;
import org.joml.*;
import org.lwjgl.BufferUtils;

import java.lang.Math;
import java.nio.DoubleBuffer;

import static org.lwjgl.glfw.GLFW.*;

public class Camera {

    public static Vector3d pos = new Vector3d(0, Chunk.terrain,0);

    public static int fov = 70;

    private static final double fallSpeed = -0.0008f;
    private static final double terminalVel = -1.15f;

    public static double pitch;
    public static double yaw;

    public static double moveSpeed = 0.02f;

    public static boolean thirdPerson = false;
    public static int thirdPersonMode = -1;

    public static Vector3i playerChunkPos = null;

    public static float thirdPersonScroll = 2.0f;


    public static PlayerEntity playerEntity = new PlayerEntity(0,0,0,Integer.MIN_VALUE);

    public static boolean isOnGround = false;
    public static boolean hitBlock = false;

    public static boolean sprinting = false;
    public static int sprintTimeout = 1;
    public static int sprintDelay = 0;

    public static final float accelTime = 80f;

    public static float sensitivity = 150;

    public static BoundingBox playerBoundingBox = new BoundingBox(-0.35f,-1.9f,-0.35f,0.35f,0.0f,0.35f, -0.15f);

    static {
        KeyHandler.register(new KeyPress() {
            @Override
            public void onPress() {
                toggleThirdPerson();
            }
        },KeyHandler.GLFW_KEY_F5);
    }

    public static void moveForeWard() {
        if(KeyHandler.keyPressed[GLFW_KEY_LEFT_SHIFT]) {
            add(- Math.cos(yaw) * moveSpeed / 3, 0, - Math.sin(yaw) * moveSpeed / 3);
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
        if(ClientMain.getClient().playerData.spectator || ClientMain.getClient().playerData.flying) {
            add(0, moveSpeed, 0);
        } else {
            if(velY == 0) {
                isOnGround = false;
                add(0, 0.045f, 0);
                if(maxX == 0) {
                    //velX = velX * 0.5f;
                }
                if(maxZ == 0) {
                    //velZ = velZ * 0.5f;
                }
            }
        }
    }

    public static void moveDown() {
        if(ClientMain.getClient().playerData.spectator || ClientMain.getClient().playerData.flying) {
            add(0, -moveSpeed, 0);
        }
    }

    public static void addToThirdPerson(float amount) {
        if(amount > 0) {
            thirdPersonScroll = Math.min(16,thirdPersonScroll + amount);
        } else {
            thirdPersonScroll = Math.max(2.0f,thirdPersonScroll + amount);
        }
    }

    public static void toggleThirdPerson() {
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

    public static float maxX,maxZ;

    public static void add(double x, double y, double z) {

        if(ClientMain.getClient().playerData.spectator) {
            pos.add(x * 4, y * 4, z * 4);
            ClientMain.getClient().sendPacket(new CUpdatePlayerPacket(pos.x,pos.y,pos.z,(float)pitch,(float)yaw,ClientMain.getClient().playerId));
        } else {

            maxX += x;
            maxZ += z;

            //System.out.println(x + " : " + z);

            if(isOnGround) {
                velX += x / 4;
                velZ += z / 4;
            } else {
                velX += x / accelTime;
                velZ += z / accelTime;
            }
            //velX += x;
            velY += y;


            //velZ += z;
        }
    }

    public static void tick() {
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

    public static Vector3d renderPos = new Vector3d();

    public static Vector3d getLookVector() {
        return new Vector3d((Math.cos(Camera.yaw) * Math.cos(Camera.pitch)), (Math.sin(Camera.pitch)), (Math.sin(Camera.yaw) * Math.cos(Camera.pitch)));
    }

    public static double newX = (float)ClientMain.getWindowX() / 2;
    public static double newY = (float)ClientMain.getWindowY() / 2;

    public static void renderPlayer(MatrixStack matrixStack) {
        if(thirdPerson) {
            playerEntity.setPos(pos);
            playerEntity.setRot((float) pitch, (float) yaw);
            playerEntity.render(matrixStack);
        }
    }

    public static double velX;
    public static double velY;
    public static double velZ;

    public static void move() {

        if (velX != 0 || velY != 0 || velZ != 0) {
            float count = 4;
            for (int a = 0; a < count; a++) {
                move(velX / count, velY / count, velZ / count);
            }
            ClientMain.getClient().sendPacket(new CUpdatePlayerPacket(pos.x, pos.y, pos.z, (float) pitch, (float) yaw, ClientMain.getClient().playerId));

        }
    }

    private static void move(double velX, double velY, double velZ) {
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

    private static void addVel(int x, double velX, double velY, double velZ) {
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

    private static Vector3d tryMovement(int side, double velX, double velY, double velZ) {
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



    public static boolean getAllowedMovement(Vector3d motion, Vector3d cameraPos, World world) {
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

    private static boolean canMove(Vector3d motion, Vector3d pos) {
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
