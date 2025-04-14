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
    private LocationService locationService;

    @Before
    public void setUp() {
        disasterVictimService = DisasterVictimService.INSTANCE;
        personAccess = new PersonAccess<>();
        victimMedicalRecordAccess = new VictimMedicalRecordAccess<>();
        supplyPersonAllocationAccess = new SupplyPersonAllocationAccess();
        supplyService = SupplyService.INSTANCE;
        locationService = LocationService.INSTANCE;
    }

    @Test
    public void testGetDisasterVictimByIdReturnsValidVictim() {
        
        DisasterVictim testVictim = null;

        
        try {
            testVictim = disasterVictimService.getDisasterVictimById(1);
        } catch (SQLException e) {
            fail("Error testing getDisasterVictimById: " + e.getMessage());
        }

        
        assertNotNull("getDisasterVictimById() should retrieve a valid DisasterVictim", testVictim);
    }

    @Test
    public void testGetDisasterVictimByIdReturnsCorrectId() {
        
        DisasterVictim testVictim = null;

        
        try {
            testVictim = disasterVictimService.getDisasterVictimById(1);
        } catch (SQLException e) {
            fail("Error testing getDisasterVictimById: " + e.getMessage());
        }

        
        assertEquals("getDisasterVictimById() should retrieve the correct Person", 1, testVictim.getAssignedId());
    }

    @Test
    public void testRefreshSuppliesUpdatesVictimsSuppliesList() {
        
        DisasterVictim testVictim = null;
        Supply testSupply = null;
        LocalDate allocationDate = LocalDate.now();
        int initialSupplyCount = 0;
        int updatedSupplyCount = 0;

        
        try {
            testVictim = disasterVictimService.getDisasterVictimById(1);
            initialSupplyCount = testVictim.getSupplies().size();
            testSupply = supplyService.getSupplyById(5);
            supplyPersonAllocationAccess.addEntry(testSupply, testVictim, allocationDate);
            disasterVictimService.refreshSupplies(testVictim);
            updatedSupplyCount = testVictim.getSupplies().size();

            
            supplyPersonAllocationAccess.removeEntry(testSupply, testVictim, allocationDate);
        } catch (SQLException e) {
            fail("Error testing refreshSupplies: " + e.getMessage());
        }

        
        assertTrue("refreshSupplies() should update the victim's supplies list", updatedSupplyCount > initialSupplyCount);
    }

    @Test
    public void testAddSupplyAllocationAddsSupplyToVictimsList() {
        
        DisasterVictim testVictim = null;
        Supply testSupply = null;
        LocalDate allocationDate = LocalDate.now();
        int initialSupplyCount = 0;
        int updatedSupplyCount = 0;

        
        try {
            testVictim = disasterVictimService.getDisasterVictimById(2);
            testSupply = supplyService.getSupplyById(6);
            initialSupplyCount = testVictim.getSupplies().size();
            disasterVictimService.addSupplyAllocation(testVictim, testSupply, allocationDate);
            updatedSupplyCount = testVictim.getSupplies().size();

            
            disasterVictimService.removeSupplyAllocation(testVictim, testSupply, allocationDate);
        } catch (SQLException e) {
            fail("Error testing addSupplyAllocation: " + e.getMessage());
        }

        
        assertEquals("addSupplyAllocation() should add the supply to the victim's supplies list",
                initialSupplyCount + 1, updatedSupplyCount);
    }

    @Test
    public void testAddSupplyAllocationReturnsTrueOnSuccess() {
        
        DisasterVictim testVictim = null;
        Supply testSupply = null;
        LocalDate allocationDate = LocalDate.now();
        boolean success = false;

        
        try {
            testVictim = disasterVictimService.getDisasterVictimById(2);
            testSupply = supplyService.getSupplyById(6);
            success = disasterVictimService.addSupplyAllocation(testVictim, testSupply, allocationDate);

            
            if (success) {
                disasterVictimService.removeSupplyAllocation(testVictim, testSupply, allocationDate);
            }
        } catch (SQLException e) {
            fail("Error testing addSupplyAllocation: " + e.getMessage());
        }

        
        assertTrue("addSupplyAllocation() should return true on successful allocation", success);
    }

    @Test
    public void testRemoveSupplyAllocationRemovesSupplyFromVictimsList() {
        
        DisasterVictim testVictim = null;
        Supply testSupply = null;
        LocalDate allocationDate = LocalDate.now();
        int supplyCountAfterAdd = 0;
        int supplyCountAfterRemove = 0;

        
        try {
            testVictim = disasterVictimService.getDisasterVictimById(3);
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
    public void testRemoveSupplyAllocationReturnsTrueOnSuccess() {
        
        DisasterVictim testVictim = null;
        Supply testSupply = null;
        LocalDate allocationDate = LocalDate.now();
        boolean success = false;

        
        try {
            testVictim = disasterVictimService.getDisasterVictimById(3);
            testSupply = supplyService.getSupplyById(7);
            disasterVictimService.addSupplyAllocation(testVictim, testSupply, allocationDate);
            success = disasterVictimService.removeSupplyAllocation(testVictim, testSupply, allocationDate);
        } catch (SQLException e) {
            fail("Error testing removeSupplyAllocation: " + e.getMessage());
        }

        
        assertTrue("removeSupplyAllocation() should return true on successful removal", success);
    }

    @Test
    public void testRefreshMedicalRecordsUpdatesVictimsMedicalRecordsList() {
        
        Location testLocation = null;
        DisasterVictim testVictim = null;
        MedicalRecord testRecord = null;
        int recordCount = 0;

        
        try {
            testVictim = disasterVictimService.getDisasterVictimById(1);
            testLocation = disasterVictimService.getPersonLocation(testVictim);
            testRecord = victimMedicalRecordAccess.addMedicalRecordToPerson(
                    testVictim.getAssignedId(), testLocation.getLocationId(), "Test treatment details");
            disasterVictimService.refreshMedicalRecords(testVictim);
            recordCount = testVictim.getMedicalRecords().size();

            
            disasterVictimService.removeMedicalRecord(testVictim, testRecord);
        } catch (SQLException e) {
            fail("Error testing refreshMedicalRecords: " + e.getMessage());
        }

        
        assertTrue("refreshMedicalRecords() should update the victim's medical records list", recordCount > 0);
    }

    @Test
    public void testAddMedicalRecordAddsRecordToVictimsList() {
        
        DisasterVictim testVictim = null;
        Location testLocation = null;
        MedicalRecord testRecord = null;
        int initialRecordCount = 0;
        int updatedRecordCount = 0;

        
        try {
            testVictim = disasterVictimService.getDisasterVictimById(1);
            testLocation = disasterVictimService.getPersonLocation(testVictim);
            initialRecordCount = testVictim.getMedicalRecords().size();
            testRecord = disasterVictimService.addMedicalRecord(testVictim, testLocation.getLocationId(), "Test treatment details");
            updatedRecordCount = testVictim.getMedicalRecords().size();

            
            disasterVictimService.removeMedicalRecord(testVictim, testRecord);
        } catch (SQLException e) {
            fail("Error testing addMedicalRecord: " + e.getMessage());
        }

        
        assertEquals("addMedicalRecord() should add a record to the victim's medical records list",
                initialRecordCount + 1, updatedRecordCount);
    }

    @Test
    public void testAddMedicalRecordReturnsNonNullRecord() {
        
        DisasterVictim testVictim = null;
        Location testLocation = null;
        MedicalRecord testRecord = null;

        
        try {
            testVictim = disasterVictimService.getDisasterVictimById(1);
            testLocation = disasterVictimService.getPersonLocation(testVictim);
            testRecord = disasterVictimService.addMedicalRecord(testVictim, testLocation.getLocationId(), "Test treatment details");

            
            disasterVictimService.removeMedicalRecord(testVictim, testRecord);
        } catch (SQLException e) {
            fail("Error testing addMedicalRecord: " + e.getMessage());
        }

        
        assertNotNull("addMedicalRecord() should return a non-null medical record", testRecord);
    }

    @Test
    public void testRemoveMedicalRecordRemovesRecordFromVictimsList() {
        
        DisasterVictim testVictim = null;
        Location testLocation = null;
        MedicalRecord testRecord = null;
        int recordCountAfterAdd = 0;
        int recordCountAfterRemove = 0;

        
        try {
            testVictim = disasterVictimService.getDisasterVictimById(1);
            testLocation = disasterVictimService.getPersonLocation(testVictim);
            testRecord = disasterVictimService.addMedicalRecord(testVictim, testLocation.getLocationId(), "Test treatment details");
            recordCountAfterAdd = testVictim.getMedicalRecords().size();
            disasterVictimService.removeMedicalRecord(testVictim, testRecord);
            recordCountAfterRemove = testVictim.getMedicalRecords().size();
        } catch (SQLException e) {
            fail("Error testing removeMedicalRecord: " + e.getMessage());
        }

        
        assertEquals("removeMedicalRecord() should remove a record from the victim's medical records list",
                recordCountAfterAdd - 1, recordCountAfterRemove);
    }

    @Test
    public void testRemoveMedicalRecordReturnsTrueOnSuccess() {
        
        DisasterVictim testVictim = null;
        Location testLocation = null;
        MedicalRecord testRecord = null;
        boolean success = false;

        
        try {
            testVictim = disasterVictimService.getDisasterVictimById(1);
            testLocation = disasterVictimService.getPersonLocation(testVictim);
            testRecord = disasterVictimService.addMedicalRecord(testVictim, testLocation.getLocationId(), "Test treatment details");
            success = disasterVictimService.removeMedicalRecord(testVictim, testRecord);
        } catch (SQLException e) {
            fail("Error testing removeMedicalRecord: " + e.getMessage());
        }

        
        assertTrue("removeMedicalRecord() should return true on successful removal", success);
    }

    @Test
    public void testRefreshDisasterVictimUpdatesSupplies() {
        
        DisasterVictim testVictim = null;
        Supply testSupply = null;
        LocalDate allocationDate = LocalDate.now();
        int initialSupplyCount = 0;
        int updatedSupplyCount = 0;

        
        try {
            testVictim = disasterVictimService.getDisasterVictimById(1);
            testSupply = supplyService.getSupplyById(5);
            initialSupplyCount = testVictim.getSupplies().size();
            supplyPersonAllocationAccess.addEntry(testSupply, testVictim, allocationDate);
            disasterVictimService.refreshDisasterVictim(testVictim);
            updatedSupplyCount = testVictim.getSupplies().size();

            
            supplyPersonAllocationAccess.removeEntry(testSupply, testVictim, allocationDate);
        } catch (SQLException e) {
            fail("Error testing refreshDisasterVictim: " + e.getMessage());
        }

        
        assertTrue("refreshDisasterVictim() should update the victim's supplies list", updatedSupplyCount > initialSupplyCount);
    }

    @Test
    public void testRefreshDisasterVictimUpdatesMedicalRecords() {
        
        DisasterVictim testVictim = null;
        Location testLocation = null;
        MedicalRecord testRecord = null;
        int initialRecordCount = 0;
        int updatedRecordCount = 0;

        
        try {
            testVictim = disasterVictimService.getDisasterVictimById(1);
            testLocation = disasterVictimService.getPersonLocation(testVictim);
            initialRecordCount = testVictim.getMedicalRecords().size();
            testRecord = victimMedicalRecordAccess.addMedicalRecordToPerson(
                    testVictim.getAssignedId(), testLocation.getLocationId(), "Test treatment details");
            disasterVictimService.refreshDisasterVictim(testVictim);
            updatedRecordCount = testVictim.getMedicalRecords().size();

            
            disasterVictimService.removeMedicalRecord(testVictim, testRecord);
        } catch (SQLException e) {
            fail("Error testing refreshDisasterVictim: " + e.getMessage());
        }

        
        assertTrue("refreshDisasterVictim() should update the victim's medical records list", updatedRecordCount > initialRecordCount);
    }

    @Test
    public void testGetPersonLocationReturnsNonNullLocation() {
        
        DisasterVictim testVictim = null;
        Location testLocation = null;

        
        try {
            testVictim = disasterVictimService.getDisasterVictimById(1);
            testLocation = disasterVictimService.getPersonLocation(testVictim);
        } catch (SQLException e) {
            fail("Error testing getPersonLocation: " + e.getMessage());
        }

        
        assertNotNull("getPersonLocation() should retrieve a valid Location", testLocation);
    }
}
