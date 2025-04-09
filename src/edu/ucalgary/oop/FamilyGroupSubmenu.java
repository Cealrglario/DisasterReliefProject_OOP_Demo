package edu.ucalgary.oop;

public class FamilyGroupSubmenu extends Menu {
    private final String[] MANAGE_GROUP_OPTIONS = languageManager.getMenuTranslation("manage_family_group_options");

    public FamilyGroupSubmenu(String[] defaultOptions) {
        super(defaultOptions);
    }

    public String[] getManageGroupOptions() {
        return this.MANAGE_GROUP_OPTIONS;
    }

    public void listAllFamilyGroups() {
        setCurrentDisplay(DEFAULT_OPTIONS);
    }

    public void viewFamilyMembers() {
        setCurrentDisplay(DEFAULT_OPTIONS);
    }

    public void manageFamilyGroup() {
        setCurrentDisplay(DEFAULT_OPTIONS);
    }

    @Override
    public void processInput() {
        switch(intInput) {
            case 1:
                listAllFamilyGroups();
                break;
            case 2:
                viewFamilyMembers();
                break;
            case 3:
                manageFamilyGroup();
                break;
        }
    }
}