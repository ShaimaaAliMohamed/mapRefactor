package com.sadeem.smap.util;

import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class DateUtil {

    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public double getWorkingHoursBetweenTwoDates(LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atTime(9, 0);
        LocalDateTime endDateTime = endDate.atTime(17, 0);

        if (startDate.isEqual(endDate)) {
            return Duration.between(startDateTime, endDateTime).toHours();
        }

        double totalHours = 0.0;
        LocalDate currentDate = startDate;

        while (!currentDate.isAfter(endDate)) {
            if (currentDate.equals(startDate)) {
                totalHours += Duration.between(startDateTime.toLocalTime(), LocalTime.of(17, 0)).toHours();
            } else if (currentDate.equals(endDate)) {
                totalHours += Duration.between(LocalTime.of(9, 0), endDateTime.toLocalTime()).toHours();
            } else {
                totalHours += 8; // Full working day
            }
            currentDate = currentDate.plusDays(1);
        }

        return Math.round(totalHours * 10) / 10.0;
    }
}