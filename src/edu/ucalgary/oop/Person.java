package edu.ucalgary.oop;

import java.time.LocalDate;

public class Person {
    private int ASSIGNED_ID;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String gender;
    private boolean inFamilyGroup;
    private String phoneNumber;

    public Person(int assignedId, String firstName, String gender, String phoneNumber) {
        this.ASSIGNED_ID = assignedId;
        this.firstName = firstName;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
    }

    public Person(int assignedId, String firstName, String gender, LocalDate dateOfBirth, String phoneNumber) {
        this.ASSIGNED_ID = assignedId;
        this.firstName = firstName;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.phoneNumber = phoneNumber;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {}

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {}

    public LocalDate getDateOfBirth() {
        return this.dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {}

    public String getGender() {
        return this.gender;
    }

    public void setGender(String gender) {}

    public boolean getInFamilyGroup() {
        return this.inFamilyGroup;
    }

    public void setInFamilyGroup(boolean inFamilyGroup) {}

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {}

    public Person[] getRelatives() {
        return null;
    }

    public void setRelatives(Person[] relatives) {};

    public int getAssignedId() {
        return this.ASSIGNED_ID;
    }
}
