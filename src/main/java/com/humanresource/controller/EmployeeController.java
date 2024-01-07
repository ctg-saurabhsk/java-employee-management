package com.humanresource.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.humanresource.exception.EmployeeException;
import com.humanresource.exception.ResourceNotFoundException1;
import com.humanresource.model.Employee;
import com.humanresource.service.EmployeeService;


@CrossOrigin("http://localhost:3000/")
@RestController
@RequestMapping("/humanresource")
public class EmployeeController {

	private final EmployeeService eService;

    @Autowired
    public EmployeeController(EmployeeService eService) {
        this.eService = eService;
    }


	/**
	 * This method will create a new employee based on given details.
	 * @param employee new employees details to be created.
	 * @return ResponseEntity {@link Employee} the newly created employee object.
	 * @throws EmployeeException if employee with provided details already exists.
	 */
	@PostMapping("/employees")
	public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) throws EmployeeException {
		return new ResponseEntity<>(eService.addEmployee(employee), HttpStatus.ACCEPTED);
	}


	/**
	 * This will fetch all the employee details from database and map them into {@link List<Employee>} and return it.
	 * @return ResponseEntity {@link List<Employee>} of employees available in database.
	 * @throws EmployeeException if no employees are available in database.
	 */
	@GetMapping("/employees")
	public ResponseEntity<List<Employee>> showAllEmployees() throws EmployeeException {
		return new ResponseEntity<>(eService.getAllEmployee(), HttpStatus.OK);
	}


	/**
	 * Delete employee from database for given employee id.
	 * @param id employee id to be deleted.
	 * @return ResponseEntity of Map<Employee,Boolean> containing employee and operation status for that employee.
	 * @throws EmployeeException if employee is not available.
	 */
	@DeleteMapping("/employees/{id}")
	public ResponseEntity<Map<Employee, Boolean>> deleteEmployee(@PathVariable("id") Long id) throws EmployeeException {

		boolean deleted = false;
		Employee deletedEmployee = eService.deleteEmployee(id);
		if (deletedEmployee != null) {
			deleted = true;
		}
		Map<Employee, Boolean> response = new HashMap<>();
		response.put(deletedEmployee, deleted);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}


	/**
	 * Update employee details for given employee.
 	 * @param e Employee details to be updated
	 * @return ResponseEntity Employee update employee object
	 * @throws EmployeeException if employee does not exist
	 */
	@PutMapping("/employees")
	public ResponseEntity<Employee> updateEmployee(@RequestBody Employee emp) {
	    try {
	        Employee updatedEmployee = eService.updateEmployee(emp);
	        return new ResponseEntity<>(updatedEmployee, HttpStatus.ACCEPTED);
	    } catch (EmployeeException e) {
	        
	        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	    } catch (ResourceNotFoundException1 e) {
	        // Rethrow ResourceNotFoundException
	        throw e;
	    }
	}



	public ResponseEntity<Employee> getEmployeeById(Long id) throws EmployeeException {
	    Employee employee = eService.getEmployeeById(id);
		return null;
	}

}
