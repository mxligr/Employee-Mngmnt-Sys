package com.company.EmployeeManagementSystem.Service;

import com.company.EmployeeManagementSystem.Controller.CustomEmployeeDetails;
import com.company.EmployeeManagementSystem.Repos.EmployeeRepository;
import com.company.EmployeeManagementSystem.Model.Employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomEmployeeDetailsService implements UserDetailsService{
    @Autowired
    private EmployeeRepository employeeRepo;

    private Employee employee;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        employee = employeeRepo.findByUsername(username);
        if (employee == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new CustomEmployeeDetails(employee);
    }

    public Employee getEmployee() {
        return employee;
    }
}
