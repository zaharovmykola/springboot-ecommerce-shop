package org.mykola.zakharov.spring.boot.first.ecommerceshop.entity;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="category")
@Data
@EqualsAndHashCode(exclude = "setOfProducts")
@ToString(exclude = "setOfProducts")
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="name", length=100, nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "category")
    private Set<Product> setOfProducts = new HashSet<>(0);
}
