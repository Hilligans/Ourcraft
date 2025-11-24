package dev.hilligans.engine.templates;

import dev.hilligans.engine.GameInstance;
import dev.hilligans.engine.mod.handler.content.ModContainer;
import dev.hilligans.engine.schema.Schema;
import dev.hilligans.engine.util.registry.IRegistryElement;
import org.json.JSONObject;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;

public class TemplateLoader {

    public static ExecutorService service = Executors.newVirtualThreadPerTaskExecutor();

    public static void loadTemplates(GameInstance gameInstance) {
        Thread parentThread = Thread.currentThread();
        AtomicInteger completedMods = new AtomicInteger(gameInstance.MOD_LIST.getCount());

        gameInstance.MOD_LIST.foreach((mod) -> {
            System.out.println("Searching path:" + mod.getModID() + "/templates");
            service.submit(() -> {
                try {
                    Thread thread = Thread.currentThread();

                    AtomicInteger counter = new AtomicInteger();
                    ConcurrentHashMap<String, List<IRegistryElement>> elements = new ConcurrentHashMap<>();

                    gameInstance.DATA_LOADER.forEach(mod.getModID() + "/templates",
                            s -> {
                                counter.getAndIncrement();
                                service.submit(() -> {
                                    try {
                                        elements.put(s, loadTemplate(gameInstance, mod, s));
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }  finally {
                                        if(counter.decrementAndGet() == 0) {
                                            LockSupport.unpark(thread);
                                        }
                                    }
                                });
                            });

                    while (counter.get() != 0) {
                        LockSupport.parkNanos(1000 * 100);
                    }

                    ArrayList<String> keys = new ArrayList<>(Arrays.asList(elements.keySet().toArray(String[]::new)));
                    Collections.sort(keys);

                    for (String key : keys) {
                        mod.registerGen(elements.get(key));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    if (completedMods.decrementAndGet() == 0) {
                        LockSupport.unpark(parentThread);
                    }
                }
            });
        });

        while(completedMods.get() != 0) {
            LockSupport.parkNanos(1000 * 1000);
        }
    }

    public static List<IRegistryElement> loadTemplate(GameInstance gameInstance, ModContainer mod, String path) {
        Object data = gameInstance.RESOURCE_LOADER.getResource(path);
        Schema<?> schema = gameInstance.getSchema(data);

        if(schema == null) {
            throw new RuntimeException("No schema found for class: " + data.getClass());
        }

        Schema.Data schemaData = schema.get(data);
        String templateName = schemaData.optString("template", null);

        if(templateName == null) {
            throw new RuntimeException("Template file: " + path + " missing template format");
        }

        ITemplate<?> template = gameInstance.TEMPLATES.getExcept(templateName);

        return template.parse(gameInstance, schemaData, path, mod.getModID());
    }
}
