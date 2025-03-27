package edu.ucalgary.oop;

import java.time.LocalDateTime;

public class Allocation {
    private final Supply ALLOCATED_SUPPLY;
    private final int ALLOCATED_PERSON_ID;
    private final int LOCATION_ID;
    private final LocalDateTime TIME_ALLOCATED;

    public Allocation(Supply allocatedSupply, int allocatedPersonId, int locationId, LocalDateTime timeAllocated) {
        this.ALLOCATED_SUPPLY = allocatedSupply;
        this.ALLOCATED_PERSON_ID = allocatedPersonId;
        this.LOCATION_ID = locationId;
        this.TIME_ALLOCATED = timeAllocated;
    }

    public LocalDateTime getTimeAllocated() {
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
