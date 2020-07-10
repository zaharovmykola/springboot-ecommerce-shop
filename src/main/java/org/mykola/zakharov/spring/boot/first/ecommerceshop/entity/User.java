package org.mykola.zakharov.spring.boot.first.ecommerceshop.entity;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="users")
@Data
@EqualsAndHashCode(exclude = {"setOfOrders", "shoppingCart"})
@ToString(exclude = {"setOfOrders", "shoppingCart"})
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="name", length=25, unique = true)
    private String name;
    @Column(name="password", length=255) // redundant -  тоесть стандарт значение 255? или что?
    private String password;

    @ManyToOne(fetch = FetchType.LAZY) // выкачиватся данные будут только когда попросят
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Set<Order> setOfOrders = new HashSet<>(0);

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    private ShoppingCart shoppingCart;
}
