package com.studentsviolation.main.repository;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import com.studentsviolation.main.entity.Students;

public interface StudentsRepository extends JpaRepository<Students, Long> {

	Students findByFirstname(String firstname);
	Students findByLastname(String lastname);
	Students findByUsernameAndPassword(String username, String password);
	Students findByUsername(String username);
	List<Students> findByCourseYear(String courseYear);
	Students findById(long id);
}

