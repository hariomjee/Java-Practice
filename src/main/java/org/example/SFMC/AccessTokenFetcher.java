package org.example.SFMC;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import scala.util.parsing.combinator.testing.Str;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class AccessTokenFetcher {

    public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException {
        String tokenUrl = "https://mchhgp5k7s63dh2c5j66tnsl2054.auth.marketingcloudapis.com/v2/token"; // Replace with your SFMC subdomain
        String clientId = "1539115en4ttmgcx20d63g0m"; // Replace with your SFMC client ID
        String clientSecret = "elEM0cCJ5tC8nklxfQNawIaK"; // Replace with your SFMC client secret

        // Obtain access token
        String accessToken = getAccessToken(tokenUrl, clientId, clientSecret);

        if (accessToken != null) {
            System.out.println("Access Token: " + accessToken);

            // Fetch all assets page by page
            List<Integer> itemIds = fetchAllAssets(accessToken);

            if (!itemIds.isEmpty()) {
                System.out.println("Total assets retrieved: " + itemIds.size());
                // Fetch id by Id's
                List<List<Integer>> subIds = new ArrayList<>();
                for(int item : itemIds){
                    List<Integer> subItemids= fetchAssetsById (accessToken , 135830);
                    System.out.println("id is : "+135830);
                    break;

                }
            } else {
                System.out.println("No assets found.");
            }
        } else {
            System.err.println("Failed to obtain access token.");
        }


    }

    // Method to obtain access token using client credentials grant
    private static String getAccessToken(String tokenUrl, String clientId, String clientSecret) throws URISyntaxException, IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(tokenUrl))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString("grant_type=client_credentials&client_id=" + clientId + "&client_secret=" + clientSecret))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            // Parse JSON response to extract access token
            JSONObject jsonResponse = new JSONObject(response.body());
            return jsonResponse.getString("access_token");
        } else {
            System.err.println("Failed to obtain access token. Status code: " + response.statusCode());
            return null;
        }
    }

    // Method to fetch all assets from all pages using the access token
    private static List<Integer> fetchAllAssets(String accessToken) throws URISyntaxException, IOException, InterruptedException {
        String assetsUrl = "https://mchhgp5k7s63dh2c5j66tnsl2054.rest.marketingcloudapis.com/asset/v1/content/assets";
        List<Integer> itemIds = new ArrayList<>();
        int currentPage = 1;

        while (true) {
            URI uri = new URI(assetsUrl + "?page=" + currentPage);
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .header("Authorization", "Bearer " + accessToken)
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                // Parse JSON response to extract asset IDs
                JSONObject jsonResponse = new JSONObject(response.body());
                JSONArray assetsArray = jsonResponse.optJSONArray("items");

                if (assetsArray != null && assetsArray.length() > 0) {
                    for (int i = 0; i < assetsArray.length(); i++) {
                        JSONObject assetObject = assetsArray.getJSONObject(i);
                        int id = assetObject.getInt("id");
                        itemIds.add(id);
                    }
                    System.out.println(itemIds);
                    // Check pagination metadata to determine if there are more pages
                    int totalPages = jsonResponse.optInt("totalPages", -1); // Default value -1 if totalPages is not present
                    System.out.println("Total pages are :"+totalPages);
                    if (totalPages == -1 || currentPage >= totalPages) {
                        break; // No more assets or reached last page
                    }
                    currentPage++; // Move to the next page
                } else {
                    break; // No more assets found, exit loop
                }
            } else {
                System.err.println("Failed to fetch assets for Page " + currentPage + ". Status code: " + response.statusCode());
                break;
            }
        }

        return itemIds;
    }

    private static List<Integer> fetchAssetsById(String accessToken , int Id) throws URISyntaxException, IOException, InterruptedException{
        String assetsUrl = "https://mchhgp5k7s63dh2c5j66tnsl2054.rest.marketingcloudapis.com/asset/v1/content/assets/";
        List<Integer> itemIds = new ArrayList<>();
        List<String> itemIdsName = new ArrayList<>();

        URI uri = new URI(assetsUrl + Id);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Authorization", "Bearer " + accessToken)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            // Parse JSON response to extract asset IDs
            JSONObject jsonResponse = new JSONObject(response.body());
            String assetType = jsonResponse.getJSONObject("assetType").getString("name");
            System.out.println("Asset Type is : "+assetType);
            if(assetType.contains("templatebasedemail")){
                // Get the views object
                JSONObject views = jsonResponse.getJSONObject("views");
                // System.out.println(views);
                JSONObject html = views.getJSONObject("html");
                JSONObject slots = html.getJSONObject("slots");
                JSONObject section1 = slots.getJSONObject("section_1");
                System.out.println(section1);
                ObjectMapper mapper = new ObjectMapper();
                JsonNode jsonNode = mapper.readTree(section1.toString());
                JsonNode id = jsonNode.path("blocks").path("wsdcclh4ong").path("id");
                int idValue= id.asInt();
                System.out.println(idValue);
                // Extract blocks from section_1
                JSONObject blocks = section1.getJSONObject("blocks");



            }


        }
        return itemIds;

    }

}
