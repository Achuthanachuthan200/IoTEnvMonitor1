package com.iot.monitor;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SensorProcessor {
    public static boolean isValid(double temp, double humidity, double pressure) {
        return (temp >= -50.0 && temp <= 100.0) &&
               (humidity >= 0.0 && humidity <= 100.0) &&
               (pressure >= 300.0 && pressure <= 1100.0);
    }

    public static String classifyStatus(double temp, double humidity, double pressure) {
        if (temp > 38.0 || humidity > 85.0 || pressure < 990.0) {
            return "Critical";
        } else if ((temp >= 30.0 && temp <= 38.0) || (humidity >= 70.0 && humidity <= 85.0)) {
            return "Warning";
        }
        return "Normal";
    }

    public static void generateHtmlReport(double temp, double humidity, double pressure, String status, String filePath) throws IOException {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String color = status.equals("Critical") ? "#d9534f" : status.equals("Warning") ? "#f0ad4e" : "#5cb85c";

        String html = "<!DOCTYPE html><html><head><title>IoT Report</title></head>"
                + "<body style='font-family: Arial; margin: 40px;'>"
                + "<h2>IoT Environmental Monitoring Report</h2>"
                + "<p><b>Timestamp:</b> " + timestamp + "</p>"
                + "<p><b>Temperature:</b> " + temp + " &deg;C</p>"
                + "<p><b>Humidity:</b> " + humidity + " %</p>"
                + "<p><b>Pressure:</b> " + pressure + " hPa</p>"
                + "<p><b>Status:</b> <span style='background-color:" + color + "; color:white; padding: 5px 10px; border-radius: 4px;'>" + status + "</span></p>"
                + "</body></html>";

        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(html);
        }
    }
}