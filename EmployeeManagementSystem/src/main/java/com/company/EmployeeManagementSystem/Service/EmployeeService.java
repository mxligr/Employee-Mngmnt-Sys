package com.company.EmployeeManagementSystem.Service;

import com.company.EmployeeManagementSystem.Model.Employee;
import com.company.EmployeeManagementSystem.Repos.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository repo;

    public void addEmployee(Employee e) {
        repo.save(e);
    }

    public List<Employee> findEmployees(){
        return repo.findAll();
    }

    public void updateEmployeeDetails(Long id, String email, String phone, String firstName, String lastName, String jobTitle, String username, String password, String image) {
        Employee myEmployee = repo.findByID(id);
        myEmployee.setEmail(email);
        myEmployee.setUsername(username);
        myEmployee.setPassword(password);
        myEmployee.setFirstName(firstName);
        myEmployee.setLastName(lastName);
        myEmployee.setJobTitle(jobTitle);
        myEmployee.setPhone(phone);
        myEmployee.setImageURL(image);
        addEmployee(myEmployee);
    }
}
