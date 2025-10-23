package org.adv.clickerflex.utils.generic;

import java.time.*;
import java.time.format.DateTimeFormatter;

public class DateUtils {
    public static LocalDateTime fromMillis(long millis){
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneId.systemDefault());
    }
    public static String convertToString(ZonedDateTime zonedDateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return zonedDateTime.format(formatter);
    }
    public static String convertToString(LocalDateTime localDateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return localDateTime.format(formatter);
    }
    public static String ConvertTime(Duration duration){
        long seconds = duration.getSeconds();

        long days = seconds / 86400;
        seconds %= 86400;

        long hours = seconds / 3600;
        seconds %= 3600;

        long minutes = seconds / 60;
        seconds %= 60;

        StringBuilder sb = new StringBuilder();
        if (days > 0) sb.append(days).append("d ");
        if (hours > 0) sb.append(hours).append("h ");
        if (minutes > 0) sb.append(minutes).append("m ");
        if (seconds > 0)sb.append(seconds).append("s");
        if(sb.isEmpty()) sb.append("0s");
        return sb.toString().trim();
    }
    public static String ConvertTime(Instant date){
        if(date.isBefore(Instant.now())) return "0s";
        return ConvertTime(Duration.between(Instant.now(), date));
    }
}
