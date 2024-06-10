package org.example.SFMC;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Extract_CloudPage_With_URLandName {
    List<List<String>> pages = new ArrayList<>();
    public static void main(String[] args) throws IOException {
        String directoryPath = "/Users/shrinepro1/Downloads/JavaPractice/src/main/java/org/example/Sandbox CloudPage";
        File directory = new File(directoryPath);
        if(directory.exists() || directory.isDirectory()){
            //   int filecount = countFile(directory);
            // System.out.println(filecount);
            processJsonFiles(directory);

        }
    }

//    public static int countFile(File dir){
//        int count =0;
//        File[] files = dir.listFiles();
//        if(files != null){
//            for(File file :files){
//                System.out.println(file+">>>>>>>>");
//                file = new File(file + "/entities/primaryLandingPages");
//                File [] primaryLandingPages = file.listFiles();
//                for(File pages : primaryLandingPages){
//                   // System.out.println(pages);
//                    if(pages.exists() && pages.getName().endsWith(".json"))
//                        count++;
//                }
//                System.out.println(count);
//            }
//        }
//        return count;
//    }

    private static void processJsonFiles(File directory) throws IOException {
        File[] files = directory.listFiles();
        if(files != null){
            int i=1;
            for(File file:files){
                file = new File(file + "/entities/primaryLandingPages");
                File [] primaryLandingPages = file.listFiles();
                for(File pages : primaryLandingPages){
                    // System.out.println(pages);
                    if(pages.exists() && pages.getName().endsWith(".json")){
                        processJson(pages,i);
                        i++;
                    }
                }
            }
        }
    }

    private static void processJson(File file ,int i) throws IOException {
        try {
            List<String > page= new ArrayList<>();
            String content = new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())));
            JSONObject jsonObject = new JSONObject(content);
            System.out.println(i);
            System.out.println("collectionId : "+jsonObject.getJSONObject("data").getJSONObject("definition").opt("collectionId"));
            String input = jsonObject.getJSONObject("data").getJSONObject("definition").opt("collectionId").toString();

            // Define the regular expression to extract the integer
            String regex = "\\d+";

            // Compile the regex
            Pattern pattern = Pattern.compile(regex);

            // Create a matcher to find matches of the regex in the input string
            Matcher matcher = pattern.matcher(input);
            String cloudPageId ;
            // Find and print the integer
            if (matcher.find()) {
                // Group 0 is the entire match
                String extractedInt = matcher.group(0);
                int intValue = Integer.parseInt(extractedInt);
                cloudPageId = String.valueOf(intValue);
                //  System.out.println("CollectionCloud: " + intValue);

            }
            String Name = jsonObject.getJSONObject("data").getJSONObject("definition").opt("name").toString();
            String URl =  jsonObject.getJSONObject("data").getJSONObject("definition").opt("url").toString();
            page.add(String.valueOf(i));
        }catch (IOException e){
            System.out.println(e);
        }

    }
}
