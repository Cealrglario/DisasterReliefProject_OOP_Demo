package edu.ucalgary.oop;

import java.sql.SQLException;
import java.util.List;

public enum MedicalRecordService {
    INSTANCE;

    private final MedicalRecordAccess<Object> medicalRecordAccess = new MedicalRecordAccess<>();

    public MedicalRecord getMedicalRecordById(int medicalRecordId) throws SQLException {
        return medicalRecordAccess.getById(medicalRecordId);
    }

    public List<MedicalRecord> getAllMedicalRecords() throws SQLException {
        return medicalRecordAccess.getAll();
    }

    public MedicalRecord addMedicalRecord(int locationId, String treatmentDetails) throws SQLException {
        return medicalRecordAccess.addMedicalRecord(locationId, treatmentDetails);
    }

    public boolean updateTreatmentDetails(MedicalRecord medicalRecord, String newTreatmentDetails) throws SQLException {
        medicalRecord.setTreatmentDetails(newTreatmentDetails);
        return medicalRecordAccess.updateInfo("treatment_details", newTreatmentDetails, medicalRecord.getMedicalRecordId());
    }

    public boolean removeMedicalRecord(MedicalRecord medicalRecord) throws SQLException {
        return medicalRecordAccess.removeMedicalRecord(medicalRecord);
    }
}
