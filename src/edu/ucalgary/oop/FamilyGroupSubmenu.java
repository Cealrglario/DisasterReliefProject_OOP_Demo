package edu.ucalgary.oop;

public class FamilyGroupSubmenu extends Menu {
    private final String[] MANAGE_GROUP_OPTIONS = new String[0];

    public FamilyGroupSubmenu(String[] defaultOptions) {
        super(defaultOptions);
    }

    public String[] getManageGroupOptions() {
        return this.MANAGE_GROUP_OPTIONS;
    }

    public void listAllFamilyGroups() {}

    public void viewFamilyMembers() {}

    public void manageFamilyGroup() {}
}