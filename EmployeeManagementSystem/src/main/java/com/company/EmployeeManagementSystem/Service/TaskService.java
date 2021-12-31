package com.company.EmployeeManagementSystem.Service;

import com.company.EmployeeManagementSystem.Model.Employee;
import com.company.EmployeeManagementSystem.Model.Task;
import com.company.EmployeeManagementSystem.Repos.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    @Autowired
    private TaskRepository repo;

    public void addTask(Task t) {
        repo.save(t);
    }

    public Task getTaskById(Long id){
        Optional<Task> e = repo.findById(id);
        return e.orElse(null);
    }

}
