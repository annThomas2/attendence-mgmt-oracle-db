package com.test.attendancemgmtsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.test.attendancemgmtsystem.exceptions.EnterValidLeaveException;
import com.test.attendancemgmtsystem.exceptions.NonExistentFacultyIdException;
import com.test.attendancemgmtsystem.service.FacultyService;

@RestController
@RequestMapping("faculty")
public class FacultyController {

	@Autowired
	FacultyService facultyService;

	// Availing Leave
	@PutMapping("availLeave/{id}/{leaveNo}")
	public ResponseEntity<String> availLeaveFaculty(@PathVariable int leaveNo, @PathVariable int id) throws Exception {
		try {
		
		return ResponseEntity.ok(facultyService.availLeaveFaculty(leaveNo, id));
		}
		catch (EnterValidLeaveException | NonExistentFacultyIdException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

}
