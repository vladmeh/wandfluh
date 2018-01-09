package com.alphahydro.wandfluh;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

/**
 * @autor mvl on 27.12.2017.
 */
public class App {
    private static final String JSON_FILE = "data/sections.json";

    public static void main(String[] args) {
        App obj = new App();
        File file = obj.getResourceFile(JSON_FILE);
        obj.printFile(file);
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
}
