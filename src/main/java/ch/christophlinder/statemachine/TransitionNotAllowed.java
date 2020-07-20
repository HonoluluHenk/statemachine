package ch.christophlinder.statemachine;

import edu.umd.cs.findbugs.annotations.Nullable;

import java.io.Serializable;

public class TransitionNotAllowed extends RuntimeException {
    @Nullable
    private final Serializable state;
    @Nullable
    private final String debugInfo;

    TransitionNotAllowed(Serializable state, @Nullable String debugInfo, @Nullable TransitionNotAllowed cause) {
        super("Transition not allowed in state: " + state + ", debugInfo: " + debugInfo, cause);
        this.state = state;
        this.debugInfo = debugInfo;
    }

    public TransitionNotAllowed(@Nullable String debugInfo) {
        super("Transition not allowed in state: (see next exception in stacktrace), debugInfo: " + debugInfo);
        this.state = null;
        this.debugInfo = debugInfo;
    }

    public TransitionNotAllowed() {
        this("(no debugInfo)");
    }

    @Nullable
    public Serializable getState() {
        return state;
    }

    @Nullable
    public String getDebugInfo() {
        return debugInfo;
    }
}
