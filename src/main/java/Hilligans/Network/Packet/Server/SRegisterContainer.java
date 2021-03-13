package Hilligans.Network.Packet.Server;

import Hilligans.Client.Rendering.Widgets.Widget;
import Hilligans.Client.Rendering.Widgets.WidgetFetcher;
import Hilligans.Container.Container;
import Hilligans.Container.Containers.ContainerBuilder;
import Hilligans.Container.Slot;
import Hilligans.Data.Other.ServerSidedData;
import Hilligans.Network.PacketBase;
import Hilligans.Network.PacketData;

public class SRegisterContainer extends PacketBase {

    Container container;
    ContainerBuilder containerBuilder;
    String textureName;

    public SRegisterContainer() {
        super(24);
    }

    public SRegisterContainer(Container container, String textureName) {
        this();
        this.container = container;
        this.textureName = textureName;
    }

    @Override
    public void encode(PacketData packetData) {
        packetData.writeShort((short) container.type);
        packetData.writeShort((short) container.textureX);
        packetData.writeShort((short) container.textureY);
        packetData.writeShort((short) container.slots.size());
        for(Slot slot : container.slots) {
            packetData.writeShort((short) slot.startX);
            packetData.writeShort((short) slot.startY);
            packetData.writeShort((short) slot.id);
        }
        short count = 0;
        for(Widget widget : container.widgets) {
            if(widget.widgetId != -1) {
                count++;
            }
        }
        packetData.writeShort(count);
        for(Widget widget : container.widgets) {
            if(widget.widgetId != -1) {
                packetData.writeShort(widget.widgetId);
                packetData.writeShort((short) widget.x);
                packetData.writeShort((short) widget.y);
                packetData.writeShort((short) widget.width);
                packetData.writeShort((short) widget.height);
            }
        }

        packetData.writeString(textureName);


    }

    @Override
    public void decode(PacketData packetData) {
        short type = packetData.readShort();
        int width = packetData.readShort();
        int height = packetData.readShort();
        Slot[] slots = new Slot[packetData.readShort()];
        for(int x = 0; x < slots.length; x++) {
            slots[x] = new Slot(packetData.readShort(),packetData.readShort(),null,packetData.readShort());
        }
        ContainerBuilder.WidgetHolder[] widgets = new ContainerBuilder.WidgetHolder[packetData.readShort()];
        for(int x = 0; x < widgets.length; x++) {
            widgets[x] = new ContainerBuilder.WidgetHolder(Widget.widgets.get(packetData.readShort()),packetData.readShort(),packetData.readShort(),packetData.readShort(),packetData.readShort());
        }
        containerBuilder = new ContainerBuilder(type,packetData.readString(),slots,widgets,width,height);
    }

    @Override
    public void handle() {
        ServerSidedData.getInstance().putContainer(textureName,containerBuilder);
    }
}
