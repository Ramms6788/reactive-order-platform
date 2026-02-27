package org.example.reactiveorderplatform.service;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Utils {

    public void simulateLatency(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }

}
