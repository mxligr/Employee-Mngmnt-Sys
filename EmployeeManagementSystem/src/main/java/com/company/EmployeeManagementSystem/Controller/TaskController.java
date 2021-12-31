package com.company.EmployeeManagementSystem.Controller;

import com.company.EmployeeManagementSystem.Model.Employee;
import com.company.EmployeeManagementSystem.Model.Task;
import com.company.EmployeeManagementSystem.Service.EmployeeService;
import com.company.EmployeeManagementSystem.Service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class TaskController {
    @Autowired
    private TaskService service;

    @Autowired
    private EmployeeService employeeService;


    @GetMapping("/tasks")
    public String listTasks(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomEmployeeDetails currentEmployee = (CustomEmployeeDetails) auth.getPrincipal();
        Employee employee = currentEmployee.getEmployee();

        List<Task> listTasks = employee.getTasks();
        model.addAttribute("listTasks", listTasks);

        return "tasks";
    }

    @PostMapping("/assignTask/{id}")
    public String assignTask(RedirectAttributes redirectAttributes, Task task,
                             @RequestParam(value = "employeeId", required = true) Long employeeId)
    {
        Employee employee = employeeService.getEmpById(employeeId);
        task.setEmployee(employee);
        service.addTask(task);
        redirectAttributes.addFlashAttribute("taskAssignedSuccessfully", "Task Assigned successfully");

        return "redirect:/employees";
    }
}
