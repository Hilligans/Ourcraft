package dev.hilligans.ourcraft.client.rendering.screens;

import dev.hilligans.ourcraft.client.audio.SoundCategory;
import dev.hilligans.ourcraft.client.Client;
import dev.hilligans.ourcraft.client.rendering.graphics.RenderWindow;
import dev.hilligans.ourcraft.client.rendering.ScreenBase;
import dev.hilligans.ourcraft.client.rendering.widgets.SliderWidget;

public class VolumeScreen extends ScreenBase {

    @Override
    public void buildContentForWindow(RenderWindow window) {
        int windowX = window.getWindowWidth();
        int windowY = window.getWindowHeight();
        addWidget(new SliderWidget("Master", windowX / 16, 50, windowX / 16 * 14, 40, 0, 100,(int)(SoundCategory.MASTER.volume * 100), value -> SoundCategory.MASTER.setVolume(value / 100f)));
        for(int x = 1; x < SoundCategory.soundCategories.size(); x++) {
            SoundCategory soundCategory = SoundCategory.soundCategories.get(x);
            addWidget(new SliderWidget(soundCategory.name,x % 2 == 1 ? windowX / 16 : windowX/ 2 + windowX / 16,50 + 50 * Math.floorDiv((x + 1) , 2), (int) (windowX / 16f * 6),40,0,100,(int)(soundCategory.volume * 100), value -> soundCategory.setVolume(value / 100f)));
        }
    }
}
