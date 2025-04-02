package edu.ucalgary.oop;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;

public class SupplyPersonAllocationAccessTest {
    private DatabaseConnectionManager connectionManager;
    private SupplyPersonAllocationAccess supplyPersonAllocationDbAccess;
    private Connection connection;

    Blanket placeholderSupply = new Blanket(-1);
    Person placeholderPerson = new Person(-1, "Test Person", "Male", "111-1111");

    @Before
    public void setUp() {
        connectionManager = DatabaseConnectionManager.INSTANCE;
        connectionManager.initializeDbConnection();
        connection = connectionManager.getDbConnection();
        supplyPersonAllocationDbAccess = new SupplyPersonAllocationAccess();

        supplyPersonAllocationDbAccess.addEntry(placeholderSupply, placeholderPerson);
    }

    @After
    public void tearDown() {
        supplyPersonAllocationDbAccess.removeEntry(placeholderSupply, placeholderPerson);
        connectionManager.closeDbConnection();
    }

    @Test
    public void testGetQueryResults() {
    }

    @Test
    public void testGetAll() {
    }

    @Test
    public void testGetById() {
    }

    @Test
    public void testAddEntry() {
    }

    @Test
    public void testRemoveEntry() {
    }
}