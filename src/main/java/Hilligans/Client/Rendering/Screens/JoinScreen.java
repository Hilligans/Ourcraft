package Hilligans.Client.Rendering.Screens;

import Hilligans.Block.Blocks;
import Hilligans.Client.Rendering.Widgets.ServerSelectorWidget;
import Hilligans.ClientMain;
import Hilligans.Client.Rendering.ScreenBase;
import Hilligans.Client.Rendering.Widgets.Button;
import Hilligans.Client.Rendering.Widgets.ButtonAction;
import Hilligans.Client.Rendering.Widgets.InputField;
import Hilligans.Network.ClientNetworkHandler;
import Hilligans.Network.Packet.Client.CSendBlockChanges;
import Hilligans.Util.Settings;

public class JoinScreen extends ScreenBase {

    InputField inputField = new InputField(100,10,200,80);
    public ServerSelectorWidget selected;
    Button play = new Button(100, ClientMain.windowY / 2 + 100, 200, 50, "Join server", new ButtonAction() {
        @Override
        public void onPress() {
            if(selected != null) {
                selected.joinServer();
            }
        }
    }).isEnabled(false);

    public JoinScreen() {
       /* widgets.add(new Button(100, 100, 200, 80, "join server", () -> {
            ClientMain.name = inputField.string;
            ClientMain.joinServer();
            ClientMain.closeScreen();
        }));

        */
        //widgets.add(inputField);
        widgets.add(play);
        widgets.add(new ServerSelectorWidget(100,200,200,80,"localhost","25586",this));

    }

    public void setActive(ServerSelectorWidget selected) {
        this.selected = selected;
        play.enabled = true;
    }


}
