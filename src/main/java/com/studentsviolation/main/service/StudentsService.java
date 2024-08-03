package com.studentsviolation.main.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.studentsviolation.main.entity.Students;
import com.studentsviolation.main.repository.StudentsRepository;
@Service
public class StudentsService {
	@Autowired
	private StudentsRepository studentsRepository;

	public List<Students> getAllStudents() {
		return studentsRepository.findAll();
	}
	
	public Students getStudentsById(Long id) {
        return studentsRepository.findById(id).orElse(null);
    }
	public Students getStudentsByFirstname(String firstname) {
        return studentsRepository.findByFirstname(firstname);
    }
	public Students getStudentByLastname(String lastname) {
        return studentsRepository.findByLastname(lastname);
    }
	public Students getStudentByUsername(String username) {
        return studentsRepository.findByUsername(username);
    }
	public Students getStudentByUsernameAndPassword(String username, String password) {
        return studentsRepository.findByUsernameAndPassword(username, password);
    }
	public List<Students> getStudentByCourseYear(String courseYear) {
        return studentsRepository.findByCourseYear(courseYear);
    }

	public Students updateStudents(Long id, Students studentsDetails) {
		Students students = studentsRepository.findById(id).orElse(null);
        if (students != null) {
        	students.setLastname(studentsDetails.getLastname());
        	students.setFirstname(studentsDetails.getFirstname());
        	students.setCourseYear(studentsDetails.getCourseYear());
            return studentsRepository.save(students);
        }
        return null;
    }
	public void deleteStudent(Long id) {
		studentsRepository.deleteById(id);
	}

}
