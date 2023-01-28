package dev.hilligans.ourcraft.Network.Packet.Server;

import dev.hilligans.ourcraft.Client.Camera;
import dev.hilligans.ourcraft.ClientMain;
import dev.hilligans.ourcraft.Network.PacketBase;
import dev.hilligans.ourcraft.Network.PacketData;

public class SUpdatePlayer extends PacketBase {

    float x;
    float y;
    float z;
    float pitch;
    float yaw;

    public SUpdatePlayer() {
        super(21);
    }

    public SUpdatePlayer(float x, float y, float z, float pitch, float yaw) {
        this();
        this.x = x;
        this.y = y;
        this.z = z;
        this.pitch = pitch;
        this.yaw = yaw;
    }


    @Override
    public void encode(PacketData packetData) {
        packetData.writeFloat(x);
        packetData.writeFloat(y);
        packetData.writeFloat(z);
        packetData.writeFloat(pitch);
        packetData.writeFloat(yaw);
    }

    @Override
    public void decode(PacketData packetData) {
        x = packetData.readFloat();
        y = packetData.readFloat();
        z = packetData.readFloat();
        pitch = packetData.readFloat();
        yaw = packetData.readFloat();
    }

    @Override
    public void handle() {
        ClientMain.getClient().rWindow.camera.setPosition(x,y,z);
        ClientMain.getClient().rWindow.camera.setRotation(pitch,yaw);
        Camera.pos.set(x,y,z);
        Camera.pitch = pitch;
        Camera.yaw = yaw;
    }
}
