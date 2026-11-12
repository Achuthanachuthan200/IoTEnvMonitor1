package com.iot.monitor;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

@WebServlet("/api/data")
public class SensorServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = req.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) sb.append(line);
        }

        String jsonStr = sb.toString();
        
        // Plain Java extraction to avoid external library dependency issues
        double temp = extractValue(jsonStr, "temperature");
        double humidity = extractValue(jsonStr, "humidity");
        double pressure = extractValue(jsonStr, "pressure");

        if (!SensorProcessor.isValid(temp, humidity, pressure)) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("Invalid Data");
            return;
        }

        String status = SensorProcessor.classifyStatus(temp, humidity, pressure);
        String reportPath = getServletContext().getRealPath("/") + "report.html";
        SensorProcessor.generateHtmlReport(temp, humidity, pressure, status, reportPath);

        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().write("Success: " + status);
    }

    private double extractValue(String json, String key) {
        String searchKey = "\"" + key + "\":";
        int idx = json.indexOf(searchKey);
        if (idx == -1) return 0.0;
        int start = idx + searchKey.length();
        int end = start;
        while (end < json.length() && (Character.isDigit(json.charAt(end)) || json.charAt(end) == '.' || json.charAt(end) == '-')) {
            end++;
        }
        try {
            return Double.parseDouble(json.substring(start, end).trim());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
}