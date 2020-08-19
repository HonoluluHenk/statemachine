package ch.christophlinder.statemachine.entity;

import java.io.Serializable;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

import ch.christophlinder.statemachine.StateMachine;
import edu.umd.cs.findbugs.annotations.DefaultAnnotation;
import edu.umd.cs.findbugs.annotations.NonNull;

import static java.util.Objects.requireNonNull;

@DefaultAnnotation(NonNull.class)
public class Executor<ActionsInterface, Entity, State extends Serializable> {
	private final Supplier<Entity> entitySupplier;
	private final Function<Entity, State> stateGetter;
	private final BiConsumer<Entity, State> stateSetter;
	private final StateMachine<State, ActionsInterface> stateMachine;

	Executor(Supplier<Entity> entitySupplier, Function<Entity, State> stateGetter, BiConsumer<Entity,
			State> stateSetter, StateMachine<State, ActionsInterface> stateMachine) {
		this.entitySupplier = entitySupplier;
		this.stateGetter = stateGetter;
		this.stateSetter = stateSetter;
		this.stateMachine = stateMachine;
	}

	/**
	 * Invoke an action without result (i.e.: a method returning only an {@link Outcome}.)
	 */
	public void accept(
			ActionInvocation<ActionsInterface, Entity, ? extends Outcome<State>> invocation
	) {
		exec(invocation);
	}

	/**
	 * Invoke an action with result (i.e.: a method returning a {@link Result}).
	 */
	public <Value, Res extends Result<State, Value>> Value apply(
			ActionInvocation<ActionsInterface, Entity, Res> invocation
	) {
		Res outcome = exec(invocation);

		return outcome.getValue();
	}

	private <Res extends Outcome<State>> Res exec(ActionInvocation<ActionsInterface, Entity, Res> invocation) {
		Entity entity = entitySupplier.get();

		State fromState = getStateFromEntity(entity);
		Res outcome = stateMachine.transition(
				fromState,
				actions -> invocation.invoke(actions, entity)
		);

		setStateIntoEntity(entity, outcome);
		return outcome;
	}

	private void setStateIntoEntity(
			Entity entity,
			Outcome<State> outcome
	) {
		outcome.nextState()
				.ifPresent(state -> stateSetter.accept(entity, state));
	}

	private State getStateFromEntity(Entity entity) {
		return requireNonNull(stateGetter.apply(entity),
				"Could not extract state from: " + entity);
	}
}
