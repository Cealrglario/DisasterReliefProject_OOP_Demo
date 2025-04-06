package edu.ucalgary.oop;

import java.time.LocalDate;

public class MedicalRecord {
    private final int LOCATION_ID;
    private final int MEDICAL_RECORD_ID;
    private String treatmentDetails;
    private final LocalDate TREATMENT_DATE;

    public MedicalRecord(int medicalRecordId, int locationId,  String treatmentDetails) {
        this.LOCATION_ID = locationId;
        this.MEDICAL_RECORD_ID = medicalRecordId;
        this.treatmentDetails = treatmentDetails;
        this.TREATMENT_DATE = LocalDate.now();
    }

    public int getLocationId() {
        return this.LOCATION_ID;
    }

    public int getMedicalRecordId() {
        return this.MEDICAL_RECORD_ID;
    }

    public String getTreatmentDetails() {
        return this.treatmentDetails;
    }

    public void setTreatmentDetails(String treatmentDetails) {
        this.treatmentDetails = treatmentDetails;
    }

    public LocalDate getTreatmentDate() {
        return this.TREATMENT_DATE;
    }
}
