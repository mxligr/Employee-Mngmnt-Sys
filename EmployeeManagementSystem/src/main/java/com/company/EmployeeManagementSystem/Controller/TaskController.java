package com.company.EmployeeManagementSystem.Controller;

import com.company.EmployeeManagementSystem.Model.Employee;
import com.company.EmployeeManagementSystem.Model.Task;
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

import javax.persistence.Column;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;

@Controller
public class TaskController {
    @Autowired
    private TaskService service;


    @GetMapping("/tasks")
    public String listEmployees(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomEmployeeDetails currentEmployee = (CustomEmployeeDetails) auth.getPrincipal();
        Long employeeId = currentEmployee.getEmployeeId();

//        List<Task> listTasks = service.findTasksByEmployeeId(employeeId);
//        model.addAttribute("listTasks", listTasks);



        return "tasks";
    }

    @PostMapping("/assignTask/{id}")
    public String assignTask(RedirectAttributes redirectAttributes, Task task,
                             @RequestParam(value = "employee_id", required = true) int employee_id)
    {
        task.setEmployee_id(employee_id);
        service.addTask(task);
        redirectAttributes.addFlashAttribute("taskAssignedSuccessfully", "Task Assigned successfully");

        return "redirect:/employees";
    }
}
