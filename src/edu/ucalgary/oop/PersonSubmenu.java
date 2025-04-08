package edu.ucalgary.oop;

public class PersonSubmenu extends Menu {
    private String[] PERSON_OPTIONS;
    private String[] MANAGE_PERSON_OPTIONS;

    public PersonSubmenu(String[] defaultOptions) {
        super(defaultOptions);
    }

    public String[] getPersonOptions() {
        return this.PERSON_OPTIONS;
    }

    public String[] getManagePersonOptions() {
        return this.MANAGE_PERSON_OPTIONS;
    }

    public void listAllPersons() {}

    public void addNewPerson() {}

    public void viewPersonInfo() {}

    public void managePersonInfo() {}

    public void getPersonRelatives() {}

    @Override
    public void processInput() {}
}

