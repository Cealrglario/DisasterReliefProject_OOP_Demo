package edu.ucalgary.oop;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public enum PersonService {
    INSTANCE;

    private final PersonAccess<Object> personAccess = new PersonAccess<>();

    public Person getPersonById(int personId) throws SQLException {
        return personAccess.getById(personId);
    }

    public List<Person> getAllPersons() throws SQLException {
        return personAccess.getAll();
    }

    public Person addPerson(String firstName, String gender, LocalDate dateOfBirth, String phoneNumber) throws SQLException {
        return personAccess.addPerson(firstName, gender, dateOfBirth, phoneNumber);
    }

    public boolean updatePersonFirstName(Person person, String newFirstName) throws SQLException {
        person.setFirstName(newFirstName);
        return personAccess.updateInfo("first_name", newFirstName, person.getAssignedId());
    }

    public boolean updatePersonLastName(Person person, String newLastName) throws SQLException {
        person.setLastName(newLastName);
        return personAccess.updateInfo("last_name", newLastName, person.getAssignedId());
    }

    public boolean updatePersonGender(Person person, String newGender) throws SQLException {
        person.setGender(newGender);
        return personAccess.updateInfo("gender", newGender, person.getAssignedId());
    }

    public boolean updatePersonDateOfBirth(Person person, LocalDate newDateOfBirth) throws SQLException {
        person.setDateOfBirth(newDateOfBirth);
        if (newDateOfBirth != null) {
            return personAccess.updateInfo("date_of_birth", java.sql.Date.valueOf(newDateOfBirth), person.getAssignedId());
        } else {
            return personAccess.updateInfo("date_of_birth", null, person.getAssignedId());
        }
    }

    public boolean updatePersonPhoneNumber(Person person, String newPhoneNumber) throws SQLException {
        person.setPhoneNumber(newPhoneNumber);
        return personAccess.updateInfo("phone_number", newPhoneNumber, person.getAssignedId());
    }

    public boolean updateFamilyGroupStatus(Person person, boolean inFamilyGroup, Integer familyGroupId) throws SQLException {
        person.setInFamilyGroup(inFamilyGroup);
        if (inFamilyGroup) {
            return personAccess.updateInfo("family_group", familyGroupId, person.getAssignedId());
        } else {
            return personAccess.updateInfo("family_group", null, person.getAssignedId());
        }
    }

    public boolean removePerson(Person person) throws SQLException {
        return personAccess.removePerson(person);
    }
}
