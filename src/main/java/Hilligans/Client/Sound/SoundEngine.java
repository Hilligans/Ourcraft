package Hilligans.Client.Sound;

import Hilligans.Client.Camera;
import org.joml.Matrix4f;
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

    private SoundListener listener;

    public final ArrayList<SoundSource> sounds = new ArrayList<>();

    private final Matrix4f cameraMatrix = new Matrix4f();


    public void init() {
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

        listener = new SoundListener(Camera.pos.get(new Vector3f()));
    }

    public void tick() {
        updateListenerPosition();
        long time = System.currentTimeMillis();
        for(int x = 0; x < sounds.size(); x++) {
            SoundSource soundSource = sounds.get(x);
            if(soundSource.endTime <= time) {
                soundSource.stop();
                soundSource.cleanup();
                sounds.remove(x);
                x--;
            }
        }
    }

    public void stopAllSounds() {
        for(SoundSource soundSource : sounds) {
            soundSource.cleanup();
        }
        sounds.clear();
    }

    public void addSound(SoundBuffer soundBuffer, Vector3d pos) {
        SoundSource soundSource = soundBuffer.createNewSound(false,false).setRollOffFactor(12f).setPosition(new Vector3f((float)pos.x,(float)pos.y,(float)pos.z)).setEndTime(System.currentTimeMillis() + (long)(1000 * soundBuffer.length)).setPitch(1.0f);;
        sounds.add(soundSource);
        soundSource.play();
    }

    public void updateListenerPosition() {
        listener.setPosition(Camera.pos.get(new Vector3f()));
        Vector3f at = new Vector3f(0,0,-1);
        Vector3f up = Camera.getLookVector().get(new Vector3f());
        listener.setOrientation(at, up);
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
