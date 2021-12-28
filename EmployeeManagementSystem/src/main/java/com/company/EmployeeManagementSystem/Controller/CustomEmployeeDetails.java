package com.company.EmployeeManagementSystem.Controller;

import com.company.EmployeeManagementSystem.Model.Employee;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomEmployeeDetails implements UserDetails {
    private Employee employee;

    public CustomEmployeeDetails(Employee employee) {
        this.employee = employee;
    }

//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        List<SimpleGrantedAuthority> auth = new ArrayList<>();
//        auth.add(new SimpleGrantedAuthority(employee.getRole()));
//        return auth;
//    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return employee.getPassword();
    }

    @Override
    public String getUsername() {
        return employee.getUsername();
    }

    public Long getEmployeeId(){
        return employee.getId();
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public boolean getAdminRights() {
        return employee.getAdmin();
    }

    public String getFullName() {
        return employee.getFirstName() + " " + employee.getLastName();
    }



}
