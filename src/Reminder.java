// Reminder.java
import java.time.LocalDateTime;

public class Reminder {
    LocalDateTime time;
    String message;

    public Reminder(LocalDateTime time, String message) {
        this.time = time;
        this.message = message;
    }
}