package edu.ucalgary.oop;

import java.util.HashSet;

public class FamilyGroup {
    private final int GROUP_ID;
    private String commonFamilyName;
    private HashSet<Person> members;

    public FamilyGroup(int groupId) {
        this.GROUP_ID = groupId;
        this.members = new HashSet<>();
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

    public boolean setCommonFamilyName(String commonFamilyName) {}

    public HashSet<Person> getMembers() {
        return this.members;
    }

    public boolean setMembers(HashSet<Person> members) {}

    public boolean addMember(Person member) {}

    public boolean removeMember(Person exMember) {}
}
