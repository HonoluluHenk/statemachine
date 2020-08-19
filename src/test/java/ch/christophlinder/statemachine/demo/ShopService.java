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

@SuppressWarnings("unused")
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
				// required if you want entities instantiated
				// by newEntity()
				() -> new Order(OrderState.INIT),
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
				.newEntity()
				.apply((trns, order) -> trns.initialize(order, customer));

		// example use-case, maybe using JPA
		persistence.create(newOrder);

		return newOrder;
	}

	public OrderLine addOrderLine(Order order, String lineItem) {
		OrderLine newLine = stateMachine
				.using(order)
				.apply((trns, o) -> trns.addOrderLine(o, lineItem));

		// example use-case, maybe using JPA
		persistence.create(newLine);
		persistence.update(order);

		return newLine;
	}

	public void placeOrder(Order order) {
		stateMachine
				.using(order)
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
