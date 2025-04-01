package edu.ucalgary.oop;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;

public class SupplyLocationAllocationAccessTest {
    private DatabaseConnectionManager connectionManager;
    private SupplyLocationAllocationAccess supplyLocationAllocationDbAccess;
    private Connection connection;

    Blanket placeholderSupply = new Blanket(-1);
    Location placeholderLocation = new Location(-1, "Test Location", "Test");

    @Before
    public void setUp() {
        connectionManager = DatabaseConnectionManager.INSTANCE;
        connectionManager.initializeDbConnection();
        connection = connectionManager.getDbConnection();
        supplyLocationAllocationDbAccess = new SupplyLocationAllocationAccess();

        supplyLocationAllocationDbAccess.addEntry(placeholderSupply, placeholderLocation);
    }

    @After
    public void tearDown() {
        supplyLocationAllocationDbAccess.removeEntry(placeholderSupply, placeholderLocation);
        connectionManager.closeDbConnection();
    }

    @Test
    public void testGetQueryResults() {
    }

    @Test
    public void testUpdateInfo() {
    }

    @Test
    public void testGetInfo() {
    }

    @Test
    public void testAddEntry() {
    }

    @Test
    public void testRemoveEntry() {
    }
}