package org.example.SFMC;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVMatcher1 {

    public static void main(String[] args) {
        String csvFile = "/Users/shrinepro1/Downloads/JavaPractice/src/main/java/org/example/Exclude flag issue(Total Data) (1).csv";
        int idColumnIndex = 0; // Assuming the "Id" column is the first column (index 0)
        int excludeSfmcSyncColumnIndex = 9; // Assuming the "excludeSfmcSync" column index is 9

        List<String> result = readCSVFile(csvFile, idColumnIndex, excludeSfmcSyncColumnIndex);

        System.out.println("Total records: " + result.size());
        for (String record : result) {
            System.out.println(record);
        }
    }

    private static List<String> readCSVFile(String csvFile, int idColumnIndex, int excludeSfmcSyncColumnIndex) {
        List<String> records = new ArrayList<>();
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
                if (values.length > idColumnIndex && values.length > excludeSfmcSyncColumnIndex) {
                    String id = values[idColumnIndex];
                    String excludeSfmcSync = values[excludeSfmcSyncColumnIndex];
                    if(excludeSfmcSync.contains("TRUE"))
                        records.add("ID: " + id + ", Exclude SFMC Sync: " + excludeSfmcSync);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return records;
    }
}
