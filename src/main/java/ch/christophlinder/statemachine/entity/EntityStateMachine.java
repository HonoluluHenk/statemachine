package ch.christophlinder.statemachine.entity;


import ch.christophlinder.statemachine.StateMachine;
import edu.umd.cs.findbugs.annotations.DefaultAnnotation;
import edu.umd.cs.findbugs.annotations.NonNull;

import java.io.Serializable;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

@DefaultAnnotation(NonNull.class)
public class EntityStateMachine<Entity, State extends Serializable, Actions> {
    private final Supplier<Entity> entityCtor;
    private final Function<Entity, State> stateGetter;
    private final BiConsumer<Entity, State> stateSetter;
    private final StateMachine<State, Actions> stateMachine;

    public EntityStateMachine(
            Supplier<Entity> entityCtor,
            Function<Entity, State> stateGetter,
            BiConsumer<Entity, State> stateSetter,
            Map<State, Actions> actions
    ) {
        this.entityCtor = requireNonNull(entityCtor, "No Entity constructor");
        this.stateGetter = requireNonNull(stateGetter, "No stateGetter");
        this.stateSetter = requireNonNull(stateSetter, "No stateSetter");

        stateMachine = new StateMachine<>(actions);
    }

    public static <Entity, State extends Serializable, Actions>
    EntityStateMachine<Entity, State, Actions> of(
            Supplier<Entity> entityCtor,
            Function<Entity, State> stateGetter,
            BiConsumer<Entity, State> stateSetter,
            Map<State, Actions> actions
    ) {
        return new EntityStateMachine<>(entityCtor, stateGetter, stateSetter, actions);
    }

    public static <Entity extends EntityWithState<State>, State extends Serializable, Actions>
    EntityStateMachine<Entity, State, Actions> ofEntityWithState(
            Supplier<Entity> entityCtor,
            Map<State, Actions> actionsMap
    ) {
        return of(
                entityCtor,
                EntityWithState::getState,
                EntityWithState::setState,
                actionsMap
        );
    }

    public Executor newEntity() {
        return new Executor(entityCtor);
    }

    private <Out extends Outcome<State>> void setStateInEntity(Entity entity, Out outcome) {
        outcome.nextState()
                .ifPresent(nextState -> stateSetter.accept(entity, nextState));
    }

    private State getStateFromEntity(Entity entity) {
        return requireNonNull(stateGetter.apply(entity),
                "Could not extract state from: " + entity);
    }

    public Executor using(Entity entity) {
        return new Executor(() -> entity);
    }

    public class Executor {
        private final Supplier<Entity> entitySupplier;

        private Executor(Supplier<Entity> entitySupplier) {
            this.entitySupplier = entitySupplier;
        }

        public <Out extends Outcome<State>> Out execute(
                BiFunction<Actions, Entity, Out> action
        ) {
            Entity entity = entitySupplier.get();

            State fromState = getStateFromEntity(entity);
            Out outcome = stateMachine.transition(
                    fromState,
                    actions -> action.apply(actions, entity)
            );
            setStateInEntity(entity, outcome);

            return outcome;
        }
    }
}
