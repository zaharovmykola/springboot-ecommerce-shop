package org.mykola.zakharov.spring.boot.first.ecommerceshop.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name="orders")
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // выкачиватся данные будут только когда попросят
    @JoinColumn(name = "article_id")
    private Articles article;

    @ManyToOne(fetch = FetchType.LAZY) // выкачиватся данные будут только когда попросят
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name="quantity")
    private Integer quantity;
}
