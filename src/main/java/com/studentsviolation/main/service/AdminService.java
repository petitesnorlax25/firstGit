package com.studentsviolation.main.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.studentsviolation.main.entity.Admin;
import com.studentsviolation.main.repository.AdminRepository;
@Service
public class AdminService {
	@Autowired
	private AdminRepository adminRepository;

	public List<Admin> getAllAdmins() {
		return adminRepository.findAll();
	}
	public Admin getAdminById(Long id) {
        return adminRepository.findById(id).orElse(null);
    }
	public Admin getAdminByUsername(String username) {
        return adminRepository.findByUsername(username);
    }
	public Admin getAdminByUsernameAndPassword(String username, String password, int status) {
        return adminRepository.findByUsernameAndPasswordAndStatus(username, password, status);
    }
	public List<Admin> getAdminByPassword(String password) {
        return adminRepository.findByPassword(password);
    }
	public List<Admin> getAdminsByStatus(int status) {
        return adminRepository.findByStatus(status);
    }
	public Admin createAdmin(Admin admin) {
        return adminRepository.save(admin);
    }
	public Admin updateAdmin(Long id, Admin adminDetails) {
        Admin admin = adminRepository.findById(id).orElse(null);
        if (admin != null) {
        	admin.setName(adminDetails.getName());
        	admin.setUsername(adminDetails.getUsername());
        	admin.setPassword(adminDetails.getPassword());
        	admin.setUserType(adminDetails.getUserType());
            return adminRepository.save(admin);
        }
        return null;
    }
	public void deleteAdmin(Long id) {
	    adminRepository.deleteById(id);
	}

}
