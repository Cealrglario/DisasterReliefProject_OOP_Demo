package edu.ucalgary.oop;

import java.util.List;

public class PersonAccess<U> extends DatabaseObjectAccess<Person, U> {
    @Override
    public List<Person> getAll() {
        return null;
    }

    @Override
    public Person getById(int id) {
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
