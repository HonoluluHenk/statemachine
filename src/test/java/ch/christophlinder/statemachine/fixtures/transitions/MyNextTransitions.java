package ch.christophlinder.statemachine.fixtures.transitions;

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
    public String someResult() {
        return "someResult";
    }
}
