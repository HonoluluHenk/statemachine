package ch.christophlinder.statemachine.entity.function;

import edu.umd.cs.findbugs.annotations.NonNull;

import java.util.function.Function;

@FunctionalInterface
public interface TransitionFunction<T, R> extends Function<T, R> {
    @Override
    @NonNull
    R apply(@NonNull T t);
}
