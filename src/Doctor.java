// src/Doctor.java
public class Doctor {
    int id;
    String name;
    String specialization;
    double latitude;
    double longitude;
    String[] availableSlots;

    public Doctor(int id, String name, String specialization, double latitude, double longitude, String[] availableSlots) {
        this.id = id;
        this.name = name;
        this.specialization = specialization;
        this.latitude = latitude;
        this.longitude = longitude;
        this.availableSlots = availableSlots;
    }
}
