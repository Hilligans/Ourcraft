package dev.hilligans.ourcraft.mod.handler.pipeline.standard;

import dev.hilligans.ourcraft.GameInstance;
import dev.hilligans.ourcraft.mod.handler.content.CoreExtensionView;
import dev.hilligans.ourcraft.mod.handler.content.ModContainer;
import dev.hilligans.ourcraft.mod.handler.content.ModList;
import dev.hilligans.ourcraft.mod.handler.content.RegistryView;
import dev.hilligans.ourcraft.mod.handler.pipeline.InstanceLoaderPipeline;
import dev.hilligans.ourcraft.mod.handler.pipeline.PerModPipelineStage;
import dev.hilligans.ourcraft.mod.handler.pipeline.PipelineStage;
import dev.hilligans.ourcraft.resource.loaders.ResourceLoader;
import dev.hilligans.ourcraft.util.registry.IRegistryElement;
import dev.hilligans.ourcraft.util.registry.Registry;
import dev.hilligans.ourcraft.util.sections.ISection;

import java.util.function.Consumer;

public class StandardPipeline extends InstanceLoaderPipeline<StandardPipeline> {

    public StandardPipeline(GameInstance gameInstance) {
        super(gameInstance);
    }
    public static InstanceLoaderPipeline get(GameInstance gameInstance) {

        StandardPipeline pipeline = new StandardPipeline(gameInstance);
        RegistryView registryView = new RegistryView(gameInstance);


        //load all mods
        pipeline.addStage("Locate Mods", (pipeline1, s) -> pipeline1.setModList(new ModList(gameInstance).load()));

        //load all the registries defined in each mod
        pipeline.addStage("Register Registries", (pipeline1, s) -> pipeline1.getModList().foreach(container -> {
            container.modClass.registerRegistries(registryView);
            gameInstance.copyRegistries();
        }));

        //register each of the mods core extensions into its
        pipeline.addStage("Register Core Extensions", (pipeline1, s) -> pipeline1.getModList().foreach(modContainer -> {
            modContainer.setGameInstance(pipeline1.getGameInstance());
            modContainer.modClass.registerCoreExtensions(new CoreExtensionView(modContainer));
        }));

        pipeline.addStage("Build Resource Loaders", (pipeline14, section) -> {
            Registry<ResourceLoader<?>> resourceLoaderRegistry = (Registry<ResourceLoader<?>>) pipeline14.getGameInstance().REGISTRIES.getExcept("ourcraft:resource_loader");
            for(ResourceLoader<?> resourceLoader : resourceLoaderRegistry.ELEMENTS) {
                gameInstance.RESOURCE_LOADER.add(resourceLoader);
            }
        });

        pipeline.addStage("Build Content For Game Instance", (pipeline15, section) -> {
            for (Registry<?> registry : pipeline15.getGameInstance().REGISTRIES.ELEMENTS) {
                for (Object o : registry.ELEMENTS) {
                    if (o instanceof IRegistryElement) {
                        ((IRegistryElement) o).load(pipeline15.getGameInstance());
                    }
                }
            }
        });

        //run post core hooks, clients will be started here
        pipeline.addStage("Post Core Hooks", (pipeline1, section) -> pipeline1.runPostCoreHooks());

        //this is the main loading code which will load each mods content on its own thread
        pipeline.addStage("Register Content Async", (PerModPipelineStage<StandardPipeline>) modContainer -> modContainer.modClass.registerContent(modContainer));

        //this will copy all the loaded content to the game instance in a predictable way
        pipeline.addStage("Copy Content Back", (pipeline12, section) -> pipeline12.getModList().foreach(modContainer -> pipeline12.getGameInstance().REGISTRIES.putFrom(modContainer.registries)));

        pipeline.addStage("Build Content For Game Instance", (pipeline15, section) -> pipeline15.getGameInstance().finishBuild());

        pipeline.addStage("Post Hooks", (pipeline1, section) -> pipeline1.runPostHooks());

        pipeline.addStage("Finish Building", (pipeline13, section) -> {pipeline13.getGameInstance().builtSemaphore.release();});

        return pipeline;
    }
}
