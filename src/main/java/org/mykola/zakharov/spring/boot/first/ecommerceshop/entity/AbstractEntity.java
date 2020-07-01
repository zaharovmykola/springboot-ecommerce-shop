package org.mykola.zakharov.spring.boot.first.ecommerceshop.entity;

import javax.persistence.*;

@MappedSuperclass
public abstract class AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    public AbstractEntity() {
    }

    public long getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
