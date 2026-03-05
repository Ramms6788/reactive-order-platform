package org.example.reactiveorderplatform.pipeline;

import org.example.reactiveorderplatform.model.Order;
import org.example.reactiveorderplatform.service.Utils;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public class OrderProcessor implements Subscriber<Order> {

    private final long capacity;

    private volatile Subscription subscription;
    private          long         processedCounter;

    public OrderProcessor(long capacity) {
        this.capacity         = capacity;
        this.processedCounter = 0;
    }

    @Override
    public void onSubscribe(Subscription s) {
        this.subscription = s;
        subscription.request(capacity);
    }

    /**
     * NOTE: This implementation has recursive re-entry — onNext() may trigger
     * request() within the for-loop's call stack. For 50 items this is fine,
     * but with millions of items this would cause StackOverflowError.
     * Production implementations (like Reactor) use a queue + drain loop pattern.
     */
    @Override
    public void onNext(Order order) {
        Utils.simulateLatency(100);
        System.out.println("Processing order: " + order.getId() + ", Thread: " + Thread.currentThread().getName());

        if (++processedCounter == capacity) {
            processedCounter = 0;
            subscription.request(capacity);
        }
    }

    @Override
    public void onError(Throwable t) {
        System.out.println("Error processing order: " + t.getMessage());
    }

    @Override
    public void onComplete() {
        System.out.println("All orders processed");
    }
}
