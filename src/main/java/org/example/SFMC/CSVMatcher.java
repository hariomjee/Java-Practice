package org.example.SFMC;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CSVMatcher {
    public static void main(String[] args) throws Exception {
        String csvFile1 = "/Users/shrinepro1/Downloads/JavaPractice/src/main/java/org/example/Exclude flag issue(Total Data) (1).csv";
        String csvFile2 = "/Users/shrinepro1/Downloads/JavaPractice/src/main/java/org/example/hard And Block.csv";
        int idColumnIndex = 0; // Assuming the "Id" column is the first column (index 0)

        List<String> idList1 = new ArrayList<>();
        List<String> idList2 = new ArrayList<>();
        List<String> matchingIds = new ArrayList<>();

        // Read the first CSV file
        readCSVFile(csvFile1, idColumnIndex, idList1);

        // Read the second CSV file
        readCSVFile(csvFile2, idColumnIndex, idList2);

        // Find matching "Id" values
        for (String id : idList1) {
            if(id.isEmpty() || id.isBlank()){
                throw new Exception("Id is blank");
            }
            else{
                if (idList2.contains(id)) {
                    matchingIds.add("TRUE");
                }
                else {
                    matchingIds.add("FALSE");
                }
            }

        }

        // Print the matching "Id" values
        int i=1;
        System.out.println("Matched are : ");
        for (String id : matchingIds) {
            System.out.println(id);
            i++;
        }
    }

    private static void readCSVFile(String csvFile, int idColumnIndex, List<String> idList) {
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            boolean isFirstLine = true;

            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    // Skip the header row
                    isFirstLine = false;
                    continue;
                }

                String[] values = line.split(",");
                if (values.length > idColumnIndex) {
                    String id = values[idColumnIndex];
                    idList.add(id);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}