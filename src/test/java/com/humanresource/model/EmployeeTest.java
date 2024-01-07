package com.humanresource.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class EmployeeTest {

    @Test
    void testEmployeeConstructorAndGetters() {
        // Arrange
        long id = 1L;
        String firstName = "Pratiksha";
        String lastName = "Bhende";
        String emailId = "pratikshabhende@gmail.com";

        // Act
        Employee employee = new Employee(id, firstName, lastName, emailId);

        // Assert
        assertEquals(id, employee.getId());
        assertEquals(firstName, employee.getFirstName());
        assertEquals(lastName, employee.getLastName());
        assertEquals(emailId, employee.getEmailId());
    }

    @Test
    void testEmployeeSetter() {
        // Arrange
        long id = 1L;
        String firstName = "Sakshi";
        String lastName = "Bhure";
        String emailId = "sak@gmail.com";

        // Act
        Employee employee = new Employee();
        employee.setId(id)
;
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setEmailId(emailId);

        // Assert
        assertEquals(id, employee.getId());
        assertEquals(firstName, employee.getFirstName());
        assertEquals(lastName, employee.getLastName());
        assertEquals(emailId, employee.getEmailId());
    }

}
