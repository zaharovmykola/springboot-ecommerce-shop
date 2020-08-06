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

    @Column(name="name", length=100, unique = true, nullable = false)
    private String name;
    @Column(name = "password", nullable = false)
    private String password;

    @ManyToOne(fetch = FetchType.EAGER) // выкачиватся данные будут только когда попросят
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Set<Order> setOfOrders = new HashSet<>(0);
}
