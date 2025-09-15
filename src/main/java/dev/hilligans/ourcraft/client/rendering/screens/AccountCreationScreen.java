package dev.hilligans.ourcraft.client.rendering.screens;

import dev.hilligans.ourcraft.Ourcraft;
import dev.hilligans.engine.client.graphics.resource.MatrixStack;
import dev.hilligans.ourcraft.client.rendering.ScreenBase;
import dev.hilligans.engine.client.graphics.RenderWindow;
import dev.hilligans.engine.client.graphics.api.GraphicsContext;
import dev.hilligans.ourcraft.client.rendering.widgets.Button;
import dev.hilligans.ourcraft.client.rendering.widgets.InputField;

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
        }));

    }

    @Override
    public void drawScreen(RenderWindow window, MatrixStack matrixStack, GraphicsContext graphicsContext) {
        super.drawScreen(window, matrixStack, graphicsContext);
        window.getStringRenderer().drawStringInternal(window, graphicsContext, matrixStack,debug,100,700,0.5f);
    }
}
