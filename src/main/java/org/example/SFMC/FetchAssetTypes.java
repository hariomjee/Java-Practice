package org.example.SFMC;

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

public class FetchAssetTypes {
    public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException {
        String tokenUrl = "https://mchhgp5k7s63dh2c5j66tnsl2054.auth.marketingcloudapis.com/v2/token"; // Replace with your SFMC subdomain
        String clientId = "1539115en4ttmgcx20d63g0m"; // Replace with your SFMC client ID
        String clientSecret = "elEM0cCJ5tC8nklxfQNawIaK"; // Replace with your SFMC client secret
        String MID = "526000998";

        // Obtain access token
        String accessToken = getAccessToken(tokenUrl, clientId, clientSecret, MID);
        if (accessToken != null) {
            System.out.println("Access Token: " + accessToken);

            // Fetch all assets page by page
            List<List<String>> assets = fetchAllAssets(accessToken);
            for (List asset : assets){
                System.out.println(asset);
            }

        } else {
            System.err.println("Failed to obtain access token.");
        }
    }

    private static String getAccessToken(String tokenUrl, String clientId, String clientSecret, String account_id) throws URISyntaxException, IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(tokenUrl))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString("grant_type=client_credentials&client_id=" + clientId + "&client_secret=" + clientSecret + "&account_id=" + account_id))
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
    private static List<List<String>> fetchAllAssets(String accessToken) throws URISyntaxException, IOException, InterruptedException {
        String assetsUrl = "https://mchhgp5k7s63dh2c5j66tnsl2054.rest.marketingcloudapis.com/asset/v1/content/assets";
        List<List<String>> assets = new ArrayList<>();
        int page = 1;
        int pageSize = 500;

        for (int i = page; i <= 13; i++) {
            URI uri = new URI(assetsUrl + "?$page=" + i + "&$pageSize=" + pageSize);
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .header("Authorization", "Bearer " + accessToken)
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                JSONObject jsonResponse = new JSONObject(response.body());
                JSONArray assetArrays = jsonResponse.getJSONArray("items");
                if (assetArrays != null && assetArrays.length() > 0) {
                    System.out.println("assetArrays size is : " + assetArrays.length());
                    for (int j = 0; j < assetArrays.length(); j++) {
                        JSONObject assetObject = assetArrays.getJSONObject(j);
                        String assetName = assetObject.getString("name");
                        JSONObject assetTypeObj = assetObject.getJSONObject("assetType");
                        String assetType = assetTypeObj.getString("name");
                        JSONObject assetCategory = assetObject.getJSONObject("category");
                        String assetcategoryName = assetCategory.getString("name");
                        List<String> assetDetails = new ArrayList<>();
                        assetDetails.add(assetName);
                        assetDetails.add(assetType);
                        assetDetails.add(assetcategoryName);
                        assets.add(assetDetails);
                    }
                }
            }
        }
        return assets;
    }
}


