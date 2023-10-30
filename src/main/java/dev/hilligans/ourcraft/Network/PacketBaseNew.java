package dev.hilligans.ourcraft.Network;

public abstract class PacketBaseNew<T extends IPacketHandler> extends PacketBase {


    @Override
    public void handle() {
        //TODO implement and change over
    }

    public abstract void handle(T t);
}
