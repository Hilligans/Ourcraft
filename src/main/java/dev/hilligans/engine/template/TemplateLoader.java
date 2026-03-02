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
        ArrayList<CompletableFuture<Tuple<ArrayList<String>, ConcurrentHashMap<String, List<IRegistryElement>>>>> futures = new ArrayList<>();

        gameInstance.MOD_LIST.foreach((mod) -> {
            CompletableFuture<Tuple<ArrayList<String>, ConcurrentHashMap<String, List<IRegistryElement>>>> future = new CompletableFuture<>();
            futures.add(future);

            service.submit(() -> {
                ConcurrentHashMap<String, List<IRegistryElement>> elements = new ConcurrentHashMap<>();
                try {
                    Thread thread = Thread.currentThread();

                    AtomicInteger counter = new AtomicInteger();

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
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    ArrayList<String> keys = new ArrayList<>(elements.keySet());
                    Collections.sort(keys);

                    future.complete(new Tuple<>(keys, elements));
                }
            });
        });


        AtomicInteger i = new AtomicInteger();
        gameInstance.MOD_LIST.foreach((mod) -> {
            CompletableFuture<Tuple<ArrayList<String>, ConcurrentHashMap<String, List<IRegistryElement>>>> future = futures.get(i.getAndIncrement());
            try {
                Tuple<ArrayList<String>, ConcurrentHashMap<String, List<IRegistryElement>>> tuple = future.get();

                ConcurrentHashMap<String, List<IRegistryElement>> elements = tuple.getTypeB();

                for(String s : tuple.getTypeA()) {
                    mod.registerGenCore(elements.get(s));
                }
            } catch (Exception e) {}
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
