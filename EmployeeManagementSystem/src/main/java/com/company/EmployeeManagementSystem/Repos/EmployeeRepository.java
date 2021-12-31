package com.company.EmployeeManagementSystem.Repos;

import com.company.EmployeeManagementSystem.Model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

@Component
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    @Query("SELECT e FROM Employee e WHERE e.username = ?1")
    Employee findByUsername(String username);

    @Query("SELECT e FROM Employee e WHERE e.email = ?1")
    Employee findByEmail(String email);

    Employee findByResetPasswordToken(String token);
}
