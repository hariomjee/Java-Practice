package org.example.SFMC;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class JsonUpdater {

    public static void main(String[] args) {
        String inputFilePath = "/Users/shrinepro1/Downloads/JavaPractice/src/main/java/org/example/SFMC/assets.json";
        String outputFilePath = "output.json";
        int newCategoryId = 51809;  // New category ID to set

        try {
            // Read the JSON file
            BufferedReader reader = new BufferedReader(new FileReader(inputFilePath));
            StringBuilder jsonBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }
            reader.close();

            // Parse JSON
            JSONObject jsonObject = new JSONObject(jsonBuilder.toString());

            // Get the items array
            JSONArray items = jsonObject.getJSONArray("items");

            // Update category.id for each item
            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);
                JSONObject category = item.getJSONObject("category");
                category.put("id", newCategoryId);
            }

            // Write the updated JSON back to a file
            FileWriter writer = new FileWriter(outputFilePath);
            writer.write(jsonObject.toString(4));  // Pretty print with an indent of 4 spaces
            writer.close();

            System.out.println("Category IDs updated successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
