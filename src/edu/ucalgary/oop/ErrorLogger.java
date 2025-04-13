package edu.ucalgary.oop;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public enum ErrorLogger {
    INSTANCE;

    private final String ERROR_LOG = "data/logs/error_log.txt";

    public void logError(Exception e) {
        System.out.println("Logging error in progress...");

        try {
            File logDirectory = new File("data/logs");

            if (!logDirectory.exists()) {
                logDirectory.mkdirs();
            }

            FileWriter fileWriter = new FileWriter(ERROR_LOG, true);
            PrintWriter printWriter = new PrintWriter(fileWriter);

            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            String errorTime = now.format(timeFormatter);

            printWriter.println("Exception caught: " + e.getMessage());
            printWriter.println("Timestamp of exception: " + errorTime);
            printWriter.println("Stack trace: ");
            printWriter.println("---------------------------");
            e.printStackTrace(printWriter);
            printWriter.println("");

            printWriter.close();

            System.out.println("Error has been logged. Now exiting.");
        } catch (IOException logError) {
            System.err.println("Failed to log error. Error while logging: " + logError.getMessage());
        }
    }
}
