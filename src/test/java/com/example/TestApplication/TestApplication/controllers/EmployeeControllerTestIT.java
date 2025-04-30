package com.example.TestApplication.TestApplication.controllers;

import com.example.TestApplication.TestApplication.TestContainerConfiguration;
import com.example.TestApplication.TestApplication.dto.EmployeeDto;
import com.example.TestApplication.TestApplication.entities.Employee;
import com.example.TestApplication.TestApplication.repositories.EmployeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.annotation.Id;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;



@AutoConfigureWebTestClient(timeout = "100000")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)


@Slf4j
@Import(TestContainerConfiguration.class)
class EmployeeControllerTestIT {
    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee testEmployee;

    private EmployeeDto testEmployeeDto;

@BeforeEach
void  setUp(){
      testEmployee = Employee.builder()
            .email("amit@gmail.com").
            name("AmitKumar")
            .salary(200L)
            .build();

    testEmployeeDto = EmployeeDto.builder()
            .id(1L)
            .email("amit@gmail.com").
            name("AmitKumar")
            .salary(200L)
            .build();

    employeeRepository.deleteAll();
}



    @Test
    void  testGetEmployeeById_Success(){

    Employee savedEmployee = employeeRepository.save(testEmployee);

//        assertNotNull(savedEmployee, "Saved employee should not be null");
//        assertNotNull(savedEmployee.getId(), "Saved employee ID should not be null");
//        log.info("Saved Employee: {}", savedEmployee);

    webTestClient.get()
            .uri("/employees/{id}",savedEmployee.getId())
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.id").isEqualTo(savedEmployee.getId())
            .jsonPath("$.email").isEqualTo(savedEmployee.getEmail());


//            .expectBody(EmployeeDto.class)
//            .isEqualTo(testEmployeeDto);
//            .value(employeeDto -> {
//                assertThat(employeeDto.getEmail()).isEqualTo(savedEmployee.getEmail());
//                assertThat(employeeDto.getId()).isEqualTo(savedEmployee.getId());
//            });
}

@Test
    void testGetEmployeeById_Failure(){

    webTestClient.get()
            .uri("/employees/1")
            .exchange()
            .expectStatus().isNotFound();

}


@Test
    void test_CreateNewEmployee_whenEmployeeAlreadyExist_thenThrowException(){

    Employee savedEmployee=employeeRepository.save(testEmployee);
    webTestClient.post()
            .uri("/employees")
            .bodyValue(testEmployeeDto)
            .exchange()
            .expectStatus().is5xxServerError();


}

@Test
void test_createNewEmployee_whenEmployeeIsNotPresent_thenReturnEmployee(){

    webTestClient.post()
            .uri("/employees")
            .bodyValue(testEmployeeDto)
            .exchange()
            .expectStatus().isCreated()
            .expectBody()
            .jsonPath("$.email").isEqualTo(testEmployeeDto.getEmail())
    .jsonPath("$.name").isEqualTo(testEmployeeDto.getName());

}

@Test
    void test_updateEmployee_whenEmployeeDoesNotExist_thenThrowException(){
    webTestClient.put()
            .uri("/employees/999")
            .bodyValue(testEmployeeDto)
            .exchange()
            .expectStatus().isNotFound();

}

@Test
    void updateEmployee_whenAttemptingToUpdateEmail_thenThrowException(){
    Employee savedEmployee = employeeRepository.save(testEmployee);
    testEmployeeDto.setName("Jayank Kumar");
    testEmployeeDto.setEmail("Jayank3421@gmail.com");
    webTestClient.put()
            .uri("/employees/{id}",savedEmployee.getId())
            .bodyValue(testEmployeeDto)
            .exchange()
            .expectStatus().is5xxServerError();
}


@Test
     void test_updateEmployee_whenEmployeeIsPresent(){

    Employee savedEmployee = employeeRepository.save(testEmployee);
    testEmployeeDto.setName("Jayank Kumar");
    testEmployeeDto.setSalary(250L);
    webTestClient.put()
            .uri("/employees/{id}",savedEmployee.getId())
            .bodyValue(testEmployeeDto)
            .exchange()
            .expectStatus().isOk()
            .expectBody(EmployeeDto.class)
            .isEqualTo(testEmployeeDto);


}

@Test

    void test_deleteEmployee_whenEmployeeDoesNotExist(){

    webTestClient.delete()
            .uri("/employees/999")
            .exchange()
            .expectStatus().isNotFound();

}

@Test
    void deleteEmployee_whenEmployeeIsPresent_thenDeleteEmployee(){

    Employee savedEmployee = employeeRepository.save(testEmployee);
    webTestClient.delete()
            .uri("/employees/{id}",savedEmployee.getId())
            .exchange()
            .expectStatus().isNoContent()
            .expectBody(Void.class);

}
}