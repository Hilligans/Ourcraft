package dev.hilligans.engine.test.standard;

import dev.hilligans.engine.GameInstance;
import dev.hilligans.engine.test.ITest;
import dev.hilligans.engine.util.registry.IRegistryElement;
import dev.hilligans.engine.util.registry.Registry;

import java.io.File;

public class TrackedAllocationTest implements ITest {

    public TrackedAllocationTest() {
        track();
    }

    @Override
    public void runTest(GameInstance gameInstance) {
        if(IRegistryElement.tracking) {
            gameInstance.REGISTRIES.forEach((registry -> {
                registry.forEach(element -> {
                    if (IRegistryElement.objectAllocationTrack.getOrDefault(element, null) == null) {
                        throw new RuntimeException(getFormattedPath(element.getClass(), "not being tracked, instance: " + element.getUniqueName()));
                    }
                });
            }));
        } else {
            System.out.println("Object tracking disabled, skipping tracked allocation test.");
        }
    }

    public static String getFormattedPath(Class<?> clazz, String error) {
        String userDir = System.getProperty("user.dir");
        String relativePath = "src/main/java/" + clazz.getName().stripLeading().replace(".", "/") + ".java";
        File sourceFile = new File(userDir, relativePath);
        String absolutePath = sourceFile.getAbsolutePath();

        return String.format("[file://%s:%d] %s", absolutePath, 1, error);
    }

    @Override
    public String getResourceName() {
        return "";
    }

    @Override
    public String getResourceOwner() {
        return "";
    }
}
