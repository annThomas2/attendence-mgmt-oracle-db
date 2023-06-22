package com.test.attendancemgmtsystem.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.test.attendancemgmtsystem.entity.Faculty;
import com.test.attendancemgmtsystem.entity.UniversityStudent;
import com.test.attendancemgmtsystem.exceptions.InvalidCreationFacultytException;
import com.test.attendancemgmtsystem.exceptions.InvalidCreationStudentException;
import com.test.attendancemgmtsystem.exceptions.InvalidFacultyIdException;
import com.test.attendancemgmtsystem.exceptions.InvalidStudentIdException;
import com.test.attendancemgmtsystem.exceptions.NonExistentFacultyIdException;
import com.test.attendancemgmtsystem.exceptions.NonExistentStudentIdException;
import com.test.attendancemgmtsystem.exceptions.NullFacultyException;
import com.test.attendancemgmtsystem.exceptions.NullStudentException;
import com.test.attendancemgmtsystem.service.FacultyService;
import com.test.attendancemgmtsystem.service.StudentService;

@RestController
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	StudentService studentService;
	@Autowired
	FacultyService facultyService;

	// Student Control
	// Create Student record
	@PostMapping("/students")
	public ResponseEntity<String> addStudent(@RequestBody UniversityStudent student) {
		try {
			studentService.addStudent(student);
			return ResponseEntity.ok("Student created Successfully");
		} catch (InvalidCreationStudentException | NullStudentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	// Read Student Record
	@GetMapping("students")
	public List<UniversityStudent> getStudent() {
		return studentService.getStudent();

	}

	// Update Student Record
	@PutMapping("students/{id}")
	public ResponseEntity<String> updateAddress(@PathVariable int id, @RequestBody UniversityStudent student) {
		try {
			studentService.updateStudent(student);
			return ResponseEntity.ok("Student updated Successfully");
		} catch (InvalidStudentIdException | NullStudentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	// Delete Student Record
	@DeleteMapping("students/{id}")
	public ResponseEntity<String> deleteByStudId(@PathVariable Integer id) throws Exception {
		try {
			studentService.deleteByStudId(id);
			return ResponseEntity.ok("Student deleted Successfully");
		} catch (NonExistentStudentIdException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	// Faculty Control
	@PostMapping("faculties")
	public ResponseEntity<String> addFaculty(@RequestBody Faculty faculty) throws Exception {
		try {
			facultyService.addFaculty(faculty);
			return ResponseEntity.ok("Faculty created Successfully");
		} catch (InvalidCreationFacultytException | NullFacultyException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	// Read Faculty Record
	@GetMapping("faculties")
	public List<Faculty> getFaculty() {
		return facultyService.getFaculty();
	}

	// Update Faculty Record
	@PutMapping("faculties/{id}")
	public ResponseEntity<String> updateFaculty(@RequestBody Faculty faculty, @PathVariable int id) throws Exception {
		try {
			facultyService.updateFaculty(faculty);
			return ResponseEntity.ok("Faculty updated Successfully");

		} catch (InvalidFacultyIdException | NullFacultyException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	// Delete Faculty Record
	@DeleteMapping("faculties/{id}")
	public ResponseEntity<String> deleteByFacultyId(@PathVariable Integer id) throws Exception {
		try {
			facultyService.deleteByFacultyId(id);
			return ResponseEntity.ok("Faculty deleted Successfully");
		} catch (NonExistentFacultyIdException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}

	}

}
