package com.alphahydro.wandfluh.Entity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "PRODUCT_CONSTRUCTION")
public class ProductConstruction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;

    @OneToMany(mappedBy = "construction")
    private Set<Product> products;

    public ProductConstruction(String name) {
        this.name = name;
    }

    public ProductConstruction() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }
}
