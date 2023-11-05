package dev.hilligans.ourcraft.Client;

import dev.hilligans.ourcraft.Data.Other.BoundingBox;
import org.joml.*;

public class Camera {

    public static Vector3d pos = new Vector3d(0, 64,0);

    public static int fov = 70;

    public static double pitch;
    public static double yaw;

    public static boolean sprinting = false;
    public static int sprintTimeout = 1;
    public static int sprintDelay = 0;

    public static float sensitivity = 150;

    public static BoundingBox playerBoundingBox = new BoundingBox(-0.35f,-1.9f,-0.35f,0.35f,0.0f,0.35f, -0.15f);

}
