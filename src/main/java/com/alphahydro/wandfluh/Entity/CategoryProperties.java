package com.alphahydro.wandfluh.Entity;

import javax.persistence.*;

@Entity
@Table(name = "WF_CATEGORY_PROPERTIES")
public class CategoryProperties {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String value;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    public CategoryProperties() {
    }

    public CategoryProperties(String value, Category category) {
        this.value = value;
        this.category = category;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
