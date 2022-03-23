package com.alten.jwtexercise.domain;

import com.alten.jwtexercise.enums.Categories;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;


import javax.persistence.*;

@Entity
@Table(name = "detail")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,property = "id") //per evitare l'infinite loop exception di json
public class Detail{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "category")
    @Enumerated(value = EnumType.STRING)
    private Categories category;

    @Column(name = "price")
    private Double price;

    @Column(name = "shipping")
    private Integer shipping;

    @OneToOne(mappedBy = "detail",
              cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private Product product;

    public Detail() {
    }

    public Detail(Categories category, Double price, int shipping) {
        this.category = category;
        this.price = price;
        this.shipping = shipping;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Categories getCategory() {
        return category;
    }

    public void setCategory(Categories category) {
        this.category = category;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public int getShipping() {
        return shipping;
    }

    public void setShipping(int shipping) {
        this.shipping = shipping;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public String toString() {
        return "Detail{" +
                "id=" + id +
                ", category=" + category +
                ", price=" + price +
                ", shipping=" + shipping +
                ", product=" + product +
                '}';
    }
}
