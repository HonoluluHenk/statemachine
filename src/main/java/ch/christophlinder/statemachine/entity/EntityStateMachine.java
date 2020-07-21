package ch.christophlinder.statemachine.entity;


import ch.christophlinder.statemachine.StateMachine;
import edu.umd.cs.findbugs.annotations.DefaultAnnotation;
import edu.umd.cs.findbugs.annotations.NonNull;

import java.io.Serializable;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

@DefaultAnnotation(NonNull.class)
public class EntityStateMachine<Entity, State extends Serializable, Transitions> {
    private final Function<Entity, State> stateGetter;
    private final BiConsumer<Entity, State> stateSetter;
    private final StateMachine<State, Transitions> stateMachine;


    public EntityStateMachine(
            Function<Entity, State> stateGetter,
            BiConsumer<Entity, State> stateSetter,
            Map<State, Transitions> transitions
    ) {
        this.stateGetter = requireNonNull(stateGetter, "No stateGetter");
        this.stateSetter = requireNonNull(stateSetter, "No stateSetter");

        stateMachine = new StateMachine<>(transitions);
    }

    public static <Entity, State extends Serializable, Transitions>
    EntityStateMachine<Entity, State, Transitions> of(
            Function<Entity, State> stateGetter,
            BiConsumer<Entity, State> stateSetter,
            Map<State, Transitions> transitions

    ) {
        return new EntityStateMachine<>(stateGetter, stateSetter, transitions);
    }

    public static <Entity extends EntityWithState<State>, State extends Serializable, Transitions>
    EntityStateMachine<Entity, State, Transitions> ofEntityWithState(
            Map<State, Transitions> transitions
    ) {
        return of(EntityWithState::getState, EntityWithState::setState, transitions);
    }

    public <Out extends Outcome<State>> Out transition(
            Entity entity,
            Function<Transitions, Out> transition
    ) {
        State fromState = getStateFromEntity(entity);
        Out outcome = stateMachine.transition(fromState, transition);
        setStateInEntity(entity, outcome);

        return outcome;
    }

    private <Out extends Outcome<State>> void setStateInEntity(Entity entity, Out outcome) {
        State nextState = outcome.getNextState();
        stateSetter.accept(entity, nextState);
    }

    private State getStateFromEntity(Entity entity) {
        return requireNonNull(stateGetter.apply(entity),
                "Could not extract state from: " + entity);
    }

    public StateMachine<State, Transitions> getStateMachine() {
        return stateMachine;
    }

}
