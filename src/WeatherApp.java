import netscape.javascript.JSObject;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class WeatherApp {
    public static JSONObject getWeatherData(String locationName) {

        JSONArray locationData = getLocationData(locationName);
        return null;
    }

    //Retrieves geographic coordinates for given location name
    public static JSONArray getLocationData(String locationName){
        locationName = locationName.replaceAll(" ", "+");

        //Api Call
        String URLstring = "https://geocoding-api.open-meteo.com/v1/search?name="+locationName+"&count=10&language=en&format=json";

        try{
            //callAPIResponse
            HttpURLConnection conn = fetchApiResponse(URLstring);

            //check response status
            // 200 == succesful;

            if (conn.getResponseCode() != 200){
                System.out.println("Error: Could not connect to API");
                return null;

            } else {
                // store API results
                StringBuilder resultJSON = new StringBuilder();
                Scanner scan = new Scanner(conn.getInputStream());

                // read & store resultting json data into string builder
                while (scan.hasNext()){
                    resultJSON.append(scan.nextLine());
                }
                scan.close();
                conn.disconnect();

                // parse the JSON string into JSON Obj

                JSONParser parser = new JSONParser();
                JSONObject resultJsonObj = (JSONObject) parser.parse(String.valueOf(resultJSON));

                //get list location data API Generated from location name
                JSONArray locationData = (JSONArray) resultJsonObj.get("results");
                return  locationData;

            }
        } catch (Exception e){
                e.printStackTrace();
        }


        return null;
    }

    private static HttpURLConnection fetchApiResponse(String URLstring){
        try{
            //attempt to create connection
            URL url = new URL(URLstring);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //set request method to get
            conn.setRequestMethod("GET");

            // connect to API
            conn.connect();
            return conn;

        } catch (IOException e){
            e.printStackTrace();
        }
        //no connection
        return null;
    }


}
