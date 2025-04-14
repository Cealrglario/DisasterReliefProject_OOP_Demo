package edu.ucalgary.oop;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class InquirySubmenu extends Menu {
    private final String[] LOG_INQUIRY_OPTIONS = languageManager.getMenuTranslation("inquiry_submenu_log_inquiry");
    private final String[] MANAGE_INQUIRY_OPTIONS = languageManager.getMenuTranslation("inquiry_submenu_manage_inquiry");
    private final InquiryService inquiryService = InquiryService.INSTANCE;

    private enum State {DEFAULT, LOG_INQUIRY, MANAGE_INQUIRY}
    private State currentState = State.DEFAULT;

    private int inquirerId;
    private int seekingId;
    private String comments;
    private int selectedInquiryId;

    public InquirySubmenu(String[] defaultOptions) {
        super(defaultOptions);
        setMinIntInput(1);
        setMaxIntInput(defaultOptions.length - 1);
        setRequiresIntInput(true);
    }

    public String[] getLogInquiryOptions() {
        return this.LOG_INQUIRY_OPTIONS;
    }

    public String[] getManageInquiryOptions() {
        return this.MANAGE_INQUIRY_OPTIONS;
    }

    public void logNewInquiry() {
        currentState = State.LOG_INQUIRY;
        setCurrentDisplay(LOG_INQUIRY_OPTIONS);

        System.out.println(languageManager.getTranslation("input_inquirer_id"));
        setRequiresIntInput(true);
        setMinIntInput(1);
        setMaxIntInput(Integer.MAX_VALUE);
        handleIntInput();
        inquirerId = intInput;

        System.out.println(languageManager.getTranslation("input_seeking_id"));
        handleIntInput();
        seekingId = intInput;

        System.out.println(languageManager.getTranslation("input_comments"));
        setRequiresIntInput(false);
        setStringEmptyAllowed(false);
        setStringNumbersAllowed(true);
        comments = handleStringInput();

        try {
            LocalDate currentDate = LocalDate.now();
            Inquiry newInquiry = inquiryService.addInquiry(inquirerId, seekingId, currentDate, comments);

            if (newInquiry != null) {
                System.out.println(languageManager.getTranslation("inquiry_added_success"));
            } else {
                System.out.println(languageManager.getTranslation("inquiry_added_failure"));
            }
        } catch (SQLException e) {
            System.out.println(languageManager.getTranslation("database_error") + ": " + e.getMessage());
        }

        returnToDefaultState();
    }

    public void viewInquiryDetails() {
        System.out.println(languageManager.getTranslation("input_inquiry_id"));
        setRequiresIntInput(true);
        setMinIntInput(1);
        setMaxIntInput(Integer.MAX_VALUE);
        handleIntInput();
        selectedInquiryId = intInput;

        try {
            Inquiry inquiry = inquiryService.getInquiryById(selectedInquiryId);

            if (inquiry != null) {
                System.out.println(languageManager.getTranslation("inquiry_id") + ": " + inquiry.getInquiryId());
                System.out.println(languageManager.getTranslation("inquirer_id") + ": " + inquiry.getInquirerid());
                System.out.println(languageManager.getTranslation("seeking_id") + ": " + inquiry.getMissingPersonId());
                System.out.println(languageManager.getTranslation("date_of_inquiry") + ": " + inquiry.getDateOfInquiry());
                System.out.println(languageManager.getTranslation("comments") + ": " + inquiry.getInfoProvided());

                if (inquiry.getLastKnownLocationId() > 0) {
                    System.out.println(languageManager.getTranslation("last_known_location") + ": " +
                            inquiry.getLastKnownLocationId());
                }
            } else {
                System.out.println(languageManager.getTranslation("inquiry_not_found"));
            }
        } catch (SQLException e) {
            System.out.println(languageManager.getTranslation("database_error") + ": " + e.getMessage());
        }
    }

    public void listAllInquiries() {
        try {
            List<Inquiry> inquiries = inquiryService.getAllInquiries();

            if (inquiries != null && !inquiries.isEmpty()) {
                int counter = 0;
                for (Inquiry inquiry : inquiries) {
                    counter++;
                    System.out.println(String.format(languageManager.getTranslation("inquiry_number"), counter));
                    System.out.println(languageManager.getTranslation("separator"));
                    System.out.println(languageManager.getTranslation("inquiry_id") + ": " +
                            inquiryService.getInquiryInfo("inquiry_id", inquiry.getInquiryId()));
                    System.out.println(languageManager.getTranslation("inquirer_id") + ": " +
                            inquiryService.getInquiryInfo("inquirer_id", inquiry.getInquiryId()));
                    System.out.println(languageManager.getTranslation("missing_person_id") + ": " +
                            inquiryService.getInquiryInfo("seeking_id", inquiry.getInquiryId()));
                    System.out.println(languageManager.getTranslation("last_known_location_id") + ": " +
                            inquiryService.getInquiryInfo("location_id", inquiry.getInquiryId()));
                    System.out.println(languageManager.getTranslation("date_of_inquiry") + ": " +
                            inquiryService.getInquiryInfo("date_of_inquiry", inquiry.getInquiryId()));
                    System.out.println(languageManager.getTranslation("comments") + ": " +
                            inquiryService.getInquiryInfo("comments", inquiry.getInquiryId()));
                    System.out.println();
                }
            } else {
                System.out.println(languageManager.getTranslation("no_inquiries_found"));
            }
        } catch (SQLException e) {
            System.out.println(languageManager.getTranslation("database_error") + ": " + e.getMessage());
        }
    }

    public void manageInquiryInformation() {
        System.out.println(languageManager.getTranslation("input_inquiry_id_to_manage"));
        setRequiresIntInput(true);
        setMinIntInput(1);
        setMaxIntInput(Integer.MAX_VALUE);
        handleIntInput();
        selectedInquiryId = intInput;

        currentState = State.MANAGE_INQUIRY;
        setCurrentDisplay(MANAGE_INQUIRY_OPTIONS);
        setMinIntInput(1);
        setMaxIntInput(MANAGE_INQUIRY_OPTIONS.length - 1);
    }

    private void returnToDefaultState() {
        currentState = State.DEFAULT;
        setCurrentDisplay(getDefaultOptions());
        setRequiresIntInput(true);
        setMinIntInput(1);
        setMaxIntInput(getDefaultOptions().length - 1);
    }

    @Override
    public void processInput() {
        switch (currentState) {
            case DEFAULT:
                processDefaultInput();
                break;
            case LOG_INQUIRY:
                break;
            case MANAGE_INQUIRY:
                processManageInquiryInput();
                break;
        }
    }

    private void processDefaultInput() {
        switch (intInput) {
            case 1: // Log a new inquiry
                logNewInquiry();
                break;

            case 2: // View the details of a specific inquiry
                viewInquiryDetails();
                break;

            case 3: // View all inquiries
                listAllInquiries();
                break;

            case 4: // Manage the information of a specific inquiry
                manageInquiryInformation();
                break;

            case 5: // Return to main menu
                menuManager.returnToMainMenu();
                break;

            default:
                System.out.println(languageManager.getTranslation("error_invalid_option"));
                break;
        }
    }

    private void processManageInquiryInput() {
        try {
            Inquiry inquiry = inquiryService.getInquiryById(selectedInquiryId);

            if (inquiry == null) {
                System.out.println(languageManager.getTranslation("inquiry_not_found"));
                returnToDefaultState();
                return;
            }

            switch (intInput) {
                case 1: // Update comments
                    System.out.println(languageManager.getTranslation("input_new_comments"));
                    setRequiresIntInput(false);
                    setStringEmptyAllowed(false);
                    setStringNumbersAllowed(true);
                    String newComments = handleStringInput();

                    boolean updateSuccess = inquiryService.updateInquiryInfo(inquiry, newComments);
                    if (updateSuccess) {
                        System.out.println(languageManager.getTranslation("inquiry_updated_success"));
                    } else {
                        System.out.println(languageManager.getTranslation("inquiry_updated_failure"));
                    }
                    break;

                case 2: // Update last known location
                    System.out.println(languageManager.getTranslation("input_new_location_id"));
                    setRequiresIntInput(true);
                    setMinIntInput(1);
                    setMaxIntInput(Integer.MAX_VALUE);
                    handleIntInput();
                    int newLocationId = intInput;

                    updateSuccess = inquiryService.updateLastKnownLocation(inquiry, newLocationId);
                    if (updateSuccess) {
                        System.out.println(languageManager.getTranslation("location_updated_success"));
                    } else {
                        System.out.println(languageManager.getTranslation("location_updated_failure"));
                    }
                    break;

                case 3: // Return to main submenu
                    returnToDefaultState();
                    return;
            }

            returnToDefaultState();

        } catch (SQLException e) {
            System.out.println(languageManager.getTranslation("database_error") + ": " + e.getMessage());
            returnToDefaultState();
        }
    }
}
