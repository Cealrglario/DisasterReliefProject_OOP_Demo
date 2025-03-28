package edu.ucalgary.oop;

import java.util.LinkedHashSet;
import java.util.List;

public class Location {
    private int LOCATION_ID;
    private String name;
    private String address;
    private List<Person> occupants;
    private List<Supply> inventory;
    private LinkedHashSet<Allocation> supplyAllocations;

    public Location(int locationId, String name, String address) {
        this.LOCATION_ID = locationId;
    }

    public int getLocationId() {
        return this.LOCATION_ID;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {}

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {}

    public List<Person> getOccupants() {
        return this.occupants;
    }

    public void setOccupants(List<Person> occupants) {}

    public List<Supply> getInventory() {
        return this.inventory;
    }

    public void setInventory(List<Supply> inventory) {}

    public LinkedHashSet<Allocation> getAllocations() {
        return this.supplyAllocations;
    }

    public void setAllocations (LinkedHashSet<Allocation> allocations) {}

    public void addOccupant(Person newOccupant) {}

    public void removeOccupant(Person exOccupant) {}

    public void addInventory(Supply newInventory) {}

    public void removeInventory(Supply expiredInventory) {}

    public void addAllocation(Allocation allocation) {}

    public void removeAllocation(Allocation allocation) {}

}


