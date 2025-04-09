package edu.ucalgary.oop;

public enum ErrorLogger {
    INSTANCE;

    private final String ERROR_LOG = "data/logs/error_log.txt";
    private boolean initialized = false;

    public void beginLogging() {
        initialized = true;
    }

    public void logError(Exception e) {}

    public boolean getInitialized() {
        return this.initialized;
    }
}
