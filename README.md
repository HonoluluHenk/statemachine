# State Machine

This module implements a simplified version of the [state pattern](https://en.wikipedia.org/wiki/State_pattern).

Instead of storing the state in the statemachine and passing some state-parameters in a generic (i.e.: type-unsafe) way into the statemachine, this implementation takes another opinionated approach:

1. You define an interface with all possible transitions/actions with all methods as `default` but only throwing `TransitionDeniedException`
2. You have multiple implementations of the interfaces - one per state - that each only implements the functionality allowed for that state.
3. Finally, you call the methods `StateMachine` or `EntityStateMachine`

Depending on which StateMachine you choose, the statemachine checks the required start-state and updates your object-instance with the resulting state (the `Outcome`).



## Common Usage

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

   

2. Define an interface containing all possible actions/transitions as `default` methods that only throw `TransitionDeniedException`

    ```java
    import ch.christophlinder.statemachine.ActionDeniedException;import ch.christophlinder.statemachine.TransitionDeniedException;
    
    interface OrderTransitions {
        /**
         * create a new instance of the desired object, i.e.: initialize the object
         */
        default Order initialize(String customer) {
            throw new ActionDeniedException();
        }
        
        /**
         * some transition only executing side-effects ("void")
         */
        default Outcome<OrderState> addOrderLine(OrderLine string) {
            throw new ActionDeniedException();
        }
        
        /**
         * some other transition, this time with business return value: BillingInfo
         */
        default Result<OrderState, BillingInfo> acceptOrder(Order order) {
            throw new ActionDeniedException();
        }
    }
    ```

3. Implement your business logic per state:

    ```java
    class InitTransitions implements OrderTransitions {
        public Order initialize(String customer) {
            emptyOrder.setCustomer(customer);
            
            return 
        }
    }
    
class ShoppingTransitions implements OrderTransitions {
        public Outcome<OrderState> addOrderLine(Order order, OrderLine string) {
            order.addLine(string);
            
            return Outcome.of(OrderState.SHOPPING);
        }
    }
    ```
    
    

