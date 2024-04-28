import netscape.javascript.JSObject;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class WeatherApp {
    // fetch weather data for given location
    public static JSONObject getWeatherData(String locationName) {
        // get location coordinates using the geolocation API
        JSONArray locationData = getLocationData(locationName);

        // extract latitude and longitude data
        JSONObject location = (JSONObject) locationData.get(0);
        double latitude = (double) location.get("latitude");
        double longitude = (double) location.get("longitude");

        //build API request URL with location coordinates
        String urlString =  "https://api.open-meteo.com/v1/forecast?"+
        "latitude=" + latitude + "&longitude=" + longitude+
        "&hourly=temperature_2m,relative_humidity_2m,weather_code,wind_speed_10m&timezone=Asia%2FTokyo";

        try {
            // call api and get response
            HttpURLConnection conn = fetchApiResponse(urlString);
            // check for response status
            //200 = success
            if(conn.getResponseCode()!= 200){
                System.out.println("Error: Could not connect to API");
                return null;
            }

            // store resulting JSON data
            StringBuilder resultJson = new StringBuilder();
            Scanner scan = new Scanner(conn.getInputStream());
            while(scan.hasNext()){
                //read and store into the string builder
                resultJson.append(scan.nextLine());

            }
            //close scanner
            scan.close();
            //close  url connection
            conn.disconnect();

            //parse through data
            JSONParser parser = new JSONParser();
            JSONObject resultJsonObj = (JSONObject) parser.parse(String.valueOf(resultJson));

            //retrieve hourly data
            JSONObject hourly = (JSONObject) resultJsonObj.get("hourly");

            // current hour data: get index of current hour
            JSONArray time = (JSONArray) hourly.get("time");
            int index = findIndexOfCurrentTime(time);

            // get temperature
            JSONArray temperatureData = (JSONArray) hourly.get("temperature_2m");
            double temperature = (double) temperatureData.get(index);

            // get weather
            JSONArray weathercode = (JSONArray) hourly.get("weathercode");
            String weatherCondition = convertWeatherCode((long) weathercode.get(index));



        } catch (Exception e){
            e.printStackTrace();
        }

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

    private static int findIndexOfCurrentTime(JSONArray timeList){
        String currentTime = getCurrentTime();

        // iterate through the time list and check match for current time
        for(int i = 0; i<timeList.size(); i++){
            String time = (String) timeList.get(i);
            if(time.equalsIgnoreCase(currentTime)){
                //return index
                return i;
            }
        }
        return 0;
    }

    public static String getCurrentTime(){
        // get current date and time
        LocalDateTime currentDateTime = LocalDateTime.now();
        // format date

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH':00'");

        // format and print the current date and time
        String formattedDateTime = currentDateTime.format(formatter);
        return formattedDateTime;
    }

    private static String convertWeatherCode(long weathercode){
        String weatherCondition = "";
        if (weathercode == 0L){
            weatherCondition = "Clear";

        } else if (weathercode <= 3L && weathercode > 0L){
            weatherCondition = "Cloudy";
        } else if ((weathercode >= 51L && weathercode <= 67L)
               ||(weathercode >= 80L && weathercode <= 99L)){
            weatherCondition = "Rain";
        } else if (weathercode >= 71L && weathercode <= 77L){
            weatherCondition = "Snow";
        }

        return weatherCondition;



    }
}
