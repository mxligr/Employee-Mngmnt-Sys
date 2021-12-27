package com.company.EmployeeManagementSystem;

import com.company.EmployeeManagementSystem.Model.Employee;
import com.company.EmployeeManagementSystem.Repos.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class UserRepositoryTests {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private EmployeeRepository repo;

    @Test
    public void testCreateEmployee(){
        Employee employee = new Employee();
        employee.setEmail("malina.com");
        employee.setFirstName("Malina");
        employee.setLastName("Grama");
        employee.setPassword("password");
        employee.setJobTitle("Manager");
        employee.setImageURL("bdjcbsjbcjad");
        employee.setPhone("0752181344");
        employee.setUsername("mxligr");

        Employee savedEmployee = repo.save(employee);

        Employee existEmployee = entityManager.find(Employee.class, savedEmployee.getId());

        assert(employee.getEmail()).equals(existEmployee.getEmail());

    }
}
