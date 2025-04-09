package edu.ucalgary.oop;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class SupplyPersonAllocationAccessTest {
    private SupplyPersonAllocationAccess supplyPersonAllocationDbAccess;

    Supply placeholderSupply = new Blanket(-1);
    Person placeholderPerson = new Person(-1, "Test Person", "Male", "111-1111");

    @Before
    public void setUp() throws Exception {
        supplyPersonAllocationDbAccess = new SupplyPersonAllocationAccess();

        try {
            supplyPersonAllocationDbAccess.addEntry(placeholderSupply, placeholderPerson);
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }
    }

    @After
    public void tearDown() throws Exception {
        try {
            supplyPersonAllocationDbAccess.removeEntry(placeholderSupply, placeholderPerson);
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }
    }

    @Test
    public void testGetQueryResults() {
        try {
            supplyPersonAllocationDbAccess.getAll();
        } catch (SQLException e) {
            fail("SQLException occurred while testing getQueryResults: " + e.getMessage());
        }
        assertNotNull("getQueryResults() should retrieve a valid query", supplyPersonAllocationDbAccess.getQueryResults());
    }

    @Test
    public void testGetAll() {
        List<Map<Supply, Person>> allAllocations = null;

        try {
            allAllocations = supplyPersonAllocationDbAccess.getAll();
        } catch (SQLException e) {
            fail("SQLException occurred while testing getAll: " + e.getMessage());
        }
        assertFalse("getAll() should return a list of supplies and their allocated persons", allAllocations.isEmpty());
    }

    @Test
    public void testGetById() {
        Map<Supply, Person> allocation = null;

        try {
            allocation = supplyPersonAllocationDbAccess.getById(placeholderSupply, placeholderPerson);
        } catch (SQLException e) {
            fail("SQLException occurred while testing getById: " + e.getMessage());
        }
        assertEquals("The retrieved person allocated for a supply should match the expected person when calling getById()",
                placeholderPerson.getAssignedId(), allocation.get(placeholderSupply).getAssignedId());
    }

    @Test
    public void testAddEntry() {
        List<Map<Supply, Person>> allocationsBeforeAdding = null;
        List<Map<Supply, Person>> allocationsAfterAdding = null;

        Supply newSupply = new Blanket(-2);
        Person newPerson = new Person(-2, "Test Person 2", "Male", "222-2222");

        try {
            allocationsBeforeAdding = supplyPersonAllocationDbAccess.getAll();
            supplyPersonAllocationDbAccess.addEntry(newSupply, newPerson);
            allocationsAfterAdding = supplyPersonAllocationDbAccess.getAll();
        } catch (SQLException e) {
            fail("SQLException occurred while testing addEntry: " + e.getMessage());
        }

        assertNotEquals("New supply-person entry should be added to the database",
                allocationsBeforeAdding.size(), allocationsAfterAdding.size());

        try {
            supplyPersonAllocationDbAccess.removeEntry(newSupply, newPerson);
        } catch (SQLException e) {
            fail("SQLException occurred while cleaning up addEntry test: " + e.getMessage());
        }
    }

    @Test
    public void testRemoveEntry() {
        List<Map<Supply, Person>> allocationsBeforeRemoving = null;
        List<Map<Supply, Person>> allocationsAfterRemoving = null;

        Supply testSupply = new Blanket(-2);
        Person testPerson = new Person(-2, "Test Person 2", "Male", "222-2222");

        try {
            supplyPersonAllocationDbAccess.addEntry(testSupply, testPerson);
            allocationsBeforeRemoving = supplyPersonAllocationDbAccess.getAll();
            supplyPersonAllocationDbAccess.removeEntry(testSupply, testPerson);
            allocationsAfterRemoving = supplyPersonAllocationDbAccess.getAll();
        } catch (SQLException e) {
            fail("SQLException occurred while testing removeEntry: " + e.getMessage());
        }

        assertNotEquals("Unwanted supply-person entry should be removed from the database",
                allocationsBeforeRemoving.size(), allocationsAfterRemoving.size());
    }

    @Test
    public void testGetByIdNotInDb() {
        Map<Supply, Person> allocation = null;

        Supply supplyNotInDb = new Blanket(-999);
        Person personNotInDb = new Person(-999, "Person not in db", "Male", "999-9999");

        try {
            allocation = supplyPersonAllocationDbAccess.getById(supplyNotInDb, personNotInDb);
        } catch (SQLException e) {
            fail("SQLException occurred while testing getByIdNotInDb: " + e.getMessage());
        }

        assertNull("A null association map should be returned for a supply/person association that isn't in the database",
                allocation);
    }

    @Test
    public void testAddEntryThatAlreadyExists() {
        List<Map<Supply, Person>> allocationsBeforeAdding = null;
        List<Map<Supply, Person>> allocationsAfterAdding = null;

        try {
            allocationsBeforeAdding = supplyPersonAllocationDbAccess.getAll();
            supplyPersonAllocationDbAccess.addEntry(placeholderSupply, placeholderPerson);
            allocationsAfterAdding = supplyPersonAllocationDbAccess.getAll();
        } catch (SQLException e) {
            fail("SQLException occurred while testing addEntryThatAlreadyExists: " + e.getMessage());
        }

        assertEquals("Adding an existing supply-person entry should not increase the total count",
                allocationsBeforeAdding.size(), allocationsAfterAdding.size());
    }

    @Test
    public void testRemoveEntryNotInDb() {
        List<Map<Supply, Person>> allocationsBeforeRemoving = null;
        List<Map<Supply, Person>> allocationsAfterRemoving = null;

        Supply supplyNotInDb = new Blanket(-999);
        Person personNotInDb = new Person(-999, "Person not in db", "Male", "999-9999");

        try {
            allocationsBeforeRemoving = supplyPersonAllocationDbAccess.getAll();
            supplyPersonAllocationDbAccess.removeEntry(supplyNotInDb, personNotInDb);
            allocationsAfterRemoving = supplyPersonAllocationDbAccess.getAll();
        } catch (SQLException e) {
            fail("SQLException occurred while testing removeEntryNotInDb: " + e.getMessage());
        }

        assertEquals("Removing an entry that isn't in the database should not affect the database",
                allocationsBeforeRemoving.size(), allocationsAfterRemoving.size());
    }

    @Test
    public void testAddEntryWithNullArguments() {
        boolean success = true;

        try {
            success = supplyPersonAllocationDbAccess.addEntry(null, null);
        } catch (SQLException e) {
            fail("SQLException occurred while testing addEntryWithNullArguments: " + e.getMessage());
        }

        assertFalse("addEntry() should return false when attempting to add a null entry", success);
    }
}
