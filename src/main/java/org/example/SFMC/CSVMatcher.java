package org.example.SFMC;

import java.io.*;
import java.util.*;

public class CSVMatcher {
    static int dupli=0;

    public static void main(String[] args) {
        String inputFilePath = "/Users/shrinepro1/Downloads/JavaPractice/src/main/java/org/example/import data 9 july task - Nurture_Campaign_MatchingLeads20240703 1(in).csv";
        String intermediateOutputPath = "/Users/shrinepro1/Downloads/JavaPractice/src/main/java/org/example/MatchingLeads.csv";
        int idColumnIndex = 6; // Assuming the "Id" column is the 7th column (index 6)
        int matchColumnIndex = -1; // We'll find this dynamically
        int emailColumnIndex = 3; // Assuming the email is in the 3rd column (index 2)

        try {
            // Step 1: Process initial CSV and write matching records
            List<String> matchingRecords = readCSVFile(inputFilePath, idColumnIndex, matchColumnIndex);
            writeCSVFile(intermediateOutputPath, matchingRecords);
            System.out.println("Matching records have been written to: " + intermediateOutputPath);

            // Step 2: Process the matching records to find duplicate emails
            Map<String, List<String>> duplicateEmails = findDuplicateEmails(intermediateOutputPath, emailColumnIndex);
            printDuplicateEmails(duplicateEmails);

        } catch (IOException e) {
            System.err.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println(dupli);
    }

    private static List<String> readCSVFile(String csvFile, int idColumnIndex, int matchColumnIndex) throws IOException {
        List<String> matchingRecords = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            boolean isFirstLine = true;

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");

                if (isFirstLine) {
                    // Find the index of the "Match" column
                    for (int i = 0; i < values.length; i++) {
                        if (values[i].equalsIgnoreCase("Match")) {
                            matchColumnIndex = i;
                            break;
                        }
                    }
                    if (matchColumnIndex == -1) {
                        throw new IOException("Match column not found in the CSV file.");
                    }
                    matchingRecords.add(line); // Add header to output
                    isFirstLine = false;
                    continue;
                }

                if (values.length > Math.max(idColumnIndex, matchColumnIndex)) {
                    String matchValue = values[matchColumnIndex].trim();
                    if (matchValue.equalsIgnoreCase("Found")) {
                        matchingRecords.add(line);
                    }
                }
            }
        }

        return matchingRecords;
    }

    private static void writeCSVFile(String outputFile, List<String> records) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile))) {
            for (String record : records) {
                bw.write(record);
                bw.newLine();
            }
        }
    }

    private static Map<String, List<String>> findDuplicateEmails(String csvFile, int emailColumnIndex) throws IOException {
        Map<String, List<String>> emailMap = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            String header = br.readLine(); // Read and store the header

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length > emailColumnIndex) {
                    String email = values[emailColumnIndex].trim().toLowerCase();
                    emailMap.computeIfAbsent(email, k -> new ArrayList<>()).add(line);
                }
            }
        }

        // Filter out emails that appear only once
        Map<String, List<String>> duplicateEmails = new HashMap<>();
        for (Map.Entry<String, List<String>> entry : emailMap.entrySet()) {
            if (entry.getValue().size() > 1) {
                duplicateEmails.put(entry.getKey(), entry.getValue());
            }
        }

        return duplicateEmails;
    }

    private static void printDuplicateEmails(Map<String, List<String>> duplicateEmails) {
        System.out.println("Duplicate Emails:");
        System.out.println("Email,Count,Records");

        for (Map.Entry<String, List<String>> entry : duplicateEmails.entrySet()) {
            String email = entry.getKey();
            List<String> records = entry.getValue();
            int count = records.size();
            dupli+=count-1;
            System.out.println(email + "," + count);
        }
    }
}