package ch.christophlinder.statemachine.entity;

import ch.christophlinder.statemachine.entity.fixtures.MyEntity;
import ch.christophlinder.statemachine.entity.fixtures.YourInitTransitions;
import ch.christophlinder.statemachine.entity.fixtures.YourTransitions;
import ch.christophlinder.statemachine.fixtures.MyStates;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static ch.christophlinder.statemachine.fixtures.MyStates.INIT;
import static org.assertj.core.api.Assertions.assertThat;

public class EntityStateMachineTest {

    private final EntityStateMachine<EntityWithState<MyStates>, MyStates, YourTransitions> sm = EntityStateMachine
            .ofEntityWithState(Map.of(
                    INIT, new YourInitTransitions()
                    )
            );


    @Test
    void shouldTransitionCorrectly() {
        var myEntity = new MyEntity(INIT);

        var actual = sm.transition(myEntity, YourTransitions::goNext);

        assertThat(actual.getNextState())
                .isEqualTo(MyStates.NEXT);
        assertThat(actual.getResult())
                .isEqualTo("Hello World");
    }

    @Test
    void shouldReturnUnderlyingStateMachine() {
        var actual = sm.getStateMachine();

        assertThat(actual)
                .isNotNull();
    }
}
