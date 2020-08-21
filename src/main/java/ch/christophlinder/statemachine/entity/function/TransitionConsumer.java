package ch.christophlinder.statemachine.entity.function;

import edu.umd.cs.findbugs.annotations.NonNull;

import java.util.function.Consumer;

@FunctionalInterface
public interface TransitionConsumer<T> extends Consumer<T> {
    @Override
    void accept(@NonNull T t);
}
