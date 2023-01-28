package dev.hilligans.ourcraft.Client.Rendering.Screens;

import dev.hilligans.ourcraft.Client.Audio.SoundCategory;
import dev.hilligans.ourcraft.Client.Client;
import dev.hilligans.ourcraft.Client.Rendering.Graphics.RenderWindow;
import dev.hilligans.ourcraft.Client.Rendering.ScreenBase;
import dev.hilligans.ourcraft.Client.Rendering.Widgets.SliderWidget;

public class VolumeScreen extends ScreenBase {

    public VolumeScreen(Client client) {
        super(client);
    }

    @Override
    public void buildContentForWindow(RenderWindow window) {
        int windowX = (int) window.getWindowWidth();
        int windowY = (int) window.getWindowHeight();
        addWidget(new SliderWidget("Master", windowX / 16, 50, windowX / 16 * 14, 40, 0, 100,(int)(SoundCategory.MASTER.volume * 100), value -> SoundCategory.MASTER.setVolume(value / 100f)));
        for(int x = 1; x < SoundCategory.soundCategories.size(); x++) {
            SoundCategory soundCategory = SoundCategory.soundCategories.get(x);
            addWidget(new SliderWidget(soundCategory.name,x % 2 == 1 ? windowX / 16 : windowX/ 2 + windowX / 16,50 + 50 * Math.floorDiv((x + 1) , 2), (int) (windowX / 16f * 6),40,0,100,(int)(soundCategory.volume * 100), value -> soundCategory.setVolume(value / 100f)));
        }
    }
}
