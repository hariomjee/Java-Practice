package org.example.SFMC;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GetDipCategoryId {

    public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException {
        String tokenUrl = "https://mchhgp5k7s63dh2c5j66tnsl2054.auth.marketingcloudapis.com/v2/token";
        String clientId = "1539115en4ttmgcx20d63g0m";
        String clientSecret = "elEM0cCJ5tC8nklxfQNawIaK";
        String MID = "526000998"; // Org MID Enphase

        String accessToken = getAccessToken(tokenUrl, clientId, clientSecret, MID);
        if (accessToken != null) {
            System.out.println("Access Token: " + accessToken);

            // Fetch all assets recursively starting from the root folder (ID 9945)
            List<Category> categories = fetchAllCategories(accessToken, 15320); // Pass Id of parent

            if (!categories.isEmpty()) {
                // Sort categories by name
                categories.sort(Comparator.comparing(Category::getName));

                // Print the count of all folders
                System.out.println("Total folders retrieved: " + countFolders(categories));

                // Print the categories in a tree-like structure
                System.out.println("Folder structure:");
                printCategoryTree(categories, "", 1);
            } else {
                System.out.println("No folders found.");
            }
        } else {
            System.err.println("Failed to obtain access token.");
        }
    }

    private static String getAccessToken(String tokenUrl, String clientId, String clientSecret, String MID) throws URISyntaxException, IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(tokenUrl))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString("grant_type=client_credentials&client_id=" + clientId + "&client_secret=" + clientSecret + "&account_id=" + MID))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            JSONObject jsonResponse = new JSONObject(response.body());
            return jsonResponse.getString("access_token");
        } else {
            System.err.println("Failed to obtain access token. Status code: " + response.statusCode());
            return null;
        }
    }

    private static List<Category> fetchAllCategories(String accessToken, int parentId) throws URISyntaxException, IOException, InterruptedException {
        String assetsUrl = "https://mchhgp5k7s63dh2c5j66tnsl2054.rest.marketingcloudapis.com/asset/v1/content/categories";

        List<Category> categories = new ArrayList<>();

        int page = 1;
        boolean hasNextPage = true;
        while (hasNextPage) {
            URI uri = new URI(assetsUrl + "?$filter=parentId=" + parentId + "&$pageSize=500" + "&$page=" + page);
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .header("Authorization", "Bearer " + accessToken)
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                JSONObject jsonResponse = new JSONObject(response.body());
                JSONArray items = jsonResponse.getJSONArray("items");

                for (int i = 0; i < items.length(); i++) {
                    JSONObject item = items.getJSONObject(i);
                    int id = item.getInt("id");
                    String name = item.getString("name");
                    int itemParentId = item.getInt("parentId");

                    // Recursive call to fetch subcategories
                    List<Category> subcategories = fetchAllCategories(accessToken, id);
                    Category category = new Category(id, name, itemParentId, subcategories);
                    categories.add(category);
                }

                // Check if there's another page of results
                int totalCount = jsonResponse.getInt("count");
                int pageSize = jsonResponse.getInt("pageSize");
                int totalPages = (int) Math.ceil((double) totalCount / pageSize);
                hasNextPage = page < totalPages;
                page++;
            } else {
                System.err.println("Failed to fetch categories. Status code: " + response.statusCode());
                hasNextPage = false;
            }
        }

        return categories;
    }

    private static int countFolders(List<Category> categories) {
        int count = 0;
        for (Category category : categories) {
            count++; // Increment count for current category
            count += countFolders(category.getSubcategories()); // Recursively count subcategories
        }
        return count;
    }

    private static void printCategoryTree(List<Category> categories, String prefix, int level) {
        for (Category category : categories) {
            System.out.println(prefix + category.getName() + " (ID: " + category.getId() + ")");
            if (!category.getSubcategories().isEmpty()) {
                printCategoryTree(category.getSubcategories(), prefix + "  ", level + 1);
            }
        }
    }

    static class Category {
        private int id;
        private String name;
        private int parentId;
        private List<Category> subcategories;

        public Category(int id, String name, int parentId, List<Category> subcategories) {
            this.id = id;
            this.name = name;
            this.parentId = parentId;
            this.subcategories = subcategories;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public int getParentId() {
            return parentId;
        }

        public List<Category> getSubcategories() {
            return subcategories;
        }
    }
}
