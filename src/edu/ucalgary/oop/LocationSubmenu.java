package edu.ucalgary.oop;

import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.List;

public class LocationSubmenu extends Menu {
    private final String[] MANAGE_OCCUPANTS_OPTIONS = languageManager.getMenuTranslation("location_submenu_manage_occupants");
    private final String[] MANAGE_SUPPLIES_OPTIONS = languageManager.getMenuTranslation("location_submenu_manage_supplies");
    private final LocationService locationService = LocationService.INSTANCE;

    private enum State {DEFAULT, MANAGE_OCCUPANTS, MANAGE_SUPPLIES}
    private State currentState = State.DEFAULT;

    private int selectedLocationId;

    public LocationSubmenu(String[] defaultOptions) {
        super(defaultOptions);
    }

    public String[] getManageOccupantsOptions() {
        return this.MANAGE_OCCUPANTS_OPTIONS;
    }

    public String[] getManageAllocationsOptions() {
        return this.MANAGE_SUPPLIES_OPTIONS;
    }

    public void listAllLocations() {
        try {
            List<Location> retrievedLocations = null;
            retrievedLocations = locationService.getAllLocations();

            System.out.println("Locations and their details:");
            System.out.println("--------------------------------------------");

            if (retrievedLocations != null) {
                int counter = 0;
                for (Location location : retrievedLocations) {
                    counter++;
                    String formattedString = String.format("Location #%d: ", counter);
                    System.out.println(formattedString);
                    System.out.println("--------------------------------");
                    System.out.println("Location ID: " + locationService.getLocationInfo("location_id", location.getLocationId()));
                    System.out.println("Name: " + locationService.getLocationInfo("name", location.getLocationId()));
                    System.out.println("Address: " + locationService.getLocationInfo("address", location.getLocationId()));
                    System.out.println();
                }
            } else {
                System.out.println("No locations found.");
            }
        } catch (Exception e) {
            System.out.println("Error occurred while trying to list all locations: " + e.getMessage());
            System.out.println("Try again later.");
        }
    }

    public void updateLocationName() {
        setRequiresIntInput(true);
        setMinIntInput(1);
        setMaxIntInput(Integer.MAX_VALUE);
        handleIntInput();
        int locationId = intInput;

        setRequiresIntInput(false);
        setStringEmptyAllowed(false);
        setStringNumbersAllowed(true);
        String newName = handleStringInput();

        try {
            Location location = locationService.getLocation(locationId);
            boolean success = locationService.updateLocationName(location, newName);

            if(success) {
                System.out.println("Successfully updated the location name to " + newName + ".");
            } else {
                System.out.println("Couldn't update the location name. Try again later.");
            }
        } catch (Exception e) {
            System.out.println("Error occurred while trying to update location name: " + e.getMessage());
        }
    }

    public void updateLocationAddress() {
        setRequiresIntInput(true);
        setMinIntInput(1);
        setMaxIntInput(Integer.MAX_VALUE);
        handleIntInput();
        int locationId = intInput;

        setRequiresIntInput(false);
        setStringEmptyAllowed(false);
        setStringNumbersAllowed(true);
        String newAddress = handleStringInput();

        try {
            Location retrievedLocation = locationService.getLocation(locationId);
            boolean success = locationService.updateLocationAddress(retrievedLocation, newAddress);

            if(success) {
                System.out.println("Successfully updated the location address to " + newAddress + ".");
            } else {
                System.out.println("Couldn't update the location address. Try again later.");
            }
        } catch (Exception e) {
            System.out.println("Error occurred while trying to update location address: " + e.getMessage());
        }
    }

    public void viewLocationDetails() {
        setRequiresIntInput(true);
        setMinIntInput(1);
        setMaxIntInput(Integer.MAX_VALUE);
        handleIntInput();
        int locationId = intInput;

        try {
            Location retrievedLocation = locationService.getLocationWithOccupants(locationId);
            locationService.refreshAllocations(retrievedLocation);

            if (retrievedLocation != null) {
                System.out.println("Location details:");
                System.out.println("--------------------------------");
                System.out.println("Location ID: " + locationService.getLocationInfo("location_id", retrievedLocation.getLocationId()));
                System.out.println("Name: " + locationService.getLocationInfo("name", retrievedLocation.getLocationId()));
                System.out.println("Address: " + locationService.getLocationInfo("address", retrievedLocation.getLocationId()));
                System.out.println("Number of occupants: " + retrievedLocation.getOccupants().size());
                System.out.println("Number of supplies: " + retrievedLocation.getAllocations().size());
                System.out.println();
            } else {
                System.out.println("Location couldn't be found.");
            }
        } catch (Exception e) {
            System.out.println("Error occurred while trying to view location details: " + e.getMessage());
        }
    }

    public void manageOccupants() {
        System.out.println(languageManager.getTranslation("input_location_id"));
        setRequiresIntInput(true);
        setMinIntInput(1);
        setMaxIntInput(Integer.MAX_VALUE);
        handleIntInput();
        selectedLocationId = intInput;

        currentState = State.MANAGE_OCCUPANTS;
        setCurrentDisplay(MANAGE_OCCUPANTS_OPTIONS);
        setMinIntInput(1);
        setMaxIntInput(MANAGE_OCCUPANTS_OPTIONS.length - 1);
    }

    public void manageSupplies() {
        System.out.println(languageManager.getTranslation("input_location_id"));
        setRequiresIntInput(true);
        setMinIntInput(1);
        setMaxIntInput(Integer.MAX_VALUE);
        handleIntInput();
        selectedLocationId = intInput;

        currentState = State.MANAGE_SUPPLIES;
        setCurrentDisplay(MANAGE_SUPPLIES_OPTIONS);
        setMinIntInput(1);
        setMaxIntInput(MANAGE_SUPPLIES_OPTIONS.length - 1);
    }

    public void viewLocationSupplies() {
        System.out.println(languageManager.getTranslation("input_location_id_to_view"));
        setRequiresIntInput(true);
        setMinIntInput(1);
        setMaxIntInput(Integer.MAX_VALUE);
        handleIntInput();
        selectedLocationId = intInput;

        try {
            Location retrievedLocation = locationService.getLocation(selectedLocationId);
            locationService.refreshAllocations(retrievedLocation);
            LinkedHashSet<Allocation> retrievedAllocations = retrievedLocation.getAllocations();

            if (!retrievedAllocations.isEmpty()) {
                for (Allocation allocation : retrievedAllocations) {
                    System.out.println("Supply details:");
                    System.out.println("--------------------------------");
                    System.out.println("Supply ID: " + allocation.getAllocatedSupply().getSupplyId());
                    System.out.println("Supply type: " + allocation.getAllocatedSupply().getType());
                    System.out.println("Time allocated: " + allocation.getTimeAllocated());

                    allocation.getAllocatedSupply().displayDetails();

                    if (!allocation.getAllocatedSupply().getComments().isEmpty()) {
                        System.out.println("Comments: " + allocation.getAllocatedSupply().getComments());
                    }

                    System.out.println();
                }
            } else {
                System.out.println("Location has no supplies.");
            }
        } catch (Exception e) {
            System.out.println("Error occurred when trying to view location supplies: " + e.getMessage());
        }
    }

    public void returnToDefaultState() {
        currentState = State.DEFAULT;
        setCurrentDisplay(getDefaultOptions());
        setRequiresIntInput(true);
        setMinIntInput(1);
        setMaxIntInput(getDefaultOptions().length - 1);
    }

    @Override
    public void processInput() {
        switch(currentState) {
            case DEFAULT:
                processDefaultInput();
                break;
            case MANAGE_OCCUPANTS:
                processManageOccupantsInput();
                break;
            case MANAGE_SUPPLIES:
                processManageSuppliesInput();
                break;
        }
    }

    public void processDefaultInput() {
        switch(intInput) {
            case 1: // View all locations
                listAllLocations();
                break;

            case 2: // View details of a specific location
                viewLocationDetails();
                break;

            case 3: // Update the name of a location
                updateLocationName();
                break;

            case 4: // Update the address of a location
                updateLocationAddress();
                break;

            case 5: // View the supplies at a location
                viewLocationSupplies();
                break;

            case 6: // Manage location occupants
                manageOccupants();
                break;

            case 7: // Manage supplies at a location
                manageSupplies();
                break;

            case 8: // Return to main menu
                menuManager.returnToMainMenu();
                break;

            default:
                System.out.println(languageManager.getTranslation("error_invalid_option"));
                break;
        }
    }

    public void processManageOccupantsInput() {
        try {
            Location retrievedLocation = locationService.getLocationWithOccupants(selectedLocationId);

            switch(intInput) {
                case 1: // Add an occupant

                case 2: // Remove an occupant

                case 3: // View all occupants
            }
        } catch (Exception e) {
            System.out.println("Error managing occupants at a location: " + e.getMessage());
        }
    }

    public void processManageSuppliesInput() {
        try {
            Location retrievedLocation = locationService.getLocationWithOccupants(selectedLocationId);
            locationService.refreshAllocations(retrievedLocation);

            switch (intInput) {
                case 1: // Remove a supply

                case 2: // Add a supply

                case 3: // Give a supply to an occupant
            }
        } catch (Exception e) {
            System.out.println("Error managing supplies at a location: " + e.getMessage());
        }
    }
}
