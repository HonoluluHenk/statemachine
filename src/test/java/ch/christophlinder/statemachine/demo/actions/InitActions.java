package ch.christophlinder.statemachine.demo.actions;

import ch.christophlinder.statemachine.demo.Order;
import ch.christophlinder.statemachine.demo.OrderState;
import ch.christophlinder.statemachine.entity.Result;

public class InitActions implements OrderActions {
	@Override
	public Result<OrderState, Order> initialize(Order newOrder, String customer) {
		newOrder.setCustomer(customer);

		// you might persist the order here or outside the statemachine
		// depending on your taste/ORM-framework.

		return Result.of(OrderState.SHOPPING, newOrder);
	}
}
