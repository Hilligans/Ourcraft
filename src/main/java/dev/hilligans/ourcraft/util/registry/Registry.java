package dev.hilligans.ourcraft.util.registry;

import dev.hilligans.ourcraft.GameInstance;
import dev.hilligans.ourcraft.mod.handler.content.UnknownResourceException;
import dev.hilligans.ourcraft.mod.handler.events.common.RegisterEvent;
import dev.hilligans.ourcraft.mod.handler.Identifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;

public class Registry<T extends IRegistryElement> implements IRegistryElement {

    public HashMap<String,T> MAPPED_ELEMENTS = new HashMap<>();
    public ArrayList<T> ELEMENTS = new ArrayList<>();
    public GameInstance gameInstance;
    public Class<?> classType;
    public String registryType;

    public Registry(GameInstance gameInstance, Class<?> classType, String registryType) {
        this.gameInstance = gameInstance;
        this.classType = classType;
        this.registryType = registryType;
    }

    public void clear() {
        MAPPED_ELEMENTS.clear();
        ELEMENTS.clear();
    }

    public T get(int index) {
        return ELEMENTS.get(index);
    }

    public T get(String name) {
        return MAPPED_ELEMENTS.get(name);
    }

    public T getExcept(String name) {
        T val = get(name);
        if(val == null) {
            throw new RuntimeException(STR."Unknown resource type \{name}, are you missing the modID?");
        }
        return val;
    }

    public void put(String name, T element) {
        if(!registryType.equals(element.getResourceType())) {
            throw new RegistryException("Failed to add elements to registry, " + element.getResourceType() + " does not match type of this registry of " + registryType, this);
        }
        if(gameInstance.EVENT_BUS.postEvent(new RegisterEvent<>(this,name,element)).shouldRun()) {
            if(element != null) {
                element.setUniqueID(getUniqueID());
            }
            MAPPED_ELEMENTS.put(name, element);
            ELEMENTS.add(element);
        }
    }

    public void put(T element) {
        put(element.getIdentifierName(), element);
    }

    public void put(Identifier identifier, T element) {
        put(identifier.getName(),element);
    }

    public void putAll(T[] types) {
        if(types.length != 0) {
            if(types[0] == null) {
                throw new RegistryException("Failed to add elements to registry, " + types[0].getClass() + " does not implement the IRegistryElement class.", this);
            }
            for(T t : types) {
                put(t.getIdentifierName(), t);
            }
        }
    }

    public void putUnchecked(String name, Object element) {
        put(name,(T)element);
    }

    public boolean replace(String name, Object object) {
        if(!canPut(object)) {
            throw new RegistryException("Failed to register object replacement, object " + object.getClass() + " is not an instance of " + classType, this);
        }
        if(!MAPPED_ELEMENTS.containsKey(name)) {
            return false;
        }
        T newElem = (T)object;
        T oldElem = MAPPED_ELEMENTS.replace(name, newElem);
        for(int x = 0; x < ELEMENTS.size(); x++) {
            if(ELEMENTS.get(x) == oldElem) {
                ELEMENTS.set(x, newElem);
                return true;
            }
        }
        return false;
    }

    public T remove(String name) {
        T element = MAPPED_ELEMENTS.remove(name);
        for(int x = 0; x < ELEMENTS.size(); x++) {
            T t = ELEMENTS.get(x);
            if(t == element) {
                ELEMENTS.remove(x);
                x--;
            }
        }
        return element;
    }

    public void recursivelyClear() {
        for(T element : ELEMENTS) {
            if(element instanceof Registry registry) {
                registry.recursivelyClear();
            }
        }
        clear();
    }

    public boolean canPut(Object o) {
        if(classType == null) {
            return false;
        }
        return classType.isAssignableFrom(o.getClass());
    }

    public void forEach(Consumer<T> consumer) {
        for(T element : ELEMENTS) {
            consumer.accept(element);
        }
    }

    public int getUniqueID() {
        return ELEMENTS.size();
    }

    @Override
    public String getResourceName() {
        return registryType;
    }

    @Override
    public String getResourceOwner() {
        return "ourcraft";
    }

    @Override
    public String getResourceType() {
        return "registry";
    }
}
