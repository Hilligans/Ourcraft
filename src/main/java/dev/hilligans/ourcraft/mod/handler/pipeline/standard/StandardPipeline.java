package dev.hilligans.ourcraft.mod.handler.pipeline.standard;

import dev.hilligans.ourcraft.GameInstance;
import dev.hilligans.ourcraft.mod.handler.content.CoreExtensionView;
import dev.hilligans.ourcraft.mod.handler.content.ModContainer;
import dev.hilligans.ourcraft.mod.handler.content.ModList;
import dev.hilligans.ourcraft.mod.handler.content.RegistryView;
import dev.hilligans.ourcraft.mod.handler.pipeline.InstanceLoaderPipeline;
import dev.hilligans.ourcraft.mod.handler.pipeline.PerModPipelineStage;
import dev.hilligans.ourcraft.mod.handler.pipeline.PipelineStage;
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
        pipeline.addStage("Locate Mods", (pipeline1, s) -> pipeline1.setModList(new ModList().load()));

        //load all the registries defined in each mod
        pipeline.addStage("Register Registries", (pipeline1, s) -> pipeline1.getModList().foreach(container -> container.modClass.registerRegistries(registryView)));

        //register each of the mods core extensions into its
        pipeline.addStage("Register Core Extensions", (pipeline1, s) -> pipeline1.getModList().foreach(modContainer -> modContainer.modClass.registerCoreExtensions(new CoreExtensionView(modContainer))));

        //run post core hooks, clients will be started here
        pipeline.addStage("Post Core Hooks", (pipeline1, section) -> pipeline1.getGameInstance().runPostCoreHooks());

        //this is the main loading code which will load each mods content on its own thread
        pipeline.addStage("Register Content Async", (PerModPipelineStage<StandardPipeline>) modContainer -> modContainer.modClass.registerContent(modContainer));

        //this will copy all the loaded content to the game instance in a predictable way
        pipeline.addStage("Copy Content Back", (pipeline12, section) -> pipeline12.getModList().foreach(modContainer -> gameInstance.REGISTRIES.putFrom(modContainer.registries)));


        pipeline.addStage("Finish Building", (pipeline13, section) -> pipeline13.getGameInstance().built = true);

        return pipeline;
    }
}
