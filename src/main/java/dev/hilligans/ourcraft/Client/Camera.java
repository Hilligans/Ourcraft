package dev.hilligans.ourcraft.Client;

import dev.hilligans.ourcraft.Block.Block;
import dev.hilligans.ourcraft.Block.Blocks;
import dev.hilligans.ourcraft.Client.Input.Key.KeyHandler;
import dev.hilligans.ourcraft.Client.Input.Key.KeyPress;
import dev.hilligans.ourcraft.ClientMain;
import dev.hilligans.ourcraft.Data.Other.BlockPos;
import dev.hilligans.ourcraft.Data.Other.BoundingBox;
import dev.hilligans.ourcraft.Entity.LivingEntities.PlayerEntity;
import dev.hilligans.ourcraft.Network.Packet.Client.CUpdatePlayerPacket;
import dev.hilligans.ourcraft.World.Chunk;
import dev.hilligans.ourcraft.World.World;
import org.joml.*;

import java.lang.Math;

import static org.lwjgl.glfw.GLFW.*;

public class Camera {

    public static Vector3d pos = new Vector3d(0, Chunk.terrain,0);

    public static int fov = 70;

    public static double pitch;
    public static double yaw;

    public static boolean sprinting = false;
    public static int sprintTimeout = 1;
    public static int sprintDelay = 0;

    public static float sensitivity = 150;

    public static BoundingBox playerBoundingBox = new BoundingBox(-0.35f,-1.9f,-0.35f,0.35f,0.0f,0.35f, -0.15f);

    public static double newX = (float)ClientMain.getWindowX() / 2;
    public static double newY = (float)ClientMain.getWindowY() / 2;

}
