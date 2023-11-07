package dev.hilligans.ourcraft.client.audio;

public class Sounds {

    public static SoundBuffer BLOCK_BREAK;
    public static SoundBuffer MUSIC;


    public static void reg() {
        BLOCK_BREAK = new SoundBuffer("Sounds/Hit_Hurt.ogg");
        MUSIC = new SoundBuffer("Sounds/Music/New_Recording_45.ogg").setCategory(SoundCategory.MUSIC);
    }
}
