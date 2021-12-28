package com.company.EmployeeManagementSystem.Repos;

import com.company.EmployeeManagementSystem.Model.Employee;
import com.company.EmployeeManagementSystem.Model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface TaskRepository extends JpaRepository<Task, Long> {
}
