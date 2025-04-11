package edu.ucalgary.oop;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class PersonServiceTest {
    private PersonService personService;
    private PersonAccess<Object> personAccess;

    @Before
    public void setUp() {
        personService = PersonService.INSTANCE;
        personAccess = new PersonAccess<>();
    }

    @Test
    public void testGetPersonById() {
        Person testPerson = null;

        try {
            testPerson = personService.getPersonById(1);
        } catch (SQLException e) {
            fail("Error testing getPersonById: " + e.getMessage());
        }

        assertEquals("getPersonById() should retrieve the correct Person", 1, testPerson.getAssignedId());
    }

    @Test
    public void testGetAllPersons() {
        List<Person> retrievedPersons = null;

        try {
            retrievedPersons = personService.getAllPersons();
        } catch (SQLException e) {
            fail("Error testing getAllPersons: " + e.getMessage());
        }

        assertNotNull("getAllPersons() should retrieve a valid list of Persons", retrievedPersons);
    }

    @Test
    public void testAddPerson() {
        Person testPerson = null;
        String testFirstName = "Test Person";
        String testGender = "Male";
        LocalDate testDateOfBirth = LocalDate.of(2000, 1, 1);
        String testPhoneNumber = "111-1111";

        try {
            testPerson = personService.addPerson(testFirstName, testGender, testDateOfBirth, testPhoneNumber);
        } catch (SQLException e) {
            fail("Error testing addPerson: " + e.getMessage());
        }

        assertNotNull("addPerson() should create and return a valid Person", testPerson);
        assertEquals("addPerson() should set the firstName correctly", testFirstName, testPerson.getFirstName());
        assertEquals("addPerson() should set the gender correctly", testGender, testPerson.getGender());
        assertEquals("addPerson() should set the dateOfBirth correctly", testDateOfBirth, testPerson.getDateOfBirth());
        assertEquals("addPerson() should set the phoneNumber correctly", testPhoneNumber, testPerson.getPhoneNumber());

        try {
            personService.removePerson(testPerson);
        } catch (SQLException e) {
            fail("Error testing addPerson: " + e.getMessage());
        }
    }

    @Test
    public void testUpdatePersonFirstName() {
        Person testPerson = null;
        String newFirstName = "Updated Name";
        String retrievedFirstName = null;

        try {
            testPerson = personService.addPerson("Test Person", "Male", null, "111-1111");
            personService.updatePersonFirstName(testPerson, newFirstName);

            retrievedFirstName = (String) personAccess.getInfo("first_name", testPerson.getAssignedId());
        } catch (SQLException e) {
            fail("Error testing updatePersonFirstName: " + e.getMessage());
        }

        assertEquals("updatePersonFirstName() should update the name in-memory and in database",
                testPerson.getFirstName(), retrievedFirstName);

        try {
            personService.removePerson(testPerson);
        } catch (SQLException e) {
            fail("Error testing updatePersonFirstName: " + e.getMessage());
        }
    }

    @Test
    public void testUpdatePersonLastName() {
        Person testPerson = null;
        String newLastName = "Updated Last Name";
        String retrievedLastName = null;

        try {
            testPerson = personService.addPerson("Test Person", "Female", null, "111-1111");
            personService.updatePersonLastName(testPerson, newLastName);

            retrievedLastName = (String) personAccess.getInfo("last_name", testPerson.getAssignedId());
        } catch (SQLException e) {
            fail("Error testing updatePersonLastName: " + e.getMessage());
        }

        assertEquals("updatePersonLastName() should update the last name in-memory and in database",
                testPerson.getLastName(), retrievedLastName);

        try {
            personService.removePerson(testPerson);
        } catch (SQLException e) {
            fail("Error testing updatePersonLastName: " + e.getMessage());
        }
    }

    @Test
    public void testUpdatePersonGender() {
        Person testPerson = null;
        String newGender = "Female";
        String retrievedGender = null;

        try {
            testPerson = personService.addPerson("Test Person", "Male", null, "111-1111");
            personService.updatePersonGender(testPerson, newGender);

            retrievedGender = (String) personAccess.getInfo("gender", testPerson.getAssignedId());
        } catch (SQLException e) {
            fail("Error testing updatePersonGender: " + e.getMessage());
        }

        assertEquals("updatePersonGender() should update the gender in-memory and in database",
                testPerson.getGender(), retrievedGender);

        try {
            personService.removePerson(testPerson);
        } catch (SQLException e) {
            fail("Error testing updatePersonGender: " + e.getMessage());
        }
    }

    @Test
    public void testUpdatePersonPhoneNumber() {
        Person testPerson = null;
        String newPhoneNumber = "222-2222";
        String retrievedPhoneNumber = null;

        try {
            testPerson = personService.addPerson("Test Person", "Female", null, "111-1111");
            personService.updatePersonPhoneNumber(testPerson, newPhoneNumber);

            retrievedPhoneNumber = (String) personAccess.getInfo("phone_number", testPerson.getAssignedId());
        } catch (SQLException e) {
            fail("Error testing updatePersonPhoneNumber: " + e.getMessage());
        }

        assertEquals("updatePersonPhoneNumber() should update the phone number in-memory and in database", retrievedPhoneNumber,
                testPerson.getPhoneNumber());

        try {
            personService.removePerson(testPerson);
        } catch (SQLException e) {
            fail("Error testing updatePersonPhoneNumber: " + e.getMessage());
        }
    }

    @Test
    public void testRemovePerson() {
        Person testPerson = null;
        boolean removalResult = false;

        try {
            testPerson = personService.addPerson("Test Remove", "Male", null, "111-1111");
            removalResult = personService.removePerson(testPerson);
        } catch (SQLException e) {
            fail("Error testing removePerson: " + e.getMessage());
        }

        assertTrue("removePerson() should return true when successful", removalResult);
    }
}
