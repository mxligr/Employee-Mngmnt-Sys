package com.company.EmployeeManagementSystem.Controller;

import com.company.EmployeeManagementSystem.Model.Employee;

import com.company.EmployeeManagementSystem.Service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class EmployeeController {

    @Autowired
    private EmployeeService service;

    @GetMapping("/login")
    public String showLogin(){
        return "index";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("employee", new Employee());

        return "signup_form";
    }

    @PostMapping("/process_register")
    public String processRegister(Employee employee) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(employee.getPassword());
        employee.setPassword(encodedPassword);

        service.addEmployee(employee);

        return "register_success";
    }

    @GetMapping("/employees")
    public String listEmployees(Model model) {
        List<Employee> listEmployees = service.findEmployees();
        model.addAttribute("listEmployees", listEmployees);

        return "employees";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model){
        Employee emp = service.getEmpById(id);
        model.addAttribute("emp", emp);
        System.out.println(emp.getId());
        return "edit";
    }

    @PostMapping("/update")
    public String updateEmployee(@ModelAttribute Employee emp) {
        System.out.println(emp.getId());
        service.addEmployee(emp);
        return "redirect:/";
    }
}
