package edu.ucalgary.oop;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public enum DisasterVictimService {
    INSTANCE;

    private final PersonAccess<Object> personAccess = new PersonAccess<>();
    private final SupplyPersonAllocationAccess supplyPersonAllocationAccess = new SupplyPersonAllocationAccess();
    private final LocationService locationService = LocationService.INSTANCE;
    private final VictimMedicalRecordAccess<Object> victimMedicalRecordAccess = new VictimMedicalRecordAccess<>();
    private final LanguageManager languageManager = LanguageManager.INSTANCE;

    public DisasterVictim getDisasterVictimById(int personId) throws SQLException {
        Person person = personAccess.getById(personId);
        DisasterVictim victim = null;

        if (person == null) {
            return null;
        }

        if (person.getDateOfBirth() != null) {
            victim = new DisasterVictim(person.getAssignedId(), person.getFirstName(), person.getGender(), person.getDateOfBirth(),
                    person.getPhoneNumber()
            );
        } else {
            victim = new DisasterVictim(person.getAssignedId(), person.getFirstName(), person.getGender(), person.getPhoneNumber());
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
        Location personLocation = getPersonLocation(victim);
        locationService.refreshAllocations(personLocation);

        boolean success = locationService.removeSupplyAllocation(personLocation, supply);

        if (allocation != null && success) {
            victim.addSupply(supply);
            return true;
        } else if (allocation == null) {
            System.out.println(languageManager.getTranslation("supply_allocation_failed_null"));
            return false;
        } else if (!success) {
            System.out.println(languageManager.getTranslation("supply_allocation_failed_removal"));
            return false;
        } else {
            System.out.println(languageManager.getTranslation("supply_allocation_unknown_error"));
            return false;
        }
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
            System.out.println(languageManager.getTranslation("failed_retrieve_location_id") + e.getMessage());
            return null;
        }

        return retrievedLocation;
    }
}

