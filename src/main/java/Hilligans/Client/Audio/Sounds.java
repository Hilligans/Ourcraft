package Hilligans.Client.Audio;

import java.util.ArrayList;

public class Sounds {

    public static final ArrayList<SoundBuffer> sounds = new ArrayList<>();

    public static final SoundBuffer BLOCK_BREAK = new SoundBuffer("/Sounds/Hit_Hurt.ogg");
    public static final SoundBuffer MUSIC = new SoundBuffer("/Sounds/Music/New_Recording_45.ogg").setCategory(SoundCategory.MUSIC);




}
