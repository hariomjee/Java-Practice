package org.example.SFMC.CloudPages;

import org.json.JSONObject;
import scala.util.parsing.combinator.testing.Str;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MappingOfCollectionCloudPage {
    public static void main(String[] args) throws IOException {
        String directoryPath = "/Users/shrinepro1/Downloads/JavaPractice/src/main/java/org/example/SFMC/CloudPages/Sanbox cloudPage using Package Manager/primaryLandingPages/cloudPageCollections";

        File directory = new File(directoryPath);
        if(directory.exists() && directory.isDirectory()){
            Map<String,String> collectionID = proccesCollection(directory);
            for(Map.Entry<String,String> entry :collectionID.entrySet()){
                System.out.println("Key: "+entry.getKey()+"    Value: "+entry.getValue());
            }
        }
        System.out.println(collectionValue("89513"));
    }

        public static Map<String,String> proccesCollection (File dir) throws IOException {
            File[] files = dir.listFiles();
            Map<String,String> output = new HashMap<>();
            for(File file : files){
                if(file.isFile() && file.getName().endsWith("json")){
                    String content = new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())));
                    JSONObject jsonObject = new JSONObject(content);
                    String originID = jsonObject.getString("originID");
                    JSONObject getData = jsonObject.getJSONObject("data");
                    String name = getData.getString("name");
                    output.put(originID,name);
                }
            }
            return output;
        }

        public static String collectionValue(String Key) throws IOException {
            String directoryPath = "/Users/shrinepro1/Downloads/JavaPractice/src/main/java/org/example/SFMC/CloudPages/Sanbox cloudPage using Package Manager/primaryLandingPages/cloudPageCollections";
            File directory = new File(directoryPath);
            Map<String,String>  collections = new HashMap<>();
            if(directory.exists() && directory.isDirectory()){
                collections= proccesCollection(directory);
            }

           String value = collections.get(Key);
            return value;
        }

}
