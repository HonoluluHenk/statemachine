package ch.christophlinder.statemachine.demo;

import ch.christophlinder.statemachine.demo.actions.OrderActions;
import ch.christophlinder.statemachine.demo.utility.ERPService;
import ch.christophlinder.statemachine.demo.utility.Persistence;

@SuppressWarnings("unused")
public class ShopService {

    private final OrderStateMachine stateMachine;
    private final Persistence persistence;

    // Might be Spring @Autowired or CDI @Inject
    public ShopService(ERPService erpService, Persistence persistence) {
        this.stateMachine = new OrderStateMachine(erpService);
        this.persistence = persistence;
    }

    public Order createOrder(String customer) {
        Order newOrder = stateMachine
                .newEntity()
                .execute((trns, order) -> trns.initialize(order, customer))
                .getResult();

        // example use-case, maybe using JPA
        persistence.create(newOrder);

        return newOrder;
    }

    public OrderLine addOrderLine(Order order, String lineItem) {
        OrderLine newLine = stateMachine
                .using(order)
                .execute((trns, o) -> trns.addOrderLine(o, lineItem))
                .getResult();

        // example use-case, maybe using JPA
        persistence.create(newLine);
        persistence.update(order);

        return newLine;
    }

    public void placeOrder(Order order) {
        stateMachine
                .using(order)
                .execute(OrderActions::placeOrder);

        // example use-case, maybe using JPA
        persistence.update(order);
    }

    public String printReceipt(Order order) {
        String receipt = stateMachine
                .using(order)
                .execute(OrderActions::printReceipt)
                .getResult();

        return receipt;
    }

}
