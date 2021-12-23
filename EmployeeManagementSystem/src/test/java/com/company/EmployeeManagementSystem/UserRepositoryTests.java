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
        employee.setEmail("horvat.diana2000@gmail.com");
        employee.setFirstName("Diana");
        employee.setLastName("Horvat");
        employee.setPassword("0204");
        employee.setJobTitle("Manager");
        employee.setImageURL("bdjcbsjbcjad");
        employee.setPhone("0734777806");

        Employee savedEmployee = repo.save(employee);

        Employee existEmployee = entityManager.find(Employee.class, savedEmployee.getId());

        assert(employee.getEmail()).equals(existEmployee.getEmail());

    }
}
