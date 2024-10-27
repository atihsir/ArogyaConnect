// src/Patient.java
public class Patient {
    String name;
    int age;
    double latitude;
    double longitude;
    String selectedDoctorType;

    public Patient(String name, int age, double latitude, double longitude, String selectedDoctorType) {
        this.name = name;
        this.age = age;
        this.latitude = latitude;
        this.longitude = longitude;
        this.selectedDoctorType = selectedDoctorType;
    }
}
