package com.company.EmployeeManagementSystem;

import com.company.EmployeeManagementSystem.Model.Employee;
import com.company.EmployeeManagementSystem.Repos.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
        employee.setEmail("malina@gmail.com");
        employee.setFirstName("Malina");
        employee.setLastName("Grama");
        employee.setPassword("password");
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(employee.getPassword());
        employee.setPassword(encodedPassword);
        employee.setJobTitle("CEO");
        employee.setImageURL("animegirl.jpg");
        employee.setPhone("0752181344");
        employee.setUsername("mxligr");
        employee.setAdmin(Boolean.TRUE);

//        Employee employee2 = new Employee();
//        employee2.setEmail("diana@gmail.com");
//        employee2.setFirstName("Diana");
//        employee2.setLastName("Horvat");
//        employee2.setPassword("123456");
//        BCryptPasswordEncoder passwordEncoder2 = new BCryptPasswordEncoder();
//        String encodedPassword2 = passwordEncoder2.encode(employee2.getPassword());
//        employee2.setPassword(encodedPassword2);
//        employee2.setJobTitle("Manager");
//        employee2.setImageURL("diana.jpg");
//        employee2.setPhone("0734777806");
//        employee2.setUsername("dianaahorvat");
//        employee2.setAdmin(Boolean.FALSE);

        Employee savedEmployee = repo.save(employee);

        Employee existEmployee = entityManager.find(Employee.class, savedEmployee.getId());

        assert(employee.getEmail()).equals(existEmployee.getEmail());

    }
}
