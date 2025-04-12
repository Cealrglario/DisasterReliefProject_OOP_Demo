package edu.ucalgary.oop;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class DisasterVictim extends Person {
    private List<MedicalRecord> medicalRecords = new ArrayList<>();
    private List<Supply> supplies = new ArrayList<>();
    private final LocalDate ENTRY_DATE;
    private String comments;

    public DisasterVictim(int assignedId, String firstName, String gender, LocalDate dateOfEntry, String phoneNumber) {
        super(assignedId, firstName, gender, phoneNumber);
        this.ENTRY_DATE = dateOfEntry;
    }

    public DisasterVictim(int assignedId, String firstName, String gender, LocalDate dateOfBirth, LocalDate dateOfEntry, String phoneNumber) {
        super(assignedId, firstName, gender, dateOfBirth, phoneNumber);
        this.ENTRY_DATE = dateOfEntry;
    }

    public List<MedicalRecord> getMedicalRecords() {
        return this.medicalRecords;
    }

    public void setMedicalRecords(List<MedicalRecord> records) {
        this.medicalRecords = records;
    }

    public List<Supply> getSupplies() {
        return this.supplies;
    }

    public void setSupplies(List<Supply> supplies) {
        this.supplies = supplies;
    }

    public LocalDate getEntryDate() {
        return this.ENTRY_DATE;
    }

    public String getComments() {
        return this.comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public void addSupply(Supply newSupply) {
        this.supplies.add(newSupply);
    }

    public void removeSupply(Supply unwantedSupply) {
        this.supplies.remove(unwantedSupply);
    }

    public void addMedicalRecord(MedicalRecord newRecord) {
        this.medicalRecords.add(newRecord);
    }

    public void removeMedicalRecord(MedicalRecord unwantedRecord) {
        this.medicalRecords.remove(unwantedRecord);
    }
}
