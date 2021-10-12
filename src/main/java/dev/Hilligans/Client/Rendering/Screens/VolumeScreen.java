package dev.Hilligans.Client.Rendering.Screens;

import dev.Hilligans.Client.Audio.SoundCategory;
import dev.Hilligans.Client.Client;
import dev.Hilligans.Client.Rendering.ScreenBase;
import dev.Hilligans.Client.Rendering.Widgets.SliderWidget;
import dev.Hilligans.ClientMain;

public class VolumeScreen extends ScreenBase {

    public VolumeScreen(Client client) {
        super(client);
        addWidget(new SliderWidget("Master", client.windowX / 16, 50, ClientMain.getWindowX() / 16 * 14, 40, 0, 100,(int)(SoundCategory.MASTER.volume * 100), value -> SoundCategory.MASTER.setVolume(value / 100f)));
        for(int x = 1; x < SoundCategory.soundCategories.size(); x++) {
            SoundCategory soundCategory = SoundCategory.soundCategories.get(x);
            addWidget(new SliderWidget(soundCategory.name,x % 2 == 1 ? client.windowX / 16 : client.windowX / 2 + client.windowX / 16,50 + 50 * Math.floorDiv((x + 1) , 2), (int) (client.windowX / 16f * 6),40,0,100,(int)(soundCategory.volume * 100), value -> soundCategory.setVolume(value / 100f)));
        }

    }
}
