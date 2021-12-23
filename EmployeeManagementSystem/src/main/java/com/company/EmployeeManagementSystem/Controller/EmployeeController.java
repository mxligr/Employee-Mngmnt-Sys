package com.company.EmployeeManagementSystem.Controller;

import com.company.EmployeeManagementSystem.Repos.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;

    @GetMapping("/login")
    public String showLogin(){
        return "index";
    }
}
