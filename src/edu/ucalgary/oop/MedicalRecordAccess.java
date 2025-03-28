package edu.ucalgary.oop;

import java.util.List;

public class MedicalRecordAccess<U> implements DatabaseObjectAccess<MedicalRecord, U> {
    @Override
    public List<MedicalRecord> getAll() {
        return null;
    }

    @Override
    public MedicalRecord getById(int id) {
        return null;
    }

    @Override
    public boolean updateInfo(U info) {
        return false;
    }

    @Override
    public boolean getInfo(U info) {
        return false;
    }
}
