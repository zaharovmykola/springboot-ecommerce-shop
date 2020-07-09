package org.mykola.zakharov.spring.boot.first.ecommerceshop.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="products")
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="title", columnDefinition="CHAR(4)")  //  ???  char(4)
    private String title;
    @Column(name="description", length=500)
    private String description;
    @Column(name="price", columnDefinition="DECIMAL(10,0)")  // ??? decimal(10,0)
    private BigDecimal price;
    @Column(name="quantity")
    private Integer quantity;
    @Column(name="image", columnDefinition = "LONGTEXT")  //  ???  longtext
    private String image;

    @ManyToOne(fetch = FetchType.LAZY) // выкачиватся данные будут только когда попросят
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "product")
    private Set<Orders> setOfWorkers = new HashSet<>(0);

    @ManyToOne(fetch = FetchType.LAZY) // выкачиватся данные будут только когда попросят
    @JoinColumn(name = "shoppingCart_id", nullable = false)
    private ShoppingCart shoppingCart;
}

