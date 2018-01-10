package com.alphahydro.wandfluh.Entity;

import javax.persistence.*;

@Entity
@Table(name = "PRODUCT")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;

    @Column(name = "data_sheet_no", nullable = false)
    private String dataSheetNo;
    @Column(name = "data_sheet_pdf", nullable = false)
    private String dataSheetPdf;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "product_construction_id")
    private ProductConstruction construction;

    @ManyToOne
    @JoinColumn(name = "product_size_id")
    private ProductSize size;

    @ManyToOne
    @JoinColumn(name = "product_type_id")
    private ProductType type;

    @ManyToOne
    @JoinColumn(name = "product_control_id")
    private ProductControl control;

    public Product() {
    }

    public Product(String name, Category category) {
        this.name = name;
        this.category = category;
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

    public String getDataSheetNo() {
        return dataSheetNo;
    }

    public void setDataSheetNo(String dataSheetNo) {
        this.dataSheetNo = dataSheetNo;
    }

    public String getDataSheetPdf() {
        return dataSheetPdf;
    }

    public void setDataSheetPdf(String dataSheetPdf) {
        this.dataSheetPdf = dataSheetPdf;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public ProductConstruction getConstruction() {
        return construction;
    }

    public void setConstruction(ProductConstruction construction) {
        this.construction = construction;
    }

    public ProductSize getSize() {
        return size;
    }

    public void setSize(ProductSize size) {
        this.size = size;
    }

    public ProductType getType() {
        return type;
    }

    public void setType(ProductType type) {
        this.type = type;
    }

    public ProductControl getControl() {
        return control;
    }

    public void setControl(ProductControl control) {
        this.control = control;
    }

    @Override
    public String toString() {
        String controlName = (this.control != null) ? this.control.getName() : null;
        return String.format(
                "[control: %s, name: %s, sheetNo: %s, pdf: %s, construction: %s, size: %s, type %s]",
                controlName, name, dataSheetNo, dataSheetPdf,
                (construction != null) ? construction.getName() : null,
                (size != null) ? size.getName() : null,
                (type != null) ? type.getName() : null
        );
    }
}
