package dev.Hilligans.ourcraft.Client.Audio;

import dev.Hilligans.ourcraft.Ourcraft;

import java.util.ArrayList;
import java.util.HashMap;

public class Sounds {

    public static final ArrayList<SoundBuffer> SOUNDS = new ArrayList<>();
    public static final HashMap<String, SoundBuffer> MAPPED_SOUND = new HashMap<>();

    public static final SoundBuffer BLOCK_BREAK = new SoundBuffer("Sounds/Hit_Hurt.ogg");
    public static final SoundBuffer MUSIC = new SoundBuffer("Sounds/Music/New_Recording_45.ogg").setCategory(SoundCategory.MUSIC);


    public static synchronized void registerSound(String name, SoundBuffer soundBuffer) {
        MAPPED_SOUND.put(name,soundBuffer);
        SOUNDS.add(soundBuffer);
    }

    static {
        Ourcraft.GAME_INSTANCE.OURCRAFT.registerSounds(BLOCK_BREAK,MUSIC);
    }

}
