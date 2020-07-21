package ch.christophlinder.statemachine.entity;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OutcomeTest {

    @Test
    void shouldPassValuesToGetter() {
        Outcome<BigInteger> out = new Outcome<>(BigInteger.ZERO);

        assertThat(out.getNextState())
                .isSameAs(BigInteger.ZERO);
    }

    @Test
    void shouldThrowOnNullCtorParams() {
        //noinspection ResultOfObjectAllocationIgnored
        assertThrows(
                NullPointerException.class,
                () -> new Outcome<>(null));
    }
}