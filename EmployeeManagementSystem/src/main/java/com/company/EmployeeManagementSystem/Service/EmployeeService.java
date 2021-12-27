package com.company.EmployeeManagementSystem.Service;

import com.company.EmployeeManagementSystem.Model.Employee;
import com.company.EmployeeManagementSystem.Repos.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public Employee getEmpById(Long id){
        Optional<Employee> e = repo.findById(id);
        return e.orElse(null);
    }

    public void deleteEmployee(Long id){
        repo.deleteById(id);
    }

}
