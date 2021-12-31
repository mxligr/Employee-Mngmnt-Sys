package com.company.EmployeeManagementSystem.Service;

import com.company.EmployeeManagementSystem.Model.Employee;
import com.company.EmployeeManagementSystem.Repos.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
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

    public Employee findByEmail(String email){
        return repo.findByEmail(email);
    }

    public void deleteEmployee(Long id){
        repo.deleteById(id);
    }

    public void updateResetPasswordToken(String token, String email) throws UsernameNotFoundException {
        Employee employee = repo.findByEmail(email);
        if (employee != null) {
            employee.setResetPasswordToken(token);
            repo.save(employee);
        } else {
            throw new UsernameNotFoundException("Could not find any employee with the email " + email);
        }
    }

    public Employee getByResetPasswordToken(String token) {
        return repo.findByResetPasswordToken(token);
    }

    public void updatePassword(Employee employee, String newPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(newPassword);
        employee.setPassword(encodedPassword);

        employee.setResetPasswordToken(null);
        repo.save(employee);
    }

}
