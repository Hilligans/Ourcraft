package dev.hilligans.engine.network.packet;

import dev.hilligans.engine.network.Protocol;
import dev.hilligans.engine.network.engine.NetworkEntity;
import dev.hilligans.engine.util.IByteArray;
import dev.hilligans.engine.util.StableValue;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class PacketType<T extends NetworkEntity> {

    public static final AtomicInteger UNIQUE_ID = new AtomicInteger();

    // Fake stable value LOL
    public final StableValue<Integer> id = StableValue.supplier(UNIQUE_ID::getAndIncrement);

    public int getRawID() {
        return id.get();
    }

    public short getPacketID(Protocol protocol) {
        try {
            return (short) protocol.fromPacketTypeToId(id.get());
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new RuntimeException("Packet " + this.getClass() + " is not in protocol " + protocol.getIdentifierName());
        }
    }

    public IByteArray getWriteArray(NetworkEntity entity) {
        IByteArray array = entity.allocByteArray();
        array.writeShort(getPacketID(entity.getSendProtocol()));
        return array;
    }

    /*
        All packet type sub classes must have this method in approximately this format.

        public IByteArray encode(NetworkEntity entity) {
            IByteArray array = getWriteArray(entity);

            return array;
        }
     */

    public abstract void decode(T entity, IByteArray data);
}
