package com.alphahydro.wandfluh;

import com.alphahydro.wandfluh.Entity.*;
import com.alphahydro.wandfluh.Util.HibernateUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class App {
    private static final String JSON_FILE = "data/sections.json";

    private Set<CategoryProperties> categoryPropertiesList = new HashSet<>();
    private List<ProductControl> productControlList = new ArrayList<>();
    private List<ProductConstruction> productConstructorList = new ArrayList<>();
    private List<ProductSize> productSizeList = new ArrayList<>();
    private List<ProductType> productTypeList = new ArrayList<>();

    private SessionFactory sessionFactory;

    public static void main(String[] args) throws IOException {
        App obj = new App();
        File file = obj.getResourceFile(JSON_FILE);

        obj.readDomFile(file);
    }

    private File getResourceFile(String fileName) {
        //Get file from resources folder
        ClassLoader classLoader = getClass().getClassLoader();
        return new File(Objects.requireNonNull(classLoader.getResource(fileName), "Файл '" + JSON_FILE + "' не найден.").getFile());
    }

    private void readDomFile(File file) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode rootNode = objectMapper.readTree(file);
        Iterator<JsonNode> elements = rootNode.elements();

        sessionFactory = HibernateUtil.getSessionFactory();
        iteratorJsonFields(elements, null);
        HibernateUtil.shutdown();
    }

    private void iteratorJsonFields(Iterator<JsonNode> elements, Category parentCategory){
        while (elements.hasNext()){
            JsonNode jsonNode = elements.next();

            Category category = createCategory(jsonNode, parentCategory);

            if (jsonNode.has("productCategories")){
                Iterator<JsonNode> productControlsNode = jsonNode.path("productCategories").elements();

                while (productControlsNode.hasNext()){
                    JsonNode productControlNode = productControlsNode.next();

                    String productControlName = productControlNode.path("name").toString();
                    ProductControl productControl = (!productControlName.equals(category.getName())) ? createProductControl(productControlName) : null;

                    if (productControlNode.has("products")){
                        Iterator<JsonNode> productsNode = productControlNode.path("products").elements();
                        while (productsNode.hasNext()){
                            JsonNode productNode = productsNode.next();
                            createProduct(productNode, category, productControl);
                        }
                    }
                }
            }

            if (jsonNode.path("groups").size() != 0){
                iteratorJsonFields(jsonNode.path("groups").elements(), category);
            }
        }
    }

    private Category createCategory(JsonNode jsonNode, Category parentCategory){
        String categoryName = jsonNode.path("name").toString();
        Category category = new Category(categoryName);
        if (jsonNode.has("image")) category.setImage(jsonNode.path("image").toString());
        if (parentCategory != null) category.setParentCategory(parentCategory);

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        session.save(category);

        if (jsonNode.has("property")) {
            Iterator<JsonNode> properties = jsonNode.path("property").elements();
            while (properties.hasNext()){
                CategoryProperties categoryProperties = new CategoryProperties(properties.next().toString(), category);
                session.save(categoryProperties);
            }
        }

        session.getTransaction().commit();
        session.close();

        return category;
    }

    private void createProduct(JsonNode jsonNode, Category category, ProductControl productControl){
        Product product = new Product("", category);

        product.setControl(productControl);
        product.setDataSheetNo(jsonNode.path("dataSheetNo").toString());
        product.setDataSheetPdf(jsonNode.path("pdfFile").toString());

        if (jsonNode.has("construction"))
            product.setConstruction(
                    createProductConstruction(
                            jsonNode.path("construction").toString()
                    )
            );

        if (jsonNode.has("size"))
            product.setSize(
                    createProductSize(
                            jsonNode.path("size").toString()
                    )
            );

        if (jsonNode.has("type"))
            product.setType(
                    createProductType(
                            jsonNode.path("type").toString()
                    )

            );

        saveSessionObject(product);

    }

    private ProductControl createProductControl(String controlName){

        if (!productControlList.isEmpty()){
            for (ProductControl productControl: productControlList){
                if (controlName.equals(productControl.getName()))
                    return productControl;
            }
        }
        ProductControl control = new ProductControl(controlName);

        saveSessionObject(control);

        productControlList.add(control);

        return control;
    }

    private ProductType createProductType(String typeName){
        if (!productTypeList.isEmpty()){
            for (ProductType productType: productTypeList){
                if (typeName.equals(productType.getName()))
                    return productType;
            }
        }

        ProductType type = new ProductType(typeName);

        saveSessionObject(type);

        productTypeList.add(type);

        return type;
    }

    private ProductConstruction createProductConstruction(String constructionName){
        if (!productConstructorList.isEmpty()){
            for (ProductConstruction productConstruction: productConstructorList){
                if (constructionName.equals(productConstruction.getName()))
                    return productConstruction;
            }
        }

        ProductConstruction construction = new ProductConstruction(constructionName);

        saveSessionObject(construction);

        productConstructorList.add(construction);

        return construction;
    }

    private ProductSize createProductSize(String sizeName){
        if (!productSizeList.isEmpty()){
            for (ProductSize productSize: productSizeList){
                if (sizeName.equals(productSize.getName())){
                    return productSize;
                }
            }
        }

        ProductSize size = new ProductSize(sizeName);

        saveSessionObject(size);

        productSizeList.add(size);

        return size;
    }

    private void saveSessionObject(Object o){
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        session.save(o);

        session.getTransaction().commit();
        session.close();
    }
}
