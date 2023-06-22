package com.test.attendancemgmtsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.test.attendancemgmtsystem.entity.Address;
import com.test.attendancemgmtsystem.entity.UniversityStudent;
import com.test.attendancemgmtsystem.exceptions.EnterValidLeaveException;
import com.test.attendancemgmtsystem.exceptions.NonExistentStudentIdException;
import com.test.attendancemgmtsystem.exceptions.NullStudentException;
import com.test.attendancemgmtsystem.service.StudentService;

@RestController
@RequestMapping("/student")
public class StudentController {
	UniversityStudent student;

	@Autowired
	StudentService studentService;

	// Update Student Address
	@PutMapping("/{id}")
	public ResponseEntity<String> updateStudentAddress(@PathVariable int id, @RequestBody Address studentAddress)
			throws Exception {
		try {
			studentService.updateStudentAddress(id, studentAddress);
			return ResponseEntity.ok("Student address updated successfully");
		} catch (NullStudentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	// Avail Number of leaves
	@PutMapping("/availLeave/{id}/{leaveNo}")
	public ResponseEntity<String> availLeaveStudent(@PathVariable int leaveNo, @PathVariable int id) throws Exception {
		try {

			return ResponseEntity.ok(studentService.availLeaveStudent(leaveNo, id));
		} catch (EnterValidLeaveException | NonExistentStudentIdException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

}
