package ch.christophlinder.statemachine.entity;

import edu.umd.cs.findbugs.annotations.DefaultAnnotation;
import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;

import static java.util.Objects.requireNonNull;

/**
 * Represents an action-{@link Outcome} with an additional result value.
 */
@DefaultAnnotation(NonNull.class)
public class Result<State, R> extends Outcome<State> {
	private final R value;

	protected Result(
			@Nullable State nextState,
			R value
	) {
		super(nextState);
		this.value = requireNonNull(value);
	}

	public static <State, R> Result<State, R> of(State state, R value) {
		return new Result<>(requireNonNull(state), value);
	}

	public static <State, R> Result<State, R> sameState(R result) {
		return new Result<>(null, result);
	}

	public R getValue() {
		return value;
	}

	/**
	 * Convenience: allow initializing and then re-setting the value.
	 * <p>
	 * Example:
	 * <pre>{@code
	 * Result r = Result.of("Foo", "meh");
	 * if (!itsWednesdayMyFriends) {
	 *     return r;
	 * }
	 * return r.withValue("wednesday!");
	 * }</pre>
	 */
	public <RR> Result<State, RR> withValue(RR newValue) {
		return new Result<>(nextState().orElse(null), newValue);
	}

}
