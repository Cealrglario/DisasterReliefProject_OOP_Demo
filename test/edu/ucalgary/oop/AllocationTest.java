package edu.ucalgary.oop;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.time.LocalDate;

public class AllocationTest {
    Allocation testAllocation;
    Supply testSupply;

    @Before
    public void setUp() {
        testSupply = new Blanket(1);
        testAllocation = new Allocation(testSupply, 1, 1,
                LocalDate.of(2025, 1, 1).atStartOfDay());
    }

    @Test
    public void testAllocationConstructor() {
        assertEquals("Allocated supply should be set as expected", testSupply, testAllocation.getAllocatedSupply());
        assertEquals("allocatedPersonId should be set as expected", 1, testAllocation.getAllocatedPersonId());
        assertEquals("locationId should be set as expected", 1, testAllocation.getLocationId());
        assertEquals("TIME_ALLOCATED should be set as expected", LocalDate.of(2025, 1, 1).atStartOfDay(),
                testAllocation.getTimeAllocated());
    }

    @Test
    public void testGetTimeAllocated() {
        assertEquals("TIME_ALLOCATED should be retrieved as expected", LocalDate.of(2025, 1, 1).atStartOfDay(),
                testAllocation.getTimeAllocated());
    }

    @Test
    public void testGetLocationId() {
        assertEquals("locationId should be retrieved as expected", 1, testAllocation.getLocationId());
    }

    @Test
    public void testGetAllocatedPersonId() {
        assertEquals("allocationPersonId should be retrieved as expected", 1, testAllocation.getAllocatedPersonId());
    }

    @Test
    public void testGetAllocatedSupply() {
        assertEquals("allocatedSupply should be retrieved as expected", testSupply, testAllocation.getAllocatedSupply());
    }
}