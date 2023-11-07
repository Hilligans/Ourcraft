package dev.hilligans.ourcraft.world;

import dev.hilligans.ourcraft.util.Immutable;
import dev.hilligans.ourcraft.util.math.Vector3fi;
import org.joml.Vector3f;
import org.joml.Vector3fc;

public abstract class WorldType {

    public static final Vector3fi DOWN = new Vector3fi(0,-1,0);

    /**
     * Used to control entity physics.
     */
    @Immutable
    public Vector3fc getGravityVector(Vector3f position) {
        return DOWN;
    }

    /**
     * Used to control block physics such as water movement and block texturing.
     */
    public Vector3fc getBlockGravity(int x, int y, int z) {
        return DOWN;
    }
}
