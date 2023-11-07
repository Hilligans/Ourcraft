package dev.hilligans.ourcraft.client.audio;

import dev.hilligans.ourcraft.client.rendering.graphics.api.ICamera;
import dev.hilligans.ourcraft.GameInstance;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALCCapabilities;


import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import static org.lwjgl.openal.ALC10.*;
import static org.lwjgl.openal.AL10.*;

import static org.lwjgl.system.MemoryUtil.NULL;

public class SoundEngine {

    private long device;

    private long context;

    MusicEngine musicEngine = new MusicEngine();

    private SoundListener listener;

    public final ArrayList<SoundSource> sounds = new ArrayList<>();
    public GameInstance gameInstance;
    public ICamera camera;

    public SoundEngine(GameInstance gameInstance) {
        this.gameInstance = gameInstance;
    }

    public void init(ICamera camera) {
        this.camera = camera;
        this.device = alcOpenDevice((ByteBuffer) null);
        if (device == NULL) {
            throw new IllegalStateException("Failed to open the default OpenAL device.");
        }
        ALCCapabilities deviceCaps = ALC.createCapabilities(device);
        this.context = alcCreateContext(device, (IntBuffer) null);
        if (context == NULL) {
            throw new IllegalStateException("Failed to create OpenAL context.");
        }
        alcMakeContextCurrent(context);
        AL.createCapabilities(deviceCaps);

        listener = new SoundListener(camera.getPosition().get(new Vector3f()));

        for(SoundBuffer soundBuffer : gameInstance.SOUNDS.ELEMENTS) {
            soundBuffer.soundCategory.sounds.add(soundBuffer);
        }
    }

    public void tick() {
        updateListenerPosition();
        musicEngine.tick();
        long time = System.currentTimeMillis();
        for(int x = 0; x < sounds.size(); x++) {
            SoundSource soundSource = sounds.get(x);
            soundSource.tick();
            if(soundSource.endTime <= time) {
                soundSource.stop();
                soundSource.cleanup();
                sounds.remove(x);
                x--;
            }
        }
    }

    public void stopAllSounds() {
        musicEngine.stop();
        for(SoundSource soundSource : sounds) {
            soundSource.cleanup();
        }
        sounds.clear();
    }

    public void addSound(SoundBuffer soundBuffer, Vector3d pos) {
        SoundSource soundSource = soundBuffer.createNewSound(false,false,soundBuffer.soundCategory).setRollOffFactor(soundBuffer.rollOff).setPosition(new Vector3f((float)pos.x,(float)pos.y,(float)pos.z)).setPitch(1.0f);
        addSound(soundSource);
        soundSource.play();
    }

    public void addSound(SoundSource soundSource) {
        if(sounds.size() > 255) {
            sounds.remove(0).cleanup();
        }
        sounds.add(soundSource);
    }

    public void updateListenerPosition() {
        listener.setPosition(camera.getPosition());
        listener.setOrientation(camera.getLookVector(), camera.cameraUp());
    }

    public void setAttenuationModel(int model) {
        alDistanceModel(model);
    }

    public void cleanup() {
        for (SoundSource soundSource : sounds) {
            soundSource.cleanup();
        }
        sounds.clear();
        if (context != NULL) {
            alcDestroyContext(context);
        }
        if (device != NULL) {
            alcCloseDevice(device);
        }
    }
}
