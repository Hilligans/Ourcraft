package Hilligans.Client;

import Hilligans.Client.Key.KeyHandler;
import Hilligans.Client.Key.KeyPress;
import Hilligans.ClientMain;
import Hilligans.Network.ClientNetworkHandler;
import Hilligans.Network.Packet.Client.CUpdatePlayerPacket;
import Hilligans.World.Chunk;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import java.nio.DoubleBuffer;

import static Hilligans.ClientMain.windowX;
import static Hilligans.ClientMain.windowY;
import static org.lwjgl.glfw.GLFW.glfwGetCursorPos;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPos;

public class Camera {

    public static Vector3f pos = new Vector3f(0, Chunk.terrain,0);

    public static double pitch;
    public static double yaw;

    public static float moveSpeed = 0.05f;

    public static boolean thirdPerson = false;

    static {
        KeyHandler.register(new KeyPress() {
            @Override
            public void onPress() {
                thirdPerson = !thirdPerson;
            }
        },KeyHandler.GLFW_KEY_F5);
    }

    public static void moveForeWard() {
        add(-(float)Math.cos(yaw) * moveSpeed,0, -(float)Math.sin(yaw) * moveSpeed);
    }

    public static void moveBackWard() {
        add((float)Math.cos(yaw) * moveSpeed,0, (float)Math.sin(yaw) * moveSpeed);
    }

    public static void strafeLeft() {
        add(-(float)Math.sin(yaw) * moveSpeed,0, (float)Math.cos(yaw) * moveSpeed);
    }

    public static void strafeRight() {
        add((float)Math.sin(yaw) * moveSpeed,0, -(float)Math.cos(yaw) * moveSpeed);
    }

    public static void moveUp() {
        add(0,moveSpeed,0);
    }

    public static void moveDown() {
        add(0,-moveSpeed,0);
    }

    public static void add(float x, float y, float z) {
        pos.add(x,y,z);
        ClientNetworkHandler.sendPacket(new CUpdatePlayerPacket(pos.x,pos.y,pos.z,(float)pitch,(float)yaw,ClientMain.playerId));
    }

    public static void addPitch(double amount) {
        pitch += amount;
        if(pitch > 3.1415 / 2) {
            pitch = 3.1415 / 2;
        }

        if(pitch < - 3.1415 / 2) {
            pitch = -3.1415 / 2;
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
        projection.perspective((float) Math.toRadians(70), (float) windowX / windowY,0.1f,10000.0f);
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
        matrix4f.perspective((float) Math.toRadians(70), (float) windowX / windowY,0.1f,10000.0f);
        matrix4f.mul(view);
        matrix4f.lookAt(Camera.duplicate().add((float)(Math.cos(Camera.yaw) * Math.cos(Camera.pitch)),(float)(Math.sin(Camera.pitch)),(float)(Math.sin(Camera.yaw) * Math.cos(Camera.pitch))),Camera.duplicate(), cameraUp);
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

        Camera.addPitch(deltaY / 100);
        Camera.addYaw(deltaX / 100);


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


}
