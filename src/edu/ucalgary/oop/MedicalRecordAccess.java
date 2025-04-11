package edu.ucalgary.oop;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedicalRecordAccess<U> extends DatabaseObjectAccess<MedicalRecord, U> {

    @Override
    public List<MedicalRecord> getAll() throws SQLException {
        List<MedicalRecord> retrievedMedicalRecords = new ArrayList<>();

        dbConnectionManager.initializeDbConnection();
        Connection dbConnect = dbConnectionManager.getDbConnection();

        Statement myStmt = dbConnect.createStatement();
        queryResults = myStmt.executeQuery("SELECT * FROM MedicalRecord");

        while(queryResults.next()) {
            int medicalRecordId = queryResults.getInt("medical_record_id");
            int locationId = queryResults.getInt("location_id");
            String treatmentDetails = queryResults.getString("treatment_details");

            MedicalRecord retrievedMedicalRecord = new MedicalRecord(medicalRecordId, locationId, treatmentDetails);
            retrievedMedicalRecords.add(retrievedMedicalRecord);
        }

        myStmt.close();
        dbConnectionManager.closeDbConnection();

        return retrievedMedicalRecords;
    }


    @Override
    public MedicalRecord getById(int idOfMedicalRecord) throws SQLException {
        MedicalRecord retrievedMedicalRecord = null;

        dbConnectionManager.initializeDbConnection();
        Connection dbConnect = dbConnectionManager.getDbConnection();

        Statement myStmt = dbConnect.createStatement();
        queryResults = myStmt.executeQuery("SELECT * FROM MedicalRecord WHERE medical_record_id = " + idOfMedicalRecord);

        if(queryResults.next()) {
            int medicalRecordId = queryResults.getInt("medical_record_id");
            int locationId = queryResults.getInt("location_id");
            String treatmentDetails = queryResults.getString("treatment_details");

            retrievedMedicalRecord = new MedicalRecord(medicalRecordId, locationId, treatmentDetails);
        } else {
            System.out.println("Error getting MedicalRecord by ID: MedicalRecord doesn't exist.");
            return null;
        }

        myStmt.close();
        dbConnectionManager.closeDbConnection();

        return retrievedMedicalRecord;
    }


    public MedicalRecord addMedicalRecord(int locationId, String treatmentDetails) throws SQLException {
        dbConnectionManager.initializeDbConnection();
        Connection dbConnect = dbConnectionManager.getDbConnection();

        PreparedStatement myStmt = dbConnect.prepareStatement(
                "INSERT INTO MedicalRecord (location_id, treatment_details) VALUES (?, ?)",
                Statement.RETURN_GENERATED_KEYS
        );

        myStmt.setInt(1, locationId);
        myStmt.setString(2, treatmentDetails);

        int affectedRows = myStmt.executeUpdate();
        if (affectedRows == 0) {
            System.out.println("Creating medical record failed, no rows affected.");
            return null;
        }

        MedicalRecord newMedicalRecord = null;
        try (ResultSet generatedKeys = myStmt.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                int medicalRecordId = generatedKeys.getInt(1);
                newMedicalRecord = new MedicalRecord(medicalRecordId, locationId, treatmentDetails);
            } else {
                System.out.println("Creating medical record failed, couldn't obtain medical record ID.");
                return null;
            }
        }

        myStmt.close();
        dbConnectionManager.closeDbConnection();

        return newMedicalRecord;
    }


    @Override
    public boolean updateInfo(String infoToUpdate, U newInfo, int medicalRecordId) throws SQLException {
        int affectedRows;

        dbConnectionManager.initializeDbConnection();
        Connection dbConnect = dbConnectionManager.getDbConnection();

        PreparedStatement myStmt = dbConnect.prepareStatement("UPDATE MedicalRecord SET " + infoToUpdate + " = ? WHERE medical_record_id = ?");

        myStmt.setObject(1, newInfo);
        myStmt.setInt(2, medicalRecordId);

        try {
            affectedRows = myStmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Updating medical record failed: " + e.getMessage());
            return false;
        }

        myStmt.close();
        dbConnectionManager.closeDbConnection();

        if (affectedRows == 0) {
            System.out.println("Updating medical record failed, no rows affected.");
            return false;
        } else {
            return true;
        }
    }


    @Override
    public U getInfo(String infoToGet, int medicalRecordId) throws SQLException {
        U retrievedInfo;

        dbConnectionManager.initializeDbConnection();
        Connection dbConnect = dbConnectionManager.getDbConnection();

        Statement myStmt = dbConnect.createStatement();
        queryResults = myStmt.executeQuery("SELECT " + infoToGet + " FROM MedicalRecord WHERE medical_record_id = " +
                medicalRecordId);

        if(queryResults.next()) {
            retrievedInfo = (U) queryResults.getObject(infoToGet);
        } else {
            System.out.println("Error retrieving info, results empty.");
            return null;
        }

        myStmt.close();
        dbConnectionManager.closeDbConnection();

        return retrievedInfo;
    }


    public boolean removeMedicalRecord(MedicalRecord unwantedMedicalRecord) throws SQLException {
        int unwantedMedicalRecordId = unwantedMedicalRecord.getMedicalRecordId();

        dbConnectionManager.initializeDbConnection();
        Connection dbConnect = dbConnectionManager.getDbConnection();

        PreparedStatement myStmt = dbConnect.prepareStatement("DELETE FROM MedicalRecord WHERE medical_record_id = ?");

        myStmt.setInt(1, unwantedMedicalRecordId);

        int rowsAffected = myStmt.executeUpdate();

        myStmt.close();
        dbConnectionManager.closeDbConnection();

        return rowsAffected > 0;
    }
}
