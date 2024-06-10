package org.example.SFMC;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class AppendNameintoJson {
    public static void main(String[] args) {

        String directorypath = "/Users/shrinepro1/Downloads/JavaPractice/src/main/java/org/example/attributeSets";

        File directory = new File(directorypath);

        processJSONFiles(directory);

        //  int countFile = countFile(directory);
        //  System.out.println(countFile);
    }

    public static int countFile(File dir) {
        int count = 0;
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile())
                    count++;
            }
        }
        return count;
    }

    public static void processJSONFiles(File dir) {
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isFile() && file.getName().endsWith(".json")) {
                processJSONFile(file);
            }
        }
    }

    public static void processJSONFile(File file) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())));
            JSONObject jsonObject = new JSONObject(content);
            JSONObject data = jsonObject.getJSONObject("data");
            String name = data.getString("name");
            String newName = name + "_1";

            System.out.println(name);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}