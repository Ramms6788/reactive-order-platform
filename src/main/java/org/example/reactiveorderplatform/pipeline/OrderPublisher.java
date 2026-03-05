package org.example.reactiveorderplatform.pipeline;

import lombok.NonNull;
import org.example.reactiveorderplatform.model.Order;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

import java.util.List;

public class OrderPublisher implements Publisher<Order> {

    private final List<Order> orders;

    public OrderPublisher(@NonNull List<Order> orders) {
        this.orders = orders;
    }

    @Override
    public void subscribe(Subscriber<? super Order> s) {
        OrderSubscription subscription = new OrderSubscription(s, orders);
        s.onSubscribe(subscription);
    }
}
