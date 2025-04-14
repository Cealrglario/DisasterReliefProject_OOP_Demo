package edu.ucalgary.oop;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class LocationServiceTest {
    private LocationService locationService;
    private LocationAccess<Object> locationAccess;

    @Before
    public void setUp() {
        locationService = LocationService.INSTANCE;
        locationAccess = new LocationAccess<>();
    }

    @Test
    public void testGetLocationWithOccupants() {
        PersonLocationAccess personLocationAccess = new PersonLocationAccess();
        Location testLocation;
        List<Person> occupantsInDb = null;
        List<Person> occupants = null;

        try {
            testLocation = locationService.getLocationWithOccupants(1);
            occupantsInDb = personLocationAccess.getOccupantsOfLocation(testLocation);
            occupants = testLocation.getOccupants();
        } catch (SQLException e) {
            fail("Error testing getLocationWithOccupants: " + e.getMessage());
        }

        assertEquals("getLocationWithOccupants() should populate the in-memory Location with occupants from the database",
                occupants.size(), occupantsInDb.size());
    }

    @Test
    public void testGetAllLocationsWithOccupants() {
        List<Location> retrievedLocations = null;

        try {
            retrievedLocations = locationService.getAllLocationsWithOccupants();
        } catch (SQLException e) {
            fail("Error testing getAllLocations: " + e.getMessage());
        }

        for (int i = 0; i < retrievedLocations.size(); i++) {
            assertNotEquals("getAllLocations() should retrieve a valid list of Locations with occupants", 0,
                    retrievedLocations.get(i).getOccupants().size());
        }
    }

    @Test
    public void testUpdateLocationName() {
        Location testLocation = null;
        String retrievedName = null;

        try {
            testLocation = locationAccess.getById(1);
            locationService.updateLocationName(testLocation, "New name");
            retrievedName = (String) locationAccess.getInfo("name", 1);
        } catch (SQLException e) {
            fail("Error testing updateLocationName: " + e.getMessage());
        }

        assertEquals("updateLocationName() should update the name in-memory and in database",
                testLocation.getName(), retrievedName);
    }

    @Test
    public void testUpdateLocationAddress() {
        Location testLocation = null;
        String retrievedAddress = null;

        try {
            testLocation = locationAccess.getById(1);
            locationService.updateLocationAddress(testLocation, "New address");
            retrievedAddress = (String) locationAccess.getInfo("address", 1);
        } catch (SQLException e) {
            fail("Error testing updateLocationName: " + e.getMessage());
        }

        assertEquals("updateLocationName() should update the address in-memory and in database",
                testLocation.getAddress(), retrievedAddress);
    }

    @Test
    public void testAddOccupant() {
        PersonLocationAccess personLocationAccess = new PersonLocationAccess();
        PersonAccess<Object> personAccess = new PersonAccess<>();
        Location testLocation = null;
        Person testPerson = null;
        List<Person> retrievedOccupants = null;

        try {
            testLocation = locationService.getLocationWithOccupants(1);
            testPerson = personAccess.addPerson("Test person", "Male", null, "111-1111");
            locationService.addOccupant(testLocation, testPerson);
            retrievedOccupants = personLocationAccess.getOccupantsOfLocation(testLocation);
        } catch (SQLException e) {
            fail("Error testing addOccupant: " + e.getMessage());
        }

        assertEquals("addOccupant() should add occupant in-memory and in database", retrievedOccupants.size(),
                testLocation.getOccupants().size());

        try {
            locationService.removeOccupant(testLocation, testPerson);
        } catch (SQLException e) {
            fail("Error testing addOccupant: " + e.getMessage());
        }
    }

    @Test
    public void testRemoveOccupant() {
        PersonLocationAccess personLocationAccess = new PersonLocationAccess();
        PersonAccess<Object> personAccess = new PersonAccess<>();
        Location testLocation = null;
        Person testPerson = null;
        List<Person> retrievedOccupants = null;
        List<Person> afterRemoving = null;

        try {
            testLocation = locationService.getLocationWithOccupants(1);
            testPerson = personAccess.addPerson("Test person", "Male", null, "111-1111");
            locationService.addOccupant(testLocation, testPerson);
            retrievedOccupants = personLocationAccess.getOccupantsOfLocation(testLocation);
            locationService.removeOccupant(testLocation, testPerson);
            afterRemoving = personLocationAccess.getOccupantsOfLocation(testLocation);
        } catch (SQLException e) {
            fail("Error testing addOccupant: " + e.getMessage());
        }

        assertNotEquals("removeOccupant() should remove occupant in-memory and in database", afterRemoving.size(),
                retrievedOccupants.size());
    }

    @Test
    public void testRefreshOccupants() {
        PersonLocationAccess personLocationAccess = new PersonLocationAccess();
        PersonAccess<Object> personAccess = new PersonAccess<>();
        Location testLocation = null;
        Person testPerson = null;
        List<Person> retrievedOccupants = null;

        try {
            testLocation = locationService.getLocationWithOccupants(1);
            testPerson = personAccess.addPerson("Test person", "Male", null, "111-1111");
            personLocationAccess.addEntry(testPerson, testLocation);

            retrievedOccupants = personLocationAccess.getOccupantsOfLocation(testLocation);

            locationService.refreshOccupants(testLocation);
        } catch (SQLException e) {
            fail("Error testing addOccupant: " + e.getMessage());
        }

        assertEquals("refreshOccupants() should refresh occupants in-memory based on database", retrievedOccupants.size(),
                testLocation.getOccupants().size());
    }

    @Test
    public void testRefreshAllocations() {
        SupplyLocationAllocationAccess supplyLocationAllocationAccess = new SupplyLocationAllocationAccess();
        SupplyService supplyService = SupplyService.INSTANCE;
        Location testLocation = null;
        Supply testSupply = null;
        int initialAllocationCount = 0;
        int updatedAllocationCount = 0;
        LocalDate testDate = LocalDate.now();

        try {
            testLocation = locationService.getLocation(1);
            testSupply = supplyService.getSupplyById(1);

            locationService.refreshAllocations(testLocation);
            initialAllocationCount = testLocation.getAllocations().size();

            supplyLocationAllocationAccess.addEntry(testSupply, testLocation, testDate);

            locationService.refreshAllocations(testLocation);
            updatedAllocationCount = testLocation.getAllocations().size();
        } catch (SQLException e) {
            fail("Error testing refreshAllocations: " + e.getMessage());
        }

        assertTrue("refreshAllocations() should update the in-memory allocations list from the database",
                initialAllocationCount < updatedAllocationCount);

        try {
            locationService.removeSupplyAllocation(testLocation, testSupply);
        } catch (SQLException e) {
            fail("Error testing refreshAllocations: " + e.getMessage());
        }
    }

    @Test
    public void testAddSupplyAllocation() {
        SupplyLocationAllocationAccess supplyLocationAllocationAccess = new SupplyLocationAllocationAccess();
        SupplyService supplyService = SupplyService.INSTANCE;
        Location testLocation = null;
        Supply testSupply = null;
        int memoryAllocationCount = 0;
        int dbAllocationCount = 0;
        LocalDate testDate = LocalDate.now();

        try {
            testLocation = locationService.getLocation(1);
            testSupply = supplyService.getSupplyById(1);

            locationService.refreshAllocations(testLocation);
            locationService.addSupplyAllocation(testLocation, testSupply, testDate);

            memoryAllocationCount = testLocation.getAllocations().size();
            dbAllocationCount = supplyLocationAllocationAccess.getSuppliesAtLocation(testLocation).size();
        } catch (SQLException e) {
            fail("Error occurred while testing addAllocation: " + e.getMessage());
        }

        assertEquals("addSupplyAllocations() should add exactly one allocation to the database and in-memory",
                memoryAllocationCount, dbAllocationCount);

        try {
            locationService.removeSupplyAllocation(testLocation, testSupply);
        } catch (SQLException e) {
            fail("Error occurred while testing addAllocation: " + e.getMessage());
        }
    }

    @Test
    public void testRemoveSupplyAllocation() {
        SupplyService supplyService = SupplyService.INSTANCE;
        Location testLocation = null;
        Supply testSupply = null;
        LocalDate testDate = LocalDate.now();
        boolean removeResult = false;

        try {
            testLocation = locationService.getLocation(1);
            testSupply = supplyService.getSupplyById(3);

            locationService.addSupplyAllocation(testLocation, testSupply, testDate);
            removeResult = locationService.removeSupplyAllocation(testLocation, testSupply);
        } catch (SQLException e) {
            fail("Error testing removeSupplyAllocation: " + e.getMessage());
        }

        assertTrue("removeSupplyAllocation() should return true when successful", removeResult);
    }

}