package Hilligans.Client.Rendering.Screens;

import Hilligans.ClientMain;
import Hilligans.Client.Rendering.ScreenBase;
import Hilligans.Client.Rendering.Widgets.Button;
import Hilligans.Client.Rendering.Widgets.ButtonAction;
import Hilligans.Client.Rendering.Widgets.InputField;

public class JoinScreen extends ScreenBase {

    InputField inputField = new InputField(100,10,200,80);

    public JoinScreen() {
        widgets.add(new Button(100, 100, 200, 80, "join server", () -> {
            ClientMain.name = inputField.string;
            ClientMain.joinServer();
            ClientMain.closeScreen();
        }));
        widgets.add(inputField);

    }


}
