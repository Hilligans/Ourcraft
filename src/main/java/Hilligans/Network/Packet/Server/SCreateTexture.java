package Hilligans.Network.Packet.Server;

import Hilligans.Client.Rendering.Texture;
import Hilligans.Client.Rendering.World.Managers.WorldTextureManager;
import Hilligans.Data.Other.ServerSidedData;
import Hilligans.Network.PacketBase;
import Hilligans.Network.PacketData;

import java.awt.image.BufferedImage;

public class SCreateTexture extends PacketBase {

    boolean isBlock;
    String name;
    BufferedImage texture;

    public SCreateTexture() {
        super(22);
    }

    public SCreateTexture(BufferedImage texture, String name, boolean isBlock) {
        this();
        this.texture = texture;
        this.isBlock = isBlock;
        this.name = name;
    }

    @Override
    public void encode(PacketData packetData) {
        packetData.writeBoolean(isBlock);
        packetData.writeString(name);
        packetData.writeInt(texture.getWidth());
        packetData.writeInt(texture.getHeight());
        for(int x = 0; x < texture.getWidth(); x++) {
            for(int y = 0; y < texture.getHeight(); y++) {
                packetData.writeInt(texture.getRGB(x,y));
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
                texture.setRGB(x,y,packetData.readInt());
            }
        }
    }

    @Override
    public void handle() {
        if(isBlock) {
            WorldTextureManager.instance.registerBlockTexture(texture,name);
        } else {
            ServerSidedData.getInstance().putTexture(name,new Texture(texture));
        }
    }
}
