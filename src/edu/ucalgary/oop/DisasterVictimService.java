package edu.ucalgary.oop;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DisasterVictimService {
    private PersonAccess<Object> personAccess = new PersonAccess<>();
    private SupplyPersonAllocationAccess supplyPersonAllocationAccess = new SupplyPersonAllocationAccess();

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

        if (victim.getSupplies() == null) {
            victim.setSupplies(new ArrayList<>());
        } else {
            victim.setSupplies(supplies);
        }
    }


    public boolean addSupplyAllocation(DisasterVictim victim, Supply supply, LocalDate allocationDate) throws SQLException {
        Allocation allocation = supplyPersonAllocationAccess.addEntry(supply, victim, allocationDate);

        if (allocation != null) {
            if (victim.getSupplies() == null) {
                victim.setSupplies(new ArrayList<>());
            }
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
}
