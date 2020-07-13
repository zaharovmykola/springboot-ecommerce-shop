package org.mykola.zakharov.spring.boot.first.ecommerceshop.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="products")
@Data
@EqualsAndHashCode(exclude = "setOfOrders")
@ToString(exclude = "setOfOrders")
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true, length = 25)
    private String name;
    @Column(name="description", nullable = false, length=500)
    private String description;
    @Column(name="price", columnDefinition="DECIMAL(10,0)")  // ??? decimal(10,0)
    private BigDecimal price;
    @Column(name="quantity")
    private Integer quantity;
    @Column(name="image", columnDefinition = "LONGTEXT")  //  ???  longtext
    private String image;

    @ManyToOne // выкачиватся данные будут сразу
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "product")
    private Set<Order> setOfOrders = new HashSet<>(0);

    @ManyToOne(fetch = FetchType.LAZY) // выкачиватся данные будут только когда попросят
    @JoinColumn(name = "shoppingCart_id", nullable = false)
    private ShoppingCart shoppingCart;
}

