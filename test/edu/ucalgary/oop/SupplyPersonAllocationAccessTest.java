package edu.ucalgary.oop;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import static org.junit.Assert.*;

public class SupplyPersonAllocationAccessTest {

    private SupplyPersonAllocationAccess supplyPersonDbAccess;
    private SupplyService supplyService;
    private PersonService personService;
    private Supply placeholderSupply;
    private Person placeholderPerson;
    private LocalDate testAllocationDate;
    private Allocation testAllocation;

    @Before
    public void setUp() throws Exception {
        supplyService = SupplyService.INSTANCE;
        personService = PersonService.INSTANCE;
        supplyPersonDbAccess = new SupplyPersonAllocationAccess();

        placeholderSupply = supplyService.getSupplyById(1);
        placeholderPerson = personService.getPersonById(1);
        testAllocationDate = LocalDate.now();

        try {
            testAllocation = supplyPersonDbAccess.addEntry(placeholderSupply, placeholderPerson, testAllocationDate);
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }
    }

    @After
    public void tearDown() throws Exception {
        try {
            supplyPersonDbAccess.removeEntry(placeholderSupply, placeholderPerson, testAllocationDate);
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }
    }

    @Test
    public void testGetQueryResults() {
        try {
            supplyPersonDbAccess.getAll();
        } catch (SQLException e) {
            fail("Error occurred while testing getQueryResults: " + e.getMessage());
        }

        assertNotNull("getQueryResults() should retrieve a valid query", supplyPersonDbAccess.getQueryResults());
    }

    @Test
    public void testGetAll() {
        List<Allocation> allRetrievedAllocations = null;

        try {
            allRetrievedAllocations = supplyPersonDbAccess.getAll();
        } catch (SQLException e) {
            fail("Error occurred while testing getAll: " + e.getMessage());
        }

        assertFalse("getAll() should return a list of allocations", allRetrievedAllocations.isEmpty());
    }

    @Test
    public void testGetById() {
        Allocation retrievedAllocation = null;

        try {
            retrievedAllocation = supplyPersonDbAccess.getById(placeholderSupply, placeholderPerson);
        } catch (SQLException e) {
            fail("Error occurred while testing getById: " + e.getMessage());
        }

        assertNotNull("getById() should return a valid allocation", retrievedAllocation);
        assertEquals("The retrieved allocation's supply should match",
                placeholderSupply.getSupplyId(), retrievedAllocation.getAllocatedSupply().getSupplyId());
        assertEquals("The retrieved allocation's person ID should match",
                placeholderPerson.getAssignedId(), retrievedAllocation.getAllocatedPersonId());
    }

    @Test
    public void testAddEntry() {
        List<Allocation> allocationsBeforeAdding = null;
        List<Allocation> allocationsAfterAdding = null;
        Supply newSupply = null;
        Person newPerson = null;
        LocalDate newDate = LocalDate.now();
        Allocation newAllocation = null;

        try {
            newSupply = supplyService.getSupplyById(2);
            newPerson = personService.getPersonById(2);

            allocationsBeforeAdding = supplyPersonDbAccess.getAll();
            newAllocation = supplyPersonDbAccess.addEntry(newSupply, newPerson, newDate);
            allocationsAfterAdding = supplyPersonDbAccess.getAll();
        } catch (SQLException e) {
            fail("Error occurred while testing addEntry: " + e.getMessage());
        }

        assertNotNull("addEntry() should return a new Allocation object", newAllocation);
        assertEquals("The allocation's supply should match", newSupply.getSupplyId(),
                newAllocation.getAllocatedSupply().getSupplyId());
        assertEquals("The allocation's person ID should match", newPerson.getAssignedId(),
                newAllocation.getAllocatedPersonId());
        assertTrue("The allocations list should have grown",
                allocationsAfterAdding.size() > allocationsBeforeAdding.size());

        try {
            supplyPersonDbAccess.removeEntry(newSupply, newPerson, newDate);
        } catch (SQLException e) {
            fail("Error occurred during cleanup: " + e.getMessage());
        }
    }

    @Test
    public void testRemoveEntry() {
        List allocationsBeforeRemoving = null;
        List allocationsAfterRemoving = null;
        Supply extraSupply = null;
        Person extraPerson = null;
        LocalDate extraDate = LocalDate.now();

        try {
            extraSupply = supplyService.getSupplyById(3);
            extraPerson = personService.getPersonById(3);

            supplyPersonDbAccess.addEntry(extraSupply, extraPerson, extraDate);
            allocationsBeforeRemoving = supplyPersonDbAccess.getAll();
            supplyPersonDbAccess.removeEntry(extraSupply, extraPerson, extraDate);
            allocationsAfterRemoving = supplyPersonDbAccess.getAll();
        } catch (SQLException e) {
            fail("Error occurred while testing removeEntry: " + e.getMessage());
        }

        assertTrue("removeEntry() should remove the unwanted entry", allocationsAfterRemoving.size() <
                allocationsBeforeRemoving.size());
    }

    @Test
    public void testGetPersonSupplies() {
        List testSupplies = null;
        Person testPerson = null;

        try {
            testPerson = personService.getPersonById(1);
            testSupplies = supplyPersonDbAccess.getPersonSupplies(testPerson);
        } catch (SQLException e) {
            fail("Error occurred while testing getPersonSupplies: " + e.getMessage());
        }

        assertFalse("Person should have at least one supply allocated", testSupplies.isEmpty());
    }

    @Test
    public void testGetByIdNotInDb() {
        Allocation retrievedAllocation = null;
        Supply supplyNotInDb = new Blanket(-999);
        Person personNotInDb = new Person(-999, "Test Person", "Man", "111-1111");

        try {
            retrievedAllocation = supplyPersonDbAccess.getById(supplyNotInDb, personNotInDb);
        } catch (SQLException e) {
            fail("Error occurred while testing GetByIdNotInDb: " + e.getMessage());
        }

        assertNull("A null allocation should be returned for a non-existent allocation", retrievedAllocation);
    }

    @Test
    public void testAddEntryWithNullArguments() {
        Allocation nullAllocation = null;

        try {
            nullAllocation = supplyPersonDbAccess.addEntry(null, null, LocalDate.now());
        } catch (SQLException e) {
            fail("Error occurred while testing addEntryWithNullArguments: " + e.getMessage());
        }

        assertNull("addEntry() should return null when attempting to add with null arguments", nullAllocation);
    }
}
