import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.Scanner;

public class UserInputApp {
    // Database URL, username, and password
    private static final String DB_URL = "jdbc:mysql://localhost:3306/patient";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "Rk@106283";

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            // Get user input
            System.out.print("Enter your name: ");
            String name = scanner.nextLine();
            System.out.print("Enter your age: ");
            int age = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            // Fetch IP, Latitude, and Longitude using PublicIPService class
            String publicIP = PublicIPService.getPublicIP();
            String coordinates = PublicIPService.getCoordinatesFromIP(publicIP);

            // Parse coordinates to get latitude and longitude
            String[] locationParts = coordinates.replace("Latitude: ", "").replace("Longitude: ", "").split(",");
            double latitude = Double.parseDouble(locationParts[0].trim());
            double longitude = Double.parseDouble(locationParts[1].trim());

            // Save user input to the database, including latitude and longitude
            saveToDatabase(name, age, latitude, longitude);

            // Display data from the database
            displayDatabaseRecords();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to save user input along with latitude and longitude to the database
    public static void saveToDatabase(String name, int age, double latitude, double longitude) {
        String insertSQL = "INSERT INTO user_info (name, age, latitude, longitude) VALUES (?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {

            // Set parameters for the PreparedStatement
            pstmt.setString(1, name);
            pstmt.setInt(2, age);
            pstmt.setDouble(3, latitude);
            pstmt.setDouble(4, longitude);

            // Execute the insert command
            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Record inserted successfully with coordinates.");
            }
        } catch (SQLException e) {
            System.out.println("Database operation failed.");
            e.printStackTrace();
        }
    }

    // Method to display data from the user_info table
    public static void displayDatabaseRecords() {
        String selectSQL = "SELECT * FROM user_info";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement pstmt = conn.prepareStatement(selectSQL);
             ResultSet rs = pstmt.executeQuery()) {

            // Print out the data from the ResultSet
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int age = rs.getInt("age");
                double latitude = rs.getDouble("latitude");
                double longitude = rs.getDouble("longitude");

                System.out.printf("ID: %d, Name: %s, Age: %d, Latitude: %.6f, Longitude: %.6f%n", id, name, age, latitude, longitude);
            }
        } catch (SQLException e) {
            System.out.println("Failed to retrieve data.");
            e.printStackTrace();
        }
    }
}
