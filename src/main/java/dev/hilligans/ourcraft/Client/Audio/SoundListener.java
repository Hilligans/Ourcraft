package dev.hilligans.ourcraft.Client.Audio;

import org.joml.Vector3d;
import org.joml.Vector3f;

import static org.lwjgl.openal.AL10.*;

public class SoundListener {

    public SoundListener() {
        this(new Vector3f());
    }

    public SoundListener(Vector3f position) {
        alListener3f(AL_POSITION, position.x, position.y, position.z);
        alListener3f(AL_VELOCITY, 0, 0, 0);
    }

    public void setSpeed(Vector3f speed) {
        alListener3f(AL_VELOCITY, speed.x, speed.y, speed.z);
    }

    public void setPosition(Vector3d position) {
        alListener3f(AL_POSITION, (float) position.x, (float) position.y, (float) position.z);
    }

    public void setOrientation(Vector3d at, Vector3d up) {
        float[] data = new float[6];
        data[0] = (float) at.x;
        data[1] = (float) at.y;
        data[2] = (float) at.z;
        data[3] = (float) up.x;
        data[4] = (float) up.y;
        data[5] = (float) up.z;
        alListenerfv(AL_ORIENTATION, data);
    }


}
