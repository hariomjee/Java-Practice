package org.example.SFMC;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WorkingWithJSON {

    public static void main(String[] args) throws IOException {
        File file = new File("/Users/shrinepro1/Downloads/JavaPractice/src/main/java/org/example/Journeys.json");
        // System.out.println(file);
        String content = new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())));
        //System.out.println(content);
        JSONObject jsonObject = new JSONObject(content);
        JSONArray jsonArray = jsonObject.getJSONArray("items");
        System.out.println(jsonArray.length());
        Set<String> names= new HashSet<>();

        for(int i=0;i<jsonArray.length();i++){
            JSONObject rec= jsonArray.getJSONObject(i);
            names.add(rec.getString("name"));
        }

        for(String name :names){
            System.out.println(name);
        }

    }
}
