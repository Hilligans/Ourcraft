package dev.hilligans.ourcraft.client.rendering.screens;

import dev.hilligans.ourcraft.client.rendering.ScreenBase;
import dev.hilligans.ourcraft.client.rendering.widgets.Button;
import dev.hilligans.ourcraft.client.rendering.widgets.FolderWidget;
import dev.hilligans.engine.data.IntegerWrapper;
import dev.hilligans.engine.save.FileLoader;
import dev.hilligans.engine.tag.CompoundNBTTag;
import org.lwjgl.PointerBuffer;

public class TagEditorScreen extends ScreenBase {

    static final int size = 29;

    CompoundNBTTag compoundTag;
    public String filePath;
    public FolderWidget folderWidget;
    public IntegerWrapper yOffset;

    public TagEditorScreen() {
        folderWidget = new FolderWidget(100,100,100,40,"Data");
        folderWidget.minY = 100 + FolderWidget.spacing;
        addWidget(folderWidget);
        addWidget(new Button(0, 0, 100, 40, "Open", () -> FileLoader.openFile("", null, new FileLoader.HandleFile() {
            @Override
            public void success(PointerBuffer path) {
                path.mark();
                compoundTag = FileLoader.loadTag(path.getStringUTF8());
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
                getClient().openScreen(new JoinScreen());
            }
        })));
        addWidget(new Button(100, 0, 100, 40, "Save", () -> FileLoader.save(compoundTag,filePath)));
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
