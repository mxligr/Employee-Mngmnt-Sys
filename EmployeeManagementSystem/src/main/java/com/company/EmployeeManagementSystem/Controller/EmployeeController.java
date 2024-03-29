package com.company.EmployeeManagementSystem.Controller;

import com.company.EmployeeManagementSystem.EmployeeManagementSystemApplication;
import com.company.EmployeeManagementSystem.Model.Email;
import com.company.EmployeeManagementSystem.Model.Employee;

import com.company.EmployeeManagementSystem.Model.Task;
import com.company.EmployeeManagementSystem.Service.EmailService;
import com.company.EmployeeManagementSystem.Service.EmployeeService;
import com.company.EmployeeManagementSystem.Service.ImageService;
import com.company.EmployeeManagementSystem.Service.ImageUploadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.transaction.Transactional;
import java.io.File;
import java.util.List;

@Transactional
@Controller
public class EmployeeController {
    private static final Logger logger = LoggerFactory.getLogger(EmployeeManagementSystemApplication.class);

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
    public String viewLoginPage() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            logger.info("User enter on the login page");
            return "login";
        }
        logger.warn("User was already logged in so it was redirected to the home page");
        return "redirect:/";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        Employee emp = new Employee();
        emp.setEmail(registrationEmail);
        emp.setJobTitle(jobTitle);
        model.addAttribute("employee", emp);

        logger.info("User received the signUp form");

        return "signup_form";
    }

    @PostMapping("/process_register")
    public String processRegister(Employee employee, @RequestParam("image")MultipartFile imageFile, RedirectAttributes redirectAttributes) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(employee.getPassword());
        employee.setPassword(encodedPassword);
        employee.setAdmin(Boolean.FALSE);

        if(imageFile.isEmpty()) {
            redirectAttributes.addFlashAttribute("noImageUploadedErrorMessage", "Please choose file to upload.");
            logger.error("There was an error while uploading the user image");
            return "redirect:/register";
        }

        File file = imageUploadService.upload(imageFile);
        if(file == null) {
            redirectAttributes.addFlashAttribute("uploadFailErrorMessage", "Upload failed.");
            logger.error("There was an error while uploading the user image");
            return "redirect:/register";
        }

        boolean resizeResult =  imageService.resizeImage(file);
        if(!resizeResult) {
            redirectAttributes.addFlashAttribute("resizeFailed", "Resize failed.");
            logger.error("There was an error while resizing the user image");
            return "redirect:/register";
        }
        String[] splitted = file.toString().split("static");

        employee.setImageURL(splitted[1]);

        service.addEmployee(employee);

        logger.info("User registered successfully");

        return "redirect:/login";
    }

    @GetMapping("/employees")
    public String listEmployees(Model model) {
        List<Employee> listEmployees = service.findEmployees();
        model.addAttribute("listEmployees", listEmployees);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomEmployeeDetails currentEmployee = (CustomEmployeeDetails) auth.getPrincipal();
        Boolean isAdmin = currentEmployee.getAdminRights();
        Long emplId = currentEmployee.getEmployeeId();
        Employee emp = service.getEmpById(emplId);
        Boolean noTasks = emp.getTasks().isEmpty();

        Employee currentLoggedEmployee = currentEmployee.getEmployee();

        model.addAttribute("noTasks", noTasks);
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("currentEmployee", currentLoggedEmployee);

        Task task = new Task();
        model.addAttribute("task", task);

        logger.info("User accessed the employees list");

        return "employees";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model){
        Employee emp = service.getEmpById(id);

        model.addAttribute("emp", emp);

        logger.info("User wants to edit his profile");

        return "edit";
    }

    @PostMapping("/update")
    public String updateEmployee(@ModelAttribute Employee emp, Model model, @RequestParam("image")MultipartFile imageFile, RedirectAttributes redirectAttributes) {
        emp.setId(emp.getId());

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomEmployeeDetails currentEmployee = (CustomEmployeeDetails) auth.getPrincipal();
        Boolean isAdmin = currentEmployee.getAdminRights();

        if(imageFile.isEmpty()) {
            redirectAttributes.addFlashAttribute("noImageUploadedErrorMessage", "Please choose file to upload.");
            logger.error("There was an error while uploading the user image");
            return "redirect:/edit/{id}";
        }

        File file = imageUploadService.upload(imageFile);
        if(file == null) {
            redirectAttributes.addFlashAttribute("uploadFailErrorMessage", "Upload failed.");
            logger.error("There was an error while uploading the user image");

            return "redirect:/edit/{id}";
        }

        boolean resizeResult =  imageService.resizeImage(file);
        if(!resizeResult) {
            redirectAttributes.addFlashAttribute("resizeFailed", "Resize failed.");
            logger.error("There was an error while resizing the user image");

            return "redirect:/edit/{id}";
        }
        String[] splitted = file.toString().split("static");

        emp.setImageURL(splitted[1]);

        model.addAttribute("isAdmin", isAdmin);
        service.addEmployee(emp);

        logger.info("User edited his profile");

        return "redirect:/employees";
    }

    @GetMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable Long id, Model model){
        service.deleteEmployee(id);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomEmployeeDetails currentEmployee = (CustomEmployeeDetails) auth.getPrincipal();
        Boolean isAdmin = currentEmployee.getAdminRights();

        model.addAttribute("isAdmin", isAdmin);

        logger.info("Admin deleted the employee with id = " + id);

        return "redirect:/employees";
    }

    @PostMapping("/sendRegistrationLink")
    public String sendRegistrationLink(@RequestParam(value = "newEmployeeEmail") String newEmployeeEmail,
                                       @RequestParam(value = "newEmployeeJobTitle") String newEmployeeJobTitle,
                                       RedirectAttributes redirectAttributes, Model model){
        if(!newEmployeeEmail.isEmpty() && !newEmployeeJobTitle.isEmpty()){
            registrationEmail = newEmployeeEmail;
            jobTitle = newEmployeeJobTitle;
            Email email = new Email();
            emailService.sendMail(newEmployeeEmail, "Registration link", "Hello! ", email);

            redirectAttributes.addFlashAttribute("success", "Email sent successfully");
        }else{
            logger.error("Email address is not valid");
            redirectAttributes.addFlashAttribute("error", "The email field is required");
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomEmployeeDetails currentEmployee = (CustomEmployeeDetails) auth.getPrincipal();
        Boolean isAdmin = currentEmployee.getAdminRights();

        model.addAttribute("isAdmin", isAdmin);
        logger.info("Admin send a registration link successfully");

        return "redirect:/employees";
    }
}
