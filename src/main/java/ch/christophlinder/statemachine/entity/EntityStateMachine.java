package ch.christophlinder.statemachine.entity;

import java.io.Serializable;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

import ch.christophlinder.statemachine.ActionDeniedException;
import ch.christophlinder.statemachine.StateMachine;
import edu.umd.cs.findbugs.annotations.DefaultAnnotation;
import edu.umd.cs.findbugs.annotations.NonNull;

import static java.util.Objects.requireNonNull;

/**
 * @param <ActionsInterface> An interface containing all possible action functions with a default-implementation
 * throwing {@link ActionDeniedException}.
 * @param <Entity> Your entity class this statemachine will handle.
 * @param <State> Your state demarcation class.
 * Most probably an enum but might be anything as long as it is suitable as a {@link Map} key.
 */
@DefaultAnnotation(NonNull.class)
public class EntityStateMachine<ActionsInterface, Entity, State extends Serializable> {
	private final Supplier<Entity> entityCtor;
	private final Function<Entity, State> stateGetter;
	private final BiConsumer<Entity, State> stateSetter;
	private final StateMachine<State, ActionsInterface> stateMachine;

	private EntityStateMachine(
			Supplier<Entity> entityCtor,
			Function<Entity, State> stateGetter,
			BiConsumer<Entity, State> stateSetter,
			Map<State, ActionsInterface> actions
	) {
		this.entityCtor = requireNonNull(entityCtor, "No Entity constructor");
		this.stateGetter = requireNonNull(stateGetter, "No stateGetter");
		this.stateSetter = requireNonNull(stateSetter, "No stateSetter");

		stateMachine = new StateMachine<>(actions);
	}

	public static <ActionsInterface, Entity, State extends Serializable>
	EntityStateMachine<ActionsInterface, Entity, State> of(
			Supplier<Entity> entityCtor,
			Function<Entity, State> stateGetter,
			BiConsumer<Entity, State> stateSetter,
			Map<State, ActionsInterface> actions
	) {
		return new EntityStateMachine<>(entityCtor, stateGetter, stateSetter, actions);
	}

	public Executor<ActionsInterface, Entity, State> newEntity() {
		return new Executor<>(entityCtor, stateGetter, stateSetter, stateMachine);
	}

	public Executor<ActionsInterface, Entity, State> using(Entity entity) {
		return new Executor<>(() -> entity, stateGetter, stateSetter, stateMachine);
	}

}
