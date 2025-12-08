package dev.hilligans.engine.template;

import dev.hilligans.engine.GameInstance;
import dev.hilligans.engine.data.Tuple;
import dev.hilligans.engine.mod.content.ModContainer;
import dev.hilligans.engine.schema.Schema;
import dev.hilligans.engine.util.registry.IRegistryElement;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;

public class TemplateLoader {

    public static ExecutorService service = Executors.newVirtualThreadPerTaskExecutor();

    public static void loadTemplates(GameInstance gameInstance) {
        Thread parentThread = Thread.currentThread();
        AtomicInteger completedMods = new AtomicInteger(gameInstance.MOD_LIST.getCount());

        ConcurrentHashMap<String, Tuple<ArrayList<String>, ConcurrentHashMap<String, List<IRegistryElement>>>> allContent = new ConcurrentHashMap<>();

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

                    allContent.put(mod.getModID(), new Tuple<>(keys, elements));
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

        gameInstance.MOD_LIST.foreach((m) -> {
            Tuple<ArrayList<String>, ConcurrentHashMap<String, List<IRegistryElement>>> tuple = allContent.getOrDefault(m.getModID(), null);

            if(tuple != null) {
                ConcurrentHashMap<String, List<IRegistryElement>> elements = tuple.getTypeB();

                for(String s : tuple.getTypeA()) {
                    m.registerGenCore(elements.get(s));
                }
            }
        });
    }

    public static List<IRegistryElement> loadTemplate(GameInstance gameInstance, ModContainer mod, String path) {
        Object data = gameInstance.RESOURCE_LOADER.getResource(path);
        if(data == null) {
            // If this fails, something is borked. the path was provided to us.
            throw new RuntimeException("Failed to load resource: " + path);
        }

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
