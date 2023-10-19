package helper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateManager {

    public static String formatDate(LocalDateTime now) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return now.format(formatter);
    }

    public static LocalDateTime parseDate(String dateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(dateStr, formatter);
    }

}
