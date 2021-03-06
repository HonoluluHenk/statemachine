# State Machine

This module implements a simplified version of the [state pattern](https://en.wikipedia.org/wiki/State_pattern).

Instead of storing the state in the statemachine and passing some state-parameters in a generic (probably type-unsafe) way into the statemachine, this implementation takes another opinionated approach:

1. You define one interface with all possible transitions/actions with all methods marked as `default` and an
 implementation that throws `ActionDeniedException`
2. You create implementations of the interface - one per state - that each only implements the functionality allowed for that state.
3. You initialize the statemachine with a map of states to implementations.
4. Now you can call the interface methods via the statemachine and it will select the correct implementation class.

The EntityStateMachine checks the required start-state, then executes the action and then updates your 
entity-instance with the resulting state of the action.

The return value of action methods must be either an [Outcome](src/main/java/ch/christophlinder/statemachine/entity/Outcome.java) (just holds the next state) 
or a [Result](src/main/java/ch/christophlinder/statemachine/entity/Result.java) (next state + a function-result value).

# Null/NonNull
All parameters/return-values are NonNull if not explicitly stated otherwise.

Nullability is marked by the `edu.umd.cs.findbugs.annotations.Nullable` Annotation. 

# Demo classes
There is a full demo implementation included in the test sources, see 
[ShopService](src/test/java/ch/christophlinder/statemachine/demo/ShopService.java).

# Common Usage patterns

The machine is implemented by following these easy steps:

1. Define your state-defining object(s), preferably an Enum:

   ```java
   public enum OrderState {
       INIT,
       SHOPPING,
       PLACED,
       CANCELLED
   }
   ```

2. Define an interface containing all possible actions as `default` methods that only throw `ActionDeniedException`

    ```java
    import ch.christophlinder.statemachine.ActionDeniedException;
    import ch.christophlinder.statemachine.entity.Outcome;
    import ch.christophlinder.statemachine.entity.Result;
    
    public interface OrderActions {
        default Result<OrderState, Order> initialize(Order emptyOrder, String customer) {
            throw new ActionDeniedException();
        }
        
        default Result<OrderState, OrderLine> addOrderLine(Order order, String lineItem) {
            throw new ActionDeniedException();
        }
        
        default Outcome<OrderState> placeOrder(Order order) {
            throw new ActionDeniedException();
        }
    
        default Result<OrderState, String> printReceipt(Order order) {
            throw new ActionDeniedException();
        }
    }
    ```

3. Implement your business logic per state:

    ```java
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
    ```
    
    ```java
    import ch.christophlinder.statemachine.entity.Outcome;
    import ch.christophlinder.statemachine.entity.Result;
    
    public class ShoppingActions implements OrderActions {
        private final ERPService erpService;
    
        public ShoppingActions(ERPService erpService) {
            this.erpService = erpService;
        }
    
        @Override
        public Result<OrderState, OrderLine> addOrderLine(Order order, String lineItem) {
            OrderLine newLine = new OrderLine(lineItem);
            order.addOrderLine(newLine);
    
            return Result.sameState(newLine);
        }
    
        @Override
        public Outcome<OrderState> placeOrder(Order order) {
            erpService.orderPlaced(order);
    
            return Outcome.of(OrderState.PLACED);
        }
    }
    ```
    
4. Setup your statemachine with the allowed actions

    ```java
    import ch.christophlinder.statemachine.entity.EntityStateMachine;
    
    public class ShopService {
        private final EntityStateMachine<OrderActions, Order, OrderState> stateMachine;
        
        public ShopService() {
            this.stateMachine = EntityStateMachine.of(
                Order::new,
                Order::getState,
                Order::setState,
                buildStates(new ERPService()));
        }
        
        private static Map<OrderState, OrderActions> buildStates(ERPService erpService) {
            Map<OrderState, OrderActions> states = new EnumMap<>(OrderState.class);
        
            states.put(OrderState.INIT, new InitActions());
            states.put(OrderState.SHOPPING, new ShoppingActions(erpService));
            states.put(OrderState.PLACED, new PlacedActions());
        
            // please note: since CANCELLED is a terminal state without any actions,
            // nothing needs to be implemented for this state:
            // states.put(OrderState.CANCELLED, new CancelledActions());
        
            return states;
        }
    
        // ...
    }
    ```
    
5. Invoke it:

    ```java
    public class ShopService {
        // ...continued
    
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
                // action without return-value
                .accept(OrderActions::placeOrder);
        
            // example use-case, maybe using JPA
            persistence.update(order);
        }
    }
    ```

    See [demo folder](src/test/java/ch/christophlinder/statemachine/demo/) for more demo invocations.

# Installation

This lib is released as a maven artifact.

This artifact is built and hosted on [JitPack](https://jitpack.io/).
 You'll find information on how to integrate this repo at the jitpack page or in the [Maven](#Maven) section below. 

groupId: com.github.HonoluluHenk
artifactId: statemachine

## Versioning
This project tries really hard to stick to [semantic versioning](https://semver.org).

See [project releases](https://github.com/HonoluluHenk/statemachine/releases) for available versions.

## Maven
Add the artifact repository:
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

Dependency:
```xml
<dependency>
    <groupId>com.github.HonoluluHenk</groupId>
    <artifactId>statemachine</artifactId>
    <version>x.y.z</version>
</dependency>
```


# Licensing

This software is distributed under the LGPL, see [LICENSE] for more details.

