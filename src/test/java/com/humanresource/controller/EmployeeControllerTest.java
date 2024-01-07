package com.humanresource.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.humanresource.exception.EmployeeException;
import com.humanresource.exception.ResourceNotFoundException;
import com.humanresource.model.Employee;
import com.humanresource.repository.EmployeeRepository;
import com.humanresource.service.EmployeeService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class EmployeeControllerTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeController employeeController;
    
    @Mock
    private EmployeeService employeeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
//    @BeforeEach
//    void setUp1() {
//        // Reset mock before each test
//        Mockito.reset(employeeRepository);
//    }


    @Test
    void getAllEmployees() throws EmployeeException {
        // Mocking the behavior of employeeService.getAllEmployees
        List<Employee> employeeList = Arrays.asList(
            new Employee(1L, "Sakshi", "Bhure", "sak@example.com"),
            new Employee(2L, "Shweta", "Todkar", "shweta@example.com")
        );
        when(employeeService.getAllEmployee()).thenReturn(employeeList);

        // Call the actual method you want to test
        ResponseEntity<List<Employee>> responseEntity = employeeController.showAllEmployees();

        // Check if the responseEntity is not null before accessing its properties
        assertNotNull(responseEntity);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        List<Employee> responseBody = responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals(employeeList, responseBody);

        verify(employeeService, times(1)).getAllEmployee();
    }



    @Test
    void createEmployee() throws EmployeeException {
        // Given
        Employee employeeToCreate = new Employee(1L, "Sakshi", "Bhure", "sak@gmail.com");

        // Mocking the addEmployee method of the employeeService
        when(employeeService.addEmployee(any(Employee.class))).thenReturn(employeeToCreate);

        // When
        ResponseEntity<Employee> responseEntity = employeeController.createEmployee(employeeToCreate);

        // Then
        // Verify that the addEmployee method of employeeService was called once with any Employee argument
        verify(employeeService, times(1)).addEmployee(any(Employee.class));

        // Extracting the employee from the ResponseEntity
        Employee createdEmployee = responseEntity.getBody();

        // Assertions
        assertNotNull(createdEmployee);
        assertEquals("Sakshi", createdEmployee.getFirstName());
        assertEquals("Bhure", createdEmployee.getLastName());
        assertEquals("sak@gmail.com", createdEmployee.getEmailId());

        // Verify that the HTTP status code is HttpStatus.ACCEPTED
        assertEquals(HttpStatus.ACCEPTED, responseEntity.getStatusCode());
    }

    @Test
    void getEmployeeById() throws EmployeeException {
        // Create a mock for EmployeeService
        EmployeeService employeeService = mock(EmployeeService.class);

        // Mock the behavior of employeeService.getEmployeeById
        when(employeeService.getEmployeeById(1L)).thenReturn(new Employee(2L, "Shweta", "Todkar", "shweta@example.com"));

        // Create an instance of EmployeeController with the mocked EmployeeService
        EmployeeController employeeController = new EmployeeController(employeeService);

        // Call the actual method you want to test
        ResponseEntity<Employee> responseEntity = employeeController.getEmployeeById(1L);

        // Verify that the method was called with the correct argument
        verify(employeeService, times(1)).getEmployeeById(1L);

        // Rest of your assertions...
    }


//    @Test
//    void getEmployeeById_ThrowsException() throws EmployeeException {
//        long nonExistentEmployeeId = 99L;
//
//        // Mocking the repository to return an empty result when findById is called
//        when(employeeRepository.findById(ArgumentMatchers.eq(nonExistentEmployeeId))).thenReturn(Optional.empty());
//
//        // Setting up the service to throw ResourceNotFoundException when calling getEmployeeById
//        when(employeeService.getEmployeeById(ArgumentMatchers.eq(nonExistentEmployeeId)))
//                .thenThrow(ResourceNotFoundException.class);
//
//        // Call the method that should throw the exception
//        assertThrows(ResourceNotFoundException.class, () -> {
//            employeeController.getEmployeeById(nonExistentEmployeeId);
//        });
//
//        // Verify that findById was invoked once
//        verify(employeeRepository, times(1)).findById(nonExistentEmployeeId);
//    }


    @Test
    void updateEmployee() throws EmployeeException {
        long employeeId = 1L;
        Employee existingEmployee = new Employee(employeeId, "Sakshi", "Bhure", "sak@gmail.com");
        Employee updatedEmployeeDetails = new Employee(employeeId, "Sakshi", "Bhure", "sak123@gmail.com");

        when(employeeService.updateEmployee(any(Employee.class))).thenReturn(updatedEmployeeDetails);
        
        // Assuming you have mocked the service method and it returns the updated employee

        ResponseEntity<Employee> responseEntity = employeeController.updateEmployee(updatedEmployeeDetails);

        assertEquals(HttpStatus.ACCEPTED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals("Sakshi", responseEntity.getBody().getFirstName());  // Update the expected value based on your test data

        verify(employeeService, times(1)).updateEmployee(any(Employee.class));
    }


//    @Test
//    void updateEmployee_ThrowsException() {
//        // Given
//        long nonExistentEmployeeId = 99L;
//        Employee updatedEmployeeDetails = new Employee(nonExistentEmployeeId, "John", "Doe", "john.doe@gmail.com");
//
//        // Mocking the findById method to return an empty optional
//        when(employeeRepository.findById(nonExistentEmployeeId)).thenReturn(Optional.empty());
//
//        // Mocking the save method to prevent unexpected interactions
//        when(employeeRepository.save(any(Employee.class))).thenReturn(null);
//
//        // When and Then
//        assertThrows(EmployeeException.class, () -> 
//        employeeController.updateEmployee(updatedEmployeeDetails));
//
//        // Verify that the findById method of employeeRepository was called once with the specified employeeId
//        verify(employeeRepository, times(1)).findById(nonExistentEmployeeId);
//
//        // Verify that the save method of employeeRepository was NOT called
//        verify(employeeRepository, times(0)).save(any(Employee.class));
//    }


    @Test
    void deleteEmployee() throws EmployeeException {
        long employeeId = 1L;
        Employee existingEmployee = new Employee(employeeId, "John", "Doe", "john.doe@gmail.com");

        when(employeeService.deleteEmployee(employeeId)).thenReturn(existingEmployee);

        ResponseEntity<Map<Employee, Boolean>> responseEntity = employeeController.deleteEmployee(employeeId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        Map<Employee, Boolean> responseBody = responseEntity.getBody();
        assertNotNull(responseBody);
        assertTrue(responseBody.containsValue(true));

        verify(employeeService, times(1)).deleteEmployee(employeeId);
    }

    @Test
    void deleteEmployee_ThrowsException() throws EmployeeException {
        long nonExistentEmployeeId = 99L;

        // Mock behavior for employeeService.deleteEmployee to throw ResourceNotFoundException
        when(employeeService.deleteEmployee(nonExistentEmployeeId)).thenThrow(new ResourceNotFoundException("Employee not found"));

        assertThrows(ResourceNotFoundException.class,
                () -> employeeController.deleteEmployee(nonExistentEmployeeId));

        verify(employeeService, times(1)).deleteEmployee(nonExistentEmployeeId);
        // No need to verify employeeRepository in this case
        verify(employeeRepository, times(0)).findById(anyLong());
        verify(employeeRepository, times(0)).delete(any(Employee.class));
    }

}