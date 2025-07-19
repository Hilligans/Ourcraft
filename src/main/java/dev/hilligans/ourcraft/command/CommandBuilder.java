package dev.hilligans.ourcraft.command;

import dev.hilligans.ourcraft.Ourcraft;
import dev.hilligans.ourcraft.command.executors.CommandExecutor;
import dev.hilligans.ourcraft.util.EnumParser;
import dev.hilligans.ourcraft.util.interfaces.QuadConsumer;
import dev.hilligans.ourcraft.util.interfaces.QuinConsumer;
import dev.hilligans.ourcraft.util.interfaces.SextConsumer;
import dev.hilligans.ourcraft.util.interfaces.TriConsumer;
import dev.hilligans.ourcraft.util.registry.IRegistryElement;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class CommandBuilder {

    public static EmptyCommand<CommandExecutor> create(String... aliases) {
        return new EmptyCommand<>(CommandExecutor.class, aliases);
    }

    public static <T extends CommandExecutor> EmptyCommand<T> create(Class<T> clazz, String... aliases) {
        return new EmptyCommand<>(clazz, aliases);
    }

    public static abstract class BuilderCommand<T> extends Command {
        public int argCount;
        public Class<T> executorClass;

        public BuilderCommand(Class<T> executorClass, int argCount, String[] aliases) {
            super(aliases[0]);
            this.executorClass = executorClass;
            this.argCount = argCount;
            this.aliases = aliases;
        }

        public BuilderCommand(BuilderCommand<T> parent) {
            super(parent.name);
            this.executorClass = parent.executorClass;
            this.argCount = parent.argCount + 1;
            this.aliases = parent.aliases;
            this.permissions = parent.permissions;
        }

        @Override
        public boolean execute(CommandExecutor executor, String[] args) {
            if(args.length < argCount || !executorClass.isInstance(executor)) {
                return false;
            }
            parseAndExecute((T)executor, args);
            return true;
        }

        public abstract void parseAndExecute(T executor, String[] args);
    }


    public static class EmptyCommand<T extends CommandExecutor> extends BuilderCommand<T> {
        public Consumer<T> consumer;

        public EmptyCommand(Class<T> executorClass, String... aliases) {
            super(executorClass, 0, aliases);
        }

        public EmptyCommand(EmptyCommand<T> self, Consumer<T> consumer) {
            super(self);
            this.argCount = self.argCount;

            this.consumer = consumer;
        }

        @Override
        public void parseAndExecute(T executor, String[] args) {
            consumer.accept(executor);
        }

        public <X> SingleCommand<T, X> with(Function<String, X> parser) {
            return new SingleCommand<>(this, parser);
        }

        public SingleCommand<T, Boolean> withBool() { return with(Boolean::parseBoolean); }
        public SingleCommand<T, Integer> withInt() { return with(Integer::parseInt); }
        public SingleCommand<T, Long> withLong() { return with(Long::parseLong); }
        public SingleCommand<T, Float> withFloat() { return with(Float::parseFloat); }
        public SingleCommand<T, Double> withDouble() { return with(Double::parseDouble); }
        public SingleCommand<T, String> withString() { return with((str) -> str); }
        public <X extends Enum<X>> SingleCommand<T, X> withEnum(Class<X> clazz) { return with((str) -> EnumParser.parse(str, clazz)); }
        public <X extends IRegistryElement> SingleCommand<T, X> withElement(Class<X> clazz) { return with((str) -> getGameInstance().get(str, clazz)); }
        public SingleCommand<T, ICommand> withSubCommand(ICommand... commands) { return null; }
        public TripleCommand<T, Integer, Integer, Integer> withXYZ() { return new TripleCommand<>(this, Integer::parseInt, Integer::parseInt, Integer::parseInt); }

        public EmptyCommand<T> withExecutor(Class<T> executorClass) {
            this.executorClass = executorClass;
            return this;
        }

        public EmptyCommand<T> addPermission(String... permissions) {
            this.permissions = concatenate(this.permissions, permissions);
            return this;
        }

        public BuilderCommand<T> build(Consumer<T> consumer) {
            return new EmptyCommand<>(this, consumer);
        }
    }

    public static class SingleCommand<T, A> extends BuilderCommand<T>  {
        public Function<String, A> parserA;
        public BiConsumer<T, A> consumer;

        public SingleCommand(BuilderCommand<T> parent, Function<String, A> parserA) {
            super(parent);
            this.parserA = parserA;
        }

        public SingleCommand(BuilderCommand<T> parent, Function<String, A> parserA, int argCount) {
            super(parent);
            this.parserA = parserA;
            this.argCount = argCount;
        }

        public SingleCommand(SingleCommand<T, A> self, BiConsumer<T, A> consumer) {
            super(self);
            this.argCount = self.argCount;

            this.parserA = self.parserA;
            this.consumer = consumer;
        }

        @Override
        public void parseAndExecute(T executor, String[] args) {
            consumer.accept(executor, parserA.apply(args[0]));
        }

        public <X> DoubleCommand<T, A, X> with(Function<String, X> parser) {
            return new DoubleCommand<>(this, parser);
        }

        public DoubleCommand<T, A, Boolean> withBool() { return with(Boolean::parseBoolean); }
        public DoubleCommand<T, A, Integer> withInt() { return with(Integer::parseInt); }
        public DoubleCommand<T, A, Long> withLong() { return with(Long::parseLong); }
        public DoubleCommand<T, A, Float> withFloat() { return with(Float::parseFloat); }
        public DoubleCommand<T, A, Double> withDouble() { return with(Double::parseDouble); }
        public DoubleCommand<T, A, String> withString() { return with((str) -> str); }
        public <X extends Enum<X>> DoubleCommand<T, A, X> withEnum(Class<X> clazz) { return with((str) -> EnumParser.parse(str, clazz)); }
        public <X extends IRegistryElement> DoubleCommand<T, A, X> withElement(Class<X> clazz) { return with((str) -> getGameInstance().get(str, clazz)); }
        public DoubleCommand<T, A, ICommand> withSubCommand(ICommand... commands) { return null; }
        public QuadrupleCommand<T, A, Integer, Integer, Integer> withXYZ() { return new QuadrupleCommand<>(this, parserA, Integer::parseInt, Integer::parseInt, Integer::parseInt); }

        public SingleCommand<T, A> withExecutor(Class<T> executorClass) {
            this.executorClass = executorClass;
            return this;
        }

        public SingleCommand<T, A> addPermission(String... permissions) {
            this.permissions = concatenate(this.permissions, permissions);
            return this;
        }

        public BuilderCommand<T> build(BiConsumer<T, A> consumer) {
            return new SingleCommand<>(this, consumer);
        }
    }

    public static class DoubleCommand<T, A, B> extends BuilderCommand<T> {
        public Function<String, A> parserA;
        public Function<String, B> parserB;
        public TriConsumer<T, A, B> consumer;

        public DoubleCommand(SingleCommand<T, A> parent, Function<String, B> parserB) {
            super(parent);
            this.parserA = parent.parserA;
            this.parserB = parserB;
        }

        public DoubleCommand(BuilderCommand<T> parent, Function<String, A> parserA, Function<String, B> parserB) {
            super(parent);
            this.parserA = parserA;
            this.parserB = parserB;
            this.argCount = 2;
        }

        public DoubleCommand(DoubleCommand<T, A, B> self, TriConsumer<T, A, B> consumer) {
            super(self);
            this.argCount = self.argCount;

            this.parserA = self.parserA;
            this.parserB = self.parserB;
            this.consumer = consumer;
        }

        public void parseAndExecute(T executor, String[] args) {
            consumer.accept(executor, parserA.apply(args[0]), parserB.apply(args[1]));
        }

        public <X> TripleCommand<T, A, B, X> with(Function<String, X> parser) {
            return new TripleCommand<>(this, parser);
        }

        public TripleCommand<T, A, B, Boolean> withBool() { return with(Boolean::parseBoolean); }
        public TripleCommand<T, A, B, Integer> withInt() { return with(Integer::parseInt); }
        public TripleCommand<T, A, B, Long> withLong() { return with(Long::parseLong); }
        public TripleCommand<T, A, B, Float> withFloat() { return with(Float::parseFloat); }
        public TripleCommand<T, A, B, Double> withDouble() { return with(Double::parseDouble); }
        public TripleCommand<T, A, B, String> withString() { return with((str) -> str); }
        public <X extends Enum<X>> TripleCommand<T, A, B, X> withEnum(Class<X> clazz) { return with((str) -> EnumParser.parse(str, clazz)); }
        public <X extends IRegistryElement> TripleCommand<T, A, B, X> withElement(Class<X> clazz) { return with((str) -> getGameInstance().get(str, clazz)); }
        public TripleCommand<T, A, B, ICommand> withSubCommand(ICommand... command) { return null; }
        public QuintupleCommand<T, A, B, Integer, Integer, Integer> withXYZ() { return new QuintupleCommand<>(this, parserA, parserB, Integer::parseInt, Integer::parseInt, Integer::parseInt); }


        public DoubleCommand<T, A, B> withExecutor(Class<T> executorClass) {
            this.executorClass = executorClass;
            return this;
        }

        public DoubleCommand<T, A, B> addPermission(String... permissions) {
            this.permissions = concatenate(this.permissions, permissions);
            return this;
        }

        public BuilderCommand<T> build(TriConsumer<T, A, B> consumer) {
            return new DoubleCommand<>(this, consumer);
        }
    }

    public static class TripleCommand<T, A, B, C> extends BuilderCommand<T> {
        public Function<String, A> parserA;
        public Function<String, B> parserB;
        public Function<String, C> parserC;
        public QuadConsumer<T, A, B, C> consumer;

        public TripleCommand(DoubleCommand<T, A, B> parent, Function<String, C> parserC) {
            super(parent);
            this.parserA = parent.parserA;
            this.parserB = parent.parserB;
            this.parserC = parserC;
        }

        public TripleCommand(BuilderCommand<T> parent, Function<String, A> parserA, Function<String, B> parserB, Function<String, C> parserC) {
            super(parent);
            this.parserA = parserA;
            this.parserB = parserB;
            this.parserC = parserC;
            this.argCount = 3;
        }

        public TripleCommand(TripleCommand<T, A, B, C> self, QuadConsumer<T, A, B, C> consumer) {
            super(self);
            this.argCount = self.argCount;

            this.parserA = self.parserA;
            this.parserB = self.parserB;
            this.parserC = self.parserC;
            this.consumer = consumer;
        }

        @Override
        public void parseAndExecute(T executor, String[] args) {
            consumer.accept(executor, parserA.apply(args[0]), parserB.apply(args[1]), parserC.apply(args[2]));
        }

        public <X> QuadrupleCommand<T, A, B, C, X> with(Function<String, X> parser) {
            return new QuadrupleCommand<>(this, parser);
        }

        public QuadrupleCommand<T, A, B, C, Boolean> withBool() { return with(Boolean::parseBoolean); }
        public QuadrupleCommand<T, A, B, C, Integer> withInt() { return with(Integer::parseInt); }
        public QuadrupleCommand<T, A, B, C, Long> withLong() { return with(Long::parseLong); }
        public QuadrupleCommand<T, A, B, C, Float> withFloat() { return with(Float::parseFloat); }
        public QuadrupleCommand<T, A, B, C, Double> withDouble() { return with(Double::parseDouble); }
        public QuadrupleCommand<T, A, B, C, String> withString() { return with((str) -> str); }
        public <X extends Enum<X>> QuadrupleCommand<T, A, B, C, X> withEnum(Class<X> clazz) { return with((str) -> EnumParser.parse(str, clazz)); }
        public <X extends IRegistryElement> QuadrupleCommand<T, A, B, C, X> withElement(Class<X> clazz) { return with((str) -> Ourcraft.GAME_INSTANCE.get(str, clazz)); }
        public QuadrupleCommand<T, A, B, C, ICommand> withSubCommand(ICommand... command) { return null; }

        public TripleCommand<T, A, B, C> withExecutor(Class<T> executorClass) {
            this.executorClass = executorClass;
            return this;
        }

        public TripleCommand<T, A, B, C> addPermission(String... permissions) {
            this.permissions = concatenate(this.permissions, permissions);
            return this;
        }

        public BuilderCommand<T> build(QuadConsumer<T, A, B, C> consumer) {
            return new TripleCommand<>(this, consumer);
        }
    }

    public static class QuadrupleCommand<T, A, B, C, D> extends BuilderCommand<T> {
        public Function<String, A> parserA;
        public Function<String, B> parserB;
        public Function<String, C> parserC;
        public Function<String, D> parserD;
        public QuinConsumer<T, A, B, C, D> consumer;

        public QuadrupleCommand(TripleCommand<T, A, B, C> parent, Function<String, D> parserD) {
            super(parent);
            this.parserA = parent.parserA;
            this.parserB = parent.parserB;
            this.parserC = parent.parserC;
            this.parserD = parserD;
        }

        public QuadrupleCommand(BuilderCommand<T> parent, Function<String, A> parserA, Function<String, B> parserB, Function<String, C> parserC, Function<String, D> parserD) {
            super(parent);
            this.parserA = parserA;
            this.parserB = parserB;
            this.parserC = parserC;
            this.parserD = parserD;
            this.argCount = 4;
        }

        public QuadrupleCommand(QuadrupleCommand<T, A, B, C, D> self, QuinConsumer<T, A, B, C, D> consumer) {
            super(self);
            this.argCount = self.argCount;

            this.parserA = self.parserA;
            this.parserB = self.parserB;
            this.parserC = self.parserC;
            this.parserD = self.parserD;
            this.consumer = consumer;
        }

        public <X> QuintupleCommand<T, A, B, C, D, X> with(Function<String, X> parser) {
            return new QuintupleCommand<>(this, parser);
        }

        public QuintupleCommand<T, A, B, C, D, Boolean> withBool() { return with(Boolean::parseBoolean); }
        public QuintupleCommand<T, A, B, C, D, Integer> withInt() { return with(Integer::parseInt); }
        public QuintupleCommand<T, A, B, C, D, Long> withLong() { return with(Long::parseLong); }
        public QuintupleCommand<T, A, B, C, D, Float> withFloat() { return with(Float::parseFloat); }
        public QuintupleCommand<T, A, B, C, D, Double> withDouble() { return with(Double::parseDouble); }
        public QuintupleCommand<T, A, B, C, D, String> withString() { return with((str) -> str); }
        public <X extends Enum<X>> QuintupleCommand<T, A, B, C, D, X> withEnum(Class<X> clazz) { return with((str) -> EnumParser.parse(str, clazz)); }
        public <X extends IRegistryElement> QuintupleCommand<T, A, B, C, D, X> withElement(Class<X> clazz) { return with((str) -> Ourcraft.GAME_INSTANCE.get(str, clazz)); }
        public QuintupleCommand<T, A, B, C, D, ICommand> withSubCommand(ICommand... command) { return null; }


        @Override
        public void parseAndExecute(T executor, String[] args) {
            consumer.accept(executor, parserA.apply(args[0]), parserB.apply(args[1]), parserC.apply(args[2]), parserD.apply(args[3]));
        }

        public BuilderCommand<T> build(QuinConsumer<T, A, B, C, D> consumer) {
            return new QuadrupleCommand<>(this, consumer);
        }
    }

    public static class QuintupleCommand<T, A, B, C, D, E> extends BuilderCommand<T> {
        public Function<String, A> parserA;
        public Function<String, B> parserB;
        public Function<String, C> parserC;
        public Function<String, D> parserD;
        public Function<String, E> parserE;
        public SextConsumer<T, A, B, C, D, E> consumer;

        public QuintupleCommand(QuadrupleCommand<T, A, B, C, D> parent, Function<String, E> parserE) {
            super(parent);
            this.parserA = parent.parserA;
            this.parserB = parent.parserB;
            this.parserC = parent.parserC;
            this.parserD = parent.parserD;
            this.parserE = parserE;
        }

        public QuintupleCommand(BuilderCommand<T> parent, Function<String, A> parserA, Function<String, B> parserB, Function<String, C> parserC, Function<String, D> parserD, Function<String, E> parserE) {
            super(parent);
            this.parserA = parserA;
            this.parserB = parserB;
            this.parserC = parserC;
            this.parserD = parserD;
            this.parserE = parserE;
            this.argCount = 5;
        }

        public QuintupleCommand(QuintupleCommand<T, A, B, C, D, E> self, SextConsumer<T, A, B, C, D, E> consumer) {
            super(self);
            this.argCount = self.argCount;

            this.parserA = self.parserA;
            this.parserB = self.parserB;
            this.parserC = self.parserC;
            this.parserD = self.parserD;
            this.consumer = consumer;
        }

        @Override
        public void parseAndExecute(T executor, String[] args) {
            consumer.accept(executor, parserA.apply(args[0]), parserB.apply(args[1]), parserC.apply(args[2]), parserD.apply(args[3]), parserE.apply(args[4]));
        }

        public BuilderCommand<T> build(SextConsumer<T, A, B, C, D, E> consumer) {
            return new QuintupleCommand<>(this, consumer);
        }
    }

    public static String[] concatenate(String[] a, String[] b) {
        if(a == null) {
            return b;
        } else if(b == null) {
            return a;
        }

        int aLen = a.length;
        int bLen = b.length;

        String[] c = (String[]) Array.newInstance(String.class, aLen + bLen);
        System.arraycopy(a, 0, c, 0, aLen);
        System.arraycopy(b, 0, c, aLen, bLen);

        return c;
    }
}
