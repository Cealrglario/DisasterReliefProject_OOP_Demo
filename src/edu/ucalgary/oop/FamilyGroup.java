package edu.ucalgary.oop;

import java.util.HashSet;

public class FamilyGroup {
    private final int GROUP_ID;
    private String commonFamilyName;
    private HashSet<Person> members;

    public FamilyGroup(int groupId) {
        this.GROUP_ID = groupId;
    }

    public FamilyGroup(int groupId, HashSet<Person> members) {
        this.GROUP_ID = groupId;
        this.members = members;
    }

    public int getGroupId() {
        return this.GROUP_ID;
    }

    public String getCommonFamilyName() {
        return this.commonFamilyName;
    }

    public void setCommonFamilyName(String commonFamilyName) {}

    public HashSet<Person> getMembers() {
        return this.members;
    }

    public void setMembers(HashSet<Person> members) {}

    public void addMember(Person member) {}

    public void removeMember(Person exMember) {}
}
