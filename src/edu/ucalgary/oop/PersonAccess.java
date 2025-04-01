package edu.ucalgary.oop;

import java.util.List;

public class PersonAccess<U, V, W> extends DatabaseObjectAccess<Person, U, V, W> {
    @Override
    public List<Person> getAll() {
        return null;
    }

    @Override
    public Person getById(int id) {
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
    public boolean addEntry(Person newEntry) {
        return false;
    }

    @Override
    public boolean removeEntry(Person exEntry) {
        return false;
    }
}
