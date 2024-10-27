import java.util.Scanner;


public class Appointment {
    private static Doctor[] doctors;
    private static Graph doctorGraph;
    private static final int PATIENT_NODE_INDEX = 5; // Adding patient as the 6th node

    public static void initializeDoctors() {
        // Example doctors (in reality, this would come from a database)
        doctors = new Doctor[5];
        doctors[0] = new Doctor(0, "Sharma", "Cardiologist", 30.3165, 78.0322, new String[]{"10 AM", "11 AM", "1 PM"}); // Dehradun (Lat: 30.3165, Lon: 78.0322)
        doctors[1] = new Doctor(1, "Verma", "Cardiologist", 28.6139, 77.2090, new String[]{"9 AM", "12 PM", "3 PM"}); // Delhi (Lat: 28.6139, Lon: 77.2090)
        doctors[2] = new Doctor(2, "Gupta", "Cardiologist", 19.0760, 72.8777, new String[]{"10 AM", "12 PM", "4 PM"}); // Mumbai (Lat: 19.0760, Lon: 72.8777)
        doctors[3] = new Doctor(3, "Reddy", "Cardiologist", 28.6139, 77.2090, new String[]{"9 AM", "11 AM", "2 PM"}); // Delhi (Lat: 28.6139, Lon: 77.2090)
        doctors[4] = new Doctor(4, "Singh", "Cardiologist", 19.0760, 72.8777, new String[]{"10 AM", "1 PM", "5 PM"}); // Mumbai (Lat: 19.0760, Lon: 72.8777)


        doctorGraph = new Graph(doctors.length + 1); // Adding one more vertex for the patient node
    }
     
    public static void bookAppointment(Scanner scanner) {
        System.out.println("\nSelect the type of doctor you need:");
        System.out.println("1. Physician/ चिकित्सक");
        System.out.println("2. Cardiologist/ हृदय रोग विशेषज्ञ");
        System.out.println("3. Surgeon/ शल्य चिकित्सक");
        System.out.println("4. Dermatologist/ त्वचा रोग विशेषज्ञ");
        System.out.print("Enter your choice: ");
        int doctorType = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        System.out.print("Enter your name: ");
        String name = scanner.nextLine();

        // Automatically fetch the public IP of the patient
        String userIp = PublicIPService.getPublicIP();
        System.out.println("Your IP: " + userIp); // Print the IP for reference

        // Fetch patient coordinates dynamically from API using the IP address
        String coordinates = PublicIPService.getCoordinatesFromIP(userIp);
        if (coordinates.startsWith("Latitude/Longitude not available") || coordinates.startsWith("Unable")) {
            System.out.println("Error fetching your coordinates.");
            return; // Exit if unable to fetch coordinates
        }

        System.out.println(coordinates);

        // Split the coordinates to extract latitude and longitude
        String[] locationParts = coordinates.replace("Latitude: ", "").replace("Longitude: ", "").split(",");
        double latitude = Double.parseDouble(locationParts[0].trim());
        double longitude = Double.parseDouble(locationParts[1].trim());

        System.out.print("Enter your age: ");
        int age = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        // Store patient information in the database
        UserInputApp.saveToDatabase(name, age, latitude, longitude);  // Save to the database using UserInputApp

        String selectedDoctorType = getDoctorType(doctorType);
        Patient patient = new Patient(name, age, latitude, longitude, selectedDoctorType);

        // Add edges between the patient and all doctors
        for (Doctor doctor : doctors) {
            double distance = DistanceCalculator.calculateDistance(patient.latitude, patient.longitude, doctor.latitude, doctor.longitude);
            doctorGraph.addEdge(PATIENT_NODE_INDEX, doctor.id, distance); // Connect patient to doctors
        }

        // Pass the scanner to matchDoctors
        matchDoctors(patient, doctorType, scanner);
    }

    private static String getDoctorType(int doctorType) {
        switch (doctorType) {
            case 1: return "Physician";
            case 2: return "Cardiologist";
            case 3: return "Surgeon";
            case 4: return "Dermatologist";
            default: return "Unknown";
        }
    }

    private static void matchDoctors(Patient patient, int doctorType, Scanner scanner) {

        System.out.println("\nSearching for nearby doctors...");
        
        // Run Dijkstra’s algorithm from the patient node
        double[] distances = doctorGraph.dijkstra(PATIENT_NODE_INDEX); // Start from the patient node

        // Filter doctors based on the selected type (e.g., Physician)
        Doctor[] filteredDoctors = new Doctor[doctors.length];
        int count = 0;
        for (Doctor doctor : doctors) {
            if (doctor.specialization.equals(getDoctorType(doctorType))) {
                filteredDoctors[count++] = doctor; // Add only doctors matching the selected type
            }
        }

        // Sort filtered doctors by distance to the patient
        for (int i = 0; i < count - 1; i++) {
            for (int j = i + 1; j < count; j++) {
                if (distances[filteredDoctors[i].id] > distances[filteredDoctors[j].id]) {
                    // Swap doctors
                    Doctor temp = filteredDoctors[i];
                    filteredDoctors[i] = filteredDoctors[j];
                    filteredDoctors[j] = temp;
                }
            }
        }

        // Print sorted doctors based on distance
        System.out.println("We found the following " + getDoctorType(doctorType) + "s:");
        for (int i = 0; i < count; i++) {
            System.out.printf("%d. %s (%.2f km away)\n", i + 1, filteredDoctors[i].name, distances[filteredDoctors[i].id]);
        }

        // Let the patient select a doctor
        System.out.print("Select a doctor : ");
        int selectedDoctor = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        Doctor doctor = filteredDoctors[selectedDoctor - 1];
        displayTimeSlots(doctor, scanner);
    }

    private static void displayTimeSlots(Doctor doctor, Scanner scanner) {
        System.out.println("\nAvailable time slots for Dr. " + doctor.name + ":");
        for (String slot : doctor.availableSlots) {
            System.out.println("- " + slot);
        }

        System.out.print("Select a time slot: ");
        int slot = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.println("Appointment confirmed with Dr. " + doctor.name + " at " + doctor.availableSlots[slot - 1]);
    }
}
