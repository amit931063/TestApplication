package com.example.TestApplication.TestApplication.repositories;

import com.example.TestApplication.TestApplication.TestContainerConfiguration;
import com.example.TestApplication.TestApplication.entities.Employee;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;

import java.util.List;


import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
//@SpringBootTest // when we add this annotation this add the  whole dependency and the webserver etc //

// this annotation  is automatically autoconfigured the embedded database //

@Import(TestContainerConfiguration.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class EmployeeRepositoryTest {

    @Autowired
     private EmployeeRepository employeeRepository;

     private  Employee employee;
     @BeforeEach
     void setUp(){
         employee=Employee.builder()
                 .name("amit")
                 .email("kumaramit10143@gmail.com")
                 .salary(10000L).build();


     }

    @Test
    void testFindByEmail_whenEmailIsPresent_thenReturnEmployee() {
        // Arrange
//        Employee saved = employeeRepository.save(employee);
//        Employee fresh = employeeRepository.findById(saved.getId()).get();
//        employeeRepository.save(fresh);
//        //Act
//        List<Employee>employeeList=employeeRepository.findByEmail(employee.getEmail());
//
//        // Assert
//       assertThat(employeeList).isNotNull();
//       assertThat(employeeList).isNotEmpty();
//       assertThat(employeeList.get(0).getEmail()).isEqualTo(employee.getEmail());



        employeeRepository.deleteAll(); // Ensure a clean state
        Employee saved = employeeRepository.save(employee); // Save once and use saved object

        // Act
        List<Employee> employeeList = employeeRepository.findByEmail(saved.getEmail());

        // Assert
        assertThat(employeeList).isNotNull();
        assertThat(employeeList).isNotEmpty();
        assertThat(employeeList.get(0).getEmail()).isEqualTo(saved.getEmail());
    }


    @Test
    void  testFindByEmail_whenEmailIsNotFound_thenReturnEmptyEmployeeList(){
         //Arrange
         String email="not_present123@gmail.com";

         //Act
        List<Employee>employeeList=employeeRepository.findByEmail(email);

        // Assert
        assertThat(employeeList).isNotNull();
        assertThat(employeeList).isEmpty();

    }

}