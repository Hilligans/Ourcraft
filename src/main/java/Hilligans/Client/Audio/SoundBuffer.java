package Hilligans.Client.Audio;

import Hilligans.WorldSave.WorldLoader;
import org.lwjgl.stb.STBVorbisInfo;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.stb.STBVorbis.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class SoundBuffer {

    public int samples;
    public int sampleRate;
    public float length;
    public float rollOff = 12f;
    public SoundCategory soundCategory = SoundCategory.MASTER;
    int channelType;
    ShortBuffer pcm;

    public SoundBuffer(String file)  {
        Sounds.sounds.add(this);
        try (STBVorbisInfo info = STBVorbisInfo.malloc()) {
            try {
                pcm = readVorbis(file, info);
                sampleRate = info.sample_rate();
                length = samples / (float) sampleRate;
                channelType = info.channels() == 1 ? AL_FORMAT_MONO16 : AL_FORMAT_STEREO16;
            } catch (Exception ignored) {}
        }
    }

    public SoundSource createNewSound(boolean loop, boolean relative, SoundCategory soundCategory) {
        int bufferID = alGenBuffers();
        alBufferData(bufferID,channelType,pcm,sampleRate);
        return new SoundSource(loop,relative,soundCategory).setBuffer(bufferID).defaultEndTime(length);
    }

    public SoundBuffer setRollOff(float rollOff) {
        this.rollOff = rollOff;
        return this;
    }

    public SoundBuffer setCategory(SoundCategory category) {
        this.soundCategory = category;
        return this;
    }

    public void cleanup() {
        if (pcm != null) {
            MemoryUtil.memFree(pcm);
        }
    }

    private ShortBuffer readVorbis(String resource, STBVorbisInfo info) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            ByteBuffer vorbis = WorldLoader.readResource(resource);
            IntBuffer intBuf = stack.mallocInt(1);
            long decoder = stb_vorbis_open_memory(vorbis, intBuf, null);
            if (decoder == NULL) {
                throw new RuntimeException("Failed to open Ogg Vorbis file. Error: " + intBuf.get(0));
            }
            stb_vorbis_get_info(decoder, info);
            int channels = info.channels();
            samples = stb_vorbis_stream_length_in_samples(decoder);
            pcm = MemoryUtil.memAllocShort(samples);
            pcm.limit(stb_vorbis_get_samples_short_interleaved(decoder, channels, pcm) * channels);
            stb_vorbis_close(decoder);

            return pcm;
        }
    }


}
