package org.example.SFMC.CloudPages;

import org.json.JSONObject;
import scala.util.parsing.combinator.testing.Str;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FetchCloudPages {
    public static void main(String[] args) throws IOException {
        String directoryPath = "/Users/shrinepro1/Downloads/JavaPractice/src/main/java/org/example/SFMC/CloudPages/Sanbox cloudPage using Package Manager/primaryLandingPages";
        File directory = new File(directoryPath);
        if(directory.exists() && directory.isDirectory()){
            int fileCount = countFiles(directory);
            System.out.println(fileCount);
            List<List<String>> landingPages = processJsonFile(directory);
            int i=1;
            for (List page : landingPages){
                System.out.println(i + ".  " +page);
                i++;
            }
            // Write the data to a CSV file
            String csvFilePath = "/Users/shrinepro1/Downloads/JavaPractice/src/main/java/org/example/SFMC/CloudPages/output1.csv";
            writeDataToCsv(landingPages, csvFilePath);
        }
    }

    private static int countFiles(File dir){
        int count =0;
        File[] files = dir.listFiles();
        if(files != null){
            for(File file :files){
                if(file.isFile() && file.getName().endsWith("json")){
                    count ++;
                }
            }
        }
        return count;
    }

    private  static List<List<String>> processJsonFile (File dir) throws IOException {
        List<List<String>> cloudPages  = new ArrayList<>();
        File[] files = dir.listFiles();
        if(files!=null){
            for (File file: files){
                if(file.isFile() && file.getName().endsWith("json")){
                    String content = new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())));
                    JSONObject jsonObject = new JSONObject(content);
                    JSONObject fetchData = jsonObject.getJSONObject("data");
                    JSONObject fetchDefinition = fetchData.getJSONObject("definition");
                    String name = fetchDefinition.getString("name");
                    String url = fetchDefinition.getString("url");
                    String collectionId = fetchDefinition.getString("collectionId");
                    String excatCollectionId = collectionId.substring(38,43);
                    String getCollectionName = MappingOfCollectionCloudPage.collectionValue(excatCollectionId);
                    List<String> page = new ArrayList<>();
                    page.add(name);
                    page.add(url);
                    page.add(getCollectionName);
                    cloudPages.add(page);
                }
            }
        }
        // Sort the list first by CollectionName (3rd element of each sublist) and then by Name (1st element of each sublist)
        Collections.sort(cloudPages, new Comparator<List<String>>() {
            @Override
            public int compare(List<String> o1, List<String> o2) {
                int collectionComparison = o1.get(2).compareTo(o2.get(2));
                if (collectionComparison == 0) {
                    // If CollectionNames are the same, sort by Name
                    return o1.get(0).compareTo(o2.get(0));
                } else {
                    return collectionComparison;
                }
            }
        });

        return cloudPages;
    }

    private static void writeDataToCsv(List<List<String>> data, String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // Write the header
            writer.write("Name,URL,CollectionName");
            writer.newLine();

            // Write the data
            for (List<String> row : data) {
                writer.write(String.join(",", row));
                writer.newLine();
            }
        }
    }


}
