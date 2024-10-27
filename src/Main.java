// src/Main.java
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Appointment.initializeDoctors();
        System.out.println("Welcome to ArogyaConnect!");
        while (true) {
            System.out.println("\nWhich feature do you want to access?");
            System.out.println("1. Book an Appointment");
            System.out.println("2. Emergency");
            System.out.println("3. Set Medicine Reminder");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    Appointment.bookAppointment(scanner);
                    break;
                case 2:
                    Emergency.handleEmergency();
                    break;
                case 3:
                    MedicineReminderApp.setMedicineReminder(scanner);
                    break;
                case 4:
                    System.out.println("Exiting...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
}


