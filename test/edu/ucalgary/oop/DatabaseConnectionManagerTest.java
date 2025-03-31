package edu.ucalgary.oop;

import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;

import static org.junit.Assert.*;

public class DatabaseConnectionManagerTest {
    private DatabaseConnectionManager connection;

    @Before
    public void setUp() {
        connection = DatabaseConnectionManager.INSTANCE;
    }

    @Test
    public void testGetDatabaseUrl() {
        String expectedUrl = "jdbc:postgresql://localhost/project";
        assertEquals("Connection Manager URL should match the expected Database URL", expectedUrl,
                connection.getDatabaseUrl());
    }

    @Test
    public void testGetUsername() {
        String expectedUsername = "oop";
        assertEquals("Connection Manager username should match the expected login username", expectedUsername,
                connection.getUsername());
    }

    @Test
    public void testGetPassword() {
        String expectedPassword = "ucalgary";
        assertEquals("Connection Manager username should match the expected login username", expectedPassword,
                connection.getPassword());
    }

    @Test
    public void testGetDbConnection() {
        connection.initializeDbConnection();

        assertNotNull("Assuming a properly initialized connection, getDbConnection should return a connection",
                connection.getDbConnection());

        connection.closeDbConnection();
    }

    @Test
    public void testInitializeDbConnection() {
        assertNull("Before calling initializeDbConnection(), the connection should be null",
                connection.getDbConnection());

        connection.initializeDbConnection();

        assertNotNull("After calling initializeDbConnection(), the connection should exist",
                connection.getDbConnection());

        connection.closeDbConnection();
    }

    @Test
    public void testCloseDbConnection() {
        connection.initializeDbConnection();

        try {
            assertFalse("An initialized connection should be open until closeDbConnection() is called",
                    connection.getDbConnection().isClosed());
        } catch (SQLException e) {
            fail("SQLException occured while checking connection status: " + e.getMessage());
        }

        connection.closeDbConnection();

        try {
            assertTrue("The connection should be closed after calling closeDbConnection",
                    connection.getDbConnection().isClosed());
        } catch (SQLException e) {
            fail("SQLException occured while checking connection status: " + e.getMessage());
        }
    }

    @Test
    public void testMultipleInitializeConnection() {
        connection.initializeDbConnection();
        connection.initializeDbConnection();
        connection.initializeDbConnection();

        assertNotNull("Connection should still be valid after multiple initialization calls without closing",
                connection.getDbConnection());
    }

    @Test
    public void testInitializeAfterClosing() {
        connection.initializeDbConnection();
        connection.closeDbConnection();
        connection.initializeDbConnection();

        assertNotNull("The connection should be valid after initialization, closing, and then re-initializing",
                connection.getDbConnection());
    }
}
