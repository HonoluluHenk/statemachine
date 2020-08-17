package ch.christophlinder.statemachine.entity;

import ch.christophlinder.statemachine.ActionDeniedException;
import ch.christophlinder.statemachine.entity.fixtures.*;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Supplier;

import static ch.christophlinder.statemachine.entity.EntityStateMachine.ofEntityWithState;
import static ch.christophlinder.statemachine.entity.fixtures.YourState.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class EntityStateMachineTest {

    public static EntityStateMachine<YourEntity, YourState, YourTransitions> buildStateMachine(Supplier<YourEntity> entityCtor) {
        return ofEntityWithState(entityCtor, allowedTransitions());
    }

    private static Map<YourState, YourTransitions> allowedTransitions() {
        Map<YourState, YourTransitions> map = new EnumMap<>(YourState.class);
        map.put(INIT, new YourInitTransitions());
        map.put(NEXT, new YourNextTransitions());
        return map;
    }

    @Nested
    class Transitions {
        private final EntityStateMachine<YourEntity, YourState, YourTransitions> sm = buildStateMachine(() -> new YourEntity(INIT));

        @Test
        void allow_invoking_a_valid_transition() {
            YourEntity start = new YourEntity(INIT);

            Result<YourState, String> actual = sm.using(start)
                    .execute((t, e) -> t.goNext());

            assertSoftly(softly -> {
                softly.assertThat(actual.nextState())
                        .hasValue(YourState.NEXT);
                softly.assertThat(actual.getResult())
                        .isEqualTo("Hello World");
            });
        }

        @Test
        void throw_when_invoking_illegal_transition() {
            YourEntity start = new YourEntity(CANCELLED);

            ActionDeniedException ex = assertThrows(
                    ActionDeniedException.class,
                    () -> sm.using(start).execute((t, e) -> t.goNext())
            );

            assertThat(ex)
                    .hasMessageContaining(CANCELLED.name());
        }

        @Test
        void allow_results_with_subclasses_in_transitions() {
            YourEntity start = new YourEntity(NEXT);

            Result<YourState, String> result = sm.using(start)
                    .execute((tr, e) -> tr.cancelWithResult("Test Cancel Message"));

            assertSoftly(softly -> {
                softly.assertThat(result.nextState())
                        .hasValue(CANCELLED);
                softly.assertThat(result.getResult())
                        .isEqualTo("Test Cancel Message");
            });
        }
    }

    @Nested
    class CreateAndThen {
        @Test
        void should_instantiate_without_parameters() {
            YourState initialState = INIT;
            YourEntity original = new YourEntity(initialState, "constructor message");

            EntityStateMachine<YourEntity, YourState, YourTransitions> sm = buildStateMachine(() -> original);

            YourEntity newEntity = sm.newEntity()
                    .execute(YourTransitions::initialize)
                    .getResult();

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
            EntityStateMachine<YourEntity, YourState, YourTransitions> sm = buildStateMachine(() -> original);

            YourEntity newEntity = sm.newEntity()
                    .execute((tr, entity) -> tr.initializeWithParams(entity, "test message"))
                    .getResult();

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
            EntityStateMachine<YourEntity, YourState, YourTransitions> sm = buildStateMachine(() -> original);

            YourEntity newEntity = sm.newEntity()
                    .execute((tr, entity) -> tr.initializeWithNewInstance(entity, CANCELLED))
                    .getResult();

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
            EntityStateMachine<YourEntity, YourState, YourTransitions> sm = buildStateMachine(() -> original);

            ActionDeniedException ex = assertThrows(
                    ActionDeniedException.class,
                    () -> sm.newEntity()
                            .execute(YourTransitions::initialize)
            );

            assertThat(ex)
                    .hasMessageContaining(CANCELLED.name());

        }
    }
}
