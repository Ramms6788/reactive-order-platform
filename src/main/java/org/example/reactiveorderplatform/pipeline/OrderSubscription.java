package org.example.reactiveorderplatform.pipeline;

import org.example.reactiveorderplatform.model.Order;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class OrderSubscription implements Subscription {

    private final    Subscriber<? super Order> subscriber;
    private final    List<Order>               orders;
    private final    AtomicLong                counter   = new AtomicLong(0);
    private volatile boolean                   cancelled = false;
    private volatile boolean                   completed = false;

    public OrderSubscription(Subscriber<? super Order> subscriber,
                             List<Order> orders) {
        this.subscriber = subscriber;
        this.orders     = orders;
    }

    @Override
    public void request(long n) {
        if (n <= 0) {
            subscriber.onError(new IllegalArgumentException(
                    "§3.9: request amount must be > 0, got " + n));
            return;
        }

        if (cancelled || completed) return;

        System.out.println("Requesting next batch of " + n);
        long startIndex = counter.get();
        long endIndex   = Math.min(startIndex + n, orders.size());
        for (long i = startIndex; i < endIndex; i++) {
            counter.incrementAndGet();
            subscriber.onNext(orders.get((int) i));
        }

        if (!completed && counter.get() >= orders.size()) {
            completed = true;
            subscriber.onComplete();
        }

    }

    @Override
    public void cancel() {
        cancelled = true;
    }
}
