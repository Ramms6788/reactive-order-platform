package org.example.reactiveorderplatform;

import jakarta.annotation.PostConstruct;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.example.reactiveorderplatform.model.Order;
import org.example.reactiveorderplatform.service.OrderService;
import org.example.reactiveorderplatform.service.Utils;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
public class OrderSimulationService {

    private final OrderService orderService;

    @PostConstruct
    public void init() {
        simulateOrderProcessing(Utils.generateOrders(20));
    }

    /**
     *  It is clear that nothing happens without on subscribe.
     *  `Flux.log()` also does not work without on subscribe.
     *
     *  With `subscribe()` called we can see a log and data flow.
     *  What interesting is that after `onSubscribe()` there is a call `request(256)` which is a request for 256 elements
     *  and I did not expect this.
     *  The other observation is that each run I receive error (simulated) and processing is stopped with `Operator called default onErrorDropped`
     *  and different exception is thrown. So, it is clear I need some error handling to catch order processing errors and continue to
     *  process remaining orders.
     */
    public void simulateOrderProcessing(@NonNull Iterable<Order> orders) {
        Flux.fromIterable(orders)
                .log()
                .flatMap(orderService::processOrder)
                .subscribe();
    }

}
