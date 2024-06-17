package org.example.SFMC;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

public class ContentBuilderTask {
    public static void main(String[] args) throws IOException {
        File file = new File("/Users/shrinepro1/Downloads/JavaPractice/src/main/java/org/example/SFMC/Enphase_AssetsCategory.json");
        File file1 = new File("/Users/shrinepro1/Downloads/JavaPractice/src/main/java/org/example/SFMC/Sandbox_AssetsCategory.json");

        // System.out.println(file);
        String content = new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())));
        String content1 = new String(Files.readAllBytes(Paths.get(file1.getAbsolutePath())));
        //System.out.println(content);
        JSONObject jsonObject = new JSONObject(content);
        JSONObject jsonObject1 = new JSONObject(content1);
        JSONArray jsonArray = jsonObject.getJSONArray("items");
        JSONArray jsonArray1 = jsonObject1.getJSONArray("items");
        System.out.println("Enphase Assets : "+jsonArray.length());
        System.out.println("Sandbox Assets : "+ jsonArray1.length());

    }
}
