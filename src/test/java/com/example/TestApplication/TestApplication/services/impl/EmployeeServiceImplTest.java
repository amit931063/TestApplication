package com.example.TestApplication.TestApplication.services.impl;

import com.example.TestApplication.TestApplication.TestContainerConfiguration;
import com.example.TestApplication.TestApplication.dto.EmployeeDto;
import com.example.TestApplication.TestApplication.entities.Employee;
import com.example.TestApplication.TestApplication.exceptions.ResourceNotFoundException;
import com.example.TestApplication.TestApplication.repositories.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import org.springframework.context.annotation.Import;
import org.testcontainers.shaded.org.checkerframework.common.value.qual.ArrayLenRange;


import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;


@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestContainerConfiguration.class)
@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {
    @Mock
    private EmployeeRepository  employeeRepository;

    @Spy
    private ModelMapper modelMapper;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee mockedEmployee;
    private EmployeeDto  mockedEmployeeDto;

    @BeforeEach
    void setUp(){
        mockedEmployee = Employee.builder()
                .id(1L)
                .email("amit@gmail.com").
                name("AmitKumar")
                .salary(200L)
                .build();

        mockedEmployeeDto = modelMapper.map(mockedEmployee,EmployeeDto.class);
    }

    @Test
    void test_GetEmployeeById_whenEmployeeByIdIsPresent() {
        Long id = mockedEmployee.getId();
        Employee mockedEmployee = Employee.builder()
                .id(id)
                .email("amit@gmail.com").
                name("AmitKumar")
                .salary(200L)
                .build();


         when(employeeRepository.findById(id)).thenReturn(Optional.of(mockedEmployee));

// Action part is here occur //
        EmployeeDto employeeDto = employeeService.getEmployeeById(id);

 // Assert
        assertThat(employeeDto.getId()).isEqualTo(id);
        assertThat(employeeDto.getEmail()).isEqualTo(mockedEmployee.getEmail());
        verify(employeeRepository).findById(id);
        verify(employeeRepository,times(1)).findById(id);
        verify(employeeRepository,atLeastOnce()).findById(id);
        verify(employeeRepository,atLeast(1)).findById(id);
        verify(employeeRepository,atMost(1)).findById(id);
    }


    @Test
    void test_GetEmployeeById_whenEmployeeIsNotPresent_ThenThrowException(){

        // Arrange //

        when(employeeRepository.findById(anyLong())).thenReturn(Optional.empty());


        // Act and Verify //
        assertThatThrownBy(()-> employeeService.getEmployeeById(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Employee not found with id: 1");


verify(employeeRepository).findById(1L);


    }

@Test
    void testCreateNewEmployee_WhenValidEmployee_ThenCreateNewEmployee(){


        // Assign //

    when(employeeRepository.findByEmail(anyString())).thenReturn(List.of());
    when(employeeRepository.save(any(Employee.class))).thenReturn(mockedEmployee);



    // Act //
EmployeeDto employeeDto = employeeService.createNewEmployee(mockedEmployeeDto);

    // Assert //

    assertThat(employeeDto).isNotNull();
//    assertThat(employeeDto.getEmail()).isEqualTo(mockedEmployeeDto.getEmail());

    ArgumentCaptor<Employee>employeeArgumentCaptor=ArgumentCaptor.forClass(Employee.class);
    verify(employeeRepository).save(employeeArgumentCaptor.capture());

    Employee captureEmployee= employeeArgumentCaptor.getValue();
    assertThat(captureEmployee.getEmail()).isEqualTo(mockedEmployee.getEmail());

}


@Test
    void test_CreateNewEmployee_whenAttemptingCreateEmployeeWithExistingEmail_thenThrownException(){
        // Arrange //

    when(employeeRepository.findByEmail(mockedEmployeeDto.getEmail())).thenReturn(List.of(mockedEmployee));




     // Action and Verification //

    assertThatThrownBy(()-> employeeService.createNewEmployee(mockedEmployeeDto))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("Employee already exists with email: "+mockedEmployee.getEmail());


    verify(employeeRepository).findByEmail(mockedEmployeeDto.getEmail());
    verify(employeeRepository,never()).save(any());

}


@Test
    void  test_updateEmployee_EmployeeDoesNotExist_thenThrownException(){
        // Arrange //
    when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

    // Assert and Verify //
    assertThatThrownBy( ()-> employeeService.updateEmployee(1L,mockedEmployeeDto))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage("Employee not found with id: 1");


    verify(employeeRepository).findById(1L);
    verify(employeeRepository,never()).save(any());

    }


    @Test
    void updateEmployee_whenAttemptingToUpdateEmailThenThrownException(){

        // Arrange//
        when( employeeRepository.findById(mockedEmployeeDto.getId())).thenReturn(Optional.of(mockedEmployee));
        mockedEmployeeDto.setName("amitKumar");
        mockedEmployeeDto.setEmail("jayank@gmail.com");


        // Assert and Verification //
        assertThatThrownBy(()-> employeeService.updateEmployee(mockedEmployeeDto.getId(), mockedEmployeeDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("The email of the employee cannot be updated");


        verify(employeeRepository).findById(1L);
        verify(employeeRepository,never()).save(any());


    }

    @Test
     void test_updateEmployee_whenEmployeeIsPresentThenUpdateEmployee(){
        //Arrange //
        when(employeeRepository.findById(mockedEmployeeDto.getId())).thenReturn(Optional.of(mockedEmployee));
        mockedEmployeeDto.setName("KeshavKumar");
        mockedEmployeeDto.setSalary(100L);

        Employee newEmployee= modelMapper.map(mockedEmployeeDto,Employee.class);
        when(employeeRepository.save(any(Employee.class))).thenReturn(newEmployee);

        // Act //

        EmployeeDto  updateEmployeeDto  =employeeService.updateEmployee(mockedEmployeeDto.getId(),mockedEmployeeDto);

        // Assertion //

        assertThat(updateEmployeeDto).isEqualTo(mockedEmployeeDto);

        verify(employeeRepository).findById(1L);
        verify(employeeRepository).save(any());



    }


    @Test
     void deleteEmployee_whenEmployeeDoesNotExist_thenThrownException(){


       // Arrange //
        when(employeeRepository.existsById(1L)).thenReturn(false);

        // Assert and Verification //
        assertThatThrownBy(()->employeeService.deleteEmployee(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Employee not found with id: 1");


        verify(employeeRepository,never()).deleteById(1L);



    }
@Test

    void deleteEmployee_whenEmployeeIsPresentThenDeleteEmployee(){
        // Arrange //
        when(employeeRepository.existsById(1L)).thenReturn(true);

        assertThatCode(()->employeeService.deleteEmployee(1L))
                .doesNotThrowAnyException();

        verify(employeeRepository).deleteById(1L);

}





}
