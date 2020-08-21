package ch.christophlinder.statemachine;

import ch.christophlinder.statemachine.entity.function.TransitionConsumer;
import ch.christophlinder.statemachine.entity.function.TransitionFunction;
import edu.umd.cs.findbugs.annotations.NonNull;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

public class StateMachine<State extends Serializable, Transitions> {

    @NonNull
    private final Map<State, Transitions> stateMap;

    public StateMachine(
            @NonNull Map<State, Transitions> transitions
    ) {
        requireNonNull(transitions, "No transitions map");
        if (transitions.isEmpty()) {
            throw new IllegalArgumentException("Empty transitions map");
        }

        this.stateMap = requireValuesNotNull(new HashMap<>(transitions));
    }

    @NonNull
    private static <K, V> Map<K, V> requireValuesNotNull(
            @NonNull Map<K, V> map
    ) {
        boolean containsNullValues = map.containsValue(null);

        if (containsNullValues) {
            throw new IllegalArgumentException("Transitions map may not contain null values");
        }

        return map;
    }

    @NonNull
    public static <State extends Serializable, Transitions> StateMachine<State, Transitions> of(
            @NonNull Map<State, Transitions> transitions
    ) {
        return new StateMachine<>(requireNonNull(transitions));
    }

    @NonNull
    private Transitions transitionsFor(
            @NonNull State state
    ) {
        requireNonNull(state);

        return stateMap.computeIfAbsent(state, (ignored) -> {
            throw new ActionDeniedException(state, "state not registered", null);
        });
    }

    /**
     * <strong>Requires</strong> the transition function to return a non-null value!.
     * <p>
     * Just in case you absolutely need a null: wrap it into an {@link Optional}.
     */
    @NonNull
    public <R> R transition(
            @NonNull State fromState,
            @NonNull TransitionFunction<Transitions, R> transition
    ) {
        requireNonNull(fromState);
        requireNonNull(transition);

        try {
            Transitions t = transitionsFor(fromState);
            R result = requireNonNull(transition.apply(t), "transition invocation returned null, state: " + fromState);
            return result;

        } catch (ActionDeniedException e) {
            throw enrichWithState(fromState, e);
        }
    }

    public void doTransition(
            @NonNull State fromState,
            @NonNull TransitionConsumer<Transitions> transition
    ) {
        requireNonNull(fromState);
        requireNonNull(transition);

        try {
            Transitions t = transitionsFor(fromState);
            transition.accept(t);
        } catch (ActionDeniedException e) {
            throw enrichWithState(fromState, e);
        }
    }

    @NonNull
    private ActionDeniedException enrichWithState(
            @NonNull State state,
            @NonNull ActionDeniedException e
    ) {
        requireNonNull(state);
        requireNonNull(e);

        if (e.getState() != null) {
            return e;
        }
        return new ActionDeniedException(state, e.getDebugInfo(), e);
    }

    @Override
    @NonNull
    public String toString() {
        return String.format("[StateMachine,{%s}]", prettyPrintStates());
    }

    @NonNull
    private String prettyPrintStates() {
        String result = this.stateMap.entrySet().stream()
                .map(e -> e.getKey() + "=" + e.getValue().getClass().getSimpleName())
                .collect(Collectors.joining(","));

        return result;
    }
}
