package com.company.EmployeeManagementSystem.Controller;

import com.company.EmployeeManagementSystem.Model.Email;
import com.company.EmployeeManagementSystem.Model.Employee;

import com.company.EmployeeManagementSystem.Service.EmailService;
import com.company.EmployeeManagementSystem.Service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class EmployeeController {
    @Autowired
    private EmailService emailService;

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
        employee.setAdmin(false);

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
        return "edit";
    }

    @PostMapping("/update")
    public String updateEmployee(@ModelAttribute Employee emp) {
        emp.setId(emp.getId());
        service.addEmployee(emp);
        return "redirect:/employees";
    }

    @GetMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable Long id){
        service.deleteEmployee(id);
        return "redirect:/employees";
    }

    @PostMapping("/sendRegistrationLink")
    public String sendRegistrationLink(@RequestParam(value = "newEmployeeEmail", required = true) String newEmployeeEmail,
                                       RedirectAttributes redirectAttributes){
        if(!newEmployeeEmail.isEmpty()){
            Email email = new Email();
            emailService.sendMail(newEmployeeEmail, "Registration link", "Hello! ", email);

            redirectAttributes.addFlashAttribute("success", "Email sent successfully");
        }else{
            redirectAttributes.addFlashAttribute("error", "The email field is required");
        }

        return "redirect:/employees";
    }
}
