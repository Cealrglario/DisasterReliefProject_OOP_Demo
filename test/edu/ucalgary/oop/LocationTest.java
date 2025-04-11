package edu.ucalgary.oop;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ArrayList;

public class LocationTest {
    Location testLocation;
    Person testPerson;
    Supply testSupply;
    Allocation testAllocation;

    @Before
    public void setUp() {
        testLocation = new Location(1, "Test Location", "Test address");
        testPerson = new Person(1, "Test 1", "Male", "111-1111");
        testSupply = new Blanket(1);
        testAllocation = new Allocation(testSupply, 1, 1, LocalDate.now());
        testLocation.setOccupants(new ArrayList<>());
        testLocation.setInventory(new ArrayList<>());
        testLocation.setAllocations(new LinkedHashSet<>());
    }

    @Test
    public void testLocationConstructor() {
        assertEquals("LOCATION_ID should be set as expected", 1, testLocation.getLocationId());
        assertEquals("name should be set as expected", "Test Location", testLocation.getName());
        assertEquals("address should be set as expected", "Test address", testLocation.getAddress());
    }

    @Test
    public void testGetLocationId() {
        assertEquals("LOCATION_ID should be retrieved as expected", 1, testLocation.getLocationId());
    }

    @Test
    public void testGetName() {
        assertEquals("name should be retrieved as expected", "Test Location", testLocation.getName());
    }

    @Test
    public void testSetName() {
        testLocation.setName("Updated Location");
        assertEquals("name should be updated as expected", "Updated Location", testLocation.getName());
    }

    @Test
    public void testGetAddress() {
        assertEquals("address should be retrieved as expected", "Test address", testLocation.getAddress());
    }

    @Test
    public void testSetAddress() {
        testLocation.setAddress("New address");
        assertEquals("address should be updated as expected", "New address", testLocation.getAddress());
    }

    @Test
    public void testGetOccupants() {
        assertNotNull("occupants should not be null", testLocation.getOccupants());
        assertTrue("occupants should be empty initially", testLocation.getOccupants().isEmpty());
    }

    @Test
    public void testSetOccupants() {
        List<Person> newOccupants = new ArrayList<>();
        newOccupants.add(testPerson);

        testLocation.setOccupants(newOccupants);

        assertEquals("occupants should be updated as expected", newOccupants, testLocation.getOccupants());
    }

    @Test
    public void testGetInventory() {
        assertNotNull("inventory should not be null", testLocation.getInventory());
        assertTrue("inventory should be empty initially", testLocation.getInventory().isEmpty());
    }

    @Test
    public void testSetInventory() {
        List<Supply> newInventory = new ArrayList<>();
        newInventory.add(testSupply);

        testLocation.setInventory(newInventory);

        assertEquals("inventory should be updated as expected", newInventory, testLocation.getInventory());
    }

    @Test
    public void testGetAllocations() {
        assertNotNull("allocations should not be null", testLocation.getAllocations());
        assertTrue("allocations should be empty initially", testLocation.getAllocations().isEmpty());
    }

    @Test
    public void testSetAllocations() {
        LinkedHashSet<Allocation> newAllocations = new LinkedHashSet<>();
        newAllocations.add(testAllocation);

        testLocation.setAllocations(newAllocations);

        assertEquals("allocations should be updated as expected", newAllocations, testLocation.getAllocations());
    }

    @Test
    public void testAddOccupant() {
        testLocation.addOccupant(testPerson);
        assertTrue("occupant should be added to the list", testLocation.getOccupants().contains(testPerson));
    }

    @Test
    public void testRemoveOccupant() {
        testLocation.addOccupant(testPerson);
        testLocation.removeOccupant(testPerson);
        assertFalse("occupant should be removed from the list", testLocation.getOccupants().contains(testPerson));
    }

    @Test
    public void testAddInventory() {
        testLocation.addInventory(testSupply);
        assertTrue("supply should be added to inventory", testLocation.getInventory().contains(testSupply));
    }

    @Test
    public void testRemoveInventory() {
        testLocation.addInventory(testSupply);
        testLocation.removeInventory(testSupply);
        assertFalse("supply should be removed from inventory", testLocation.getInventory().contains(testSupply));
    }

    @Test
    public void testAddAllocation() {
        testLocation.addAllocation(testAllocation);
        assertTrue("allocation should be added", testLocation.getAllocations().contains(testAllocation));
    }

    @Test
    public void testRemoveAllocation() {
        testLocation.addAllocation(testAllocation);
        testLocation.removeAllocation(testAllocation);
        assertFalse("allocation should be removed", testLocation.getAllocations().contains(testAllocation));
    }
}
