package dev.hilligans.ourcraft.client.rendering.screens;

import dev.hilligans.ourcraft.client.Client;
import dev.hilligans.ourcraft.client.MatrixStack;
import dev.hilligans.ourcraft.client.rendering.graphics.RenderWindow;
import dev.hilligans.ourcraft.client.rendering.ScreenBase;
import dev.hilligans.ourcraft.client.rendering.widgets.Button;
import dev.hilligans.ourcraft.client.rendering.widgets.InputField;
import dev.hilligans.ourcraft.network.packet.auth.CCreateAccount;
import dev.hilligans.ourcraft.Ourcraft;

public class AccountCreationScreen extends ScreenBase {

    public String debug = "";

    InputField username = new InputField(100,100,200,100, "Account name");
    InputField password = new InputField(100,250,200,100, "Password");
    InputField email = new InputField(100,400,200,100,"Email");
    InputField token = new InputField(100,550,200,100,"Email Token");

    public AccountCreationScreen() {
        addWidget(username);
        addWidget(password);
        addWidget(email);
        addWidget(token);

        addWidget(new Button(500, 200, 200, 50, "Create Account", () -> {
            getClient().playerData.email = email.string;
            String passwordVal = Ourcraft.hashString(password.string, email.string);
            getClient().playerData.userName = username.string;
            getClient().saveUsernameAndPassword();
            getClient().authNetwork.sendPacket(new CCreateAccount(username.string,passwordVal,email.string,token.string));
        }));

    }

    @Override
    public void drawScreen(RenderWindow window, MatrixStack matrixStack) {
        super.drawScreen(window, matrixStack);
        window.getStringRenderer().drawStringInternal(window, matrixStack,debug,100,700,0.5f);
    }
}
