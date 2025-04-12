package edu.ucalgary.oop;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class VictimMedicalRecordAccess<U> extends DatabaseObjectAccess<DisasterVictim, U> {

    public List<MedicalRecord> getMedicalRecordsForPerson(int personId) throws SQLException {
        List<MedicalRecord> records = new ArrayList<>();

        dbConnectionManager.initializeDbConnection();
        Connection dbConnect = dbConnectionManager.getDbConnection();

        PreparedStatement myStmt = dbConnect.prepareStatement(
                "SELECT * FROM MedicalRecord WHERE person_id = ?"
        );

        myStmt.setInt(1, personId);
        queryResults = myStmt.executeQuery();

        while (queryResults.next()) {
            int medicalRecordId = queryResults.getInt("medical_record_id");
            int locationId = queryResults.getInt("location_id");
            String treatmentDetails = queryResults.getString("treatment_details");

            MedicalRecord record = new MedicalRecord(medicalRecordId, locationId, treatmentDetails);
            records.add(record);
        }

        myStmt.close();
        dbConnectionManager.closeDbConnection();

        return records;
    }


    public MedicalRecord addMedicalRecordToPerson(int personId, int locationId, String treatmentDetails) throws SQLException {
        dbConnectionManager.initializeDbConnection();
        Connection dbConnect = dbConnectionManager.getDbConnection();

        PreparedStatement myStmt = dbConnect.prepareStatement(
                "INSERT INTO MedicalRecord (person_id, location_id, treatment_details, date_of_treatment) VALUES (?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
        );

        myStmt.setInt(1, personId);
        myStmt.setInt(2, locationId);
        myStmt.setString(3, treatmentDetails);
        myStmt.setDate(4, Date.valueOf(LocalDate.now()));

        int affectedRows = myStmt.executeUpdate();

        if (affectedRows == 0) {
            System.out.println("Creating medical record failed, no rows affected.");
            myStmt.close();
            dbConnectionManager.closeDbConnection();
            return null;
        }

        MedicalRecord newRecord = null;
        try (ResultSet generatedKeys = myStmt.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                int recordId = generatedKeys.getInt(1);
                newRecord = new MedicalRecord(recordId, locationId, treatmentDetails);
            } else {
                System.out.println("Creating medical record failed, couldn't obtain record ID.");
                myStmt.close();
                dbConnectionManager.closeDbConnection();
                return null;
            }
        }

        myStmt.close();
        dbConnectionManager.closeDbConnection();

        return newRecord;
    }


    public boolean removeMedicalRecordFromPerson(int personId, int medicalRecordId) throws SQLException {
        dbConnectionManager.initializeDbConnection();
        Connection dbConnect = dbConnectionManager.getDbConnection();

        PreparedStatement myStmt = dbConnect.prepareStatement(
                "DELETE FROM MedicalRecord WHERE medical_record_id = ? AND person_id = ?");

        myStmt.setInt(1, medicalRecordId);
        myStmt.setInt(2, personId);

        int rowsAffected = myStmt.executeUpdate();

        myStmt.close();
        dbConnectionManager.closeDbConnection();

        return rowsAffected > 0;
    }
}
