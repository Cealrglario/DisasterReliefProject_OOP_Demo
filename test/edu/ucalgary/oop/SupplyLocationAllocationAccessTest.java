package edu.ucalgary.oop;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import static org.junit.Assert.*;

public class SupplyLocationAllocationAccessTest {

    private SupplyLocationAllocationAccess supplyLocationDbAccess;
    private SupplyService supplyService;
    private LocationService locationService;
    private Supply placeholderSupply;
    private Location placeholderLocation;
    private LocalDate testAllocationDate;
    private Allocation testAllocation;

    @Before
    public void setUp() throws Exception {
        supplyService = SupplyService.INSTANCE;
        locationService = LocationService.INSTANCE;
        supplyLocationDbAccess = new SupplyLocationAllocationAccess();

        placeholderSupply = supplyService.getSupplyById(5);
        placeholderLocation = locationService.getLocation(1);
        testAllocationDate = LocalDate.now();

        try {
            testAllocation = supplyLocationDbAccess.addEntry(placeholderSupply, placeholderLocation, testAllocationDate);
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }
    }

    @After
    public void tearDown() throws Exception {
        try {
            supplyLocationDbAccess.removeEntry(testAllocation);
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }
    }

    @Test
    public void testGetQueryResults() {
        try {
            supplyLocationDbAccess.getAll();
        } catch (SQLException e) {
            fail("Error occurred while testing getQueryResults: " + e.getMessage());
        }

        assertNotNull("getQueryResults() should retrieve a valid query", supplyLocationDbAccess.getQueryResults());
    }

    @Test
    public void testGetAll() {
        List<Allocation> allRetrievedAllocations = null;

        try {
            allRetrievedAllocations = supplyLocationDbAccess.getAll();
        } catch (SQLException e) {
            fail("Error occurred while testing getAll: " + e.getMessage());
        }

        assertFalse("getAll() should return a list of allocations", allRetrievedAllocations.isEmpty());
    }

    @Test
    public void testGetById() {
        Allocation retrievedAllocation = null;

        try {
            retrievedAllocation = supplyLocationDbAccess.getById(placeholderSupply, placeholderLocation);
        } catch (SQLException e) {
            fail("Error occurred while testing getById: " + e.getMessage());
        }

        assertNotNull("getById() should return a valid allocation", retrievedAllocation);
        assertEquals("The retrieved allocation's supply should match",
                placeholderSupply.getSupplyId(), retrievedAllocation.getAllocatedSupply().getSupplyId());
        assertEquals("The retrieved allocation's location ID should match",
                placeholderLocation.getLocationId(), retrievedAllocation.getLocationId());
    }

    @Test
    public void testAddEntry() {
        List<Allocation> allocationsBeforeAdding = null;
        List<Allocation> allocationsAfterAdding = null;
        Supply newSupply = null;
        Location newLocation = null;
        LocalDate newDate = LocalDate.now().plusDays(1);
        Allocation newAllocation = null;

        try {
            newSupply = supplyService.getSupplyById(6);
            newLocation = locationService.getLocation(2);
            allocationsBeforeAdding = supplyLocationDbAccess.getAll();

            newAllocation = supplyLocationDbAccess.addEntry(newSupply, newLocation, newDate);
            allocationsAfterAdding = supplyLocationDbAccess.getAll();
        } catch (SQLException e) {
            fail("Error occurred while testing addEntry: " + e.getMessage());
        }

        assertNotNull("addEntry() should return a new Allocation object", newAllocation);
        assertEquals("The allocation's supply should match", newSupply.getSupplyId(),
                newAllocation.getAllocatedSupply().getSupplyId());
        assertEquals("The allocation's location ID should match", newLocation.getLocationId(),
                newAllocation.getLocationId());
        assertTrue("The allocations list should have grown",
                allocationsAfterAdding.size() > allocationsBeforeAdding.size());

        try {
            supplyLocationDbAccess.removeEntry(newAllocation);
        } catch (SQLException e) {
            fail("Error occurred during cleanup: " + e.getMessage());
        }
    }

    @Test
    public void testRemoveEntry() {
        List<Allocation> allocationsBeforeRemoving = null;
        List<Allocation> allocationsAfterRemoving = null;
        Supply extraSupply = null;
        Location extraLocation = null;
        LocalDate extraDate = LocalDate.now();

        try {
            extraSupply = supplyService.getSupplyById(7);
            extraLocation = locationService.getLocation(2);

            Allocation newAllocation = supplyLocationDbAccess.addEntry(extraSupply, extraLocation, extraDate);
            allocationsBeforeRemoving = supplyLocationDbAccess.getAll();
            supplyLocationDbAccess.removeEntry(newAllocation);
            allocationsAfterRemoving = supplyLocationDbAccess.getAll();
        } catch (SQLException e) {
            fail("Error occurred while testing removeEntry: " + e.getMessage());
        }

        assertTrue("removeEntry() should remove the unwanted entry", allocationsAfterRemoving.size() <
                allocationsBeforeRemoving.size());
    }

    @Test
    public void testGetSuppliesAtLocation() {
        List<Allocation> testSupplies = null;
        Location testLocation = null;
        
        try {
            testLocation = locationService.getLocation(1);
            testSupplies = supplyLocationDbAccess.getSuppliesAtLocation(testLocation);
        } catch (SQLException e) {
            fail("Error occurred while testing getSuppliesAtLocation: " + e.getMessage());
        }
        
        assertFalse("Location should have at least one supply allocated", testSupplies.isEmpty());
    }

    @Test
    public void testGetByIdNotInDb() {
        Allocation retrievedAllocation = null;
        Supply supplyNotInDb = new Blanket(-999);
        Location locationNotInDb = new Location(-999, "Location not in db", "Test");
        
        try {
            retrievedAllocation = supplyLocationDbAccess.getById(supplyNotInDb, locationNotInDb);
        } catch (SQLException e) {
            fail("Error occurred while testing GetByIdNotInDb: " + e.getMessage());
        }

        assertNull("A null allocation should be returned for a non-existent allocation", retrievedAllocation);
    }

    @Test
    public void testAddEntryWithNullArguments() {
        Allocation nullAllocation = null;
        
        try {
            nullAllocation = supplyLocationDbAccess.addEntry(null, null, LocalDate.now());
        } catch (SQLException e) {
            fail("Error occurred while testing addEntryWithNullArguments: " + e.getMessage());
        }

        assertNull("addEntry() should return null when attempting to add with null arguments", nullAllocation);
    }
}
