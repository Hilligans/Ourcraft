package dev.Hilligans.ourcraft.Client.Rendering.Screens;

import dev.Hilligans.ourcraft.Client.Client;
import dev.Hilligans.ourcraft.Client.MatrixStack;
import dev.Hilligans.ourcraft.Client.Rendering.Graphics.RenderWindow;
import dev.Hilligans.ourcraft.Client.Rendering.ScreenBase;
import dev.Hilligans.ourcraft.Client.Rendering.Widgets.Button;
import dev.Hilligans.ourcraft.Client.Rendering.Widgets.FolderWidget;
import dev.Hilligans.ourcraft.Data.Primitives.IntegerWrapper;
import dev.Hilligans.ourcraft.Tag.CompoundNBTTag;
import dev.Hilligans.ourcraft.WorldSave.FileLoader;
import dev.Hilligans.ourcraft.WorldSave.WorldLoader;
import org.lwjgl.PointerBuffer;

public class TagEditorScreen extends ScreenBase {

    static final int size = 29;

    CompoundNBTTag compoundTag;
    public String filePath;
    public FolderWidget folderWidget;
    public IntegerWrapper yOffset;

    public TagEditorScreen(Client client) {
        super(client);
        folderWidget = new FolderWidget(100,100,100,40,"Data");
        folderWidget.minY = 100 + FolderWidget.spacing;
        addWidget(folderWidget);
        addWidget(new Button(0, 0, 100, 40, "Open", () -> FileLoader.openFile("", null, new FileLoader.HandleFile() {
            @Override
            public void success(PointerBuffer path) {
                path.mark();
                compoundTag = WorldLoader.loadTag(path.getStringUTF8());
                path.reset();
                filePath = path.getStringUTF8();
                yOffset = new IntegerWrapper(0);
                folderWidget.addAll(compoundTag);
                folderWidget.addOffset(yOffset);
                folderWidget.yOffset = new IntegerWrapper(0);
                folderWidget.minY = 100;
            }
            @Override
            public void cancel() {
                client.openScreen(new JoinScreen(client));
            }
        })));
        addWidget(new Button(100, 0, 100, 40, "Save", () -> WorldLoader.save(compoundTag,filePath)));
    }

    @Override
    public void mouseClick(int x, int y, int mouseButton) {
        super.mouseClick(x, y, mouseButton);
    }

    @Override
    public void mouseScroll(int x, int y, float amount) {
        if(amount == 1.0f) {
            if(yOffset.value < 0) {
                yOffset.value += FolderWidget.spacing;
            }
        } else {
            yOffset.value-= FolderWidget.spacing;
        }
    }
}
