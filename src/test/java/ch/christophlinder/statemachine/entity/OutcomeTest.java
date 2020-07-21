package ch.christophlinder.statemachine.entity;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class OutcomeTest {

    @Test
    void getter_retrieves_value_from_ctor() {
        Outcome<BigInteger> out = new Outcome<>(BigInteger.ZERO);

        assertThat(out.getNextState())
                .isSameAs(BigInteger.ZERO);
    }

    @Test
    void throws_on_null_state_param() {
        //noinspection ResultOfObjectAllocationIgnored
        assertThrows(
                NullPointerException.class,
                () -> new Outcome<>(null));
    }
}
