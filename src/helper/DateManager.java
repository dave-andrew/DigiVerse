package helper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateManager {

    public static String formatDate(LocalDateTime now) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return now.format(formatter);
    }

    public static String formatDate(LocalDate date, LocalTime time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.of(date, time).format(formatter);
    }

    public static LocalDateTime parseDate(String dateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(dateStr, formatter);
    }

    public static String getNow() {
    	return formatDate(LocalDateTime.now());
    }

    public static String ddMMMyy(String date) {
        	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        	LocalDateTime dateTime = LocalDateTime.parse(date, formatter);
        	formatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm");
        	return dateTime.format(formatter);
    }

}
