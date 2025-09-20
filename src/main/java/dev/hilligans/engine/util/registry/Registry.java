package dev.hilligans.engine.util.registry;

import dev.hilligans.engine.EngineMain;
import dev.hilligans.engine.GameInstance;
import dev.hilligans.engine.mod.handler.Identifier;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

public class Registry<T extends IRegistryElement> implements IRegistryElement {

    public HashMap<String,T> MAPPED_ELEMENTS = new HashMap<>();
    public ArrayList<T> ELEMENTS = new ArrayList<>();
    public GameInstance gameInstance;
    public Class<T> classType;
    public String registryType;
    public String owner;

    public boolean coreRegistry;
    public final boolean debug;
    public boolean mapping = true;


    public Registry(GameInstance gameInstance, Class<?> classType, String registryType) {
        this.gameInstance = gameInstance;
        this.classType = (Class<T>) classType;
        this.registryType = registryType;
        this.debug = EngineMain.debug.get(gameInstance);
    }

    public void clear() {
        MAPPED_ELEMENTS.clear();
        ELEMENTS.clear();
    }

    public T get(int index) {
        return ELEMENTS.get(index);
    }

    public T get(String name) {
        if(mapping) {
            return MAPPED_ELEMENTS.get(name);
        } else {
            for(T element : ELEMENTS) {
                if(name.equals(element.getIdentifierName())) {
                    return element;
                }
            }
        }
        return null;
    }

    public void setCoreRegistry() {
        coreRegistry = true;
    }

    public T getExcept(String name) {
        T val = get(name);
        if(val == null) {
            throw new RuntimeException("Unknown resource type "+name+", are you missing the modID?");
            //throw new RuntimeException(STR."Unknown resource type \{name}, are you missing the modID?");
        }
        return val;
    }

    public void put(String name, T element) {
        if (!registryType.equals(element.getResourceType())) {
            throw new RegistryException("Failed to add elements to registry, " + element.getResourceType() + " does not match type of this registry of " + registryType, this);
        }
        //if(gameInstance.EVENT_BUS.postEvent(new RegisterEvent<>(this,name,element)).shouldRun()) {
        if (element != null) {
            element.setUniqueID(getUniqueID());
        }
        if (coreRegistry) {
            if (mapping) {
                if (MAPPED_ELEMENTS.containsKey(name)) {
                    return;
                }
            } else if (ELEMENTS.contains(element)) {
                return;
            }
        }
        if (debug) {
            if (ELEMENTS.contains(element)) {
                throw new RegistryException("Attempting to registry to duplicate element: " + name + " into the registry", this);
            }
            if (mapping) {
                if (MAPPED_ELEMENTS.containsKey(name)) {
                    throw new RegistryException("Attempting to registry to duplicate element: " + name + " into the registry", this);
                }
            }
        }
        if (mapping) {
            MAPPED_ELEMENTS.put(name, element);
        }
        ELEMENTS.add(element);
        // }
    }

    public void putFrom(Registry<?> registry) {
        if(registry.classType.equals(Registry.class)) {
            if(!classType.equals(Registry.class)) {
                throw new RegistryException("Failed to copy container elements to this registry", this);
            }
            registry.forEach(o -> {
                Registry<?> r = (Registry<?>) o;

                Registry<?> myReg = (Registry<?>) computeIfAbsent(r.getIdentifierName(), s -> (T) new Registry<>(gameInstance, r.classType, r.getResourceName()).assignOwner(r.owner));
                myReg.putFrom(r);
            });
        } else {
            putAll((ArrayList<T>) registry.ELEMENTS);
        }
    }

    public Registry<T> duplicate() {
        Registry<T> registry = new Registry<>(gameInstance, classType, registryType);
        registry.owner = this.owner;
        registry.putFrom(this);
        return registry;
    }

    public T computeIfAbsent(String key, Function<String, ? extends T> mappingFunction) {
        T val = null;
        if(mapping) {
            val = MAPPED_ELEMENTS.get(key);
        } else {
            for(T e : ELEMENTS) {
                if(key.equals(e.getIdentifierName())) {
                    val = e;
                    break;
                }
            }
        }
        if(val == null) {
            val = mappingFunction.apply(key);
            put(val);
        }
        return val;
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

    public <Q extends IRegistryElement> void putAllGen(Q[] data) {
        if(data.length != 0) {
            if(data[0] != null) {
                if(!classType.isInstance(data[0])) {
                    throw new RegistryException("Failed to add elements to registry, " + data[0].getClass() + " is not an instance of the class you are trying to register into " + classType, this);
                }
            }
            for(Q t : data) {
                put(t.getIdentifierName(), (T) t);
            }
        }
    }

    public void putAll(ArrayList<T> types) {
        if(!types.isEmpty()) {
            if(types.getFirst() == null) {
                throw new RegistryException("Failed to add elements to registry, " + types.getFirst().getClass() + " does not implement the IRegistryElement class.", this);
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

    public boolean contains(IRegistryElement element) {
        if(!canPut(element)) {
            return false;
        }
        return ELEMENTS.contains(element);
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

    public Registry<T> assignOwner(String owner) {
        this.owner = owner;
        return this;
    }

    public T[] getArray() {
        return ELEMENTS.toArray((T[]) Array.newInstance(classType, ELEMENTS.size()));
    }

    public String listElements() {
        return registryType + ": [" + ELEMENTS.stream().map(IRegistryElement::getResourceName).reduce("", ((a, b) -> a + (a.isBlank() ? "" : ", ") + b)) + "]";
    }

    @Override
    public String toString() {
        return "Registry{" +
                "ELEMENTS=" + ELEMENTS +
                ", registryType='" + registryType + '\'' +
                '}';
    }

    @Override
    public String getResourceName() {
        return registryType;
    }

    @Override
    public String getResourceOwner() {
        return owner;
    }

    @Override
    public String getResourceType() {
        return "registry";
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Registry<?> registry = (Registry<?>) o;
        return Objects.equals(ELEMENTS, registry.ELEMENTS) && Objects.equals(owner, registry.owner) && Objects.equals(registryType, registry.registryType);
    }

    @Override
    public int hashCode() {
        int hashCode = getUniqueName().hashCode();
        for(T element : ELEMENTS) {
            hashCode = 31 * hashCode + element.hashcode();
        }

        return hashCode;
    }
}
