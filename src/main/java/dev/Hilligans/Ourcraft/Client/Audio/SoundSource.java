package dev.Hilligans.Ourcraft.Client.Audio;

import org.joml.Vector3f;
import org.lwjgl.openal.AL10;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.AL11.AL_SPEED_OF_SOUND;

public class SoundSource {

    public final int sourceId;
    public boolean playing;

    public float defaultVolume = 1.0f;
    public float startingVolume = 1.0f;
    public long endTime;
    public SoundCategory soundCategory;

    public SoundSource(boolean loop, boolean relative, SoundCategory soundCategory) {
        this.sourceId = alGenSources();
        this.soundCategory = soundCategory;
        soundCategory.addSource(this);
        if (loop) {
            alSourcei(sourceId, AL_LOOPING, AL_TRUE);
        }
        if (relative) {
            alSourcei(sourceId, AL_SOURCE_RELATIVE, AL_TRUE);
        }
    }

    public void tick() { }

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

    public SoundSource defaultEndTime(float bufferLength) {
        endTime = System.currentTimeMillis() + (long)(1000 * bufferLength);
        return this;
    }

    public SoundSource setPosition(Vector3f position) {
        alSource3f(sourceId, AL_POSITION, position.x, position.y, position.z);
        return this;
    }

    public SoundSource setMovementSpeed(Vector3f speed) {
        alSource3f(sourceId, AL_VELOCITY, speed.x, speed.y, speed.z);
        return this;
    }

    public SoundSource setPlayBackSpeed(float speed) {
        alSourcef(sourceId,AL_SPEED_OF_SOUND,speed);
        return this;
    }

    private SoundSource setGain(float gain) {
        alSourcef(sourceId, AL_GAIN, gain);
        return this;
    }

    public SoundSource setDefaultVolume(float volume) {
        this.defaultVolume = volume;
        setGain(defaultVolume * startingVolume);
        return this;
    }

    public SoundSource setVolume(float volume) {
        this.startingVolume = volume;
        setGain(defaultVolume * startingVolume);
        return this;
    }

    public SoundSource setPitch(float pitch) {
        alSourcef(sourceId,AL_PITCH,pitch);
        return this;
    }

    public SoundSource play() {
        playing = true;
        alSourcePlay(sourceId);
        return this;
    }

    public SoundSource pause() {
        playing = false;
        alSourcePause(sourceId);
        return this;
    }

    public SoundSource stop() {
        playing = false;
        alSourceStop(sourceId);
        return this;
    }

    public void cleanup() {
        stop();
        soundCategory.removeSource(this);
        alDeleteSources(sourceId);
    }
}