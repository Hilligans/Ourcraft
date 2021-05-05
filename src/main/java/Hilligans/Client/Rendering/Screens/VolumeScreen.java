package Hilligans.Client.Rendering.Screens;

import Hilligans.Client.Audio.SoundCategory;
import Hilligans.Client.Client;
import Hilligans.Client.Rendering.ScreenBase;
import Hilligans.Client.Rendering.Widgets.SliderChange;
import Hilligans.Client.Rendering.Widgets.SliderWidget;
import Hilligans.ClientMain;

public class VolumeScreen extends ScreenBase {

    public VolumeScreen() {

        widgets.add(new SliderWidget("Master", ClientMain.getWindowX() / 16, 50, ClientMain.getWindowX() / 16 * 14, 40, 0, 100,(int)SoundCategory.MASTER.volume * 100, value -> SoundCategory.MASTER.setVolume(value / 100f)));
        for(int x = 1; x < SoundCategory.soundCategories.size(); x++) {
            SoundCategory soundCategory = SoundCategory.soundCategories.get(x);
            widgets.add(new SliderWidget(soundCategory.name,x % 2 == 1 ? ClientMain.getWindowX() / 16 : ClientMain.getWindowX() / 2 + ClientMain.getWindowX() / 16,50 + 50 * Math.floorDiv((x + 1) , 2), (int) (ClientMain.getWindowX() / 16f * 6),40,0,100,(int)(soundCategory.volume * 100), value -> soundCategory.setVolume(value / 100f)));
        }

    }





}
