package ch.christophlinder.statemachine;

import ch.christophlinder.statemachine.fixtures.MyState;
import ch.christophlinder.statemachine.fixtures.transitions.MyAcceptedTransitions;
import ch.christophlinder.statemachine.fixtures.transitions.MyInitTransitions;
import ch.christophlinder.statemachine.fixtures.transitions.MyNextTransitions;
import ch.christophlinder.statemachine.fixtures.transitions.MyTransitions;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static ch.christophlinder.statemachine.fixtures.MyState.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SuppressWarnings({"FieldNamingConvention", "ResultOfObjectAllocationIgnored"})
@SuppressFBWarnings("SIC_INNER_SHOULD_BE_STATIC")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class StateMachineTest {
    private static final String A = "A";
    private static final String B = "B";
    private static final String X = "X";

    @Nested
    class ConstructorTest {
        @Test
        void throws_when_no_transitions_defined() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> new StateMachine<MyState, MyTransitions>(Collections.emptyMap())
            );
        }

        @Test
        void throws_on_null_transitions_map() {
            assertThrows(
                    NullPointerException.class,
                    () -> new StateMachine<MyState, MyTransitions>(null)
            );
        }

        @Test
        void throws_on_null_transition() {
            Map<MyState, MyTransitions> values = new EnumMap<>(MyState.class);
            values.put(INIT, null);

            IllegalArgumentException iae = assertThrows(
                    IllegalArgumentException.class,
                    () -> new StateMachine<>(values)
            );

            assertThat(iae)
                    .hasMessage("Transitions map may not contain null values");
        }
    }

    @Nested
    class WithTransitionsTest {
        private final StateMachine<MyState, MyTransitions> sm = StateMachine.of(buildTransitions());

        private Map<MyState, MyTransitions> buildTransitions() {
            Map<MyState, MyTransitions> map = new EnumMap<>(MyState.class);
            map.put(NULL, new MyInitTransitions());
            map.put(INIT, new MyInitTransitions());
            map.put(NEXT, new MyNextTransitions());
            map.put(ACCEPTED, new MyAcceptedTransitions());
            // MyStates.CANCELLED, // leave as not registered

            return map;
        }

        @Test
        public void accepts_parameters_and_returns_results() {
            assertAll(
                    () -> assertThat(sm.<String>transition(INIT, t -> t.goNext(A, B)))
                            .isEqualTo("AB"),
                    () -> assertThat(sm.<Boolean>transition(NEXT, t -> t.accept(X)))
                            .isEqualTo(true),
                    () -> assertThat(sm.<Boolean>transition(NEXT, t -> t.accept("not X")))
                            .isEqualTo(false),
                    () -> assertThat(sm.transition(NEXT, MyTransitions::someResult))
                            .isEqualTo("someResult")
            );
        }

        @Test
        void transitions_without_parameters_do_not_throw() {
                assertThatCode(() -> sm.doTransition(NEXT, MyTransitions::cancel))
                        .doesNotThrowAnyException();
        }

        @Test
        void doTransition_calls_transition_impl() {
            AtomicInteger inOut = new AtomicInteger(0);

            sm.doTransition(NEXT, t -> t.sideEffect(inOut, 42));

            assertThat(inOut.get())
                    .isEqualTo(42);
        }

        @Nested
        class IllegalTransitions {
            @Test
            public void throws_if_transition_not_registered() {
                assertSoftly( softly -> {
                    softly.assertThatThrownBy(() -> sm.transition(CANCELLED, t -> t.goNext(A, B)))
                            .isExactlyInstanceOf(ActionDeniedException.class);

                    softly.assertThatThrownBy(() -> sm.transition(CANCELLED, t -> t.accept(X)))
                            .isExactlyInstanceOf(ActionDeniedException.class);

                    softly.assertThatThrownBy(() -> sm.doTransition(CANCELLED, MyTransitions::cancel))
                            .isExactlyInstanceOf(ActionDeniedException.class);
                });
            }

            @Test
            void throws_if_transition_not_allowed_in_state() {
                ActionDeniedException ex = assertThrows(
                        ActionDeniedException.class,
                        () -> sm.<Boolean>transition(INIT, t -> t.accept("Schnitzel"))
                );

                assertThat(ex)
                        .hasMessageContaining("INIT");
            }

            @Test
            void throws_on_null_return_from_transition_implementation() {
                assertThrows(
                        NullPointerException.class,
                        () -> sm.transition(NULL, MyTransitions::returnNull)
                );
            }

            @Test
            void IllegalTransitionException_is_enriched_with_debug_info() {
                ActionDeniedException ex = assertThrows(
                        ActionDeniedException.class,
                        () -> sm.transition(NEXT, t -> t.goNext("Schnitzel", "Klopfer"))
                );

                assertThat(ex)
                        .hasMessageContaining("Schnitzel")
                        .hasMessageContaining("Klopfer");
            }

            @Test
            void prettyPrint_should_not_crash() {
                String actual = sm.toString();

                // some example values
                assertThat(actual)
                        .contains("StateMachine")
                        .contains("INIT")
                        .contains("MyInitTransitions")
                ;
            }
        }
    }

}
