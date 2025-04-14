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
    private final Pattern dateFormat = Pattern.compile("^(\\d{4})-(0?[1-9]|1[0-2])-(0?[1-9]|[12][0-9]|3[01])$");
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
            System.out.println(languageManager.getTranslation("locations_and_details"));
            System.out.println(languageManager.getTranslation("separator"));
            if (retrievedLocations != null) {
                int counter = 0;
                for (Location location : retrievedLocations) {
                    counter++;
                    System.out.println(String.format(languageManager.getTranslation("location_number"), counter));
                    System.out.println(languageManager.getTranslation("separator_short"));
                    System.out.println(languageManager.getTranslation("location_id") + locationService.getLocationInfo("location_id", location.getLocationId()));
                    System.out.println(languageManager.getTranslation("name") + locationService.getLocationInfo("name", location.getLocationId()));
                    System.out.println(languageManager.getTranslation("address") + locationService.getLocationInfo("address", location.getLocationId()));
                    System.out.println();
                }
            } else {
                System.out.println(languageManager.getTranslation("no_locations_found"));
            }
        } catch (Exception e) {
            System.out.println(languageManager.getTranslation("list_locations_error") + e.getMessage());
            System.out.println(languageManager.getTranslation("try_again_later"));
        }
    }

    public void updateLocationName() {
        System.out.println(languageManager.getTranslation("input_location_id_to_update"));
        setRequiresIntInput(true);
        setMinIntInput(1);
        setMaxIntInput(Integer.MAX_VALUE);
        handleIntInput();
        int locationId = intInput;

        System.out.println(languageManager.getTranslation("input_location_updated_name"));
        setRequiresIntInput(false);
        setStringEmptyAllowed(false);
        setStringNumbersAllowed(true);
        String newName = handleStringInput();

        try {
            Location location = locationService.getLocation(locationId);
            boolean success = locationService.updateLocationName(location, newName);
            if(success) {
                System.out.println(String.format(languageManager.getTranslation("location_name_updated_success"), newName));
            } else {
                System.out.println(languageManager.getTranslation("location_name_updated_failure"));
            }
        } catch (Exception e) {
            System.out.println(languageManager.getTranslation("update_location_name_error") + e.getMessage());
        }
    }

    public void updateLocationAddress() {
        System.out.println(languageManager.getTranslation("input_location_id_to_update"));
        setRequiresIntInput(true);
        setMinIntInput(1);
        setMaxIntInput(Integer.MAX_VALUE);
        handleIntInput();
        int locationId = intInput;

        System.out.println(languageManager.getTranslation("input_location_updated_address"));
        setRequiresIntInput(false);
        setStringEmptyAllowed(false);
        setStringNumbersAllowed(true);
        String newAddress = handleStringInput();

        try {
            Location retrievedLocation = locationService.getLocation(locationId);
            boolean success = locationService.updateLocationAddress(retrievedLocation, newAddress);
            if(success) {
                System.out.println(String.format(languageManager.getTranslation("location_address_updated_success"), newAddress));
            } else {
                System.out.println(languageManager.getTranslation("location_address_updated_failure"));
            }
        } catch (Exception e) {
            System.out.println(languageManager.getTranslation("update_location_address_error") + e.getMessage());
        }
    }

    public void viewLocationDetails() {
        System.out.println(languageManager.getTranslation("input_location_id_to_view"));
        setRequiresIntInput(true);
        setMinIntInput(1);
        setMaxIntInput(Integer.MAX_VALUE);
        handleIntInput();
        int locationId = intInput;

        try {
            Location retrievedLocation = locationService.getLocationWithOccupants(locationId);
            locationService.refreshAllocations(retrievedLocation);

            if (retrievedLocation != null) {
                System.out.println(languageManager.getTranslation("location_details"));
                System.out.println(languageManager.getTranslation("separator_short"));
                System.out.println(languageManager.getTranslation("location_id") + locationService.getLocationInfo("location_id", retrievedLocation.getLocationId()));
                System.out.println(languageManager.getTranslation("name") + locationService.getLocationInfo("name", retrievedLocation.getLocationId()));
                System.out.println(languageManager.getTranslation("address") + locationService.getLocationInfo("address", retrievedLocation.getLocationId()));
                System.out.println(languageManager.getTranslation("number_of_occupants") + retrievedLocation.getOccupants().size());
                System.out.println(languageManager.getTranslation("number_of_supplies") + retrievedLocation.getAllocations().size());
                System.out.println();
            } else {
                System.out.println(languageManager.getTranslation("location_not_found"));
            }
        } catch (Exception e) {
            System.out.println(languageManager.getTranslation("view_location_details_error") + e.getMessage());
        }
    }

    public void manageOccupants() {
        System.out.println(languageManager.getTranslation("input_location_id_to_manage"));
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
        System.out.println(languageManager.getTranslation("input_location_id_to_manage"));
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
        System.out.println(languageManager.getTranslation("input_location_id_to_view_supplies"));
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
                    System.out.println(languageManager.getTranslation("supply_details"));
                    System.out.println(languageManager.getTranslation("separator_short"));
                    System.out.println(languageManager.getTranslation("supply_id") + allocation.getAllocatedSupply().getSupplyId());
                    System.out.println(languageManager.getTranslation("supply_type") + allocation.getAllocatedSupply().getType());
                    System.out.println(languageManager.getTranslation("time_allocated") + allocation.getTimeAllocated());
                    allocation.getAllocatedSupply().displayDetails();
                    System.out.println();
                }
            } else {
                System.out.println(languageManager.getTranslation("location_no_supplies"));
            }
        } catch (Exception e) {
            System.out.println(languageManager.getTranslation("view_location_supplies_error") + e.getMessage());
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
                    try {
                        // Get first name
                        System.out.println(languageManager.getTranslation("enter_first_name"));
                        setRequiresIntInput(false);
                        setStringNumbersAllowed(false);
                        setStringEmptyAllowed(false);
                        String firstName = handleStringInput();

                        // Get gender
                        String[] genderOptions = languageManager.getMenuTranslation("gender_options");
                        for (String option : genderOptions) {
                            System.out.println(option);
                        }

                        System.out.println(languageManager.getTranslation("enter_gender_number"));
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

                        // Reset the min/max int input range to match menu options
                        setMinIntInput(1);
                        setMaxIntInput(MANAGE_OCCUPANTS_OPTIONS.length - 1);

                        // Get date of birth
                        setRequiresIntInput(false);
                        setStringNumbersAllowed(true);
                        setStringEmptyAllowed(true);
                        System.out.println(languageManager.getTranslation("enter_date_of_birth"));
                        System.out.println(languageManager.getTranslation("enter_no_date_of_birth"));
                        String stringDateOfBirth = handleStringInput();
                        LocalDate realDateOfBirth = null;

                        if (!stringDateOfBirth.isEmpty()) {
                            Matcher dateMatcher = dateFormat.matcher(stringDateOfBirth);
                            while(!dateMatcher.matches()) {
                                System.out.println(languageManager.getTranslation("invalid_date_format"));
                                stringDateOfBirth = handleStringInput();
                                if (stringDateOfBirth.isEmpty()) {
                                    break;
                                }
                                dateMatcher = dateFormat.matcher(stringDateOfBirth);
                            }
                            if (!stringDateOfBirth.isEmpty()) {
                                realDateOfBirth = LocalDate.parse(stringDateOfBirth);
                            }
                        }

                        // Get phone number
                        System.out.println(languageManager.getTranslation("enter_phone_number"));
                        System.out.println(languageManager.getTranslation("enter_no_phone_number"));
                        setStringNumbersAllowed(true);
                        setStringEmptyAllowed(true);
                        String phoneNumber = handleStringInput();
                        if (!phoneNumber.isEmpty()) {
                            Matcher phoneNumberMatcher = phoneNumberFormat.matcher(phoneNumber);
                            while(!phoneNumberMatcher.matches()) {
                                System.out.println(languageManager.getTranslation("invalid_phone_format"));
                                phoneNumber = handleStringInput();
                                if (phoneNumber.isEmpty()) {
                                    break;
                                }
                                phoneNumberMatcher = phoneNumberFormat.matcher(phoneNumber);
                            }
                        }
                        if (phoneNumber.isEmpty()) {
                            phoneNumber = null;
                        }

                        // Create new person
                        Person newPerson = personService.addPerson(firstName, gender, realDateOfBirth, phoneNumber);
                        locationService.addOccupant(retrievedLocation, newPerson);

                        if (newPerson != null) {
                            System.out.println(String.format(languageManager.getTranslation("person_added_success"), newPerson.getAssignedId()));
                        } else {
                            System.out.println(languageManager.getTranslation("person_added_failure"));
                        }
                    } catch (Exception e) {
                        System.out.println(languageManager.getTranslation("add_person_error") + e.getMessage());
                    }

                    break;

                case 2: // Remove an occupant
                    System.out.println(languageManager.getTranslation("input_occupant_id_to_remove"));
                    setRequiresIntInput(true);
                    setMinIntInput(1);
                    setMaxIntInput(Integer.MAX_VALUE);
                    handleIntInput();
                    int personToRemoveId = intInput;

                    Person personToRemove = personService.getPersonById(personToRemoveId);
                    Location personLocation = disasterVictimService.getPersonLocation(personToRemove);

                    if (retrievedLocation.getLocationId() != personLocation.getLocationId()) {
                        System.out.println(languageManager.getTranslation("cannot_remove_occupant"));
                    } else {
                        boolean success = locationService.removeOccupant(retrievedLocation, personToRemove);
                        if (success) {
                            personService.removePerson(personToRemove);
                            System.out.println(languageManager.getTranslation("occupant_removed_success"));
                        } else {
                            System.out.println(languageManager.getTranslation("occupant_removed_failure"));
                        }
                    }

                    break;

                case 3: // View all occupants
                    if (!retrievedLocation.getOccupants().isEmpty()) {
                        System.out.println(languageManager.getTranslation("occupants_of_location"));
                        System.out.println(languageManager.getTranslation("divider_long"));
                        for (Person person : retrievedLocation.getOccupants()) {
                            System.out.println(languageManager.getTranslation("first_name") + ": " + person.getFirstName());
                            System.out.println(languageManager.getTranslation("assigned_id") + ": " + person.getAssignedId());
                            System.out.println(languageManager.getTranslation("gender") + ": " + person.getGender());
                            System.out.println(languageManager.getTranslation("phone_number") + ": " + person.getPhoneNumber());
                            System.out.println();
                        }
                    } else {
                        System.out.println(languageManager.getTranslation("no_occupants"));
                    }

                    break;

                case 4: // Return to main submenu
                    returnToDefaultState();
                    break;
            }
        } catch (Exception e) {
            System.out.println(languageManager.getTranslation("manage_occupants_error") + e.getMessage());
        }
    }

    public void processManageSuppliesInput() {
        try {
            SupplyService supplyService = SupplyService.INSTANCE;
            Location retrievedLocation = locationService.getLocation(selectedLocationId);
            locationService.refreshAllocations(retrievedLocation);

            switch (intInput) {
                case 1: // Remove a supply
                    System.out.println(languageManager.getTranslation("input_supply_id_to_remove"));
                    setRequiresIntInput(true);
                    setMinIntInput(1);
                    setMaxIntInput(Integer.MAX_VALUE);
                    handleIntInput();
                    int supplyToRemoveId = intInput;

                    Supply supplyToRemove = supplyService.getSupplyById(supplyToRemoveId);

                    if (supplyToRemove == null) {
                        System.out.println(languageManager.getTranslation("supply_not_found"));
                        break;
                    }

                    boolean success = locationService.removeSupplyAllocation(retrievedLocation, supplyToRemove);

                    if (success) {
                        supplyService.removeSupply(supplyToRemove);
                        System.out.println(languageManager.getTranslation("supply_removed_success"));
                    } else {
                        System.out.println(languageManager.getTranslation("supply_removed_failure"));
                    }
                    break;

                case 2: // Add a supply
                    String[] addSupplyOptions = languageManager.getMenuTranslation("add_supply_options");
                    for (String option : addSupplyOptions) {
                        System.out.println(option);
                    }

                    System.out.println(languageManager.getTranslation("choose_supply_type"));
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

                    System.out.println(languageManager.getTranslation("enter_supply_comments"));
                    setRequiresIntInput(false);
                    setStringNumbersAllowed(true);
                    setStringEmptyAllowed(true);
                    String supplyComments = handleStringInput();

                    if (supplyType.equalsIgnoreCase("Cot") ) {
                        Pattern cotLocationFormat = Pattern.compile("^(\\d{1,3}) (\\s{1}\\d{1,2})$");
                        Matcher cotLocationMatcher = cotLocationFormat.matcher(supplyComments);
                        while (!cotLocationMatcher.matches()) {
                            System.out.println(languageManager.getTranslation("invalid_cot_location"));
                            supplyComments = handleStringInput();
                        }
                    }

                    Supply newSupply = supplyService.addSupply(supplyType, supplyComments);

                    if (newSupply == null) {
                        System.out.println(languageManager.getTranslation("create_supply_failure"));
                        break;
                    }

                    success = locationService.addSupplyAllocation(retrievedLocation, newSupply, LocalDate.now());

                    if (success) {
                        System.out.println(languageManager.getTranslation("supply_added_success"));
                    } else {
                        System.out.println(languageManager.getTranslation("supply_added_failure"));
                    }
                    break;

                case 3: // Give a supply to an occupant
                    System.out.println(languageManager.getTranslation("input_supply_id_to_give"));
                    setRequiresIntInput(true);
                    setMinIntInput(1);
                    setMaxIntInput(Integer.MAX_VALUE);
                    handleIntInput();
                    int supplyToGiveId = intInput;

                    Supply supplyToGive = supplyService.getSupplyById(supplyToGiveId);

                    if (supplyToGive == null) {
                        System.out.println(languageManager.getTranslation("supply_not_found"));
                        break;
                    }

                    System.out.println(languageManager.getTranslation("input_occupant_id"));
                    setRequiresIntInput(true);
                    setMinIntInput(1);
                    setMaxIntInput(Integer.MAX_VALUE);
                    handleIntInput();
                    int occupantId = intInput;

                    PersonService personService = PersonService.INSTANCE;
                    Person occupant = personService.getPersonById(occupantId);

                    if (occupant == null) {
                        System.out.println(languageManager.getTranslation("occupant_not_found"));
                        break;
                    }

                    DisasterVictimService disasterVictimService = DisasterVictimService.INSTANCE;
                    Location occupantLocation = disasterVictimService.getPersonLocation(occupant);

                    if (occupantLocation == null || occupantLocation.getLocationId() != retrievedLocation.getLocationId()) {
                        System.out.println(languageManager.getTranslation("person_not_occupant"));
                        break;
                    }

                    System.out.println(languageManager.getTranslation("enter_supply_allocation_date"));
                    setRequiresIntInput(false);
                    setStringNumbersAllowed(true);
                    setStringEmptyAllowed(false);

                    String stringAllocationDate = handleStringInput();
                    LocalDate allocationDate = null;

                    if (!stringAllocationDate.isEmpty()) {
                        Matcher dateMatcher = dateFormat.matcher(stringAllocationDate);
                        while(!dateMatcher.matches()) {
                            System.out.println(languageManager.getTranslation("invalid_date_format"));
                            stringAllocationDate = handleStringInput();
                            if (stringAllocationDate.isEmpty()) {
                                break;
                            }
                            dateMatcher = dateFormat.matcher(stringAllocationDate);
                        }
                        if (!stringAllocationDate.isEmpty()) {
                            allocationDate = LocalDate.parse(stringAllocationDate);
                        }
                    }

                    DisasterVictim victim = disasterVictimService.getDisasterVictimById(occupantId);

                    if (victim == null) {
                        System.out.println(languageManager.getTranslation("retrieve_victim_failure"));
                        break;
                    }

                    boolean addedToOccupant = disasterVictimService.addSupplyAllocation(victim, supplyToGive, allocationDate);

                    if (addedToOccupant) {
                        System.out.println(languageManager.getTranslation("supply_given_to_occupant_success"));
                    } else {
                        System.out.println(languageManager.getTranslation("supply_given_to_occupant_failure"));
                    }

                    break;

                case 4: // Return to main submenu
                    returnToDefaultState();
                    break;
            }
        } catch (Exception e) {
            System.out.println(languageManager.getTranslation("manage_supplies_error") + e.getMessage());
        }
    }
}
