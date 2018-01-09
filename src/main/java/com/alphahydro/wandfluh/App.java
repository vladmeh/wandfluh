package com.alphahydro.wandfluh;

import com.alphahydro.wandfluh.Entity.Category;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Objects;
import java.util.Scanner;

/**
 * @autor mvl on 27.12.2017.
 */
public class App {
    private static final String JSON_FILE = "data/sections.json";

    public static void main(String[] args) throws IOException {
        App obj = new App();
        File file = obj.getResourceFile(JSON_FILE);
        //obj.printFile(file);
        obj.readDomFile(file);
    }

    private File getResourceFile(String fileName) {
        //Get file from resources folder
        ClassLoader classLoader = getClass().getClassLoader();
        return new File(Objects.requireNonNull(classLoader.getResource(fileName)).getFile());
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

        getCategoryFields(elements, 0);

    }

    private void getCategoryFields(Iterator<JsonNode> elements, int level){
        while (elements.hasNext()){
            JsonNode jsonNode = elements.next();
            Category category = new Category(jsonNode.path("name").toString());

            printCategory(category, level);

            if (jsonNode.path("groups").size() != 0){
                getCategoryFields(jsonNode.path("groups").elements(), level+5);
            }
        }
    }

    private void printCategory(Category category, int level){
        String format;
        if (level == 0)
            format = "%s%s\n";
        else
            format = "%" + level + "s %s\n";

        System.out.printf(format, "", category.getName());
    }
}
