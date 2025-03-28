package edu.ucalgary.oop;

public class InquirySubmenu extends Menu {
    private final String[] LOG_INQUIRY_OPTIONS = new String[0];
    private final String[] MANAGE_INQUIRY_OPTIONS = new String[0];

    public InquirySubmenu(String[] defaultOptions) {
        super(defaultOptions);
    }

    public String[] getLogInquiryOptions() {
        return this.LOG_INQUIRY_OPTIONS;
    }

    public String[] getManageInquiryOptions() {
        return this.MANAGE_INQUIRY_OPTIONS;
    }

    public void logNewInquiry() {}

    public void viewInquiryDetails() {}

    public void listAllInquiries() {}

    public void manageInquiryInformation() {}

}
