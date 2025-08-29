package dev.hilligans.ourcraft.client.audio;

import java.util.Random;

public class MusicEngine {

    public SoundSource song;
    public long lastAttempt;
    Random random = new Random();

    public static final int chancePerSecond = 1000;

    public void tick() {
        long time = System.currentTimeMillis();
        out:
        if(song != null) {
            if(song.endTime < time) {
                song = null;
                break out;
            }
            if (song.playing) {
                return;
            }
        }
        if(lastAttempt + 1000 > time) {
            return;
        }
        if(random.nextInt() % chancePerSecond == 0) {
            //song = SoundCategory.MUSIC.getRandomSource(false,true);
            //song.play();
        }
        lastAttempt = time;
    }

    public void stop() {
        if(song == null) {
            return;
        }
        song.stop();
        song = null;
    }

}
