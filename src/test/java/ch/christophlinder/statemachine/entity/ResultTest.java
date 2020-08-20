package ch.christophlinder.statemachine.entity;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ResultTest {
    @Test
    void factory_passes_arguments_to_getter() {
        Result<String, Integer> r = Result.of("Foo", 42);

        assertThat(r.nextState())
                .hasValue("Foo");
        assertThat(r.getValue())
                .isEqualTo(42);
    }

    @Test
    void withValue_must_keep_state_but_change_value() {
        Result<String, Integer> r = new Result<>("Foo", 42);

        Result<String, Integer> changed = r.withValue(23);

        assertThat(changed.nextState())
                .hasValue("Foo");
        assertThat(changed.getValue())
                .isEqualTo(23);
    }

    @Test
    void withValue_must_keep_state_but_change_value_for_samestate() {
        Result<String, Integer> r = Result.sameState(42);

        Result<String, Integer> changed = r.withValue(23);

        assertThat(changed.nextState())
                .isEmpty();
        assertThat(changed.getValue())
                .isEqualTo(23);
    }

}
