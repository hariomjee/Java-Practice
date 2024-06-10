package org.example.SFMC;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class FileCounter {
    public static List<String> stoppedJourney= new ArrayList<>();
    public static Set<String >foldersName = new HashSet<>();


    public static void main(String[] args) {


        // Specify the path to the directory provided by the user
        String directoryPath = "/Users/shrinepro1/Downloads/JavaPractice/src/main/java/org/example/journey";

        // Create a File object representing the directory
        File directory = new File(directoryPath);

        // Validate that the directory exists and is indeed a directory
        if (directory.exists() && directory.isDirectory()) {
            // Call the method to count all files in the directory recursively
            int fileCount = countFiles(directory);
            System.out.println(fileCount);
            // Call the method to process JSON files in the directory

            processJSONFiles(directory);
            Collections.sort(stoppedJourney);
            int i=1;
            for(String stop : stoppedJourney){
                // System.out.println(i +" "+stop);
                i++;
            }
            //     System.out.println(stoppedJourney.size());
            //  System.out.println(foldersName);


        } else {
            System.out.println("Invalid directory path.");
        }
    }

    private static int countFiles(File dir) {
        int count = 0;

        // Get all files and directories within the current directory
        File[] files = dir.listFiles();

        if (files != null) {
            // Iterate through each file/directory
            for (File file : files) {
                // Check if it's a file
                if (file.isFile()) {
                    // Increment the count for each file found
                    count++;
                } else if (file.isDirectory()) {
                    // If it's a directory, recursively call countFiles on this directory
                    count += countFiles(file); // Recursively count files in subdirectory
                }
            }
        }

        return count;
    }
    private static void processJSONFiles(File dir) {
        // Get all files within the current directory
        File[] files = dir.listFiles();

        if (files != null) {
            // Iterate through each file
            for (File file : files) {
                // Check if it's a file and ends with ".json"
                if (file.isFile() && file.getName().endsWith(".json")) {
                    // Process the JSON file
                    // System.out.println(i);
                    processJSONFile(file);
                } else if (file.isDirectory()) {
                    // If it's a directory, recursively call processJSONFiles on this directory
                    processJSONFiles(file);
                }
            }
        }
    }

    private static void processJSONFile(File file) {
        try {
            // Read the contents of the JSON file as a String
            String content = new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())));

            // Parse the JSON string into a JSONObject
            JSONObject jsonObject = new JSONObject(content);


            // Extract the relevant attributes from the JSONObject
            String status = jsonObject.optString("status", "");




            // Extract the 'name' and 'r__folder_Path' fields from the JSONObject
            String name = jsonObject.optString("name", "");
            String folderPath = jsonObject.optString("r__folder_Path", "");

            foldersName.add(folderPath);

            if(folderPath.endsWith("my journeys") && !name.contains("Drupal_INST")){
                System.out.println(name + " , Folder :"+folderPath);

            }





            // Print the extracted values
            //   System.out.println("File: " + file.getName());
            // Print the extracted values including journey status
            if(status.contains("Stopped")){
//               System.out.println("Name: " + name);
//               System.out.println("Folder Path: " + folderPath);
//               System.out.println("Status: "+status);
                stoppedJourney.add(name + " " + folderPath);
            }



        } catch (IOException e) {
            System.out.println("Error reading JSON file: " + file.getName());
            e.printStackTrace();
        }
    }



}


