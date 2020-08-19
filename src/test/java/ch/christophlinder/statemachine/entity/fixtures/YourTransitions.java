package ch.christophlinder.statemachine.entity.fixtures;

import ch.christophlinder.statemachine.ActionDeniedException;
import ch.christophlinder.statemachine.entity.Outcome;
import ch.christophlinder.statemachine.entity.Result;

public interface YourTransitions {

    default Result<YourState, YourEntity> initialize(YourEntity entity) {
        throw new ActionDeniedException();
    }

    default Result<YourState, YourEntity> initializeWithParams(YourEntity entity, String message) {
        throw new ActionDeniedException();
    }

    default Result<YourState, YourEntity> initializeWithNewInstance(YourEntity entity, YourState otherEntitySTate) {
        throw new ActionDeniedException();
    }

    default Result<YourState, String> cancelWithResult(String resultMessage) {
        throw new ActionDeniedException();
    }

    default Outcome<YourState> cancelWithoutResult(YourEntity entity, String resultMessage) {
        throw new ActionDeniedException();

    }

    default Result<YourState, String> goNext() {
        throw new ActionDeniedException();
    }

    default Result<YourState, String> keepState(String message) {
        throw new ActionDeniedException();
    }
}
