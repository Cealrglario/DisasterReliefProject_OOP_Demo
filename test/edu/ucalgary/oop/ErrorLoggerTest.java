package edu.ucalgary.oop;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.File;

public class ErrorLoggerTest {
    private ErrorLogger errorLogger;

    @Before
    public void setUp() throws Exception {
        errorLogger = ErrorLogger.INSTANCE;
    }

    @Test
    public void testLogError() {
        try {
            int i = 1 / 0;
        } catch (Exception e) {
            System.out.println("Fatal exception caught - logging error");
            System.out.println("-------------------------------------------------");
            errorLogger.logError(e);
        }

        File errorLog = new File("data/logs/error_log.txt");
        assertTrue(errorLog.exists());
    }
}
