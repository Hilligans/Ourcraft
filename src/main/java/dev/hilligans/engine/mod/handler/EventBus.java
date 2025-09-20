package dev.hilligans.engine.mod.handler;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;

public class EventBus {

    public HashMap<Class<? extends Event>, ArrayList<IEventListener>> events = new HashMap<>();

    public <T extends Event> void register(Class<T> eventClass, Consumer<T> consumer) {
        ArrayList<IEventListener> list = events.computeIfAbsent(eventClass, k -> new ArrayList<>());
        list.add(e-> consumer.accept((T)e));
    }

    public <T extends Event> void register(Consumer<T> consumer, Class<T> eventClass) {
        register(eventClass,consumer);
    }

    public void register(Object obj) {
        Method[] methods = obj.getClass().getMethods();
        for(Method method : methods) {
            if(method.getParameterCount() == 1) {
                Class<? extends Event> mainClass = (Class<? extends Event>) method.getParameterTypes()[0];
                Class<?> superclass = mainClass.getSuperclass();
                if(superclass != null) {
                    ArrayList<IEventListener> list = events.computeIfAbsent(mainClass, k -> new ArrayList<>());
                    list.add(event -> {
                        try {
                            method.invoke(obj, event);
                        } catch (Exception ignored) {}
                    });
                }
            }
        }
    }

    public Event postEvent(Event event) {
        ArrayList<IEventListener> list = events.get(event.getClass());
        if(list != null) {
            for(IEventListener eventListener : list) {
                eventListener.run(event);
            }
        }
        return event;
    }
}
