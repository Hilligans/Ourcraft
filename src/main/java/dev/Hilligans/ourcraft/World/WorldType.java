package dev.Hilligans.ourcraft.World;

import dev.Hilligans.ourcraft.Util.Immutable;
import dev.Hilligans.ourcraft.Util.Math.Vector3fi;
import org.joml.Vector3f;
import org.joml.Vector3fc;

public abstract class WorldType {

    public static final Vector3fi DOWN = new Vector3fi(0,-1,0);

    @Immutable
    public Vector3fc getGravityVector(Vector3f position) {
        return DOWN;
    }

    public void b() {
        ((Vector3f)getGravityVector(null)).x = 10;
    }
}
