package com.alphahydro.wandfluh.Entity;

import javax.persistence.*;

@Entity
@Table(name = "PRODUCT")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String data_sheet_no;
    private String data_sheet_pdf;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    public Product() {
    }

    public Product(String name) {
        this.name = name;
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

    public String getData_sheet_no() {
        return data_sheet_no;
    }

    public void setData_sheet_no(String data_sheet_no) {
        this.data_sheet_no = data_sheet_no;
    }

    public String getData_sheet_pdf() {
        return data_sheet_pdf;
    }

    public void setData_sheet_pdf(String data_sheet_pdf) {
        this.data_sheet_pdf = data_sheet_pdf;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
