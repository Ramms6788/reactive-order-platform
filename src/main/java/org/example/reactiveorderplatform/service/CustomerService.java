package org.example.reactiveorderplatform.service;

import org.example.reactiveorderplatform.model.CustomerDetails;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface CustomerService {

    Mono<CustomerDetails> lookup(UUID customerId);

}
