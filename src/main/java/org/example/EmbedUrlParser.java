package org.example;

import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmbedUrlParser {

    public static void main(String[] args) {
        // Create a Scanner object to take input from the user
        Scanner scanner = new Scanner(System.in);

        // Prompt the user to enter the JSON string
        System.out.println("Enter the JSON string:");
        String jsonString = scanner.nextLine();

        // Get the embedUrl and fetch HTML content
        JSONObject embedInfo = getEmbedInfo(jsonString);

        // Extract and display the URL of the asset with "display_name": "720p"
        String url720p = getAssetUrlWithDisplayName(embedInfo, "720p");

        // Open the URL in the default browser and trigger a download
        openUrlInBrowser(url720p);

        // Close the scanner
        scanner.close();
    }

    public static JSONObject getEmbedInfo(String jsonString) {
        // Parse the JSON string
        JSONObject jsonObject = new JSONObject(jsonString);

        // Get the embedUrl value
        String embedUrl = jsonObject.getString("embedUrl");

        // Fetch HTML content
        String htmlContent = fetchHtmlContent(embedUrl);

        // Extract assets information using regular expression
        JSONArray assetsInfo = extractAssetsInfo(htmlContent);

        // Create the final result JSON object
        JSONObject result = new JSONObject();
        result.put("embedUrl", embedUrl);
        result.put("assets", assetsInfo);

        return result;
    }

    private static String fetchHtmlContent(String url) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Return the HTML content
            return response.body();
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to fetch HTML content";
        }
    }

    private static JSONArray extractAssetsInfo(String htmlContent) {
        // Define the regular expression pattern
        String regex = "W\\.iframeInit\\((\\{.+?\\})\\);";

        // Create a Pattern object
        Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);

        // Create a Matcher object
        Matcher matcher = pattern.matcher(htmlContent);

        // Find the matching part
        if (matcher.find()) {
            String iframeInitJson = matcher.group(1); // Extract the content inside the parentheses
            JSONObject iframeInitObject = new JSONObject(iframeInitJson);

            // Extract the "assets" array
            if (iframeInitObject.has("assets")) {
                return iframeInitObject.getJSONArray("assets");
            } else {
                return new JSONArray().put(new JSONObject().put("error", "No assets information found"));
            }
        } else {
            return new JSONArray().put(new JSONObject().put("error", "No assets information found"));
        }
    }

    private static String getAssetUrlWithDisplayName(JSONObject embedInfo, String displayName) {
        JSONArray assets = embedInfo.getJSONArray("assets");

        for (int i = 0; i < assets.length(); i++) {
            JSONObject asset = assets.getJSONObject(i);
            if (asset.has("display_name") && asset.getString("display_name").equals(displayName)
                    && asset.has("url")) {
                        String url = asset.getString("url");
                        // Remove ".bin" extension
                if (url.endsWith(".bin")) {
                    url = url.substring(0, url.length() - 4);
                }
                return url;
            }
        }

        return "No asset found with display_name '" + displayName + "'";
    }

    private static void openUrlInBrowser(String url) {
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            try {
                Desktop.getDesktop().browse(new URI(url));
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Opening URL in browser is not supported on this platform.");
        }
    }
}
