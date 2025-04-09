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

    public boolean setCommonFamilyName(String commonFamilyName) {
        if (commonFamilyName.isEmpty()) {
            return false;
        } else {
            this.commonFamilyName = commonFamilyName;
            return true;
        }
    }

    public HashSet<Person> getMembers() {
        return this.members;
    }

    public void setMembers(HashSet<Person> members) {
        this.members = members;
    }

    public void addMember(Person member) {
        this.members.add(member);
    }

    public boolean removeMember(Person exMember) {
        if (members.contains(exMember)) {
            members.remove(exMember);
            return true;
        } else {
            return false;
        }
    }
}
