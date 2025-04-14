package edu.ucalgary.oop;

import java.time.LocalDate;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PersonSubmenu extends Menu {
    private final String[] MANAGE_INFO_OPTIONS = languageManager.getMenuTranslation("person_submenu_manage_info");
    private final String[] MANAGE_MEDICAL_RECORDS_OPTIONS = languageManager.getMenuTranslation("person_submenu_manage_medical_records");

    private final PersonService personService = PersonService.INSTANCE;
    private final DisasterVictimService disasterVictimService = DisasterVictimService.INSTANCE;
    private final MedicalRecordService medicalRecordService = MedicalRecordService.INSTANCE;

    private final Pattern dateFormat = Pattern.compile("^(\\d{4})-(0?[1-9]|1[0-2])-(0?[1-9]|[12][0-9]|3[01])$");
    private final Pattern phoneNumberFormat = Pattern.compile("^(\\d{3})-(\\d{3})-(\\d{4})$");

    private enum State {DEFAULT, MANAGE_INFO, MANAGE_MEDICAL_RECORDS}
    private State currentState = State.DEFAULT;
    private int selectedPersonId;

    public PersonSubmenu(String[] defaultOptions) {
        super(defaultOptions);
    }

    public String[] getManageInfoOptions() {
        return this.MANAGE_INFO_OPTIONS;
    }

    public String[] getManageMedicalRecordsOptions() {
        return this.MANAGE_MEDICAL_RECORDS_OPTIONS;
    }

    public void listAllPersons() {
        try {
            List<Person> retrievedPersons = personService.getAllPersons();

            if (retrievedPersons != null) {
                int counter = 0;
                for (Person person : retrievedPersons) {
                    counter++;
                    String formattedString = String.format("Person #%d: ", counter);
                    System.out.println(formattedString);
                    System.out.println("--------------------------------");
                    System.out.println("Person ID: " + person.getAssignedId());
                    System.out.println("First Name: " + person.getFirstName());
                    if (person.getLastName() != null) {
                        System.out.println("Last Name: " + person.getLastName());
                    }
                    System.out.println("Gender: " + person.getGender());
                    if (person.getDateOfBirth() != null) {
                        System.out.println("Date of Birth: " + person.getDateOfBirth());
                    }
                    if (person.getPhoneNumber() != null) {
                        System.out.println("Phone Number: " + person.getPhoneNumber());
                    }
                    System.out.println();
                }
            } else {
                System.out.println("No persons found.");
            }
        } catch (Exception e) {
            System.out.println("Error occurred while trying to list all persons: " + e.getMessage());
            System.out.println("Try again later.");
        }
    }

    public void viewPersonDetails() {
        setRequiresIntInput(true);
        setMinIntInput(1);
        setMaxIntInput(Integer.MAX_VALUE);
        handleIntInput();
        int personId = intInput;

        try {
            Person retrievedPerson = personService.getPersonById(personId);

            if (retrievedPerson != null) {
                System.out.println("Person details:");
                System.out.println("--------------------------------");
                System.out.println("Person ID: " + retrievedPerson.getAssignedId());
                System.out.println("First Name: " + retrievedPerson.getFirstName());
                if (retrievedPerson.getLastName() != null) {
                    System.out.println("Last Name: " + retrievedPerson.getLastName());
                }
                System.out.println("Gender: " + retrievedPerson.getGender());
                if (retrievedPerson.getDateOfBirth() != null) {
                    System.out.println("Date of Birth: " + retrievedPerson.getDateOfBirth());
                }
                if (retrievedPerson.getPhoneNumber() != null) {
                    System.out.println("Phone Number: " + retrievedPerson.getPhoneNumber());
                }

                Location personLocation = disasterVictimService.getPersonLocation(retrievedPerson);

                if (personLocation != null) {
                    System.out.println("Location: " + personLocation.getName() + " (" + personLocation.getAddress() + ")");
                }

                DisasterVictim victim = disasterVictimService.getDisasterVictimById(personId);

                if (victim != null) {
                    disasterVictimService.refreshSupplies(victim);
                    disasterVictimService.refreshMedicalRecords(victim);

                    List<Supply> supplies = victim.getSupplies();

                    if (supplies != null && !supplies.isEmpty()) {
                        System.out.println("\nSupplies:");
                        for (Supply supply : supplies) {
                            System.out.println("- " + supply.getType() +
                                    (supply.getComments() != null ? " (" + supply.getComments() + ")" : ""));
                        }
                    }

                    List<MedicalRecord> medicalRecords = victim.getMedicalRecords();

                    if (medicalRecords != null && !medicalRecords.isEmpty()) {
                        System.out.println("\nMedical Records:");
                        for (MedicalRecord record : medicalRecords) {
                            System.out.println("- Record ID: " + record.getMedicalRecordId());
                            System.out.println("  Treatment: " + record.getTreatmentDetails());
                            System.out.println("  Date: " + record.getTreatmentDate());
                        }
                    }
                }

                if (retrievedPerson.getInFamilyGroup()) {
                    System.out.println("\nFamily Group ID: " + retrievedPerson.getFamilyGroupId());
                }

                System.out.println();

            } else {
                System.out.println("Person couldn't be found.");
            }
        } catch (Exception e) {
            System.out.println("Error occurred while trying to view person details: " + e.getMessage());
        }
    }


    public void managePersonInfo() {
        System.out.println(languageManager.getTranslation("input_person_id"));
        setRequiresIntInput(true);
        setMinIntInput(1);
        setMaxIntInput(Integer.MAX_VALUE);
        handleIntInput();
        selectedPersonId = intInput;

        currentState = State.MANAGE_INFO;
        setCurrentDisplay(MANAGE_INFO_OPTIONS);
        setMinIntInput(1);
        setMaxIntInput(MANAGE_INFO_OPTIONS.length - 1);
    }

    public void managePersonMedicalRecords() {
        System.out.println(languageManager.getTranslation("input_person_id"));
        setRequiresIntInput(true);
        setMinIntInput(1);
        setMaxIntInput(Integer.MAX_VALUE);
        handleIntInput();
        selectedPersonId = intInput;

        currentState = State.MANAGE_MEDICAL_RECORDS;
        setCurrentDisplay(MANAGE_MEDICAL_RECORDS_OPTIONS);
        setMinIntInput(1);
        setMaxIntInput(MANAGE_MEDICAL_RECORDS_OPTIONS.length - 1);
    }

    public void getPersonRelatives() {
        System.out.println(languageManager.getTranslation("input_person_id"));
        setRequiresIntInput(true);
        setMinIntInput(1);
        setMaxIntInput(Integer.MAX_VALUE);
        handleIntInput();
        int personId = intInput;

        try {
            Person retrievedPerson = personService.getPersonById(personId);

            if (retrievedPerson != null) {
                List<Person> relatives = personService.getPersonRelatives(retrievedPerson);

                if (relatives != null && !relatives.isEmpty()) {
                    System.out.println("Relatives of " + retrievedPerson.getFirstName() + ":");
                    System.out.println("----------------------------------");

                    for (Person relative : relatives) {
                        if (relative.getAssignedId() != retrievedPerson.getAssignedId()) {
                            System.out.println("ID: " + relative.getAssignedId());
                            System.out.println("Name: " + relative.getFirstName() + " " +
                                    (relative.getLastName() != null ? relative.getLastName() : ""));
                            System.out.println("Gender: " + relative.getGender());
                            if (relative.getDateOfBirth() != null) {
                                System.out.println("Date of Birth: " + relative.getDateOfBirth());
                            }
                            System.out.println();
                        }
                    }
                } else {
                    System.out.println("No relatives found for this person.");
                }
            } else {
                System.out.println("Person couldn't be found.");
            }
        } catch (Exception e) {
            System.out.println("Error occurred while trying to get person's relatives: " + e.getMessage());
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
            case MANAGE_INFO:
                processManageInfoInput();
                break;
            case MANAGE_MEDICAL_RECORDS:
                processManageMedicalRecordsInput();
                break;
        }
    }

    public void processDefaultInput() {
        switch(intInput) {
            case 1: // View all persons
                listAllPersons();
                break;

            case 2: // View specific details of a specific person
                viewPersonDetails();
                break;

            case 3: // Manage a person's information
                managePersonInfo();
                break;

            case 4: // Get a person's relatives
                getPersonRelatives();
                break;

            case 5: // Manage a person's medical records
                managePersonMedicalRecords();
                break;

            case 6: // Return to main menu
                menuManager.returnToMainMenu();
                break;

            default:
                System.out.println(languageManager.getTranslation("error_invalid_option"));
                break;
        }
    }

    public void processManageInfoInput() {
        try {
            Person retrievedPerson = personService.getPersonById(selectedPersonId);
            if (retrievedPerson == null) {
                System.out.println("Person not found. Returning to main submenu.");
                returnToDefaultState();
                return;
            }

            switch(intInput) {
                case 1: // Update first name
                    System.out.println("Enter the new first name: ");
                    setRequiresIntInput(false);
                    setStringNumbersAllowed(false);
                    setStringEmptyAllowed(false);
                    String newFirstName = handleStringInput();
                    boolean success = personService.updatePersonFirstName(retrievedPerson, newFirstName);
                    if (success) {
                        System.out.println("Successfully updated first name to " + newFirstName + ".");
                    } else {
                        System.out.println("Failed to update first name.");
                    }
                    break;

                case 2: // Update last name
                    System.out.println("Enter the new last name (or press Enter to remove): ");
                    setRequiresIntInput(false);
                    setStringNumbersAllowed(false);
                    setStringEmptyAllowed(true);
                    String newLastName = handleStringInput();
                    if (newLastName.isEmpty()) {
                        newLastName = null;
                    }
                    success = personService.updatePersonLastName(retrievedPerson, newLastName);
                    if (success) {
                        System.out.println("Successfully updated last name" + (newLastName != null ? " to " + newLastName : "") + ".");
                    } else {
                        System.out.println("Failed to update last name.");
                    }
                    break;

                case 3: // Update gender
                    String[] genderOptions = languageManager.getMenuTranslation("gender_options");
                    for (String option : genderOptions) {
                        System.out.println(option);
                    }

                    System.out.println("Enter the new gender by typing in a corresponding number (e.g. '1' for Male): ");
                    setRequiresIntInput(true);
                    setMinIntInput(1);
                    setMaxIntInput(3);
                    handleIntInput();

                    String newGender = switch (intInput) {
                        case 1 -> "Male";
                        case 2 -> "Female";
                        case 3 -> "Non-binary";
                        default -> null;
                    };

                    success = personService.updatePersonGender(retrievedPerson, newGender);

                    if (success) {
                        System.out.println("Successfully updated gender to " + newGender + ".");
                    } else {
                        System.out.println("Failed to update gender.");
                    }
                    break;

                case 4: // Update date of birth
                    System.out.println("Enter the new date of birth (e.g. 2000-12-31 for December 31, 2000), or press Enter to set to empty: ");
                    setRequiresIntInput(false);
                    setStringNumbersAllowed(true);
                    setStringEmptyAllowed(true);
                    String stringDateOfBirth = handleStringInput();
                    LocalDate newDateOfBirth = null;

                    if (!stringDateOfBirth.isEmpty()) {
                        Matcher dateMatcher = dateFormat.matcher(stringDateOfBirth);
                        while(!dateMatcher.matches()) {
                            System.out.println("Invalid date format. Enter a date in the format YYYY-MM-DD: ");
                            stringDateOfBirth = handleStringInput();
                            if (stringDateOfBirth.isEmpty()) {
                                break;
                            }
                            dateMatcher = dateFormat.matcher(stringDateOfBirth);
                        }
                        if (!stringDateOfBirth.isEmpty()) {
                            newDateOfBirth = LocalDate.parse(stringDateOfBirth);
                        }
                    }

                    success = personService.updatePersonDateOfBirth(retrievedPerson, newDateOfBirth);

                    if (success) {
                        System.out.println("Successfully updated date of birth" + (newDateOfBirth != null ? " to " + newDateOfBirth : "") + ".");
                    } else {
                        System.out.println("Failed to update date of birth.");
                    }
                    break;

                case 5: // Update phone number
                    System.out.println("Enter the new phone number (e.g. 123-456-7890), or press Enter to set to empty: ");
                    setRequiresIntInput(false);
                    setStringNumbersAllowed(true);
                    setStringEmptyAllowed(true);
                    String newPhoneNumber = handleStringInput();

                    if (!newPhoneNumber.isEmpty()) {
                        Matcher phoneNumberMatcher = phoneNumberFormat.matcher(newPhoneNumber);
                        while(!phoneNumberMatcher.matches()) {
                            System.out.println("Invalid phone number format. Enter a phone number in the format 123-456-7890: ");
                            newPhoneNumber = handleStringInput();
                            if (newPhoneNumber.isEmpty()) {
                                break;
                            }
                            phoneNumberMatcher = phoneNumberFormat.matcher(newPhoneNumber);
                        }
                    }

                    if (newPhoneNumber.isEmpty()) {
                        newPhoneNumber = null;
                    }

                    success = personService.updatePersonPhoneNumber(retrievedPerson, newPhoneNumber);

                    if (success) {
                        System.out.println("Successfully updated phone number" + (newPhoneNumber != null ? " to " + newPhoneNumber : "") + ".");
                    } else {
                        System.out.println("Failed to update phone number.");
                    }
                    break;

                case 6: // Update family group status
                    boolean currentStatus = retrievedPerson.getInFamilyGroup();

                    if (currentStatus) {
                        System.out.println("Person is currently in family group with ID: " + retrievedPerson.getFamilyGroupId());
                        System.out.println("Do you want to remove them from this family group? (1 for Yes, 2 for No): ");
                        setRequiresIntInput(true);
                        setMinIntInput(1);
                        setMaxIntInput(2);
                        handleIntInput();

                        if (intInput == 1) {
                            success = personService.updateFamilyGroupStatus(retrievedPerson, false, null);

                            if (success) {
                                System.out.println("Successfully removed person from family group.");
                            } else {
                                System.out.println("Failed to remove person from family group.");
                            }
                        }

                    } else {
                        System.out.println("Person is not currently in a family group.");
                        System.out.println("Do you want to add them to a family group? (1 for Yes, 2 for No): ");
                        setRequiresIntInput(true);
                        setMinIntInput(1);
                        setMaxIntInput(2);
                        handleIntInput();

                        if (intInput == 1) {
                            System.out.println("Enter the family group ID: ");
                            setMinIntInput(1);
                            setMaxIntInput(Integer.MAX_VALUE);
                            handleIntInput();
                            int familyGroupId = intInput;

                            success = personService.updateFamilyGroupStatus(retrievedPerson, true, familyGroupId);

                            if (success) {
                                System.out.println("Successfully added person to family group with ID: " + familyGroupId);
                            } else {
                                System.out.println("Failed to add person to family group.");
                            }
                        }
                    }

                    break;

                case 7: // Return to main submenu
                    returnToDefaultState();
                    break;

                default:
                    System.out.println(languageManager.getTranslation("error_invalid_option"));
                    break;
            }
        } catch (Exception e) {
            System.out.println("Error managing person information: " + e.getMessage());
        }
    }

    public void processManageMedicalRecordsInput() {
        try {
            DisasterVictim victim = disasterVictimService.getDisasterVictimById(selectedPersonId);

            if (victim == null) {
                System.out.println("Person not found or is not a disaster victim. Returning to main submenu.");
                returnToDefaultState();
                return;
            }

            disasterVictimService.refreshMedicalRecords(victim);

            switch(intInput) {
                case 1: // View all medical records
                    List<MedicalRecord> medicalRecords = victim.getMedicalRecords();

                    if (medicalRecords != null && !medicalRecords.isEmpty()) {
                        System.out.println("Medical records for " + victim.getFirstName() + ":");
                        System.out.println("------------------------------------");

                        for (MedicalRecord record : medicalRecords) {
                            System.out.println("Record ID: " + record.getMedicalRecordId());
                            System.out.println("Location ID: " + record.getLocationId());
                            System.out.println("Treatment Details: " + record.getTreatmentDetails());
                            System.out.println("Treatment Date: " + record.getTreatmentDate());
                            System.out.println();
                        }
                    } else {
                        System.out.println("No medical records found for this person.");
                    }

                    break;

                case 2: // Add a medical record
                    System.out.println("Enter the location ID for the treatment: ");
                    setRequiresIntInput(true);
                    setMinIntInput(1);
                    setMaxIntInput(Integer.MAX_VALUE);
                    handleIntInput();
                    int locationId = intInput;

                    System.out.println("Enter the treatment details: ");
                    setRequiresIntInput(false);
                    setStringNumbersAllowed(true);
                    setStringEmptyAllowed(false);
                    String treatmentDetails = handleStringInput();

                    MedicalRecord newRecord = disasterVictimService.addMedicalRecord(victim, locationId, treatmentDetails);

                    if (newRecord != null) {
                        System.out.println("Successfully added a new medical record with ID: " + newRecord.getMedicalRecordId());
                    } else {
                        System.out.println("Failed to add a new medical record.");
                    }

                    break;

                case 3: // Update a medical record
                    System.out.println("Enter the ID of the medical record to update: ");
                    setRequiresIntInput(true);
                    setMinIntInput(1);
                    setMaxIntInput(Integer.MAX_VALUE);
                    handleIntInput();
                    int recordId = intInput;

                    // Find the record
                    MedicalRecord recordToUpdate = null;
                    for (MedicalRecord record : victim.getMedicalRecords()) {
                        if (record.getMedicalRecordId() == recordId) {
                            recordToUpdate = record;
                            break;
                        }
                    }

                    if (recordToUpdate == null) {
                        System.out.println("Medical record not found for this person.");
                        break;
                    }

                    System.out.println("Enter the new treatment details: ");
                    setRequiresIntInput(false);
                    setStringNumbersAllowed(true);
                    setStringEmptyAllowed(false);
                    String newTreatmentDetails = handleStringInput();

                    boolean success = medicalRecordService.updateTreatmentDetails(recordToUpdate, newTreatmentDetails);

                    if (success) {
                        System.out.println("Successfully updated treatment details.");
                    } else {
                        System.out.println("Failed to update treatment details.");
                    }

                    break;

                case 4: // Remove a medical record
                    System.out.println("Enter the ID of the medical record to remove: ");
                    setRequiresIntInput(true);
                    setMinIntInput(1);
                    setMaxIntInput(Integer.MAX_VALUE);
                    handleIntInput();
                    int recordToRemoveId = intInput;

                    // Find the record
                    MedicalRecord recordToRemove = null;
                    for (MedicalRecord record : victim.getMedicalRecords()) {
                        if (record.getMedicalRecordId() == recordToRemoveId) {
                            recordToRemove = record;
                            break;
                        }
                    }

                    if (recordToRemove == null) {
                        System.out.println("Medical record not found for this person.");
                        break;
                    }

                    success = disasterVictimService.removeMedicalRecord(victim, recordToRemove);

                    if (success) {
                        System.out.println("Successfully removed medical record.");
                    } else {
                        System.out.println("Failed to remove medical record.");
                    }

                    break;

                case 5: // Return to main submenu
                    returnToDefaultState();
                    break;

                default:
                    System.out.println(languageManager.getTranslation("error_invalid_option"));
                    break;
            }
        } catch (Exception e) {
            System.out.println("Error managing person's medical records: " + e.getMessage());
        }
    }
}
