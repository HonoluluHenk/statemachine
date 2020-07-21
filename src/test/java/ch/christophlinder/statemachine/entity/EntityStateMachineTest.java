package ch.christophlinder.statemachine.entity;

import java.util.EnumMap;
import java.util.Map;

import ch.christophlinder.statemachine.StateMachine;
import ch.christophlinder.statemachine.entity.fixtures.MyEntity;
import ch.christophlinder.statemachine.entity.fixtures.YourInitTransitions;
import ch.christophlinder.statemachine.entity.fixtures.YourTransitions;
import ch.christophlinder.statemachine.entity.fixtures.YourTransitions.YourOutcome;
import ch.christophlinder.statemachine.fixtures.MyStates;
import org.junit.jupiter.api.Test;

import static ch.christophlinder.statemachine.fixtures.MyStates.INIT;
import static org.assertj.core.api.Assertions.assertThat;

public class EntityStateMachineTest {
	private final EntityStateMachine<EntityWithState<MyStates>, MyStates, YourTransitions> sm =
			EntityStateMachine.ofEntityWithState(buildTransitions());

	private Map<MyStates, YourTransitions> buildTransitions() {
		Map<MyStates, YourTransitions> map = new EnumMap<>(MyStates.class);
		map.put(INIT, new YourInitTransitions());
		return map;
	}

	@Test
	void shouldTransitionCorrectly() {
		MyEntity myEntity = new MyEntity(INIT);

		YourOutcome<String> actual = sm.transition(myEntity, YourTransitions::goNext);

		assertThat(actual.getNextState())
				.isEqualTo(MyStates.NEXT);
		assertThat(actual.getResult())
				.isEqualTo("Hello World");
	}

	@Test
	void shouldReturnUnderlyingStateMachine() {
		StateMachine<MyStates, YourTransitions> actual = sm.getStateMachine();

		assertThat(actual)
				.isNotNull();
	}
}
