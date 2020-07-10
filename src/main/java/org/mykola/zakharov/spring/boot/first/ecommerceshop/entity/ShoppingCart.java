package org.mykola.zakharov.spring.boot.first.ecommerceshop.entity;


import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="shopping_cart")
@Data
@EqualsAndHashCode(exclude = "setOfProducts")
@ToString(exclude = "setOfProducts")
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @PrimaryKeyJoinColumn // это нужно указывать для одного из один-один
    private User user;

    @OneToMany(mappedBy = "shoppingCart")
    private Set<Product> setOfProducts = new HashSet<>(0);  // here is problem - solve it
}
