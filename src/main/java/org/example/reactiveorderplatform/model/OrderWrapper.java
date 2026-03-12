package org.example.reactiveorderplatform.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderWrapper {

    private Order order;
    private CustomerDetails customerDetails;

}
