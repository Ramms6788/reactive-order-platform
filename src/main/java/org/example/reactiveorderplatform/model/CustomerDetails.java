package org.example.reactiveorderplatform.model;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class CustomerDetails {
    private UUID    id;
    private String  name;
    private String  email;
    private boolean vip;
}
