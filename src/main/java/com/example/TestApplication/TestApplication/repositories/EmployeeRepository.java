package com.example.TestApplication.TestApplication.repositories;


import com.example.TestApplication.TestApplication.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
//@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
        List<Employee> findByEmail(String email);

}
