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
public class ProductModel {
    private Long id;
    private String title;
    private String description;
    private BigDecimal price;
    private Integer quantity;
    private String image;
    public Long category_id;
}
