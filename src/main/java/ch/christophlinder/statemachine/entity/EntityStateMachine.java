package ch.christophlinder.statemachine.entity;

import ch.christophlinder.statemachine.ActionDeniedException;
import ch.christophlinder.statemachine.StateMachine;
import ch.christophlinder.statemachine.entity.function.EntityBiConsumer;
import ch.christophlinder.statemachine.entity.function.EntityFunction;
import ch.christophlinder.statemachine.entity.function.EntitySupplier;
import edu.umd.cs.findbugs.annotations.NonNull;

import java.io.Serializable;
import java.util.Map;

import static java.util.Objects.requireNonNull;

/**
 * @param <ActionsInterface> An interface containing all possible action functions with a default-implementation
 * throwing {@link ActionDeniedException}.
 * @param <Entity> Your entity class this statemachine will handle.
 * @param <State> Your state demarcation class.
 * Most probably an enum but might be anything as long as it is suitable as a {@link Map} key.
 */
public class EntityStateMachine<ActionsInterface, Entity, State extends Serializable> {

    @NonNull
    private final EntitySupplier<Entity> entityCtor;
    @NonNull
    private final EntityFunction<Entity, State> stateGetter;
    @NonNull
    private final EntityBiConsumer<Entity, State> stateSetter;
    @NonNull
    private final StateMachine<State, ActionsInterface> stateMachine;

    private EntityStateMachine(
            @NonNull EntitySupplier<Entity> entityCtor,
            @NonNull EntityFunction<Entity, State> stateGetter,
            @NonNull EntityBiConsumer<Entity, State> stateSetter,
            @NonNull Map<State, ActionsInterface> actions
    ) {
        this.entityCtor = requireNonNull(entityCtor, "No Entity constructor");
        this.stateGetter = requireNonNull(stateGetter, "No stateGetter");
        this.stateSetter = requireNonNull(stateSetter, "No stateSetter");

        stateMachine = new StateMachine<>(actions);
    }

    @NonNull
    public static <ActionsInterface, Entity, State extends Serializable>
    EntityStateMachine<ActionsInterface, Entity, State> of(
            @NonNull EntitySupplier<Entity> entityCtor,
            @NonNull EntityFunction<Entity, State> stateGetter,
            @NonNull EntityBiConsumer<Entity, State> stateSetter,
            @NonNull Map<State, ActionsInterface> actions
    ) {
        return new EntityStateMachine<>(entityCtor, stateGetter, stateSetter, actions);
    }

    @NonNull
    public Executor<ActionsInterface, Entity, State> newEntity() {
        return new Executor<>(entityCtor, stateGetter, stateSetter, stateMachine);
    }

    @NonNull
    public Executor<ActionsInterface, Entity, State> using(
            @NonNull Entity entity
    ) {
        requireNonNull(entity);

        return new Executor<>(() -> entity, stateGetter, stateSetter, stateMachine);
    }

}
