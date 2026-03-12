package org.example.reactiveorderplatform.service;

import org.example.reactiveorderplatform.model.CustomerDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Override
    public Mono<CustomerDetails> lookup(UUID customerId) {
        return Mono.fromCallable(() -> {
            Utils.simulateLatency(100);

            return CustomerDetails.builder()
                    .id(customerId)
                    .name("John Doe")
                    .email("john.doe@example.com")
                    .vip(Math.random() < 0.2)
                    .build();
            }
        );
    }
}
