package org.mykola.zakharov.spring.boot.first.ecommerceshop.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder(toBuilder = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoleModel {
    private Long id;
    private String name;
    private String status;
    private String message;
    private RoleModel role;
    private List roles;
}
