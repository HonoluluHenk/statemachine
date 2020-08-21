package ch.christophlinder.statemachine.entity.function;

import edu.umd.cs.findbugs.annotations.NonNull;

@FunctionalInterface
public interface EntityFunction<T, R> {
    @NonNull
    R apply(@NonNull T t);
}
