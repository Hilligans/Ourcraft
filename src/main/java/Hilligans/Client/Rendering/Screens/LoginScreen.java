package Hilligans.Client.Rendering.Screens;

import Hilligans.Client.Rendering.ScreenBase;
import Hilligans.Client.Rendering.Widgets.Button;
import Hilligans.Client.Rendering.Widgets.ButtonAction;
import Hilligans.Client.Rendering.Widgets.InputField;
import Hilligans.ClientMain;
import Hilligans.Network.ClientAuthNetworkHandler;
import Hilligans.Network.Packet.AuthServerPackets.CLogin;
import Hilligans.Ourcraft;

public class LoginScreen extends ScreenBase {

    InputField username = new InputField(100,100,200,100, "menu.account_name");
    InputField password = new InputField(100,250,200,100, "menu.password");
    InputField email = new InputField(100,400,200,100,"menu.email");

    public LoginScreen() {
        widgets.add(username);
        widgets.add(password);
        widgets.add(email);

        widgets.add(new Button(100, 550, 200, 50, "Log in", () -> {
            ClientMain.getClient().playerData.email = email.string;
            String passwordVal = Ourcraft.hashString(password.string, email.string);
            ClientMain.getClient().playerData.userName = username.string;
            ClientMain.getClient().saveUsernameAndPassword();
            ClientAuthNetworkHandler.sendPacketDirect(new CLogin(username.string,passwordVal,email.string));
            ClientMain.getClient().openScreen(new JoinScreen());
        }));
    }

}
