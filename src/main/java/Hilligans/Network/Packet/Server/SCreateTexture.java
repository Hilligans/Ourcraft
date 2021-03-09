package Hilligans.Network.Packet.Server;

import Hilligans.Client.Rendering.Textures;
import Hilligans.Client.Rendering.World.Managers.WorldTextureManager;
import Hilligans.Network.PacketBase;
import Hilligans.Network.PacketData;
import org.lwjgl.system.CallbackI;

import java.awt.image.BufferedImage;

public class SCreateTexture extends PacketBase {

    boolean isBlock;
    String name;
    BufferedImage texture;

    public SCreateTexture() {
        super(22);
    }

    @Override
    public void encode(PacketData packetData) {
        packetData.writeBoolean(isBlock);
        packetData.writeString(name);
        packetData.writeInt(texture.getWidth());
        packetData.writeInt(texture.getHeight());
        for(int x = 0; x < texture.getWidth(); x++) {
            for(int y = 0; y < texture.getHeight(); y++) {
                int rgb = texture.getRGB(x,y);
                packetData.writeByte((byte) (rgb & 0xFF));
                packetData.writeByte((byte) (rgb & 0xFF << 8));
                packetData.writeByte((byte) (rgb & 0xFF << 16));
                packetData.writeByte((byte) (rgb & 0xFF << 24));
            }
        }
    }

    @Override
    public void decode(PacketData packetData) {
        isBlock = packetData.readBoolean();
        name = packetData.readString();
        int width = packetData.readInt();
        int height = packetData.readInt();
        texture = new BufferedImage(width,height, BufferedImage.TYPE_INT_ARGB);
        for(int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++) {
                int rgb = packetData.readByte() | packetData.readByte() << 8 | packetData.readByte() << 16 | packetData.readByte() << 24;
                texture.setRGB(x,y,rgb);
            }
        }
    }

    @Override
    public void handle() {
        if(isBlock) {
            WorldTextureManager.instance.registerBlockTexture(texture,name);
        } else {
            Textures.addTexture(name,texture);
        }
    }
}
