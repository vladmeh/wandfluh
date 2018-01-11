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

        obj.sessionFactory = HibernateUtil.getSessionFactory();
        obj.readDomFile(file);
        HibernateUtil.shutdown();

        obj.printLists();
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

        System.out.println("categories count: " + rootNode.size());

        iteratorJsonFields(elements, null);
    }

    private void iteratorJsonFields(Iterator<JsonNode> elements, Category parentCategory){
        while (elements.hasNext()){
            JsonNode jsonNode = elements.next();
            int level = jsonNode.path("level").asInt();

            Category category = createCategory(jsonNode, parentCategory);
            printCategory(category, level*3);

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
                            Product product = createProduct(productNode, category, productControl);

                            System.out.printf("%10s%s\n", "", product.toString());
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
                categoryPropertiesList.add(categoryProperties);
                session.save(categoryProperties);
            }
            category.setProperties(categoryPropertiesList);
        }


        session.getTransaction().commit();
        session.close();

        return category;
    }

    private Product createProduct(JsonNode jsonNode, Category category, ProductControl productControl){
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

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        session.save(product);

        session.getTransaction().commit();
        session.close();

        return product;
    }

    private ProductControl createProductControl(String controlName){

        if (!productControlList.isEmpty()){
            for (ProductControl productControl: productControlList){
                if (controlName.equals(productControl.getName()))
                    return productControl;
            }
        }
        ProductControl control = new ProductControl(controlName);

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        session.save(control);

        session.getTransaction().commit();
        session.close();

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

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        session.save(type);

        session.getTransaction().commit();
        session.close();

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

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        session.save(construction);

        session.getTransaction().commit();
        session.close();

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

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        session.save(size);

        session.getTransaction().commit();
        session.close();

        productSizeList.add(size);

        return size;
    }

    private void printCategory(Category category, int tabSize){
        String format;
        if (tabSize == 0)
            format = "%s%s\n";
        else
            format = "%" + tabSize + "s %s\n";

        System.out.printf(format, "", category.toString());
    }

    private void printLists() {
        System.out.println();
        System.out.println("CONTROLS-------------------------------------");
        for (ProductControl productControl : productControlList)
            System.out.println(productControl.getName());

        System.out.println();
        System.out.println("CONSTRUCTION-------------------------------------");
        for (ProductConstruction productConstruction : productConstructorList)
            System.out.println(productConstruction.getName());

        System.out.println();
        System.out.println("SIZE-------------------------------------");
        for (ProductSize size : productSizeList)
            System.out.println(size.getName());

        System.out.println();
        System.out.println("TYPE-------------------------------------");
        for (ProductType type : productTypeList)
            System.out.println(type.getName());

        System.out.println();
        System.out.println("PROPERTIES-------------------------------------");
        for (CategoryProperties properties : categoryPropertiesList)
            System.out.println(properties.getValue() + ", " + properties.getCategory().getId());
    }

}
