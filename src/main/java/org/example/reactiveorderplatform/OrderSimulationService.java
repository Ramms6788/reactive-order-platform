package org.example.reactiveorderplatform;

import jakarta.annotation.PostConstruct;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.example.reactiveorderplatform.model.Order;
import org.example.reactiveorderplatform.model.OrderWrapper;
import org.example.reactiveorderplatform.service.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.Duration;

@RequiredArgsConstructor
public class OrderSimulationService {

    private final OrderService             orderService;
    private final CustomerService          customerService;
    private final FraudCheckService        fraudCheckService;
    private final FraudNotificationService fraudNotificationService;

    @PostConstruct
    public void init() {
//        simulateOrderProcessing(Utils.generateOrders(20));
        Flux.merge(
                        restApiOrders(Utils.generateOrders(10)),
                        messageQueueOrders(Utils.generateOrders(10))
                )
//                .log()
                .distinct(Order::getCustomerId)
                .flatMap(order -> {
                    if (order.getTotalAmount().compareTo(BigDecimal.valueOf(100.0)) < 0)
                        return Mono.just(order);

                    return fraudCheckService.check(order)
                            .flatMap(isFraud -> {
                                if (isFraud)
                                    return fraudNotificationService.notify(order).then(Mono.empty());

                                return Mono.just(order);
                            });
                })
                .flatMap(order -> Mono.zip(customerService.lookup(order.getCustomerId()), Mono.just(order))
                        .map(tuple -> OrderWrapper.builder()
                                .order(tuple.getT2())
                                .customerDetails(tuple.getT1())
                                .build())
                )
                .groupBy(orderWrapper -> orderWrapper.getCustomerDetails().isVip())
                .flatMap(group -> {
                    if (group.key())
                        //looks like in terms of performance not much changed and there is no difference in vip processing
                        return group
                                .doOnNext(orderWrapper -> System.out.println("Is VIP = " + orderWrapper.getCustomerDetails().isVip() + " OrderId = " + orderWrapper.getOrder().getId()))
                                .concatMap(orderWrapper -> orderService.processOrder(orderWrapper.getOrder()));//20 seconds overall
//                        return group
//                                .doOnNext(orderWrapper -> System.out.println("Is VIP = " + orderWrapper.getCustomerDetails().isVip() + " OrderId = " + orderWrapper.getOrder().getId()))
//                                .flatMap(orderWrapper -> orderService.processOrder(orderWrapper.getOrder()));//20 seconds overall
                    else
                        return group
                                .doOnNext(orderWrapper -> System.out.println("Is VIP = " + orderWrapper.getCustomerDetails().isVip() + " OrderId = " + orderWrapper.getOrder().getId()))
                                .flatMap(orderWrapper -> orderService.processOrder(orderWrapper.getOrder()));
                })
                .subscribe();
    }

    public Flux<Order> restApiOrders(@NonNull Iterable<Order> orders) {
        return Flux.fromIterable(orders)
                .delayElements(Duration.ofMillis(2));
    }

    public Flux<Order> messageQueueOrders(@NonNull Iterable<Order> orders) {
        return Flux.fromIterable(orders)
                .delayElements(Duration.ofMillis(30));
    }

    /**
     * It is clear that nothing happens without on subscribe.
     * `Flux.log()` also does not work without on subscribe.
     * <p>
     * With `subscribe()` called we can see a log and data flow.
     * What interesting is that after `onSubscribe()` there is a call `request(256)` which is a request for 256 elements
     * and I did not expect this.
     * The other observation is that each run I receive error (simulated) and processing is stopped with `Operator called default onErrorDropped`
     * and different exception is thrown. So, it is clear I need some error handling to catch order processing errors and continue to
     * process remaining orders.
     */
    public void simulateOrderProcessing(@NonNull Iterable<Order> orders) {
        Flux.fromIterable(orders)
                .log()
                .flatMap(orderService::processOrder)
                .subscribe();
    }

}
