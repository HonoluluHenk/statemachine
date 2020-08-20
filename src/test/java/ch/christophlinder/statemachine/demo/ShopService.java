package ch.christophlinder.statemachine.demo;

import java.util.EnumMap;
import java.util.Map;

import ch.christophlinder.statemachine.demo.actions.InitActions;
import ch.christophlinder.statemachine.demo.actions.OrderActions;
import ch.christophlinder.statemachine.demo.actions.PlacedActions;
import ch.christophlinder.statemachine.demo.actions.ShoppingActions;
import ch.christophlinder.statemachine.demo.utility.ERPService;
import ch.christophlinder.statemachine.demo.utility.Persistence;
import ch.christophlinder.statemachine.entity.EntityStateMachine;

@SuppressWarnings({ "unused", "RedundantSuppression" })
public class ShopService {

	private final EntityStateMachine<OrderActions, Order, OrderState> stateMachine;
	private final Persistence persistence;

	// Might be Spring @Autowired or CDI @Inject
	public ShopService(ERPService erpService, Persistence persistence) {
		this.stateMachine = buildStateMachine(erpService);
		this.persistence = persistence;
	}

	public EntityStateMachine<OrderActions, Order, OrderState> buildStateMachine(ERPService erpService) {
		return EntityStateMachine.of(
				Order::new,
				Order::getState,
				Order::setState,
				buildStates(erpService)
		);
	}

	private static Map<OrderState, OrderActions> buildStates(ERPService erpService) {
		Map<OrderState, OrderActions> states = new EnumMap<>(OrderState.class);

		states.put(OrderState.INIT, new InitActions());
		states.put(OrderState.SHOPPING, new ShoppingActions(erpService));
		states.put(OrderState.PLACED, new PlacedActions());

		// please note: since CANCELLED is a terminal state without any actions,
		// nothing needs to be implemented for this state:
		// states.put(OrderState.CANCELLED, new CancelledTransitions());

		return states;
	}

	public Order createOrder(String customer) {
		Order newOrder = stateMachine
				// create a new entity instance using the configured CTOR
				.newEntity()
				// apply: invoke action that returns new state + result (the "Result")
				.apply((actions, order) -> actions.initialize(order, customer));

		// example use-case, maybe using JPA
		persistence.create(newOrder);

		return newOrder;
	}

	public OrderLine addOrderLine(Order order, String lineItem) {
		OrderLine newLine = stateMachine
				// use the existing entity
				.using(order)
				// apply: invoke action that returns new state + result (the "Result")
				.apply((actions, o) -> actions.addOrderLine(o, lineItem));

		// example use-case, maybe using JPA
		persistence.create(newLine);
		persistence.update(order);

		return newLine;
	}

	public void placeOrder(Order order) {
		stateMachine
				// use the existing entity
				.using(order)
				// accept: invoke action that only returns a new state (the "Outcome").
				.accept(OrderActions::placeOrder);

		// example use-case, maybe using JPA
		persistence.update(order);
	}

	public String printReceipt(Order order) {
		String receipt = stateMachine
				.using(order)
				.apply(OrderActions::printReceipt);

		return receipt;
	}

}
