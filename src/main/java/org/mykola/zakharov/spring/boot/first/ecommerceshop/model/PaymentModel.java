package org.mykola.zakharov.spring.boot.first.ecommerceshop.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder(toBuilder = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentModel {
    private Long id;
    private String transactionId;
    private String vendor;
    private String paymentDateString;
    private BigDecimal amount;
}
