package dev.Hilligans.ourcraft.Client.Audio;

import dev.Hilligans.ourcraft.Ourcraft;

import java.util.ArrayList;
import java.util.HashMap;

public class Sounds {

    public static SoundBuffer BLOCK_BREAK;
    public static SoundBuffer MUSIC;


    public static void reg() {
        BLOCK_BREAK = new SoundBuffer("Sounds/Hit_Hurt.ogg");
        MUSIC = new SoundBuffer("Sounds/Music/New_Recording_45.ogg").setCategory(SoundCategory.MUSIC);
    }
}
