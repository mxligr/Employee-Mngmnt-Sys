package com.company.EmployeeManagementSystem.Controller;

import com.company.EmployeeManagementSystem.Model.Email;
import com.company.EmployeeManagementSystem.Model.Employee;

import com.company.EmployeeManagementSystem.Service.EmailService;
import com.company.EmployeeManagementSystem.Service.EmployeeService;
import com.company.EmployeeManagementSystem.Service.ImageService;
import com.company.EmployeeManagementSystem.Service.ImageUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.util.List;

@Controller
public class EmployeeController {
    @Autowired
    private EmailService emailService;

    private String registrationEmail;
    private String jobTitle;

    @Autowired
    private EmployeeService service;

    @Autowired
    private ImageUploadService imageUploadService;

    @Autowired
    private ImageService imageService;

    @GetMapping("/login")
    public String showLogin(){
        return "index";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        Employee emp = new Employee();
        emp.setEmail(registrationEmail);
        emp.setJobTitle(jobTitle);
        model.addAttribute("employee", emp);

        return "signup_form";
    }

    @PostMapping("/process_register")
    public String processRegister(Employee employee, @RequestParam("image")MultipartFile imageFile, RedirectAttributes redirectAttributes) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(employee.getPassword());
        employee.setPassword(encodedPassword);
        employee.setAdmin(false);

        if(imageFile.isEmpty()) {
            redirectAttributes.addFlashAttribute("noImageUploadedErrorMessage", "Please choose file to upload.");
            return "redirect:/register";
        }

        File file = imageUploadService.upload(imageFile);
        if(file == null) {
            redirectAttributes.addFlashAttribute("uploadFailErrorMessage", "Upload failed.");
            return "redirect:/register";
        }

        boolean resizeResult =  imageService.resizeImage(file);
        if(!resizeResult) {
            redirectAttributes.addFlashAttribute("resizeFailed", "Resize failed.");
            return "redirect:/register";
        }
        String[] splitted = file.toString().split("static");

        employee.setImageURL(splitted[1]);
        System.out.println(splitted[1]);

        service.addEmployee(employee);

        return "redirect:/login";
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
                                       @RequestParam(value = "newEmployeeJobTitle", required = true) String newEmployeeJobTitle,
                                       RedirectAttributes redirectAttributes){
        if(!newEmployeeEmail.isEmpty() && !newEmployeeJobTitle.isEmpty()){
            registrationEmail = newEmployeeEmail;
            jobTitle = newEmployeeJobTitle;
            Email email = new Email();
            emailService.sendMail(newEmployeeEmail, "Registration link", "Hello! ", email);

            redirectAttributes.addFlashAttribute("success", "Email sent successfully");
        }else{
            redirectAttributes.addFlashAttribute("error", "The email field is required");
        }

        return "redirect:/employees";
    }
}
