package edu.ucalgary.oop;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LocationSubmenu extends Menu {
    private final String[] MANAGE_OCCUPANTS_OPTIONS = languageManager.getMenuTranslation("location_submenu_manage_occupants");
    private final String[] MANAGE_SUPPLIES_OPTIONS = languageManager.getMenuTranslation("location_submenu_manage_supplies");
    private final LocationService locationService = LocationService.INSTANCE;
    private final Pattern dateFormat = Pattern.compile("^(\\d{4})-(0?[1-9]|1[0-2])-(0?[1-9]|[12][0-9]|3[01])$\n");
    private final Pattern phoneNumberFormat = Pattern.compile("^(\\d{3})-(\\d{3})-(\\d{4}$)");

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
        PersonService personService = PersonService.INSTANCE;
        DisasterVictimService disasterVictimService = DisasterVictimService.INSTANCE;

        try {
            Location retrievedLocation = locationService.getLocationWithOccupants(selectedLocationId);

            switch(intInput) {
                case 1: // Add an occupant

                    // Get first name
                    System.out.println("Enter the occupant's first name: ");
                    setRequiresIntInput(false);
                    setStringNumbersAllowed(false);
                    setStringEmptyAllowed(false);
                    String firstName = handleStringInput();

                    // Get gender
                    String[] genderOptions = languageManager.getMenuTranslation("gender_options");
                    for (String option : genderOptions) {
                        System.out.println(option);
                    }

                    System.out.println("Enter the occupant's gender by typing in a corresponding number (for example, '1' for Male): ");
                    setRequiresIntInput(true);
                    setMinIntInput(1);
                    setMaxIntInput(3);
                    handleIntInput();

                    String gender = switch (intInput) {
                        case 1 -> "Male";
                        case 2 -> "Female";
                        case 3 -> "Non-binary";
                        default -> null;
                    };

                    // Get date of birth
                    setRequiresIntInput(false);
                    setStringNumbersAllowed(true);
                    setStringEmptyAllowed(false);

                    System.out.println("Enter the occupant's date of birth (e.g. 2000-12-31 for December 31, 2000): ");
                    String stringDateOfBirth = handleStringInput();
                    Matcher dateMatcher = dateFormat.matcher(stringDateOfBirth);

                    while(!dateMatcher.matches()) {
                        System.out.println("Invalid date format. Enter a date in the format YYYY-MM-DD: ");
                        stringDateOfBirth = handleStringInput();
                    }

                    LocalDate realDateOfBirth = LocalDate.parse(stringDateOfBirth);

                    // Get phone number
                    System.out.println("Enter the occupant's phone number (e.g. 123-456-7890)");
                    System.out.println("If no known phone number, simply hit enter: ");
                    setRequiresIntInput(false);
                    setStringNumbersAllowed(true);
                    setStringEmptyAllowed(true);

                    String phoneNumber = handleStringInput();
                    Matcher phoneNumberMatcher = phoneNumberFormat.matcher(phoneNumber);

                    while(!phoneNumberMatcher.matches()) {
                        System.out.println("Invalid phone number format. Enter a phone number in the format 123-456-7890: ");
                        phoneNumber = handleStringInput();
                    }

                    Person newPerson = personService.addPerson(firstName, gender, realDateOfBirth, phoneNumber);
                    boolean success = locationService.addOccupant(retrievedLocation, newPerson);

                    if(success) {
                        System.out.println("Successfully added a new occupant.");
                    } else {
                        System.out.println("Failed to add a new occupant.");
                    }

                    break;

                case 2: // Remove an occupant
                    System.out.println("Input the ID of the occupant you'd like to remove: ");
                    setRequiresIntInput(true);
                    setMinIntInput(1);
                    setMaxIntInput(Integer.MAX_VALUE);
                    handleIntInput();

                    int personToRemoveId = intInput;
                    Person personToRemove = personService.getPersonById(personToRemoveId);
                    Location personLocation = disasterVictimService.getPersonLocation(personToRemove);

                    if (retrievedLocation.getLocationId() != personLocation.getLocationId()) {
                        System.out.println("Cannot remove this occupant as they are not an occupant of this location.");
                    } else {
                        success = locationService.removeOccupant(retrievedLocation, personToRemove);
                        if (success) {
                            System.out.println("Occupant successfully removed from this location.");
                        } else {
                            System.out.println("Failed to remove occupant from this location.");
                        }

                    }

                    break;

                case 3: // View all occupants
                    if (!retrievedLocation.getOccupants().isEmpty()) {
                        System.out.println("Occupants of location: ");
                        System.out.println("---------------------------------");

                        for (Person person : retrievedLocation.getOccupants()) {
                            System.out.println("First name: " + person.getFirstName());
                            System.out.println("Assigned ID: " + person.getAssignedId());
                            System.out.println("Gender: " + person.getGender());
                            System.out.println("Phone Number: " + person.getPhoneNumber());
                            System.out.println();
                        }
                    } else {
                        System.out.println("No occupants at this location.");
                    }

                case 4: // Return to main submenu
                returnToDefaultState();
                break;
            }
        } catch (Exception e) {
            System.out.println("Error managing occupants at a location: " + e.getMessage());
        }
    }

    public void processManageSuppliesInput() {
        try {
            SupplyService supplyService = SupplyService.INSTANCE;
            Location retrievedLocation = locationService.getLocation(selectedLocationId);
            locationService.refreshAllocations(retrievedLocation);


            switch (intInput) {
                case 1: // Remove a supply
                    System.out.println("Input the ID of the supply you'd like to remove: ");
                    setRequiresIntInput(true);
                    setMinIntInput(1);
                    setMaxIntInput(Integer.MAX_VALUE);
                    handleIntInput();
                    int supplyToRemoveId = intInput;

                    Supply supplyToRemove = supplyService.getSupplyById(supplyToRemoveId);

                    if (supplyToRemove == null) {
                        System.out.println("Supply not found.");
                        break;
                    }

                    System.out.println("Enter the supply's allocation date (e.g. 2000-12-31 for December 31, 2000): ");
                    setRequiresIntInput(false);
                    setStringNumbersAllowed(true);
                    setStringEmptyAllowed(false);

                    String stringAllocationDate = handleStringInput();
                    Matcher dateMatcher = dateFormat.matcher(stringAllocationDate);

                    while(!dateMatcher.matches()) {
                        System.out.println("Invalid date format. Enter a date in the format YYYY-MM-DD: ");
                        stringAllocationDate = handleStringInput();
                    }

                    LocalDate allocationDate = LocalDate.parse(stringAllocationDate);

                    boolean success = locationService.removeSupplyAllocation(retrievedLocation, supplyToRemove, allocationDate);

                    if (success) {
                        System.out.println("Supply successfully removed from this location.");
                    } else {
                        System.out.println("Failed to remove supply from this location.");
                    }

                    break;

                case 2: // Add a supply
                    String[] addSupplyOptions = languageManager.getMenuTranslation("add_supply_options");

                    for (String option : addSupplyOptions) {
                        System.out.println(option);
                    }

                    System.out.println("Choose the type of supply (e.g. '1' for Blanket): ");
                    setRequiresIntInput(true);
                    setMinIntInput(1);
                    setMaxIntInput(addSupplyOptions.length - 1);
                    handleIntInput();

                    String supplyType = switch (intInput) {
                        case 1 -> "Blanket";
                        case 2 -> "Cot";
                        case 3 -> "Personal belonging";
                        case 4 -> "Water";
                        default -> null;
                    };

                    System.out.println("Enter any comments for this supply (like a description, cot location like 12 A14, or leave empty): ");
                    setRequiresIntInput(false);
                    setStringNumbersAllowed(true);
                    setStringEmptyAllowed(true);
                    String supplyComments = handleStringInput();

                    if (supplyType.equalsIgnoreCase("Cot") ) {
                        Pattern cotLocationFormat = Pattern.compile("^(\\d{1,3}) (\\s{1}\\d{1,2})$");
                        Matcher cotLocationMatcher = cotLocationFormat.matcher(supplyComments);

                        while (!cotLocationMatcher.matches()) {
                            System.out.println("Cot location format is invalid. Format should be 123 A23 for room number and grid location: ");
                            supplyComments = handleStringInput();
                        }
                    }

                    Supply newSupply = supplyService.addSupply(supplyType, supplyComments);

                    if (newSupply == null) {
                        System.out.println("Failed to create new supply.");
                        break;
                    }

                    success = locationService.addSupplyAllocation(retrievedLocation, newSupply, LocalDate.now());

                    if (success) {
                        System.out.println("Supply successfully added to this location.");
                    } else {
                        System.out.println("Failed to add supply to this location.");
                    }

                    break;

                case 3: // Give a supply to an occupant
                    System.out.println("Input the ID of the supply you'd like to give: ");
                    setRequiresIntInput(true);
                    setMinIntInput(1);
                    setMaxIntInput(Integer.MAX_VALUE);
                    handleIntInput();
                    int supplyToGiveId = intInput;

                    Supply supplyToGive = supplyService.getSupplyById(supplyToGiveId);

                    if (supplyToGive == null) {
                        System.out.println("Supply not found.");
                        break;
                    }

                    System.out.println("Input the ID of the occupant to give the supply to: ");
                    setRequiresIntInput(true);
                    setMinIntInput(1);
                    setMaxIntInput(Integer.MAX_VALUE);
                    handleIntInput();
                    int occupantId = intInput;

                    PersonService personService = PersonService.INSTANCE;
                    Person occupant = personService.getPersonById(occupantId);

                    if (occupant == null) {
                        System.out.println("Occupant not found.");
                        break;
                    }

                    DisasterVictimService disasterVictimService = DisasterVictimService.INSTANCE;
                    Location occupantLocation = disasterVictimService.getPersonLocation(occupant);

                    if (occupantLocation == null || occupantLocation.getLocationId() != retrievedLocation.getLocationId()) {
                        System.out.println("The specified person is not an occupant of this location.");
                        break;
                    }

                    System.out.println("Enter the supply's allocation date (e.g. 2000-12-31 for December 31, 2000): ");
                    setRequiresIntInput(false);
                    setStringNumbersAllowed(true);
                    setStringEmptyAllowed(false);

                    stringAllocationDate = handleStringInput();
                    dateMatcher = dateFormat.matcher(stringAllocationDate);

                    while(!dateMatcher.matches()) {
                        System.out.println("Invalid date format. Enter a date in the format YYYY-MM-DD: ");
                        stringAllocationDate = handleStringInput();
                    }

                    allocationDate = LocalDate.parse(stringAllocationDate);

                    boolean removedFromLocation = locationService.removeSupplyAllocation(retrievedLocation, supplyToGive, allocationDate);

                    if (!removedFromLocation) {
                        System.out.println("Failed to remove supply from location.");
                        break;
                    }

                    DisasterVictim victim = disasterVictimService.getDisasterVictimById(occupantId);

                    if (victim == null) {
                        System.out.println("Failed to retrieve disaster victim.");
                        break;
                    }

                    boolean addedToOccupant = disasterVictimService.addSupplyAllocation(victim, supplyToGive, allocationDate);

                    if (addedToOccupant) {
                        System.out.println("Supply successfully given to occupant.");
                    } else {
                        System.out.println("Failed to give supply to occupant.");
                    }
                    break;

                case 4: // Return to main submenu
                    returnToDefaultState();
                    break;
            }
        } catch (Exception e) {
            System.out.println("Error managing supplies at a location: " + e.getMessage());
        }
    }

}
