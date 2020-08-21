package ch.christophlinder.statemachine.entity;

import ch.christophlinder.statemachine.StateMachine;
import ch.christophlinder.statemachine.entity.function.ActionInvocation;
import ch.christophlinder.statemachine.entity.function.EntityBiConsumer;
import ch.christophlinder.statemachine.entity.function.EntityFunction;
import ch.christophlinder.statemachine.entity.function.EntitySupplier;
import edu.umd.cs.findbugs.annotations.NonNull;

import java.io.Serializable;
import java.util.function.Consumer;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

public class Executor<ActionsInterface, Entity, State extends Serializable> {
    @NonNull
    private final EntitySupplier<Entity> entitySupplier;
    @NonNull
    private final EntityFunction<Entity, State> stateGetter;
    @NonNull
    private final EntityBiConsumer<Entity, State> stateSetter;
    @NonNull
    private final StateMachine<State, ActionsInterface> stateMachine;

    Executor(
            @NonNull EntitySupplier<Entity> entitySupplier,
            @NonNull EntityFunction<Entity, State> stateGetter,
            @NonNull EntityBiConsumer<Entity, State> stateSetter,
            @NonNull StateMachine<State, ActionsInterface> stateMachine
    ) {
        this.entitySupplier = requireNonNull(entitySupplier);
        this.stateGetter = requireNonNull(stateGetter);
        this.stateSetter = requireNonNull(stateSetter);
        this.stateMachine = requireNonNull(stateMachine);
    }

    /**
     * Invoke an action without result (i.e.: a method returning only an {@link Outcome}).
     * <p>
     * For the caller of the statemachine, this effectively behaves like
     * {@link Consumer#accept(Object)} (hence the name).
     */
    public void accept(
            @NonNull ActionInvocation<ActionsInterface, Entity, ? extends Outcome<State>> invocation
    ) {
        requireNonNull(invocation);

        exec(invocation);
    }

    /**
     * Invoke an action with result (i.e.: a method returning a {@link Result}).
     * <p>
     * For the caller of the statemachine, this effectively behaves like
     * {@link Function#apply(Object)} (hence the name).
     */
    @NonNull
    public <Value, Res extends Result<State, Value>> Value apply(
            @NonNull ActionInvocation<ActionsInterface, Entity, Res> invocation
    ) {
        requireNonNull(invocation);

        Res outcome = exec(invocation);

        return requireNonNull(outcome.getValue());
    }

    @NonNull
    private <Res extends Outcome<State>> Res exec(
            @NonNull ActionInvocation<ActionsInterface, Entity, Res> invocation
    ) {
        requireNonNull(invocation);

        Entity entity = entitySupplier.get();

        State fromState = getStateFromEntity(entity);
        Res outcome = stateMachine.transition(
                fromState,
                actions -> invocation.invoke(actions, entity)
        );

        setStateIntoEntity(entity, outcome);

        return requireNonNull(outcome);
    }

    private void setStateIntoEntity(
            @NonNull Entity entity,
            @NonNull Outcome<State> outcome
    ) {
        requireNonNull(entity);
        requireNonNull(outcome);

        outcome.nextState()
                .ifPresent(state -> stateSetter.accept(entity, state));
    }

    @NonNull
    private State getStateFromEntity(
            @NonNull Entity entity
    ) {
        requireNonNull(entity);

        return requireNonNull(stateGetter.apply(entity),
                "Could not extract state from: " + entity);
    }
}
