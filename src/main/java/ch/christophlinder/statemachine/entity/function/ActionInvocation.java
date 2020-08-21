package ch.christophlinder.statemachine.entity.function;

import edu.umd.cs.findbugs.annotations.NonNull;

@FunctionalInterface
public interface ActionInvocation<ActionsInterface, Entity, Result> {
    @NonNull
    Result invoke(
            @NonNull ActionsInterface actions,
            @NonNull Entity entity
    );
}
