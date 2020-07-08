package org.mykola.zakharov.spring.boot.first.ecommerceshop.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="users")
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="login", length=25)
    private String vendor;
    @Column(name="password", length=255) // redundant -  тоесть стандарт значение 255? или что?
    private String password;

    @ManyToOne(fetch = FetchType.LAZY) // выкачиватся данные будут только когда попросят
    @JoinColumn(name = "role_id")
    private Roles role;

    @OneToMany(mappedBy = "user")
    private Set<Orders> setOfWorkers = new HashSet<>(0);

    @OneToOne(mappedBy = "user")
    private ShoppingCart shoppingCart;
}
