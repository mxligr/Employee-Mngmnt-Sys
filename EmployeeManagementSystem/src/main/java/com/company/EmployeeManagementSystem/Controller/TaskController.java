package com.company.EmployeeManagementSystem.Controller;

import com.company.EmployeeManagementSystem.Model.Email;
import com.company.EmployeeManagementSystem.Model.Employee;

import com.company.EmployeeManagementSystem.Model.Task;
import com.company.EmployeeManagementSystem.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.security.Principal;
import java.util.List;

@Transactional
@Controller
public class TaskController {
    @Autowired
    private TaskService service;

    @Autowired
    private EmployeeService employeeService;

    @PersistenceContext
    private EntityManager entityManager;

    @GetMapping("/tasks/{id}")
    public String listTasks(Model model, @PathVariable Long id) {
        Employee employee = employeeService.getEmpById(id);

        List<Task> listTasks = employee.getTasks();
        model.addAttribute("listTasks", listTasks);

        return "tasks";
    }

    @PostMapping("/assignTask/{id}")
    public String assignTaskToEmployee(RedirectAttributes redirectAttributes, Task task,
                             @PathVariable Long id,
                                       @RequestParam(value = "employeeId", required = true) Long employeeId)
    {
        Employee employee = employeeService.getEmpById(employeeId);

        task.setCompleted(false);
        task.assignEmployee(employee);

        service.addTask(task);
        redirectAttributes.addFlashAttribute("taskAssignedSuccessfully", "Task Assigned successfully");

        return "redirect:/employees";
    }

    @GetMapping("/taskCompleted/{taskId}")
    public String markTaskAsCompleted(@PathVariable Long taskId, Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomEmployeeDetails currentEmployee = (CustomEmployeeDetails) auth.getPrincipal();
        Long empId = currentEmployee.getEmployeeId();

        Employee employee = employeeService.getEmpById(empId);

        Task task = service.getTaskById(taskId);
        task.setCompleted(true);
        service.addTask(task);

        List<Task> listTasks = employee.getTasks();
        model.addAttribute("listTasks", listTasks);

        return "tasks";
    }
}
