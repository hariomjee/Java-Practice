package org.example.SFMC;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashSet;
import java.util.Set;

public class FolderProcessor {
    static int collectionCount = 1;

    public static void main(String[] args) {
        // Specify the root directory path containing the CloudPages structure
        String directoryPath = "/Users/shrinepro1/Downloads/JavaPractice/src/main/java/org/example/CloudPages";
        File directory = new File(directoryPath);
        if (!directory.isDirectory()) {
            System.out.println("Specified path is not a directory.");
            return;
        }

        try {
            // Process each main subfolder dynamically
            processMainFolder(directory);
        } catch (IOException | ParseException | org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }
    }

    private static void processMainFolder(File mainFolder) throws IOException, ParseException, org.json.simple.parser.ParseException {
        File[] subFolders = mainFolder.listFiles(File::isDirectory);

        if (subFolders == null) {
            return;
        }

        for (File subFolder : subFolders) {
            // Check if the subfolder is the target subfolder we're interested in
            if (subFolder.getName().equalsIgnoreCase("entities")) {
                processEntitiesFolder(subFolder);
            } else {
                // Recursively process other subfolders
                processMainFolder(subFolder);
            }
        }
    }

    private static void processEntitiesFolder(File entitiesFolder) throws IOException, ParseException, org.json.simple.parser.ParseException {
        File[] subFolders = entitiesFolder.listFiles(File::isDirectory);

        if (subFolders == null) {
            return;
        }

        for (File subFolder : subFolders) {
            // Check for specific subfolders of interest (assets, cloudPageCollections, primaryLandingPages)
            switch (subFolder.getName()) {
                case "cloudPageCollections":
                    // processCloudPageCollectionsFolder(subFolder);
                    break;
                case "assets":
                    processAssetsFolder(subFolder);
                    break;
                case "primaryLandingPages":
                    //  processPrimaryLandingPagesFolder(subFolder);
                    break;
                default:
                    // Recursively process other subfolders
                    processEntitiesFolder(subFolder);
                    break;
            }
        }
    }

    private static void processAssetsFolder(File assetsFolder) throws IOException, ParseException, org.json.simple.parser.ParseException {
        // Process JSON files in the assets folder
        Set<String> fileNames = readJsonFileNamesFromFolder(assetsFolder);
        // System.out.println("File Names from JSON files in assets folder:");
        for (String fileName : fileNames) {
            System.out.println(fileName);
            collectionCount++;
        }
        System.out.println("Total: "+collectionCount);

    }

    private static void processCloudPageCollectionsFolder(File cloudPageCollectionsFolder) throws IOException, ParseException {
        // Process JSON files in the cloudPageCollections folder
        String cloudPageCollectionName = extractCloudPageCollectionNameFromFolder(cloudPageCollectionsFolder);
        System.out.print(cloudPageCollectionName + ", ");
    }

    private static void processPrimaryLandingPagesFolder(File primaryLandingPagesFolder) throws IOException, ParseException {
        // Process JSON files in the primaryLandingPages folder
        String[] landingPages = extractPrimaryLandingPagesFromFolder(primaryLandingPagesFolder);

        for (String landingPage : landingPages) {
            System.out.println(collectionCount++ +" "+ landingPage);
        }
    }

    private static Set<String>  readJsonFileNamesFromFolder(File folder) throws IOException, ParseException, org.json.simple.parser.ParseException {
        File[] jsonFiles = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".json"));

        if (jsonFiles == null) {
            throw new IOException("Failed to list JSON files in the folder:  " + folder.getAbsolutePath());
        }

        // String[] fileNames = new String[jsonFiles.length];
        Set<String> fileNames = new HashSet<>();

        int index = 0;

        JSONParser jsonParser = new JSONParser();

        for (File jsonFile : jsonFiles) {
            try (FileReader reader = new FileReader(jsonFile)) {
                Object obj = jsonParser.parse(reader);
                JSONObject jsonObject = (JSONObject) obj;

                JSONObject dataObject = (JSONObject) jsonObject.get("data");
                JSONObject filePropertiesObject = (JSONObject) dataObject.get("fileProperties");
                String fileName = (String) filePropertiesObject.get("fileName");

                //fileNames.add(fileName);
                if (fileName != null && !fileName.isEmpty()) {
                    // Check if the file name is already added to the set
                    if (!fileNames.contains(fileName)) {
                        fileNames.add(fileName);
                    } else {
                        System.out.println("Duplicate File Name Found: " + fileName);
                    }
                }

                // fileNames[index++] = fileName;
            } catch (IOException e) {
                System.err.println("Error reading JSON file: " + jsonFile.getName());
                e.printStackTrace();
            }
        }

        return fileNames;
    }

    private static String extractCloudPageCollectionNameFromFolder(File folder) throws IOException, ParseException {
        File[] jsonFiles = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".json"));

        if (jsonFiles == null) {
            throw new IOException("Failed to list JSON files in the folder: " + folder.getAbsolutePath());
        }

        JSONParser jsonParser = new JSONParser();

        for (File jsonFile : jsonFiles) {
            try (FileReader reader = new FileReader(jsonFile)) {
                Object obj = jsonParser.parse(reader);
                JSONObject jsonObject = (JSONObject) obj;

                JSONObject dataObject = (JSONObject) jsonObject.get("data");
                String cloudPageCollectionName = (String) dataObject.get("name");

                if (cloudPageCollectionName != null && !cloudPageCollectionName.isEmpty()) {

                    return cloudPageCollectionName;
                }
            } catch (IOException | org.json.simple.parser.ParseException e) {
                System.err.println("Error reading JSON file: " + jsonFile.getName());
                e.printStackTrace();
            }
        }

        return null;
    }

    private static String[] extractPrimaryLandingPagesFromFolder(File folder) throws IOException, ParseException {
        File[] jsonFiles = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".json"));

        if (jsonFiles == null) {
            throw new IOException("Failed to list JSON files in the folder: " + folder.getAbsolutePath());
        }

        JSONParser jsonParser = new JSONParser();
        String[] landingPages = new String[jsonFiles.length];
        int index = 0;

        for (File jsonFile : jsonFiles) {
            try (FileReader reader = new FileReader(jsonFile)) {
                Object obj = jsonParser.parse(reader);
                JSONObject jsonObject = (JSONObject) obj;

                JSONObject dataObject = (JSONObject) jsonObject.get("data");
                JSONObject definitionObject = (JSONObject) dataObject.get("definition");
                String name = (String) definitionObject.get("name");
                String url = (String) definitionObject.get("url");

                String landingPageInfo = "Name: " + name + ", URL: " + url;
                landingPages[index++] = landingPageInfo;
            } catch (IOException | org.json.simple.parser.ParseException e) {
                System.err.println("Error reading JSON file: " + jsonFile.getName());
                e.printStackTrace();
            }
        }

        return java.util.Arrays.copyOf(landingPages, index);
    }
}
