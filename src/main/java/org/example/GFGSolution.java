package org.example;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URL;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.io.BufferedReader;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class GFGSolution {
    public Map<String , List<String>> GETRequest() throws IOException {
        String urlName = "http://be.activity.shrinesoft.com/api/0/buckets/aw-watcher-window_Hariom/events";
        String redirectedUrl = getRedirectedURL(urlName);
        if (redirectedUrl != null) {
            urlName = redirectedUrl;
        }
        URL urlForGetReq = new URL(urlName);
        String read = null;
        HttpURLConnection connection = (HttpURLConnection) urlForGetReq.openConnection();
        connection.setRequestMethod("GET");
        int codeResponse = connection.getResponseCode();

        // Print the response code and message for debugging
        System.out.println("Response Code: " + codeResponse);
        System.out.println("Response Message: " + connection.getResponseMessage());
        Map<String , List<String>> data = new HashMap<>();
        // checking whether the connection has been established or not
        if (codeResponse == HttpURLConnection.HTTP_OK) {
            // reading the response from the server
            InputStreamReader isrObj = new InputStreamReader(connection.getInputStream());
            BufferedReader bf = new BufferedReader(isrObj);
            // to store the response from the serversz
            StringBuffer responseStr = new StringBuffer();
            while ((read = bf.readLine()) != null) {
                responseStr.append(read);
            }
            // closing the BufferedReader
            bf.close();

            System.out.println(connection.getContent());
            // disconnecting the connection
            connection.disconnect();
            String jsonResponse = responseStr.toString();
           // System.out.println("JSON String Result is: \n" + jsonResponse);
            // Parse JSON string to JsonArray
            JsonElement jsonElement = JsonParser.parseString(jsonResponse);
            if (jsonElement.isJsonArray()) {
                JsonArray jsonArray = jsonElement.getAsJsonArray();
              //  System.out.println("JSON Array Result is: \n" + jsonArray.toString());
              //  System.out.println(jsonArray.size());
                for(JsonElement element : jsonArray){
                    if(element.isJsonObject()){
                        JsonObject jsonObject = element.getAsJsonObject();
                        String timestamp = jsonObject.get("timestamp").toString();
                     //   String formatTimestamp = formatData(timestamp);
                        //System.out.println(duration);
                        JsonObject jsonObject1 = jsonObject.getAsJsonObject("data");
                      //  System.out.println(jsonObject1);
                        List<String> data1 = new ArrayList<>();
                        String app = jsonObject1.get("app").toString();
                        String title = jsonObject1.get("title").toString();
                        String duration = jsonObject.get("duration").getAsString();
                        data1.add(duration);
                        data1.add(app);
                        data1.add(title);
                        data.put(timestamp,data1);
                    }
                }
            } else {
                System.out.println("Response is not a JSON Array");
            }
        } else {
            System.out.println("GET Request did not work");
        }
        return data;
    }

    // Method to get redirected URL
    public String getRedirectedURL(String urlName) throws IOException {
        URL url = new URL(urlName);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setInstanceFollowRedirects(false);  // Make the logic handle the redirection manually
        connection.setRequestMethod("GET");
        int codeResponse = connection.getResponseCode();

        if (codeResponse == HttpURLConnection.HTTP_MOVED_PERM || codeResponse == HttpURLConnection.HTTP_MOVED_TEMP) {
            String redirectedUrl = connection.getHeaderField("Location");
            System.out.println("Redirected to: " + redirectedUrl);
            return redirectedUrl;
        }

        return null;
    }



    // main method
    public static void main(String args[]) throws IOException {
        // creating an object of the FindDurations class
        GFGSolution getObj = new GFGSolution();
        // invoking the method GETRequest()
        Map<String,List<String>> response = getObj.GETRequest();
        TreeMap<String,List<String>> sortedRespoonse = new TreeMap<>();
        sortedRespoonse.putAll(response);
        float total =0;
        for(Map.Entry<String,List<String>> respons : sortedRespoonse.entrySet()){
          //  System.out.println(respons.getKey() + " data is --->"+respons.getValue());
            String timeStamp = respons.getKey();
            List<String> dateValue = respons.getValue();
            String duration = dateValue.get(0);
            float durarationValue = Float.parseFloat(duration);
            if(timeStamp.contains("2024-07-10")){
                System.out.println(timeStamp +"Value   : "+dateValue);
                if(durarationValue>180){
                    total+=durarationValue;
                }else{
                    total+=durarationValue;
                }
            }
        }
        System.out.println(total);
        System.out.println(total/60);
        System.out.println(total/3600+"h");
    }

    public static String formatData(String date){
        OffsetDateTime oft = OffsetDateTime.parse(date.trim());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd 'T' HH:mm:ss");
        String formatTime = oft.format(formatter);
        return formatTime;


    }
}
