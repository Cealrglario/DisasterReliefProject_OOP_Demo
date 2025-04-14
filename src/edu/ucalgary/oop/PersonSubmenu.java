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
            List<Person> retrievedPersons = null;
            retrievedPersons = personService.getAllPersons();
            System.out.println(languageManager.getTranslation("persons_and_details"));
            System.out.println(languageManager.getTranslation("separator"));
            if (retrievedPersons != null) {
                int counter = 0;
                for (Person person : retrievedPersons) {
                    counter++;
                    System.out.println(String.format(languageManager.getTranslation("person_number"), counter));
                    System.out.println(languageManager.getTranslation("separator_short"));
                    System.out.println(languageManager.getTranslation("person_id") + ": " + person.getAssignedId());
                    System.out.println(languageManager.getTranslation("first_name") + ": " + person.getFirstName());
                    if (person.getLastName() != null) {
                        System.out.println(languageManager.getTranslation("last_name") + ": " + person.getLastName());
                    }
                    System.out.println(languageManager.getTranslation("gender") + ": " + person.getGender());
                    if (person.getDateOfBirth() != null) {
                        System.out.println(languageManager.getTranslation("date_of_birth") + ": " + person.getDateOfBirth());
                    }
                    if (person.getPhoneNumber() != null) {
                        System.out.println(languageManager.getTranslation("phone_number") + ": " + person.getPhoneNumber());
                    }
                    System.out.println();
                }
            } else {
                System.out.println(languageManager.getTranslation("no_persons_found"));
            }
        } catch (Exception e) {
            System.out.println(languageManager.getTranslation("list_persons_error") + ": " + e.getMessage());
            System.out.println(languageManager.getTranslation("try_again_later"));
        }
    }

    public void viewPersonDetails() {
        System.out.println(languageManager.getTranslation("input_person_id"));
        setRequiresIntInput(true);
        setMinIntInput(1);
        setMaxIntInput(Integer.MAX_VALUE);
        handleIntInput();
        int personId = intInput;

        try {
            Person retrievedPerson = personService.getPersonById(personId);
            if (retrievedPerson != null) {
                System.out.println(languageManager.getTranslation("person_details"));
                System.out.println(languageManager.getTranslation("separator_short"));
                System.out.println(languageManager.getTranslation("person_id") + ": " + retrievedPerson.getAssignedId());
                System.out.println(languageManager.getTranslation("first_name") + ": " + retrievedPerson.getFirstName());
                if (retrievedPerson.getLastName() != null) {
                    System.out.println(languageManager.getTranslation("last_name") + ": " + retrievedPerson.getLastName());
                }
                System.out.println(languageManager.getTranslation("gender") + ": " + retrievedPerson.getGender());
                if (retrievedPerson.getDateOfBirth() != null) {
                    System.out.println(languageManager.getTranslation("date_of_birth") + ": " + retrievedPerson.getDateOfBirth());
                }
                if (retrievedPerson.getPhoneNumber() != null) {
                    System.out.println(languageManager.getTranslation("phone_number") + ": " + retrievedPerson.getPhoneNumber());
                }

                // Get and display location
                Location personLocation = disasterVictimService.getPersonLocation(retrievedPerson);
                if (personLocation != null) {
                    System.out.println(languageManager.getTranslation("location") + ": " + personLocation.getName() + " (" + personLocation.getAddress() + ")");
                }

                // Display family group info
                if (retrievedPerson.getInFamilyGroup()) {
                    System.out.println(languageManager.getTranslation("family_group_id") + ": " + retrievedPerson.getFamilyGroupId());
                }

                // If person is a disaster victim, display more information
                DisasterVictim victim = disasterVictimService.getDisasterVictimById(personId);
                if (victim != null) {
                    disasterVictimService.refreshSupplies(victim);
                    disasterVictimService.refreshMedicalRecords(victim);

                    // Display supplies
                    List<Supply> supplies = victim.getSupplies();
                    if (supplies != null && !supplies.isEmpty()) {
                        System.out.println("\n" + languageManager.getTranslation("supplies") + ":");
                        for (Supply supply : supplies) {
                            System.out.println("- " + supply.getType() +
                                    (supply.getComments() != null ? " (" + supply.getComments() + ")" : ""));
                        }
                    }

                    // Display medical records
                    List<MedicalRecord> medicalRecords = victim.getMedicalRecords();
                    if (medicalRecords != null && !medicalRecords.isEmpty()) {
                        System.out.println("\n" + languageManager.getTranslation("medical_records") + ":");
                        for (MedicalRecord record : medicalRecords) {
                            System.out.println("- " + languageManager.getTranslation("record_id") + ": " + record.getMedicalRecordId());
                            System.out.println("  " + languageManager.getTranslation("treatment") + ": " + record.getTreatmentDetails());
                            System.out.println("  " + languageManager.getTranslation("date") + ": " + record.getTreatmentDate());
                        }
                    }
                }

                System.out.println();
            } else {
                System.out.println(languageManager.getTranslation("person_not_found"));
            }
        } catch (Exception e) {
            System.out.println(languageManager.getTranslation("view_person_error") + ": " + e.getMessage());
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
                    System.out.println(String.format(languageManager.getTranslation("relatives_of"), retrievedPerson.getFirstName()));
                    System.out.println(languageManager.getTranslation("separator_short"));
                    for (Person relative : relatives) {
                        if (relative.getAssignedId() != retrievedPerson.getAssignedId()) {
                            System.out.println(languageManager.getTranslation("id") + ": " + relative.getAssignedId());
                            System.out.println(languageManager.getTranslation("name") + ": " + relative.getFirstName() + " " +
                                    (relative.getLastName() != null ? relative.getLastName() : ""));
                            System.out.println(languageManager.getTranslation("gender") + ": " + relative.getGender());
                            if (relative.getDateOfBirth() != null) {
                                System.out.println(languageManager.getTranslation("date_of_birth") + ": " + relative.getDateOfBirth());
                            }
                            System.out.println();
                        }
                    }
                } else {
                    System.out.println(languageManager.getTranslation("no_relatives_found"));
                }
            } else {
                System.out.println(languageManager.getTranslation("person_not_found"));
            }
        } catch (Exception e) {
            System.out.println(languageManager.getTranslation("get_relatives_error") + ": " + e.getMessage());
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
            case 2: // View details of a specific person
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
                System.out.println(languageManager.getTranslation("person_not_found_returning"));
                returnToDefaultState();
                return;
            }

            switch(intInput) {
                case 1: // Update first name
                    System.out.println(languageManager.getTranslation("enter_new_first_name"));
                    setRequiresIntInput(false);
                    setStringNumbersAllowed(false);
                    setStringEmptyAllowed(false);
                    String newFirstName = handleStringInput();
                    boolean success = personService.updatePersonFirstName(retrievedPerson, newFirstName);
                    if (success) {
                        System.out.println(String.format(languageManager.getTranslation("first_name_updated_success"), newFirstName));
                    } else {
                        System.out.println(languageManager.getTranslation("first_name_update_failed"));
                    }
                    break;

                case 2: // Update last name
                    System.out.println(languageManager.getTranslation("enter_new_last_name"));
                    setRequiresIntInput(false);
                    setStringNumbersAllowed(false);
                    setStringEmptyAllowed(true);
                    String newLastName = handleStringInput();
                    if (newLastName.isEmpty()) {
                        newLastName = null;
                    }
                    success = personService.updatePersonLastName(retrievedPerson, newLastName);
                    if (success) {
                        if (newLastName != null) {
                            System.out.println(String.format(languageManager.getTranslation("last_name_updated_success"), newLastName));
                        } else {
                            System.out.println(languageManager.getTranslation("last_name_removed_success"));
                        }
                    } else {
                        System.out.println(languageManager.getTranslation("last_name_update_failed"));
                    }
                    break;

                case 3: // Update gender
                    String[] genderOptions = languageManager.getMenuTranslation("gender_options");
                    for (String option : genderOptions) {
                        System.out.println(option);
                    }

                    System.out.println(languageManager.getTranslation("enter_new_gender"));
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

                    // Reset the min/max int input range to match menu options
                    setMinIntInput(1);
                    setMaxIntInput(MANAGE_INFO_OPTIONS.length - 1);

                    success = personService.updatePersonGender(retrievedPerson, newGender);
                    if (success) {
                        System.out.println(String.format(languageManager.getTranslation("gender_updated_success"), newGender));
                    } else {
                        System.out.println(languageManager.getTranslation("gender_update_failed"));
                    }
                    break;

                case 4: // Update date of birth
                    System.out.println(languageManager.getTranslation("enter_new_dob"));
                    setRequiresIntInput(false);
                    setStringNumbersAllowed(true);
                    setStringEmptyAllowed(true);
                    String stringDateOfBirth = handleStringInput();
                    LocalDate newDateOfBirth = null;

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
                            newDateOfBirth = LocalDate.parse(stringDateOfBirth);
                        }
                    }
                    success = personService.updatePersonDateOfBirth(retrievedPerson, newDateOfBirth);
                    if (success) {
                        if (newDateOfBirth != null) {
                            System.out.println(String.format(languageManager.getTranslation("dob_updated_success"), newDateOfBirth));
                        } else {
                            System.out.println(languageManager.getTranslation("dob_removed_success"));
                        }
                    } else {
                        System.out.println(languageManager.getTranslation("dob_update_failed"));
                    }
                    break;

                case 5: // Update phone number
                    System.out.println(languageManager.getTranslation("enter_new_phone"));
                    setRequiresIntInput(false);
                    setStringNumbersAllowed(true);
                    setStringEmptyAllowed(true);
                    String newPhoneNumber = handleStringInput();
                    if (!newPhoneNumber.isEmpty()) {
                        Matcher phoneNumberMatcher = phoneNumberFormat.matcher(newPhoneNumber);
                        while(!phoneNumberMatcher.matches()) {
                            System.out.println(languageManager.getTranslation("invalid_phone_format"));
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
                        if (newPhoneNumber != null) {
                            System.out.println(String.format(languageManager.getTranslation("phone_updated_success"), newPhoneNumber));
                        } else {
                            System.out.println(languageManager.getTranslation("phone_removed_success"));
                        }
                    } else {
                        System.out.println(languageManager.getTranslation("phone_update_failed"));
                    }
                    break;

                case 6: // Update family group status
                    boolean currentStatus = retrievedPerson.getInFamilyGroup();
                    if (currentStatus) {
                        System.out.println(String.format(languageManager.getTranslation("in_family_group"), retrievedPerson.getFamilyGroupId()));
                        System.out.println(languageManager.getTranslation("remove_from_family_group"));
                        setRequiresIntInput(true);
                        setMinIntInput(1);
                        setMaxIntInput(2);
                        handleIntInput();
                        if (intInput == 1) {
                            success = personService.updateFamilyGroupStatus(retrievedPerson, false, null);
                            if (success) {
                                System.out.println(languageManager.getTranslation("removed_from_family_group_success"));
                            } else {
                                System.out.println(languageManager.getTranslation("removed_from_family_group_failed"));
                            }
                        }
                    } else {
                        System.out.println(languageManager.getTranslation("not_in_family_group"));
                        System.out.println(languageManager.getTranslation("add_to_family_group"));
                        setRequiresIntInput(true);
                        setMinIntInput(1);
                        setMaxIntInput(2);
                        handleIntInput();
                        if (intInput == 1) {
                            System.out.println(languageManager.getTranslation("enter_family_group_id"));
                            setMinIntInput(1);
                            setMaxIntInput(Integer.MAX_VALUE);
                            handleIntInput();
                            int familyGroupId = intInput;
                            success = personService.updateFamilyGroupStatus(retrievedPerson, true, familyGroupId);
                            if (success) {
                                System.out.println(String.format(languageManager.getTranslation("added_to_family_group_success"), familyGroupId));
                            } else {
                                System.out.println(languageManager.getTranslation("added_to_family_group_failed"));
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
            System.out.println(languageManager.getTranslation("manage_person_error") + ": " + e.getMessage());
        }
    }

    public void processManageMedicalRecordsInput() {
        try {
            DisasterVictim victim = disasterVictimService.getDisasterVictimById(selectedPersonId);
            if (victim == null) {
                System.out.println(languageManager.getTranslation("not_disaster_victim_returning"));
                returnToDefaultState();
                return;
            }

            disasterVictimService.refreshMedicalRecords(victim);

            switch(intInput) {
                case 1: // View all medical records
                    List<MedicalRecord> medicalRecords = victim.getMedicalRecords();
                    if (medicalRecords != null && !medicalRecords.isEmpty()) {
                        System.out.println(String.format(languageManager.getTranslation("medical_records_for"), victim.getFirstName()));
                        System.out.println(languageManager.getTranslation("separator_short"));
                        for (MedicalRecord record : medicalRecords) {
                            System.out.println(languageManager.getTranslation("record_id") + ": " + record.getMedicalRecordId());
                            System.out.println(languageManager.getTranslation("location_id") + ": " + record.getLocationId());
                            System.out.println(languageManager.getTranslation("treatment_details") + ": " + record.getTreatmentDetails());
                            System.out.println(languageManager.getTranslation("treatment_date") + ": " + record.getTreatmentDate());
                            System.out.println();
                        }
                    } else {
                        System.out.println(languageManager.getTranslation("no_medical_records"));
                    }
                    break;

                case 2: // Add a medical record
                    System.out.println(languageManager.getTranslation("enter_location_id"));
                    setRequiresIntInput(true);
                    setMinIntInput(1);
                    setMaxIntInput(Integer.MAX_VALUE);
                    handleIntInput();
                    int locationId = intInput;

                    System.out.println(languageManager.getTranslation("enter_treatment_details"));
                    setRequiresIntInput(false);
                    setStringNumbersAllowed(true);
                    setStringEmptyAllowed(false);
                    String treatmentDetails = handleStringInput();

                    MedicalRecord newRecord = disasterVictimService.addMedicalRecord(victim, locationId, treatmentDetails);
                    if (newRecord != null) {
                        System.out.println(String.format(languageManager.getTranslation("medical_record_added_success"), newRecord.getMedicalRecordId()));
                    } else {
                        System.out.println(languageManager.getTranslation("medical_record_added_failed"));
                    }
                    break;

                case 3: // Update a medical record
                    System.out.println(languageManager.getTranslation("enter_record_id_to_update"));
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
                        System.out.println(languageManager.getTranslation("medical_record_not_found"));
                        break;
                    }

                    System.out.println(languageManager.getTranslation("enter_new_treatment_details"));
                    setRequiresIntInput(false);
                    setStringNumbersAllowed(true);
                    setStringEmptyAllowed(false);
                    String newTreatmentDetails = handleStringInput();

                    boolean success = medicalRecordService.updateTreatmentDetails(recordToUpdate, newTreatmentDetails);
                    if (success) {
                        System.out.println(languageManager.getTranslation("treatment_details_updated_success"));
                    } else {
                        System.out.println(languageManager.getTranslation("treatment_details_updated_failed"));
                    }
                    break;

                case 4: // Remove a medical record
                    System.out.println(languageManager.getTranslation("enter_record_id_to_remove"));
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
                        System.out.println(languageManager.getTranslation("medical_record_not_found"));
                        break;
                    }

                    success = disasterVictimService.removeMedicalRecord(victim, recordToRemove);
                    if (success) {
                        System.out.println(languageManager.getTranslation("medical_record_removed_success"));
                    } else {
                        System.out.println(languageManager.getTranslation("medical_record_removed_failed"));
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
            System.out.println(languageManager.getTranslation("manage_medical_records_error") + ": " + e.getMessage());
        }
    }
}
