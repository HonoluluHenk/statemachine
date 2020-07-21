package ch.christophlinder.statemachine;

import edu.umd.cs.findbugs.annotations.DefaultAnnotation;
import edu.umd.cs.findbugs.annotations.NonNull;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

@DefaultAnnotation(NonNull.class)
public class StateMachine<State extends Serializable, Transitions> {
    private final Map<State, Transitions> stateMap;

    public StateMachine(
            Map<State, Transitions> transitions
    ) {
        requireNonNull(transitions, "No transitions map");
        if (transitions.isEmpty()) {
            throw new IllegalArgumentException("Empty transitions map");
        }

        this.stateMap = requireValuesNotNull(new HashMap<>(transitions));
    }

    private static <K, V> Map<K, V> requireValuesNotNull(
            Map<K, V> map
    ) {
        boolean containsNullValues = map.containsValue(null);

        if (containsNullValues) {
            throw new IllegalArgumentException("Transitions map may not contain null values");
        }

        return map;
    }

    public static <State extends Serializable, Transitions> StateMachine<State, Transitions> of(
            Map<State, Transitions> transitions
    ) {
        return new StateMachine<>(transitions);
    }

    private Transitions transitionsFor(
            State state
    ) {
        requireNonNull(state);

        return stateMap.computeIfAbsent(state, (ignored) -> {
            throw new TransitionNotAllowed(state, "state not registered", null);
        });
    }

    /**
     * <strong>Requires</strong> the transition function to return a non-null value!.
     * <p>
     * Just in case you absolutely need a null: wrap it into an {@link Optional}.
     */
    public <R> R transition(
            State fromState,
            Function<Transitions, R> transition
    ) {
        try {

            Transitions t = transitionsFor(fromState);
            R result = requireNonNull(transition.apply(t), "transition invocation returned null, state: " + fromState);
            return result;

        } catch (TransitionNotAllowed e) {
            throw enrichWithState(fromState, e);
        }
    }

    public void doTransition(
            State fromState,
            Consumer<Transitions> transition
    ) {
        try {
            Transitions t = transitionsFor(fromState);
            transition.accept(t);
        } catch (TransitionNotAllowed e) {
            throw enrichWithState(fromState, e);
        }
    }

    private TransitionNotAllowed enrichWithState(
            State state,
            TransitionNotAllowed e
    ) {
        if (e.getState() != null) {
            return e;
        }
        return new TransitionNotAllowed(state, e.getDebugInfo(), e);
    }

    @Override
    public String toString() {
        return String.format("[StateMachine,{%s}]", prettyPrintStates());
    }

    private String prettyPrintStates() {
        String result = this.stateMap.entrySet().stream()
                .map(e -> e.getKey() + "=" + e.getValue().getClass().getSimpleName())
                .collect(Collectors.joining(","));

        return result;
    }
}
