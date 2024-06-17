

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

public class GetSubFoldersByParentID {
    public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException {
        String tokenUrl = "https://mchhgp5k7s63dh2c5j66tnsl2054.auth.marketingcloudapis.com/v2/token"; // Replace with your SFMC subdomain
        String clientId = "1539115en4ttmgcx20d63g0m"; // Replace with your SFMC client ID
        String clientSecret = "elEM0cCJ5tC8nklxfQNawIaK"; // Replace with your SFMC client secret
        String MID = "526000998"; // Org MID Enphase

        String accessToken = getAccessToken(tokenUrl, clientId, clientSecret,MID);
        if (accessToken != null) {
            System.out.println("Access Token: " + accessToken);

            // Fetch all assets page by page
            List<String> itemIds = fetchAllCategory(accessToken);

            if (!itemIds.isEmpty()) {
                System.out.println("Total assets retrieved: " + itemIds.size());
                int i=1;
                for(String item :itemIds){
                    System.out.println(i +" "+item);
                    i++;
                }

            } else {
                System.out.println("No assets found.");
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
                .POST(HttpRequest.BodyPublishers.ofString("grant_type=client_credentials&client_id=" + clientId + "&client_secret=" + clientSecret + "&account_id="+MID))
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

    private static List<String> fetchAllCategory(String accessToken) throws URISyntaxException, IOException, InterruptedException {
        String assetsUrl = "https://mchhgp5k7s63dh2c5j66tnsl2054.rest.marketingcloudapis.com/asset/v1/content/categories";
        List<String> item = new ArrayList<>();

        for(int i=1;i<=2;i++){
            URI uri = new URI(assetsUrl +"?$page="+i+ "?$pageSize=" + 500);
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
                    for (int k = 0; k < assetsArray.length(); k++) {
                        JSONObject assetObject = assetsArray.getJSONObject(k);
                        int parentId = assetObject.getInt("parentId");
                        if(parentId == 9945){
                            int id = assetObject.getInt("id");
                            String name = assetObject.getString("name");
                            item.add(id+" "+name);
                        }
                    }
                }
            }
        }
        return item;
    }
}
