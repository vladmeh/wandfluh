package com.alphahydro.wandfluh;

import com.alphahydro.wandfluh.Entity.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @autor mvl on 27.12.2017.
 */
public class App {
    private static final String JSON_FILE = "data/sections.json";

    private List<ProductControl> productControlList = new ArrayList<>();
    private List<ProductConstruction> productConstructorList = new ArrayList<>();
    private List<ProductSize> productSizeList = new ArrayList<>();
    private List<ProductType> productTypeList = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        App obj = new App();
        File file = obj.getResourceFile(JSON_FILE);
        //obj.printFile(file);
        obj.readDomFile(file);
    }

    private File getResourceFile(String fileName) {
        //Get file from resources folder
        ClassLoader classLoader = getClass().getClassLoader();
        return new File(Objects.requireNonNull(classLoader.getResource(fileName), "Файл '" + JSON_FILE + "' не найден.").getFile());
    }

    private void printFile(File file){
        StringBuilder result = new StringBuilder("");

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                result.append(line).append("\n");
            }
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(result.toString());
    }

    private void readDomFile(File file) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode rootNode = objectMapper.readTree(file);
        Iterator<JsonNode> elements = rootNode.elements();

        System.out.println("categories count: " + rootNode.size());

        iteratorJsonFields(elements, null);


        System.out.println("-------------------------------------");
        for (ProductControl productControl : productControlList)
            System.out.println(productControl.getName());

    }

    private void iteratorJsonFields(Iterator<JsonNode> elements, Category parentCategory){
        while (elements.hasNext()){
            JsonNode jsonNode = elements.next();
            int level = jsonNode.path("level").asInt();

            Category category = setCategory(jsonNode, parentCategory);
            printCategory(category, level*3);

            if (jsonNode.has("productCategories")){
                Iterator<JsonNode> productControlsNode = jsonNode.path("productCategories").elements();

                while (productControlsNode.hasNext()){
                    JsonNode productControlNode = productControlsNode.next();

                    String productControlName = productControlNode.path("name").toString();

                    ProductControl productControl = getProductControl(productControlName);

                    if (productControlNode.has("products")){
                        Iterator<JsonNode> productsNode = productControlNode.path("products").elements();
                        while (productsNode.hasNext()){
                            JsonNode productNode = productsNode.next();
                            Product product = setProduct(productNode, category, productControl);

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

    private Category setCategory(JsonNode jsonNode, Category parentCategory){
        String categoryName = jsonNode.path("name").toString();
        Category category = new Category(categoryName);
        if (jsonNode.has("image")) category.setImage(jsonNode.path("image").toString());
        if (parentCategory != null) category.setParentCategory(parentCategory);
        if (jsonNode.has("property")) {
            Iterator<JsonNode> properties = jsonNode.path("property").elements();
            Set<CategoryProperties> propList = new HashSet<>();
            while (properties.hasNext()){
                CategoryProperties categoryProperties = new CategoryProperties(properties.next().toString(), category);
                propList.add(categoryProperties);
            }
            category.setProperties(propList);
        }

        return category;
    }

    private ProductControl getProductControl(String productControlName){

        if (!productControlList.isEmpty()){
            for (ProductControl productControl: productControlList){
                if (productControlName.equals(productControl.getName()))
                    return productControl;
            }
        }
        ProductControl control = new ProductControl(productControlName);
        productControlList.add(control);

        return control;
    }

    private Product setProduct(JsonNode jsonNode, Category category, ProductControl productControl){
        Product product = new Product();

        product.setCategory(category);
        product.setControl(productControl);
        product.setDataSheetNo(jsonNode.path("dataSheetNo").toString());
        product.setDataSheetPdf(jsonNode.path("pdfFile").toString());

        if (jsonNode.has("construction"))
            product.setConstruction(
                    new ProductConstruction(
                            jsonNode.path("construction").toString()
                    )
            );

        if (jsonNode.has("size"))
            product.setSize(
                    new ProductSize(
                            jsonNode.path("size").toString()
                    )
            );

        if (jsonNode.has("type"))
            product.setType(
                    new ProductType(
                            jsonNode.path("type").toString()
                    )

            );

        return product;
    }


    private void printCategory(Category category, int tabSize){
        String format;
        if (tabSize == 0)
            format = "%s%s\n";
        else
            format = "%" + tabSize + "s %s\n";

        System.out.printf(format, "", category.toString());
    }
}
