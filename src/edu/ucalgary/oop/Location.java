package edu.ucalgary.oop;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

public class Location {
    private int LOCATION_ID;
    private String name;
    private String address;
    private List<Person> occupants;
    private LinkedHashSet<Allocation> supplyAllocations;

    public Location(int locationId, String name, String address) {
        this.LOCATION_ID = locationId;
        this.name = name;
        this.address = address;
        this.occupants = new ArrayList<>();
        this.supplyAllocations = new LinkedHashSet<>();
    }

    public int getLocationId() {
        return this.LOCATION_ID;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;

    }
    public List<Person> getOccupants() {
        return this.occupants;
    }

    public void setOccupants(List<Person> occupants) {
        this.occupants = occupants;
    }

    public LinkedHashSet<Allocation> getAllocations() {
        return this.supplyAllocations;
    }

    public void setAllocations (LinkedHashSet<Allocation> allocations) {
        this.supplyAllocations = allocations;
    }

    public void addOccupant(Person newOccupant) {
        this.occupants.add(newOccupant);
    }

    public void removeOccupant(Person exOccupant) {
        this.occupants.remove(exOccupant);
    }

    public void addAllocation(Allocation allocation) {
        this.supplyAllocations.add(allocation);
    }

    public void removeAllocation(Allocation allocation) {
        this.supplyAllocations.remove(allocation);
    }

}


