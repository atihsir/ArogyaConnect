import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import org.json.JSONObject;

public class PublicIPService {

    // Replace with your ipstack API key
    private static final String GEOLOCATION_API_KEY = "76fb0c52f976bf67be0671f29e219a4d";

    public static void main(String[] args) {
        String publicIP = getPublicIP();
        if (!publicIP.startsWith("Unable")) {
            System.out.println("Public IP: " + publicIP);
            String coordinates = getCoordinatesFromIP(publicIP);
            System.out.println(coordinates);
        } else {
            System.out.println("Could not fetch public IP");
        }
    }

    // Method to fetch public IP using http://checkip.amazonaws.com/
    public static String getPublicIP() {
        try {
            // Use URI to create the URL object
            URI uri = new URI("http://checkip.amazonaws.com/");
            URL url = uri.toURL(); // Convert the URI to a URL object

            // Open a connection and read the IP address from the response
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String publicIP = in.readLine();
            in.close();

            return publicIP;
        } catch (Exception e) {
            e.printStackTrace();
            return "Unable to fetch public IP";
        }
    }

    // Method to fetch latitude and longitude using the IP address
    public static String getCoordinatesFromIP(String ipAddress) {
        try {
            // Replace with your ipstack API URL
            String apiUrl = "http://api.ipstack.com/" + ipAddress + "?access_key=" + GEOLOCATION_API_KEY;
            URL url = new URL(apiUrl);
            
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            in.close();
            connection.disconnect();

            // Parse the JSON response to extract latitude and longitude
            JSONObject jsonResponse = new JSONObject(response.toString());
            if (jsonResponse.has("latitude") && jsonResponse.has("longitude")) {
                double latitude = jsonResponse.getDouble("latitude");
                double longitude = jsonResponse.getDouble("longitude");
                return "Latitude: " + latitude + ", Longitude: " + longitude;
            } else {
                return "Latitude/Longitude not available for this IP address.";
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "Unable to fetch coordinates.";
        }
    }
}
