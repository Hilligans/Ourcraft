package dev.Hilligans.ourcraft.Client.Audio;

import dev.Hilligans.ourcraft.GameInstance;
import dev.Hilligans.ourcraft.ModHandler.Content.ModContent;
import dev.Hilligans.ourcraft.Resource.ResourceLocation;
import dev.Hilligans.ourcraft.Util.Registry.IRegistryElement;
import dev.Hilligans.ourcraft.Util.Settings;
import dev.Hilligans.ourcraft.Util.Side;
import dev.Hilligans.ourcraft.WorldSave.WorldLoader;
import org.lwjgl.stb.STBVorbisInfo;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.stb.STBVorbis.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class SoundBuffer implements IRegistryElement {

    public int samples;
    public int sampleRate;
    public float length;
    public float rollOff = 12f;
    public SoundCategory soundCategory = SoundCategory.MASTER;
    public String file;
    int channelType;
    public ShortBuffer pcm;
    public ByteBuffer data;
    public byte[] bytes;

    public ModContent source;

    public SoundBuffer(String file)  {
        this.file = file;
    }

    public SoundBuffer(String file, byte[] bytes) {
        this.file = file;
        this.bytes = bytes;
    }

    public SoundBuffer(ShortBuffer shortBuffer) {
        try (STBVorbisInfo info = STBVorbisInfo.malloc()) {
            try {
                pcm = shortBuffer;
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
        return decodeVorbis(source.gameInstance.getResourceDirect(new ResourceLocation(resource,source)),info);
    }

    private  ShortBuffer decodeVorbis(ByteBuffer buffer, STBVorbisInfo info) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer intBuf = stack.mallocInt(1);
            long decoder = stb_vorbis_open_memory(buffer, intBuf, null);
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


    @Override
    public void load(GameInstance gameInstance) {
        if(bytes == null && source.gameInstance.side == Side.CLIENT) {
            this.data = source.gameInstance.getResource(new ResourceLocation(file, source));
            try (STBVorbisInfo info = STBVorbisInfo.malloc()) {
                try {
                    pcm = readVorbis(file, info);
                    sampleRate = info.sample_rate();
                    length = samples / (float) sampleRate;
                    channelType = info.channels() == 1 ? AL_FORMAT_MONO16 : AL_FORMAT_STEREO16;
                } catch (Exception ignored) {
                    ignored.printStackTrace();
                }
            }
        } else {
            try (STBVorbisInfo info = STBVorbisInfo.malloc()) {
                try {
                    ByteBuffer buffer = ByteBuffer.allocateDirect(bytes.length);
                    buffer.put(bytes);
                    pcm = decodeVorbis(buffer,info);
                    sampleRate = info.sample_rate();
                    length = samples / (float) sampleRate;
                    channelType = info.channels() == 1 ? AL_FORMAT_MONO16 : AL_FORMAT_STEREO16;
                } catch (Exception ignored) {
                }
            }
        }
    }

    @Override
    public String getResourceName() {
        return file;
    }

    @Override
    public String getUniqueName() {
        return "sound." + source.getModID() + "." + file;
    }
}
