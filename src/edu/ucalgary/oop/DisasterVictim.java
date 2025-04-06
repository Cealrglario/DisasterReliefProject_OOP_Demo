package edu.ucalgary.oop;

import java.time.LocalDate;
import java.util.List;
import java.util.Stack;

public class DisasterVictim extends Person {
    private List<MedicalRecord> medicalRecords;
    private List<PersonalBelonging> personalBelongings;
    private final LocalDate ENTRY_DATE;
    private Stack<String> comments;

    public DisasterVictim(int assignedId, String firstName, String gender, LocalDate dateOfEntry, String phoneNumber) {
        super(assignedId, firstName, gender, phoneNumber);
        this.ENTRY_DATE = dateOfEntry;
    }

    public DisasterVictim(int assignedId, String firstName, String gender, LocalDate dateOfBirth, LocalDate dateOfEntry, String phoneNumber) {
        super(assignedId, firstName, gender, dateOfBirth, phoneNumber);
        this.ENTRY_DATE = dateOfEntry;
    }

    public List<MedicalRecord> getMedicalRecords() {}

    public void setMedicalRecords(List<MedicalRecord> records) {}

    public List<PersonalBelonging> getPersonalBelongings() {}

    public void setPersonalBelongings(List<PersonalBelonging> personalBelongings) {}

    public LocalDate getEntryDate() {}

    public Stack<String> getComments() {}

    public void setComments(Stack<String> comments) {}

    public void addComment(String comment) {}

    public void undoAddComment() {}

    public void addPersonalBelonging(PersonalBelonging newBelonging) {}

    public void removePersonalBelonging(PersonalBelonging unwantedBelonging) {}

    public void addMedicalRecord(MedicalRecord newRecord) {}

    public void removeMedicalRecord(MedicalRecord unwantedRecord) {}
}
