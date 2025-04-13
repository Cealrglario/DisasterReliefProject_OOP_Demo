package edu.ucalgary.oop;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public enum DisasterVictimService {
    INSTANCE;

    private final PersonAccess<Object> personAccess = new PersonAccess<>();
    private final SupplyPersonAllocationAccess supplyPersonAllocationAccess = new SupplyPersonAllocationAccess();
    private final SupplyLocationAllocationAccess supplyLocationAllocationAccess = new SupplyLocationAllocationAccess();
    private final VictimMedicalRecordAccess<Object> victimMedicalRecordAccess = new VictimMedicalRecordAccess<>();

    public DisasterVictim getDisasterVictimById(int personId, LocalDate entryDate) throws SQLException {
        Person person = personAccess.getById(personId);
        DisasterVictim victim = null;

        if (person == null) {
            return null;
        }

        if (person.getDateOfBirth() != null) {
            victim = new DisasterVictim(person.getAssignedId(), person.getFirstName(), person.getGender(), person.getDateOfBirth(),
                    entryDate, person.getPhoneNumber()
            );
        } else {
            victim = new DisasterVictim(person.getAssignedId(), person.getFirstName(), person.getGender(), entryDate,
                    person.getPhoneNumber());
        }

        victim.setLastName(person.getLastName());
        victim.setInFamilyGroup(person.getInFamilyGroup());

        refreshSupplies(victim);

        return victim;
    }


    public void refreshSupplies(DisasterVictim victim) throws SQLException {
        List<Supply> supplies = supplyPersonAllocationAccess.getPersonSupplies(victim);
        victim.setSupplies(supplies);
    }


    public boolean addSupplyAllocation(DisasterVictim victim, Supply supply, LocalDate allocationDate) throws SQLException {
        Allocation allocation = supplyPersonAllocationAccess.addEntry(supply, victim, allocationDate);
        Allocation allocationToRemove = supplyLocationAllocationAccess.getById(supply, getPersonLocation(victim));

        if (allocationToRemove != null) {
            supplyLocationAllocationAccess.removeEntry(allocationToRemove);
        }

        if (allocation != null) {
            victim.addSupply(supply);
            return true;
        }

        return false;
    }


    public boolean removeSupplyAllocation(DisasterVictim victim, Supply supply, LocalDate allocationDate) throws SQLException {
        boolean success = supplyPersonAllocationAccess.removeEntry(supply, victim, allocationDate);

        if (success && victim.getSupplies() != null) {
            victim.removeSupply(supply);
            return true;
        }

        return false;
    }

    public void refreshMedicalRecords(DisasterVictim victim) throws SQLException {
        List<MedicalRecord> medicalRecords = victimMedicalRecordAccess.getMedicalRecordsForPerson(victim.getAssignedId());
        victim.setMedicalRecords(medicalRecords);
    }


    public MedicalRecord addMedicalRecord(DisasterVictim victim, int locationId, String treatmentDetails) throws SQLException {
        MedicalRecord newRecord = victimMedicalRecordAccess.addMedicalRecordToPerson(victim.getAssignedId(), locationId, treatmentDetails);

        if (newRecord != null) {
            victim.addMedicalRecord(newRecord);
        }

        return newRecord;
    }


    public boolean removeMedicalRecord(DisasterVictim victim, MedicalRecord medicalRecord) throws SQLException {
        boolean success = victimMedicalRecordAccess.removeMedicalRecordFromPerson(
                victim.getAssignedId(), medicalRecord.getMedicalRecordId());

        if (success) {
            victim.removeMedicalRecord(medicalRecord);
            return true;
        }

        return false;
    }


    public void refreshDisasterVictim(DisasterVictim victim) throws SQLException {
        refreshSupplies(victim);
        refreshMedicalRecords(victim);
    }


    public Location getPersonLocation(Person person) {
        PersonLocationAccess personLocationAccess = new PersonLocationAccess();
        LocationAccess<Object> locationAccess = new LocationAccess<>();
        Location retrievedLocation;

        try {
            Map<Integer, Integer> retrievedPersonsInLocations = personLocationAccess.getAll();
            int retrievedLocationId = retrievedPersonsInLocations.get(person.getAssignedId());

            retrievedLocation = locationAccess.getById(retrievedLocationId);
        } catch (SQLException e) {
            System.out.println("Failed to retrieve person's location ID: " + e.getMessage());
            return null;
        }

        return retrievedLocation;
    }
}

