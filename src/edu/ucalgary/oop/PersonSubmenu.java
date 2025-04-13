package edu.ucalgary.oop;

public class PersonSubmenu extends Menu {
    private final String[] PERSON_OPTIONS = languageManager.getMenuTranslation("person_default_options");
    private final String[] MANAGE_PERSON_OPTIONS = languageManager.getMenuTranslation("person_manage_options");

    public PersonSubmenu(String[] defaultOptions) {
        super(defaultOptions);
    }

    public String[] getPersonOptions() {
        return this.PERSON_OPTIONS;
    }

    public String[] getManagePersonOptions() {
        return this.MANAGE_PERSON_OPTIONS;
    }

    public void viewPersonInfo() {}

    public void managePersonInfo() {}

    public void getPersonRelatives() {}

    public void managePersonMedicalRecords() {}

    @Override
    public void processInput() {}
}

