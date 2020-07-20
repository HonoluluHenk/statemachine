package ch.christophlinder.statemachine;


import ch.christophlinder.statemachine.fixtures.AppFailureException;
import ch.christophlinder.statemachine.fixtures.MyStates;
import ch.christophlinder.statemachine.fixtures.transitions.MyAcceptedTransitions;
import ch.christophlinder.statemachine.fixtures.transitions.MyInitTransitions;
import ch.christophlinder.statemachine.fixtures.transitions.MyNextTransitions;
import ch.christophlinder.statemachine.fixtures.transitions.MyTransitions;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static ch.christophlinder.statemachine.fixtures.MyStates.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SuppressFBWarnings("SIC_INNER_SHOULD_BE_STATIC")
public class StateMachineTest {
    private static final String A = "A";
    private static final String B = "B";
    private static final String X = "X";

    @Nested
    class ConstructorTest {
        @Test
        void shouldThrowOnEmptyTransitions() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> new StateMachine<MyStates, MyTransitions>(Collections.emptyMap())
            );
        }

        @Test
        void shouldThrowOnNullMap() {
            assertThrows(
                    NullPointerException.class,
                    () -> new StateMachine<MyStates, MyTransitions>(null)
            );
        }

        @Test
        void shouldThrowWithNullTransitionEntry() {
            var values = new HashMap<MyStates, MyTransitions>();
            values.put(INIT, null);

            var iae = assertThrows(
                    IllegalArgumentException.class,
                    () -> new StateMachine<>(values)
            );

            assertThat(iae)
                    .hasMessage("Transitions map may not contain null values");
        }
    }


    @Nested
    class WithTransitions {
        final StateMachine<MyStates, MyTransitions> sm = StateMachine.of(
                Map.of(
                        NULL, new MyInitTransitions(),
                        INIT, new MyInitTransitions(),
                        NEXT, new MyNextTransitions(),
                        ACCEPTED, new MyAcceptedTransitions()
                        // MyStates.CANCELLED, // leave as not registered
                )
        );

        @Test
        public void shouldRunTransitions() {
            assertAll(
                    () -> assertThat(sm.<String>transition(INIT, t -> t.goNext(A, B)))
                            .isEqualTo("AB"),
                    () -> assertThrows(TransitionNotAllowed.class,
                            () -> sm.<Boolean>transition(INIT, t -> t.accept(X))),
                    () -> assertThrows(AppFailureException.class,
                            () -> sm.doTransition(INIT, MyTransitions::cancel)),

                    () -> assertThrows(TransitionNotAllowed.class,
                            () -> sm.transition(NEXT, t -> t.goNext(A, B))),
                    () -> assertThat(sm.<Boolean>transition(NEXT, t -> t.accept(X)))
                            .isEqualTo(true),
                    () -> assertThat(sm.<Boolean>transition(NEXT, t -> t.accept("not X")))
                            .isEqualTo(false),
                    () -> sm.doTransition(NEXT, MyTransitions::cancel),

                    () -> assertThrows(TransitionNotAllowed.class,
                            () -> sm.transition(ACCEPTED, t -> t.goNext(A, B))),
                    () -> assertThrows(TransitionNotAllowed.class,
                            () -> sm.transition(ACCEPTED, t -> t.accept(X))),
                    () -> assertThrows(AppFailureException.class,
                            () -> sm.doTransition(ACCEPTED, MyTransitions::cancel))
            );
        }

        @Test
        public void shouldThrowIfTransitionNotRegistered() {
            assertAll(
                    () -> assertThrows(TransitionNotAllowed.class, () -> sm.transition(CANCELLED, t -> t.goNext(A, B))),
                    () -> assertThrows(TransitionNotAllowed.class, () -> sm.transition(CANCELLED, t -> t.accept(X))),
                    () -> assertThrows(TransitionNotAllowed.class, () -> sm.doTransition(CANCELLED, MyTransitions::cancel))
            );
        }

        @Test
        void shouldThrowWithState() {
            TransitionNotAllowed ex = assertThrows(
                    TransitionNotAllowed.class,
                    () -> sm.<Boolean>transition(INIT, t -> t.accept("Schnitzel")));

            assertThat(ex)
                    .hasMessageContaining("INIT");
        }

        @Test
        void shouldThrowWithDebugInfo() {
            TransitionNotAllowed ex = assertThrows(
                    TransitionNotAllowed.class,
                    () -> sm.transition(NEXT, t -> t.goNext("Schnitzel", "Klopfer")));

            assertThat(ex)
                    .hasMessageContaining("Schnitzel")
                    .hasMessageContaining("Klopfer");
        }

        @Test
        void shouldThrowOnNullReturn() {
            assertThrows(
                    NullPointerException.class,
                    () -> sm.transition(NULL, MyTransitions::returnNull)
            );
        }

        @Test
        void pretryPrintShouldNotCrash() {
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
