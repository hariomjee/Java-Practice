package org.example.SFMC;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVReader {
    public static List<String[]> readCSV(String filePath) {
        List<String[]> records = new ArrayList<>();
        String line;

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            while ((line = br.readLine()) != null) {
                records.add(line.split(","));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return records;
    }

    public static void writeCSV(List<String[]> data, String outputPath) {
        try (FileWriter writer = new FileWriter(outputPath)) {
            for (String[] record : data) {
                for (int i = 0; i < record.length; i++) {
                    writer.append(record[i]);
                    if (i < record.length - 1) {
                        writer.append(",");
                    }
                }
                writer.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        List<String[]> records = readCSV("/Users/shrinepro1/Downloads/JavaPractice/src/main/java/org/example/Comparison from Exclude flag issue sheet with target Data Extensions_ - Exclude flag issue.csv");
        List<String> idsToSearch = new ArrayList<>();
        idsToSearch.add("ID1");
        idsToSearch.add("ID2"); // Add more IDs as needed

        // Find the index of the "Exclude sfmc sync" column
        int excludeColumnIndex = -1;
        String[] header = records.get(0);
        for (int i = 0; i < header.length; i++) {
            if (header[i].equals("Exclude sfmc sync")) {
                excludeColumnIndex = i;
                break;
            }
        }

        if (excludeColumnIndex == -1) {
            System.out.println("Column 'Exclude sfmc sync' not found.");
            return;
        }

        // Search for IDs and update the "Exclude sfmc sync" field
        for (String[] record : records) {
            String id = record[0]; // Assuming the ID is in the first column
            if (idsToSearch.contains(id)) {
                record[excludeColumnIndex] = "true";
            }
        }

        writeCSV(records, "output.csv");
    }
}
