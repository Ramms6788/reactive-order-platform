package org.example.reactiveorderplatform.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class FailedOrder {
    private Order         order;
    private Throwable     ex;
    private LocalDateTime timestamp;
}
