package ch.christophlinder.statemachine.entity;

import java.io.Serializable;

import edu.umd.cs.findbugs.annotations.DefaultAnnotation;
import edu.umd.cs.findbugs.annotations.NonNull;

@FunctionalInterface
@DefaultAnnotation(NonNull.class)
public interface ActionInvocation<ActionsInterface, Entity, Result> {
	Result invoke(ActionsInterface actions, Entity entity);
}
