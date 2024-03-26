package dev.hilligans.ourcraft.client.audio;

import dev.hilligans.ourcraft.util.registry.IRegistryElement;

import java.util.ArrayList;

public class SoundCategory implements IRegistryElement {

    public static final ArrayList<SoundCategory> soundCategories = new ArrayList<>();

    public static final SoundCategory MASTER = new SoundCategory("Master");
    public static final SoundCategory MUSIC = new SoundCategory("Music").addParent(MASTER);

    public float volume = 1f;
    public String name;
    public SoundCategory parent;
    public ArrayList<SoundCategory> children = new ArrayList<>();
    public ArrayList<SoundBuffer> sounds = new ArrayList<>();
    public ArrayList<SoundSource> soundSources = new ArrayList<>();

    public SoundCategory(String name) {
        this.name = name;
        soundCategories.add(this);
    }

    public SoundCategory addParent(SoundCategory soundCategory) {
        soundCategory.children.add(this);
        parent = soundCategory;
        return this;
    }

    public float getVolume() {
        if(parent != null) {
            return parent.getVolume() * volume;
        }
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
        updateVolume();
    }

    public void addSource(SoundSource soundSource) {
        soundSources.add(soundSource);
        soundSource.setDefaultVolume(getVolume());
    }

    public void removeSource(SoundSource soundSource) {
        soundSources.remove(soundSource);
    }

    public void updateVolume() {
        float volume = getVolume();
        for(SoundSource soundSource : soundSources) {
            soundSource.setDefaultVolume(volume);
        }
        for(SoundCategory soundCategory : children) {
            soundCategory.updateVolume();
        }
    }

    public SoundBuffer getRandomBuffer() {
        int pos = (int)(sounds.size() * Math.random());
        return sounds.get(pos == sounds.size() ? pos - 1 : pos);
    }

    public SoundSource getRandomSource(boolean loop, boolean relative) {
        SoundBuffer soundBuffer = getRandomBuffer();
        return soundBuffer.createNewSound(loop,relative, this);
    }

    @Override
    public String getResourceName() {
        return null;
    }

    @Override
    public String getResourceOwner() {
        return null;
    }

    @Override
    public String getResourceType() {
        return null;
    }
}
