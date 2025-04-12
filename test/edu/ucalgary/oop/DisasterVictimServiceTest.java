package edu.ucalgary.oop;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.sql.SQLException;
import java.time.LocalDate;

public class DisasterVictimServiceTest {
    private DisasterVictimService disasterVictimService;
    private PersonAccess personAccess;
    private VictimMedicalRecordAccess victimMedicalRecordAccess;
    private SupplyPersonAllocationAccess supplyPersonAllocationAccess;
    private SupplyService supplyService;

    @Before
    public void setUp() {
        disasterVictimService = DisasterVictimService.INSTANCE;
        personAccess = new PersonAccess<>();
        victimMedicalRecordAccess = new VictimMedicalRecordAccess<>();
        supplyPersonAllocationAccess = new SupplyPersonAllocationAccess();
        supplyService = SupplyService.INSTANCE;
    }

    @Test
    public void testGetDisasterVictimById() {
        DisasterVictim testVictim = null;
        LocalDate entryDate = LocalDate.now();

        try {
            testVictim = disasterVictimService.getDisasterVictimById(1, entryDate);
        } catch (SQLException e) {
            fail("Error testing getDisasterVictimById: " + e.getMessage());
        }

        assertNotNull("getDisasterVictimById() should retrieve a valid DisasterVictim", testVictim);
        assertEquals("getDisasterVictimById() should retrieve the correct Person", 1, testVictim.getAssignedId());
        assertEquals("getDisasterVictimById() should set the entry date correctly", entryDate, testVictim.getEntryDate());
    }

    @Test
    public void testRefreshSupplies() {
        DisasterVictim testVictim = null;
        Supply testSupply = null;
        LocalDate entryDate = LocalDate.now();
        LocalDate allocationDate = LocalDate.now();
        int initialSupplyCount = 0;
        int updatedSupplyCount = 0;

        try {
            testVictim = disasterVictimService.getDisasterVictimById(1, entryDate);
            initialSupplyCount = testVictim.getSupplies().size();

            testSupply = supplyService.getSupplyById(5);
            supplyPersonAllocationAccess.addEntry(testSupply, testVictim, allocationDate);

            disasterVictimService.refreshSupplies(testVictim);
            updatedSupplyCount = testVictim.getSupplies().size();
        } catch (SQLException e) {
            fail("Error testing refreshSupplies: " + e.getMessage());
        }

        assertTrue("refreshSupplies() should update the victim's supplies list", updatedSupplyCount > initialSupplyCount);

        try {
            supplyPersonAllocationAccess.removeEntry(testSupply, testVictim, allocationDate);
        } catch (SQLException e) {
            fail("Error testing refreshSupplies: " + e.getMessage());
        }
    }

    @Test
    public void testAddSupplyAllocation() {
        DisasterVictim testVictim = null;
        Supply testSupply = null;
        LocalDate entryDate = LocalDate.now();
        LocalDate allocationDate = LocalDate.now();
        int initialSupplyCount = 0;
        int updatedSupplyCount = 0;

        try {
            testVictim = disasterVictimService.getDisasterVictimById(2, entryDate);
            testSupply = supplyService.getSupplyById(6);

            initialSupplyCount = testVictim.getSupplies().size();
            disasterVictimService.addSupplyAllocation(testVictim, testSupply, allocationDate);
            updatedSupplyCount = testVictim.getSupplies().size();
        } catch (SQLException e) {
            fail("Error testing addSupplyAllocation: " + e.getMessage());
        }

        assertEquals("addSupplyAllocation() should add the supply to the victim's supplies list",
                initialSupplyCount + 1, updatedSupplyCount);

        try {
            disasterVictimService.removeSupplyAllocation(testVictim, testSupply, allocationDate);
        } catch (SQLException e) {
            fail("Error testing addSupplyAllocation: " + e.getMessage());
        }
    }

    @Test
    public void testRemoveSupplyAllocation() {
        DisasterVictim testVictim = null;
        Supply testSupply = null;
        LocalDate entryDate = LocalDate.now();
        LocalDate allocationDate = LocalDate.now();
        int supplyCountAfterAdd = 0;
        int supplyCountAfterRemove = 0;

        try {
            testVictim = disasterVictimService.getDisasterVictimById(3, entryDate);
            testSupply = supplyService.getSupplyById(7);

            disasterVictimService.addSupplyAllocation(testVictim, testSupply, allocationDate);
            supplyCountAfterAdd = testVictim.getSupplies().size();

            disasterVictimService.removeSupplyAllocation(testVictim, testSupply, allocationDate);
            supplyCountAfterRemove = testVictim.getSupplies().size();
        } catch (SQLException e) {
            fail("Error testing removeSupplyAllocation: " + e.getMessage());
        }

        assertEquals("removeSupplyAllocation() should remove the supply from the victim's supplies list",
                supplyCountAfterAdd - 1, supplyCountAfterRemove);
    }

    @Test
    public void testRefreshMedicalRecords() {
        PersonLocationAccess personLocationAccess = new PersonLocationAccess();
        Location testLocation = null;
        DisasterVictim testVictim = null;
        LocalDate entryDate = LocalDate.now();
        MedicalRecord testRecord = null;
        int recordCount = 0;

        try {
            testVictim = disasterVictimService.getDisasterVictimById(1, entryDate);
            testLocation = disasterVictimService.getPersonLocation(testVictim);

            testRecord = victimMedicalRecordAccess.addMedicalRecordToPerson(
                    testVictim.getAssignedId(), testLocation.getLocationId(), "Test treatment details");

            disasterVictimService.refreshMedicalRecords(testVictim);
            recordCount = testVictim.getMedicalRecords().size();
        } catch (SQLException e) {
            fail("Error testing refreshMedicalRecords: " + e.getMessage());
        }

        assertTrue("refreshMedicalRecords() should update the victim's medical records list",
                recordCount > 0);

        try {
            disasterVictimService.removeMedicalRecord(testVictim, testRecord);
        } catch (SQLException e) {
            fail("Error testing refreshMedicalRecords: " + e.getMessage());
        }

    }
}