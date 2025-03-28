package edu.ucalgary.oop;

public class LocationSubmenu extends Menu {
    private String[] MANAGE_OCCUPANTS_OPTIONS = new String[0];
    private String[] MANAGE_ALLOCATIONS_OPTIONS = new String[0];

    public LocationSubmenu(String[] defaultOptions) {
        super(defaultOptions);
    }

    public String[] getManageOccupantsOptions() {
        return this.MANAGE_OCCUPANTS_OPTIONS;
    }

    public String[] getManageAllocationsOptions() {
        return this.MANAGE_ALLOCATIONS_OPTIONS;
    }

    public void listAllLocations() {}

    public void updateLocationName() {}

    public void updateLocationAddress() {}

    public void viewLocationDetails() {}

    public void manageOccupants() {}

    public void manageAllocations() {}

    public void viewAllocations() {}
}
