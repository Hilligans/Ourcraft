package Hilligans.Client.Sound;

import org.joml.Vector3d;
import org.joml.Vector3f;
import org.lwjgl.openal.AL10;

import static org.lwjgl.openal.AL10.*;

public class SoundSource {

    public final int sourceId;

    public long endTime;

    public SoundSource(boolean loop, boolean relative) {
        this.sourceId = alGenSources();

        if (loop) {
            alSourcei(sourceId, AL_LOOPING, AL_TRUE);
        }
        if (relative) {
            alSourcei(sourceId, AL_SOURCE_RELATIVE, AL_TRUE);
        }
    }

    public SoundSource(int id, Vector3d position) {
        this(false,true);
        setBuffer(id);
        setPosition(new Vector3f((float)position.x,(float)position.y,(float)position.z));
    }


    public SoundSource setRollOffFactor(float val) {
        AL10.alSourcef(sourceId, AL_MAX_DISTANCE, 255);
        AL10.alSourcef(sourceId, AL_ROLLOFF_FACTOR, val);
        AL10.alSourcef(sourceId, AL_REFERENCE_DISTANCE, 0.0F);
        return this;
    }

    public SoundSource setBuffer(int bufferId) {
        alSourcei(sourceId, AL_BUFFER, bufferId);
        return this;
    }

    public SoundSource setEndTime(long time) {
        endTime = time;
        return this;
    }

    public SoundSource setPosition(Vector3f position) {
        alSource3f(sourceId, AL_POSITION, position.x, position.y, position.z);
        return this;
    }

    public SoundSource setSpeed(Vector3f speed) {
        alSource3f(sourceId, AL_VELOCITY, speed.x, speed.y, speed.z);
        return this;
    }

    public SoundSource setGain(float gain) {
        alSourcef(sourceId, AL_GAIN, gain);
        return this;
    }

    public SoundSource setPitch(float pitch) {
        alSourcef(sourceId,AL_PITCH,pitch);
        return this;
    }

    public SoundSource play() {
        alSourcePlay(sourceId);
        return this;
    }

    public SoundSource pause() {
        alSourcePause(sourceId);
        return this;
    }

    public SoundSource stop() {
        alSourceStop(sourceId);
        return this;
    }

    public void cleanup() {
        stop();
        alDeleteSources(sourceId);
    }
}