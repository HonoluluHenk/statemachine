package ch.christophlinder.statemachine.entity;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Supplier;

import ch.christophlinder.statemachine.ActionDeniedException;
import ch.christophlinder.statemachine.entity.fixtures.YourEntity;
import ch.christophlinder.statemachine.entity.fixtures.YourInitTransitions;
import ch.christophlinder.statemachine.entity.fixtures.YourNextTransitions;
import ch.christophlinder.statemachine.entity.fixtures.YourState;
import ch.christophlinder.statemachine.entity.fixtures.YourTransitions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static ch.christophlinder.statemachine.entity.fixtures.YourState.CANCELLED;
import static ch.christophlinder.statemachine.entity.fixtures.YourState.INIT;
import static ch.christophlinder.statemachine.entity.fixtures.YourState.NEXT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class EntityStateMachineTest {

	public static EntityStateMachine<YourTransitions, YourEntity, YourState> buildStateMachine(Supplier<YourEntity> entityCtor) {
		return EntityStateMachine.of(
				entityCtor,
				YourEntity::getState,
				YourEntity::setState,
				allowedTransitions());
	}

	private static Map<YourState, YourTransitions> allowedTransitions() {
		Map<YourState, YourTransitions> map = new EnumMap<>(YourState.class);
		map.put(INIT, new YourInitTransitions());
		map.put(NEXT, new YourNextTransitions());
		return map;
	}

	@Nested
	class Actions {
		private final EntityStateMachine<YourTransitions, YourEntity, YourState> sm =
				buildStateMachine(() -> new YourEntity(INIT));

		@Test
		void allow_invoking_a_valid_transition() {
			YourEntity entity = new YourEntity(INIT);

			String result = sm.using(entity)
					.apply((t, e) -> t.goNext());

			assertSoftly(softly -> {
				softly.assertThat(entity.getState())
						.isEqualTo(YourState.NEXT);
				softly.assertThat(result)
						.isEqualTo("Hello World");
			});
		}

		@Test
		void throw_when_invoking_illegal_transition() {
			YourEntity entity = new YourEntity(CANCELLED);

			ActionDeniedException ex = assertThrows(
					ActionDeniedException.class,
					() -> sm.using(entity).apply((t, e) -> t.goNext())
			);

			assertThat(ex)
					.hasMessageContaining(CANCELLED.name());
		}

		@Test
		void allow_results_with_subclasses_in_transitions() {
			YourEntity entity = new YourEntity(NEXT);

			String result = sm.using(entity)
					.apply((tr, e) -> tr.cancelWithResult("Test Cancel Message"));

			assertSoftly(softly -> {
				softly.assertThat(entity.getState())
						.isEqualTo(CANCELLED);
				softly.assertThat(result)
						.isEqualTo("Test Cancel Message");
			});
		}

		@Test
		void sameState_keeps_state() {
			YourEntity entity = new YourEntity(NEXT);

			String result = sm.using(entity)
					.apply((tr, e) -> tr.keepState("Lorem Ipsum"));

			assertSoftly(softly -> {
				softly.assertThat(entity.getState())
						.isEqualTo(NEXT);
				softly.assertThat(result)
						.isEqualTo("Lorem Ipsum");
			});
		}
	}

	@Nested
	class CreateAndThen {
		@Test
		void should_instantiate_without_parameters() {
			YourState initialState = INIT;
			YourEntity original = new YourEntity(initialState, "constructor message");

			EntityStateMachine<YourTransitions, YourEntity, YourState> sm = buildStateMachine(() -> original);

			YourEntity newEntity = sm.newEntity()
					.apply(YourTransitions::initialize);

			assertSoftly(softly -> {
				softly.assertThat(newEntity)
						.isNotNull()
						.isSameAs(original); // initialize is implemented to return the original
				softly.assertThat(newEntity.getState())
						.isEqualTo(initialState);
				softly.assertThat(newEntity.getMessage())
						.isEqualTo("set in initialize");
			});
		}

		@Test
		void should_instantiate_with_parameters() {
			YourState initialState = INIT;
			YourEntity original = new YourEntity(initialState, "constructor message");
			EntityStateMachine<YourTransitions, YourEntity, YourState> sm = buildStateMachine(() -> original);

			YourEntity newEntity = sm.newEntity()
					.apply((tr, entity) -> tr.initializeWithParams(entity, "test message"));

			assertSoftly(softly -> {
				softly.assertThat(newEntity)
						.isSameAs(original);
				softly.assertThat(newEntity.getState())
						.isEqualTo(initialState);
				softly.assertThat(newEntity.getMessage())
						.isEqualTo("test message");
			});
		}

		@Test
		void should_let_initialize_create_other_instance() {
			YourEntity original = new YourEntity(INIT, "constructor message");
			EntityStateMachine<YourTransitions, YourEntity, YourState> sm = buildStateMachine(() -> original);

			YourEntity newEntity = sm.newEntity()
					.apply((tr, entity) -> tr.initializeWithNewInstance(entity, CANCELLED));

			assertSoftly(softly -> {
				softly.assertThat(newEntity)
						.isNotSameAs(original);
				softly.assertThat(newEntity.getState())
						.isEqualTo(CANCELLED);
				softly.assertThat(newEntity.getMessage())
						.isEqualTo("other instance");
			});
		}

		@Test
		void throws_when_calling_an_illegal_transition() {
			YourEntity original = new YourEntity(CANCELLED);
			EntityStateMachine<YourTransitions, YourEntity, YourState> sm = buildStateMachine(() -> original);

			ActionDeniedException ex = assertThrows(
					ActionDeniedException.class,
					() -> sm.newEntity()
							.apply(YourTransitions::initialize)
			);

			assertThat(ex)
					.hasMessageContaining(CANCELLED.name());

		}
	}

	@Nested
	class Accept {
		@Test
		void accept_must_execute_and_set_state() {
			YourEntity entity = new YourEntity(NEXT);

			buildStateMachine(() -> new YourEntity(INIT))
					.using(entity)
					.accept((trns, e) -> trns.cancelWithoutResult(e, "Hello World"));

			assertSoftly(softly -> {
				softly.assertThat(entity.getState())
						.isEqualTo(CANCELLED);
				softly.assertThat(entity.getMessage())
						.isEqualTo("Hello World");
			});
		}
	}
}
