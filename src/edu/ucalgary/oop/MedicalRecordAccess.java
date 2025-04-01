package edu.ucalgary.oop;

import java.util.List;

public class MedicalRecordAccess<U, V, W> extends DatabaseObjectAccess<MedicalRecord, U, V, W> {
    @Override
    public List<MedicalRecord> getAll() {
        return null;
    }

    @Override
    public MedicalRecord getById(int id) {
        return null;
    }

    @Override
    public boolean updateInfo(U infoToUpdate, V newInfo) {
        return false;
    }

    @Override
    public W getInfo(U infoToGet, W infoType) {
        return null;
    }

    @Override
    public boolean addEntry(MedicalRecord newEntry) {
        return false;
    }

    @Override
    public boolean removeEntry(MedicalRecord exEntry) {
        return false;
    }
}
