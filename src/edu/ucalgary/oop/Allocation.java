package edu.ucalgary.oop;

import java.time.LocalDate;

public class Allocation {
    private final Supply ALLOCATED_SUPPLY;
    private final Integer ALLOCATED_PERSON_ID;
    private final Integer LOCATION_ID;
    private final LocalDate TIME_ALLOCATED;

    public Allocation(Supply allocatedSupply, Integer allocatedPersonId, Integer locationId, LocalDate timeAllocated) {
        this.ALLOCATED_SUPPLY = allocatedSupply;
        this.ALLOCATED_PERSON_ID = allocatedPersonId;
        this.LOCATION_ID = locationId;
        this.TIME_ALLOCATED = timeAllocated;
    }

    public LocalDate getTimeAllocated() {
        return TIME_ALLOCATED;
    }

    public int getLocationId() {
        return LOCATION_ID;
    }

    public int getAllocatedPersonId() {
        return ALLOCATED_PERSON_ID;
    }

    public Supply getAllocatedSupply() {
        return ALLOCATED_SUPPLY;
    }
}
