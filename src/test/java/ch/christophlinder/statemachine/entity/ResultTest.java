package ch.christophlinder.statemachine.entity;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ResultTest {
    @Test
    void shouldPassArgumentsToGetters() {
        Result<String, Integer> r = new Result<>("Foo", 42);

        assertThat(r.nextState())
                .hasValue("Foo");
        assertThat(r.getResult())
                .isEqualTo(42);
    }

}
