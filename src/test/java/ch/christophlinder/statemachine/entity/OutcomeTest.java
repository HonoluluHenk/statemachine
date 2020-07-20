package ch.christophlinder.statemachine.entity;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OutcomeTest {

    @Test
    void shouldPassValuesToGetter() {
        Outcome<BigInteger, BigInteger> out = new Outcome<>(BigInteger.ZERO, BigInteger.ONE);

        assertThat(out.getNextState())
                .isSameAs(BigInteger.ZERO);

        assertThat(out.getResult())
                .isSameAs(BigInteger.ONE);
    }

    @Test
    void shouldThrowOnNullCtorParams() {

        assertThrows(NullPointerException.class, () -> new Outcome<>(null, BigInteger.ONE));
        assertThrows(NullPointerException.class, () -> new Outcome<>(BigInteger.ZERO, null));
    }
}