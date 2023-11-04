package dev.hilligans.ourcraft.Network;

public abstract class PacketBaseNew<T extends IPacketHandler> extends PacketBase {

    public PacketBaseNew() {}

    public PacketBaseNew(int packetID) {
        super(packetID);
    }


    @Override
    public void handle() {
        //TODO implement and change over
    }

    public abstract void handle(T t);

    public void handle(Object t) {
        this.handle((T)t);
    }
}
