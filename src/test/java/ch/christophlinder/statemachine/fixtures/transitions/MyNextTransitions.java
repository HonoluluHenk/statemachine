package ch.christophlinder.statemachine.fixtures.transitions;

import java.util.concurrent.atomic.AtomicInteger;

public class MyNextTransitions implements MyTransitions {
    @Override
    public boolean accept(String x) {
        return "X".equals(x);
    }

    @Override
    public void cancel() {
        // example for a void method
    }

    @Override
    public void sideEffect(AtomicInteger inOut, int newValue) {
        inOut.set(newValue);
    }

    @Override
    public String someResult() {
        return "someResult";
    }
}
