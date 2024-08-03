package com.studentsviolation.main.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.studentsviolation.main.entity.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long> {

	Admin findByUsername(String username);
	Admin findByUsernameAndPasswordAndStatus(String username, String Password, int status);
	List<Admin> findByPassword(String password);
	List<Admin> findByStatus(int status);
}

